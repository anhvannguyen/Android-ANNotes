package me.anhvannguyen.android.annotes;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;


public class NotesRecyclerAdapter extends RecyclerView.Adapter<NotesRecyclerAdapter.ViewHolder> {
    private Context mContext;
    private Cursor mCursor;
    private NoteAdapterOnClickHandler mClickHandler;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mNoteTextView;
        public final ImageView mIconImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mNoteTextView = (TextView) itemView.findViewById(R.id.list_item_title);
            mIconImageView = (ImageView) itemView.findViewById(R.id.list_item_icon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            mClickHandler.onClick(this);
        }
    }

    public static interface NoteAdapterOnClickHandler {
        void onClick(ViewHolder viewHolder);
    }

    public NotesRecyclerAdapter(Context context, NoteAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_notes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        String noteString = mCursor.getString(MainActivityFragment.COL_TEXT);
        holder.mNoteTextView.setText(noteString);

        ColorGenerator generator = ColorGenerator.MATERIAL;
        int iconColor = generator.getRandomColor();
        TextDrawable drawable = TextDrawable.builder().buildRound(noteString.substring(0, 1).toUpperCase(), iconColor);
        holder.mIconImageView.setImageDrawable(drawable);
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
