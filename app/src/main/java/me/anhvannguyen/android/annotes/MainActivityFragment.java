package me.anhvannguyen.android.annotes;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private RecyclerView mNotesRecyclerView;
    private NotesRecyclerAdapter mNotesRecyclerAdapter;

    private static final String[] fakeData = {"eggs", "milk", "bread", "butter", "strawberry"};

    public MainActivityFragment() {
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
}
