package com.example.notebook;

import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class NoteDataMapping {

    public static class Fields{
        public final static String DATE = "date";
        public final static String TITLE = "title";
        public final static String DESCRIPTION = "description";
        public final static String LIKE = "like";
    }

    public static NoteEntity toCardData(String id, Map<String, Object> doc) {
        Timestamp timeStamp = (Timestamp)doc.get(Fields.DATE);
        NoteEntity answer = new NoteEntity((String) doc.get(Fields.TITLE),
                (String) doc.get(Fields.DESCRIPTION),
                (boolean) doc.get(Fields.LIKE),
                timeStamp.toDate());
        answer.setId(id);
        return answer;
    }

    public static Map<String, Object> toDocument(NoteEntity noteEntity){
        Map<String, Object> answer = new HashMap<>();
        answer.put(Fields.TITLE, noteEntity.getTitle());
        answer.put(Fields.DESCRIPTION, noteEntity.getDescription());
        answer.put(Fields.LIKE, noteEntity.isLike());
        answer.put(Fields.DATE, noteEntity.getDate());
        return answer;
    }
}
