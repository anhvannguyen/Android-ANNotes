package me.anhvannguyen.android.annotes;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import me.anhvannguyen.android.annotes.data.NotesContract;


public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int LOADER_NOTE = 0;

    private static final String[] NOTE_PROJECTION = {
            NotesContract.NoteEntry._ID,
            NotesContract.NoteEntry.COLUMN_TEXT,
            NotesContract.NoteEntry.COLUMN_DATE_CREATED
    };
    public static final int COL_ID = 0;
    public static final int COL_TEXT = 1;
    public static final int COL_DATE_CREATED = 2;

    private RecyclerView mNotesRecyclerView;
    private NotesRecyclerAdapter mNotesRecyclerAdapter;

    public MainActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(LOADER_NOTE, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_main, container, false);

        mNotesRecyclerAdapter = new NotesRecyclerAdapter(getActivity());

        mNotesRecyclerView = (RecyclerView) rootView.findViewById(R.id.notes_recyclerview);
        mNotesRecyclerView.setHasFixedSize(true);
        mNotesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mNotesRecyclerView.setAdapter(mNotesRecyclerAdapter);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_create_sample:
                generateFakeData("Milk");
                generateFakeData("Bread");
                generateFakeData("Egg");
                getLoaderManager().restartLoader(LOADER_NOTE, null, this);
                Snackbar.make(getView(), "Sample data added", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                return true;
            case R.id.action_delete_all:
                if (mNotesRecyclerAdapter.getItemCount() == 0) {
                    Snackbar.make(getView(), "No items to delete", Snackbar.LENGTH_SHORT).show();
                } else {
                    getContext().getContentResolver().delete(
                            NotesContract.NoteEntry.CONTENT_URI,
                            null,
                            null
                    );
                    getLoaderManager().restartLoader(LOADER_NOTE, null, this);
                    Snackbar.make(getView(), "Delete Pressed", Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void generateFakeData(String fakeNote) {
        ContentValues values = new ContentValues();
        values.put(NotesContract.NoteEntry.COLUMN_TEXT, fakeNote);

        getContext().getContentResolver().insert(
                NotesContract.NoteEntry.CONTENT_URI,
                values
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(LOADER_NOTE, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = NotesContract.NoteEntry.COLUMN_DATE_CREATED + " DESC";
        return new CursorLoader(
                getActivity(),
                NotesContract.NoteEntry.CONTENT_URI,
                NOTE_PROJECTION,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mNotesRecyclerAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNotesRecyclerAdapter.swapCursor(null);
    }
}
