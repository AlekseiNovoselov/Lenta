package com.example.aleksei.novoselovaleksei.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.example.aleksei.novoselovaleksei.data.Tiding;
import com.example.aleksei.novoselovaleksei.data.source.TidingDataSource;
import com.example.aleksei.novoselovaleksei.data.source.remote.common.BaseSource;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TidingLocalDataSource implements TidingDataSource {
    private static TidingLocalDataSource INSTANCE;

    private TidingsDbHelper mDbHelper;

    // Prevent direct instantiation.
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

        DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
        try {
            Date date = formatter.parse(tiding.getPublicationDate());

            ContentValues values = new ContentValues();
            values.put(TidingsPersistenceContract.TidingEntry.COLUMN_NAME_TITLE, tiding.getTitle());
            values.put(TidingsPersistenceContract.TidingEntry.COLUMN_NAME_DESCRIPTION, tiding.getDescription());
            values.put(TidingsPersistenceContract.TidingEntry.COLUMN_NAME_PUBLICATION_DATE, date.getTime());
            values.put(TidingsPersistenceContract.TidingEntry.COLUMN_NAME_SOURCE, tiding.getSource().toString());
            values.put(TidingsPersistenceContract.TidingEntry.COLUMN_NAME_IMAGE_URL, tiding.getImageUrl());

            db.insert(TidingsPersistenceContract.TidingEntry.TABLE_NAME, null, values);
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
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
    public void getTidings(@NonNull LoadTidingsCallback callback) {
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
                String publicationDate =
                        c.getString(c.getColumnIndexOrThrow(TidingsPersistenceContract.TidingEntry.COLUMN_NAME_PUBLICATION_DATE));
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

        if (tidings.isEmpty()) {
            // This will be called if the table is new or just empty.
            callback.onDataNotAvailable();
        } else {
            callback.onTidingLoaded(tidings);
        }
    }

    @Override
    public void refreshTidings() {

    }
}
