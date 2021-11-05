package com.example.notebook;

public interface NotesSource {
    NotesSource init(NotesSourceResponse notesSourceResponse);

    NoteEntity getCardData(int position);

    int size();

    void deleteNoteData(int position);

    void updateNoteData(int position, NoteEntity noteEntity);

    void addNoteData(NoteEntity noteEntity);

    void clearNoteData();
}