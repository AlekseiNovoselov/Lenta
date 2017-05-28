package com.example.aleksei.novoselovaleksei.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.aleksei.novoselovaleksei.data.source.local.TidingsPersistenceContract.*;

public class TidingsDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "Tidings.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String INTEGER_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TidingEntry.TABLE_NAME + " (" +
                    TidingEntry.COLUMN_NAME_TITLE + TEXT_TYPE + " PRIMARY KEY," +
                    TidingEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    TidingEntry.COLUMN_NAME_PUBLICATION_DATE + INTEGER_TYPE + COMMA_SEP +
                    TidingEntry.COLUMN_NAME_SOURCE + TEXT_TYPE + COMMA_SEP +
                    TidingEntry.COLUMN_NAME_IMAGE_URL + TEXT_TYPE +
                    " )";

    public TidingsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
