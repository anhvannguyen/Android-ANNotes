package me.anhvannguyen.android.annotes;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
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
    private TextInputLayout mNoteTextInputLayout;
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

        mNoteTextInputLayout = (TextInputLayout) rootView.findViewById(R.id.note_textinputlayout);
        mNoteTextInputLayout.setErrorEnabled(true);

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
                String noteString = mNoteEditText.getText().toString();
                ContentValues values = new ContentValues();
                values.put(NotesContract.NoteEntry.COLUMN_TEXT, noteString.trim());

                if (noteString == null || noteString.length() == 0) {
                    Snackbar.make(getView(), "Note field is empty", Snackbar.LENGTH_SHORT).show();
                    mNoteTextInputLayout.setError("Note field is empty");
                } else if (noteString.matches("^\\s*$")) {
                    Snackbar.make(getView(), "Note field contains only whitespace", Snackbar.LENGTH_SHORT).show();
                    mNoteTextInputLayout.setError("Note field contains only whitespace");
                } else {
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
                }
                return true;
            case R.id.action_delete_note:
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                alertBuilder.setMessage("Are you sure you want to delete this note?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (mNoteUri != null) {
                                    getContext().getContentResolver().delete(
                                            mNoteUri,
                                            null,
                                            null
                                    );
                                    getActivity().finish();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing, close dialog box
                                dialog.cancel();
                            }
                        })
                .create()
                .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem deleteMenuItem = menu.findItem(R.id.action_delete_note);
        switch (mActionString) {
            case Intent.ACTION_INSERT:
                deleteMenuItem.setVisible(false);
                break;
            case Intent.ACTION_EDIT:
                deleteMenuItem.setVisible(true);
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
