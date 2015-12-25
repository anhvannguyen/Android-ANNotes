package me.anhvannguyen.android.annotes.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;


public class NotesContentProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DBOpenHelper mOpenHelper;

    private static final int NOTE = 100;
    private static final int NOTE_WITH_ID = 101;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = NotesContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, NotesContract.PATH_NOTE, NOTE);
        matcher.addURI(authority, NotesContract.PATH_NOTE + "/#", NOTE_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DBOpenHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        Cursor returnCursor;
        switch (match) {
            case NOTE:
                returnCursor = db.query(
                        NotesContract.NoteEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case NOTE_WITH_ID:
                String noteId = NotesContract.NoteEntry.getNoteId(uri);
                returnCursor = db.query(
                        NotesContract.NoteEntry.TABLE_NAME,
                        projection,
                        NotesContract.NoteEntry._ID + " = ?",
                        new String[] { noteId },
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case NOTE:
                return NotesContract.NoteEntry.CONTENT_TYPE;
            case NOTE_WITH_ID:
                return NotesContract.NoteEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        Uri returnUri;
        switch (match) {
            case NOTE:
                long _id = db.insert(
                        NotesContract.NoteEntry.TABLE_NAME,
                        null,
                        values
                );
                if (_id > 0) {
                    returnUri = NotesContract.NoteEntry.buildNoteUri(_id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        int rowsDeleted;

        // this makes delete all rows return the number of rows deleted
        if (selection == null)
            selection = "1";

        switch (match) {
            case NOTE:
                rowsDeleted = db.delete(
                        NotesContract.NoteEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            case NOTE_WITH_ID:
                String noteId = NotesContract.NoteEntry.getNoteId(uri);
                rowsDeleted = db.delete(
                        NotesContract.NoteEntry.TABLE_NAME,
                        NotesContract.NoteEntry._ID + " = ?",
                        new String[] { noteId }
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        int rowsUpdated;

        switch (match) {
            case NOTE:
                rowsUpdated = db.update(
                        NotesContract.NoteEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );
                break;
            case NOTE_WITH_ID:
                String noteId = NotesContract.NoteEntry.getNoteId(uri);
                rowsUpdated = db.update(
                        NotesContract.NoteEntry.TABLE_NAME,
                        values,
                        NotesContract.NoteEntry._ID + " = ?",
                        new String[] { noteId }
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
