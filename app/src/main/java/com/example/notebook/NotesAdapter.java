package com.example.notebook;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {
    private List<NoteEntity> data = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    interface OnItemClickListener {
        void onItemClick(NoteEntity note);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setData(List<NoteEntity> notes) {
        data = notes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(parent, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        private final TextView subjectTextView;
        private final TextView phoneTextView;
        private final CardView cardView;
        private NoteEntity noteEntity;

        public NoteViewHolder(@NonNull ViewGroup parent, @Nullable NotesAdapter.OnItemClickListener clickListener) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false));
            cardView = (CardView) itemView;
            subjectTextView = itemView.findViewById(R.id.subject_text_view);
            phoneTextView = itemView.findViewById(R.id.phone_text_view);
            cardView.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onItemClick(noteEntity);
                }
            });
            cardView.setCardBackgroundColor(new Random().nextInt());
        }

        public void bind(NoteEntity noteEntity) {
            this.noteEntity = noteEntity;
            subjectTextView.setText(noteEntity.subject);
            phoneTextView.setText(noteEntity.phone);
            itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    menu.add("Удалить").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            data.remove(noteEntity);
                            notifyDataSetChanged();
                            Toast.makeText(itemView.getContext(), "удалено", Toast.LENGTH_LONG).show();
                            return true;
                        }
                    });
                }
            });
        }
    }
}
