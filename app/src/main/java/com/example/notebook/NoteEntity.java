package com.example.notebook;

import java.io.Serializable;
import java.util.Calendar;
import java.util.UUID;

public class NoteEntity implements Serializable {
    public final String id;
    public final String subject;
    public final long creationDate;
    public final String text;
    public final String phone;

    public NoteEntity(String id, String subject, long date, String text, String phone) {
        this.id = id;
        this.subject = subject;
        this.creationDate = date;
        this.text = text;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public static String generateNewId() {
        return UUID.randomUUID().toString();
    }

    public static long getCurrentDate() {
        return Calendar.getInstance().getTimeInMillis();
    }
}
