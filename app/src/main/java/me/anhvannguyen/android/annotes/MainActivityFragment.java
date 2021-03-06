package me.anhvannguyen.android.annotes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
import android.util.Log;
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

        mNotesRecyclerAdapter = new NotesRecyclerAdapter(getActivity(), new NotesRecyclerAdapter.NoteAdapterOnClickHandler() {
            @Override
            public void onClick(NotesRecyclerAdapter.ViewHolder viewHolder) {
                if (mNotesRecyclerAdapter.getCursor() != null) {
//                    String noteText = mNotesRecyclerAdapter.getCursor().getString(COL_TEXT);
//                    Snackbar.make(getView(), mNotesRecyclerAdapter.getCursor().getPosition() + ": " + noteText, Snackbar.LENGTH_SHORT).show();
                    long id = mNotesRecyclerAdapter.getCursor().getLong(COL_ID);
                    Uri noteUri = NotesContract.NoteEntry.buildNoteUri(id);
                    Intent intent = new Intent(getActivity(), EditorActivity.class);
                    intent.setData(noteUri);
                    startActivity(intent);
                }
            }
        });

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
                restartLoader();
                Snackbar.make(getView(), "Sample data added", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                return true;
            case R.id.action_delete_all:
                if (mNotesRecyclerAdapter.getItemCount() == 0) {
                    Snackbar.make(getView(), "No items to delete", Snackbar.LENGTH_SHORT).show();
                } else {
//                    getContext().getContentResolver().delete(
//                            NotesContract.NoteEntry.CONTENT_URI,
//                            null,
//                            null
//                    );
//                    restartLoader();
                    // Temporary hide the data from the user
                    mNotesRecyclerAdapter.emptyData();
                    Snackbar.make(getView(), "All items deleted", Snackbar.LENGTH_LONG)
                            .setCallback(new Snackbar.Callback() {
                                @Override
                                public void onDismissed(Snackbar snackbar, int event) {
                                    // Delete all items when the Snackbar timesout or user swipe to
                                    // dismiss
                                    switch (event) {
                                        case Snackbar.Callback.DISMISS_EVENT_SWIPE:
                                        case Snackbar.Callback.DISMISS_EVENT_TIMEOUT:
                                            getContext().getContentResolver().delete(
                                                    NotesContract.NoteEntry.CONTENT_URI,
                                                    null,
                                                    null
                                            );
                                            restartLoader();
                                            break;
                                    }
                                }
                            })
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Data has not been deleted, restart loader to display data
                                    restartLoader();
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
        restartLoader();
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(LOADER_NOTE, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder;
        int sortType = Utility.getSortingPreference(getActivity());
        switch (sortType) {
            case Utility.SORT_DATE:
                // Sort by date crated, newest first
                sortOrder = NotesContract.NoteEntry.COLUMN_DATE_CREATED + " DESC";
                break;
            case Utility.SORT_TITLE:
                // Sort by note title ignoring case
                sortOrder = NotesContract.NoteEntry.COLUMN_TEXT + " COLLATE NOCASE ASC";
                break;
            default:
                sortOrder = null;
        }
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
