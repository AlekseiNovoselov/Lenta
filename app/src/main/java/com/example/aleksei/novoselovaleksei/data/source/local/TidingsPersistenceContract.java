package com.example.aleksei.novoselovaleksei.data.source.local;

import android.provider.BaseColumns;

public class TidingsPersistenceContract {

    private TidingsPersistenceContract() {}

    /* Inner class that defines the table contents */
    public static abstract class TidingEntry implements BaseColumns {
        public static final String TABLE_NAME = "tiding";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_PUBLICATION_DATE = "publicationDate";
        public static final String COLUMN_NAME_SOURCE = "source";
        public static final String COLUMN_NAME_IMAGE_URL = "imageUrl";
    }
}
