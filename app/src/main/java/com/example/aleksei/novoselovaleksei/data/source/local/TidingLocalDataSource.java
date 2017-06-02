package com.example.aleksei.novoselovaleksei.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.example.aleksei.novoselovaleksei.data.Tiding;
import com.example.aleksei.novoselovaleksei.data.source.TidingDataSource;
import com.example.aleksei.novoselovaleksei.data.source.remote.common.BaseSource;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class TidingLocalDataSource implements TidingDataSource {
    private static TidingLocalDataSource INSTANCE;

    private TidingsDbHelper mDbHelper;

    private TidingLocalDataSource(@NonNull Context context) {
        mDbHelper = new TidingsDbHelper(context);
    }

    public static TidingLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new TidingLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void saveTiding(Tiding tiding) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TidingsPersistenceContract.TidingEntry.COLUMN_NAME_TITLE, tiding.getTitle());
        values.put(TidingsPersistenceContract.TidingEntry.COLUMN_NAME_DESCRIPTION, tiding.getDescription());
        values.put(TidingsPersistenceContract.TidingEntry.COLUMN_NAME_PUBLICATION_DATE, tiding.getPublicationDate());
        values.put(TidingsPersistenceContract.TidingEntry.COLUMN_NAME_SOURCE, tiding.getSource().toString());
        values.put(TidingsPersistenceContract.TidingEntry.COLUMN_NAME_IMAGE_URL, tiding.getImageUrl());

        db.insert(TidingsPersistenceContract.TidingEntry.TABLE_NAME, null, values);
        db.close();
    }

    @Override
    public void deleteAllTidings(BaseSource.Source source) {

        String whereClause = TidingsPersistenceContract.TidingEntry.COLUMN_NAME_SOURCE + " = ?";
        String[] whereArgs = { source.toString() };

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.delete(TidingsPersistenceContract.TidingEntry.TABLE_NAME, whereClause, whereArgs);

        db.close();
    }

    @Override
    public Observable<List<Tiding>> getTidings() {
        return Observable.create(subscriber -> {
            List<Tiding> tidings = new ArrayList<>();
            SQLiteDatabase db = mDbHelper.getReadableDatabase();

            String[] projection = {
                    TidingsPersistenceContract.TidingEntry.COLUMN_NAME_TITLE,
                    TidingsPersistenceContract.TidingEntry.COLUMN_NAME_DESCRIPTION,
                    TidingsPersistenceContract.TidingEntry.COLUMN_NAME_PUBLICATION_DATE,
                    TidingsPersistenceContract.TidingEntry.COLUMN_NAME_SOURCE,
                    TidingsPersistenceContract.TidingEntry.COLUMN_NAME_IMAGE_URL
            };

            Cursor c = db.query(
                    TidingsPersistenceContract.TidingEntry.TABLE_NAME, projection, null, null, null, null, TidingsPersistenceContract.TidingEntry.COLUMN_NAME_PUBLICATION_DATE);

            if (c != null && c.getCount() > 0) {
                while (c.moveToNext()) {
                    String title = c.getString(c.getColumnIndexOrThrow(TidingsPersistenceContract.TidingEntry.COLUMN_NAME_TITLE));
                    String description =
                            c.getString(c.getColumnIndexOrThrow(TidingsPersistenceContract.TidingEntry.COLUMN_NAME_DESCRIPTION));
                    Long publicationDate =
                            c.getLong(c.getColumnIndexOrThrow(TidingsPersistenceContract.TidingEntry.COLUMN_NAME_PUBLICATION_DATE));
                    String imageUrl =
                            c.getString(c.getColumnIndexOrThrow(TidingsPersistenceContract.TidingEntry.COLUMN_NAME_IMAGE_URL));
                    String source =
                            c.getString(c.getColumnIndexOrThrow(TidingsPersistenceContract.TidingEntry.COLUMN_NAME_SOURCE));

                    Tiding task = new Tiding(title, publicationDate, description, imageUrl, BaseSource.Source.valueOf(source));
                    tidings.add(task);
                }
            }
            if (c != null) {
                c.close();
            }

            db.close();
            subscriber.onNext(tidings);
            subscriber.onCompleted();
        });
    }

    @Override
    public void refreshTidings() {

    }
}
