package com.example.notebook;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Random;

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
    }
}

