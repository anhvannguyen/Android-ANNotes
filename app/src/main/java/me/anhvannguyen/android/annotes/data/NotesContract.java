package me.anhvannguyen.android.annotes.data;

import android.provider.BaseColumns;

public class NotesContract {

    public static class NoteEntry implements BaseColumns {

        public static final String TABLE_NAME = "notes";

        public static final String COLUMN_TEXT = "text";

        public static final String COLUMN_DATE_CREATED = "date_created";
    }
}
