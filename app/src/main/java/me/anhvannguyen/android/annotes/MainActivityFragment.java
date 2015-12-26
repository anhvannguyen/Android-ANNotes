package me.anhvannguyen.android.annotes;

import android.content.ContentValues;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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
public class MainActivityFragment extends Fragment {

    private RecyclerView mNotesRecyclerView;
    private NotesRecyclerAdapter mNotesRecyclerAdapter;

    private static final String[] fakeData = {"eggs", "milk", "bread", "butter", "strawberry"};

    public MainActivityFragment() {
        setHasOptionsMenu(true);
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
}
