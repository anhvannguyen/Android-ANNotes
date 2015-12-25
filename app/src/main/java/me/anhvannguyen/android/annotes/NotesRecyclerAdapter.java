package me.anhvannguyen.android.annotes;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.anhvannguyen.android.annotes.data.NotesContract;


public class NotesRecyclerAdapter extends RecyclerView.Adapter<NotesRecyclerAdapter.ViewHolder> {
    private Context mContext;
//    private List<String> mNoteList;
    private Cursor mCursor;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mNoteTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mNoteTextView = (TextView) itemView.findViewById(R.id.list_item_title);
        }
    }

    public NotesRecyclerAdapter(Context context) {
        mContext = context;
//        mNoteList = new ArrayList<String>();
    }

//    public NotesRecyclerAdapter(Context context, String[] data) {
//        mContext = context;
//        mNoteList = new ArrayList<String>();
//        for (String s : data) {
//            mNoteList.add(s);
//        }
//        notifyDataSetChanged();
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_notes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        int noteIndex = mCursor.getColumnIndex(NotesContract.NoteEntry.COLUMN_TEXT);
        String noteString = mCursor.getString(noteIndex);

        holder.mNoteTextView.setText(noteString);
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    public void swapCursor(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return mCursor;
    }
}
