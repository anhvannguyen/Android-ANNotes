package me.anhvannguyen.android.annotes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "annotes.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_NOTES_TABLE =
            "CREATE TABLE " + NotesContract.NoteEntry.TABLE_NAME + " (" +
                    NotesContract.NoteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NotesContract.NoteEntry.COLUMN_TEXT + " TEXT, " +
                    NotesContract.NoteEntry.COLUMN_DATE_CREATED + " TEXT DEFAULT CURRENT_TIMESTAMP" +
                    ")";

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NotesContract.NoteEntry.TABLE_NAME);
        onCreate(db);
    }
}
