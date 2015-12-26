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

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int LOADER_NOTE = 0;

    private RecyclerView mNotesRecyclerView;
    private NotesRecyclerAdapter mNotesRecyclerAdapter;

    private static final String[] fakeData = {"eggs", "milk", "bread", "butter", "strawberry"};

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
                return true;
        }

        return super.onOptionsItemSelected(item);
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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = NotesContract.NoteEntry.COLUMN_DATE_CREATED + " DESC";
        return new CursorLoader(
                getActivity(),
                NotesContract.NoteEntry.CONTENT_URI,
                null,
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
