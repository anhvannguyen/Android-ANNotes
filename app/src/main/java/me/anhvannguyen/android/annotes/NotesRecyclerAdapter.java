package me.anhvannguyen.android.annotes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class NotesRecyclerAdapter extends RecyclerView.Adapter<NotesRecyclerAdapter.ViewHolder> {
    private Context mContext;
    private List<String> mNoteList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mNoteTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mNoteTextView = (TextView) itemView.findViewById(R.id.list_item_title);
        }
    }

    public NotesRecyclerAdapter(Context context) {
        mContext = context;
        mNoteList = new ArrayList<String>();
    }

    public NotesRecyclerAdapter(Context context, String[] data) {
        mContext = context;
        mNoteList = new ArrayList<String>();
        for (String s : data) {
            mNoteList.add(s);
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_notes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mNoteTextView.setText(mNoteList.get(position));
    }

    @Override
    public int getItemCount() {
        return mNoteList.size();
    }


}
