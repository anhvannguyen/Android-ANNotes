package me.anhvannguyen.android.annotes;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import me.anhvannguyen.android.annotes.data.NotesContract;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditorFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String NOTE_URI = "note_uri";
    public static final int NOTE_LOADER = 0;

    private static final String[] NOTE_PROJECTION = {
            NotesContract.NoteEntry._ID,
            NotesContract.NoteEntry.COLUMN_TEXT,
            NotesContract.NoteEntry.COLUMN_DATE_CREATED
    };
    public static final int COL_ID = 0;
    public static final int COL_TEXT = 1;
    public static final int COL_DATE_CREATED = 2;

    private EditText mNoteEditText;
    private Uri mNoteUri;
    private String mActionString;

    public EditorFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_editor, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            mNoteUri = arguments.getParcelable(NOTE_URI);
        }

        mNoteEditText = (EditText) rootView.findViewById(R.id.note_edittext);
        if (mNoteUri != null) {
            mActionString = Intent.ACTION_EDIT;
            getActivity().setTitle("Edit Note");
        } else {
            mActionString = Intent.ACTION_INSERT;
            getActivity().setTitle("New Note");
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(NOTE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_editor_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_save_note:
                ContentValues values = new ContentValues();
                values.put(NotesContract.NoteEntry.COLUMN_TEXT, mNoteEditText.getText().toString());

                switch (mActionString) {
                    case Intent.ACTION_EDIT:
                        getContext().getContentResolver().update(
                                mNoteUri,
                                values,
                                null,
                                null
                        );
                        break;
                    case Intent.ACTION_INSERT:
                        getContext().getContentResolver().insert(
                                NotesContract.NoteEntry.CONTENT_URI,
                                values
                        );
                        break;
                }

                getActivity().finish();
//                Snackbar.make(getView(), "Save Pressed", Snackbar.LENGTH_SHORT).show();
                return true;
            case R.id.action_delete_note:
                Snackbar.make(getView(), "Delete Pressed", Snackbar.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (mNoteUri != null) {
            return new CursorLoader(
                    getActivity(),
                    mNoteUri,
                    NOTE_PROJECTION,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            mNoteEditText.setText(data.getString(COL_TEXT));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
