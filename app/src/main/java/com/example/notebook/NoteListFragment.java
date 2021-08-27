package com.example.notebook;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NoteListFragment extends Fragment {
    private final ArrayList<NoteEntity> noteList = new ArrayList<>();
    private Button createButton;
    private RecyclerView recyclerView;
    private NotesAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);
        createButton = view.findViewById(R.id.create_note_button);
        recyclerView = view.findViewById(R.id.recycler_view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        adapter = new NotesAdapter();
        adapter.setOnItemClickListener(getContract()::updateNote);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        renderList(noteList);
        createButton.setOnClickListener(v -> getContract().createNewNote());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (!(context instanceof Contract)) {
            throw new IllegalStateException("Activity must implement Contract");
        }
    }

    public void addNote(NoteEntity newNote) {
        NoteEntity sameNote = findNoteWithId(newNote.id);
        if (sameNote != null) {
            noteList.remove(sameNote);
        }

        noteList.add(newNote);
        renderList(noteList);
    }

    public void deleteNote() {
        renderList(noteList);
    }

    @Nullable
    private NoteEntity findNoteWithId(String id) {
        for (NoteEntity note : noteList) {
            if (note.id.equals(id)) {
                return note;
            }
        }
        return null;
    }

    private void renderList(List<NoteEntity> notes) {
        adapter.setData(notes);
    }

    private Contract getContract() {
        return (Contract) getActivity();
    }

    interface Contract {
        void createNewNote();
        void updateNote(NoteEntity note);
    }
}