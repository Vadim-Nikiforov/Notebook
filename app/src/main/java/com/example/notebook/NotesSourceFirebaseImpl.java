package com.example.notebook;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NotesSourceFirebaseImpl implements NotesSource {

    private static final String CARDS_COLLECTION = "cards";
    private static final String TAG = "[CardsSourceFirebaseImpl]";

    // База данных Firestore
    private FirebaseFirestore store = FirebaseFirestore.getInstance();

    // Коллекция документов
    private CollectionReference collection = store.collection(CARDS_COLLECTION);

    // Загружаемый список карточек
    private List<NoteEntity> cardsData = new ArrayList<NoteEntity>();

    @Override
    public NotesSource init(final NotesSourceResponse notesSourceResponse) {
        // Получить всю коллекцию отсортированную по полю "дата"
        collection.orderBy(NoteDataMapping.Fields.DATE, Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    // При удачном считывании данных загрузим список карточек
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            cardsData = new ArrayList<NoteEntity>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> doc = document.getData();
                                String id = document.getId();
                                NoteEntity noteEntity = NoteDataMapping.toCardData(id, doc);
                                cardsData.add(noteEntity);
                            }
                            Log.d(TAG, "success " + cardsData.size() + " qnt");
                            notesSourceResponse.initialized(NotesSourceFirebaseImpl.this);
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "get failed with ", e);
                    }
                });
        return this;
    }

    @Override
    public NoteEntity getCardData(int position) {
        return cardsData.get(position);
    }

    @Override
    public int size() {
        if (cardsData == null){
            return 0;
        }
        return cardsData.size();
    }

    @Override
    public void deleteNoteData(int position) {
        // Удалить документ с определенном идентификатором
        collection.document(cardsData.get(position).getId()).delete();
        cardsData.remove(position);
    }

    @Override
    public void updateNoteData(int position, NoteEntity noteEntity) {
        String id = noteEntity.getId();
        // Изменить документ по идентификатору
        collection.document(id).set(NoteDataMapping.toDocument(noteEntity));
    }

    @Override
    public void addNoteData(final NoteEntity noteEntity) {
        // Добавить документ
        collection.add(NoteDataMapping.toDocument(noteEntity)).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                noteEntity.setId(documentReference.getId());
            }
        });
    }

    @Override
    public void clearNoteData() {
        for (NoteEntity noteEntity : cardsData) {
            collection.document(noteEntity.getId()).delete();
        }
        cardsData = new ArrayList<NoteEntity>();
    }
}
