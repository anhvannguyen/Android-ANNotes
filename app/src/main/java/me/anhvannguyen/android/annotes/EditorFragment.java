package me.anhvannguyen.android.annotes;


import android.content.ContentValues;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
public class EditorFragment extends Fragment {

    private EditText mNoteEditText;

    public EditorFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_editor, container, false);

        mNoteEditText = (EditText) rootView.findViewById(R.id.note_edittext);

        return rootView;
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

                getContext().getContentResolver().insert(
                        NotesContract.NoteEntry.CONTENT_URI,
                        values
                );
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

}
