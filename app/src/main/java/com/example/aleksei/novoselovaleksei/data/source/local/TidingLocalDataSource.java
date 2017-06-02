package com.example.aleksei.novoselovaleksei.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.aleksei.novoselovaleksei.data.Tiding;
import com.example.aleksei.novoselovaleksei.data.source.TidingDataSource;
import com.example.aleksei.novoselovaleksei.data.source.remote.common.BaseSource;
import com.example.aleksei.novoselovaleksei.utils.schedulers.BaseSchedulerProvider;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE;
import static com.example.aleksei.novoselovaleksei.data.source.local.TidingsPersistenceContract.TidingEntry.COLUMN_NAME_DESCRIPTION;
import static com.example.aleksei.novoselovaleksei.data.source.local.TidingsPersistenceContract.TidingEntry.COLUMN_NAME_IMAGE_URL;
import static com.example.aleksei.novoselovaleksei.data.source.local.TidingsPersistenceContract.TidingEntry.COLUMN_NAME_PUBLICATION_DATE;
import static com.example.aleksei.novoselovaleksei.data.source.local.TidingsPersistenceContract.TidingEntry.COLUMN_NAME_SOURCE;
import static com.example.aleksei.novoselovaleksei.data.source.local.TidingsPersistenceContract.TidingEntry.COLUMN_NAME_TITLE;
import static com.example.aleksei.novoselovaleksei.data.source.local.TidingsPersistenceContract.TidingEntry.TABLE_NAME;

public class TidingLocalDataSource implements TidingDataSource {

    @Nullable
    private static TidingLocalDataSource INSTANCE;

    @NonNull
    private final BriteDatabase mDatabaseHelper;

    @NonNull
    private Func1<Cursor, Tiding> mTaskMapperFunction;

    private TidingLocalDataSource(@NonNull Context context, BaseSchedulerProvider schedulerProvider) {
        TidingsDbHelper dbHelper = new TidingsDbHelper(context);
        SqlBrite sqlBrite = SqlBrite.create();
        mDatabaseHelper = sqlBrite.wrapDatabaseHelper(dbHelper, schedulerProvider.computation());
        mTaskMapperFunction = this::getTiding;
    }

    public static TidingLocalDataSource getInstance(
            @NonNull Context context,
            @NonNull BaseSchedulerProvider schedulerProvider) {
        if (INSTANCE == null) {
            INSTANCE = new TidingLocalDataSource(context, schedulerProvider);
        }
        return INSTANCE;
    }

    @Override
    public void saveTiding(Tiding tiding) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_TITLE, tiding.getTitle());
        values.put(COLUMN_NAME_DESCRIPTION, tiding.getDescription());
        values.put(COLUMN_NAME_PUBLICATION_DATE, tiding.getPublicationDate());
        values.put(COLUMN_NAME_SOURCE, tiding.getSource().toString());
        values.put(COLUMN_NAME_IMAGE_URL, tiding.getImageUrl());

        mDatabaseHelper.insert(TABLE_NAME, values, CONFLICT_REPLACE);
    }

    @Override
    public void deleteAllTidings(BaseSource.Source source) {

        String whereClause = COLUMN_NAME_SOURCE + " = ?";
        String[] whereArgs = { source.toString() };
        mDatabaseHelper.delete(TABLE_NAME, whereClause, whereArgs);
    }

    private Tiding getTiding(Cursor c) {
        String title = c.getString(c.getColumnIndexOrThrow(COLUMN_NAME_TITLE));
        String description = c.getString(c.getColumnIndexOrThrow(COLUMN_NAME_DESCRIPTION));
        Long publicationDate = c.getLong(c.getColumnIndexOrThrow(COLUMN_NAME_PUBLICATION_DATE));
        String imageUrl = c.getString(c.getColumnIndexOrThrow(COLUMN_NAME_IMAGE_URL));
        String source = c.getString(c.getColumnIndexOrThrow(COLUMN_NAME_SOURCE));

        return new Tiding(title, publicationDate, description, imageUrl, BaseSource.Source.valueOf(source));
    }

    @Override
    public Observable<List<Tiding>> getTidings() {
        String[] projection = {
                COLUMN_NAME_TITLE,
                COLUMN_NAME_DESCRIPTION,
                COLUMN_NAME_PUBLICATION_DATE,
                COLUMN_NAME_SOURCE,
                COLUMN_NAME_IMAGE_URL
        };
        String sql = String.format("SELECT %s FROM %s", TextUtils.join(",", projection),
                TABLE_NAME);
        return mDatabaseHelper.createQuery(TABLE_NAME, sql)
                .mapToList(mTaskMapperFunction);
    }

    @Override
    public void refreshTidings() {

    }
}
