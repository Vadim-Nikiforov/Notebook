package com.example.notebook;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class NoteEntity implements Parcelable {
    private String id;          // Идентификатор
    private String title;       // заголовок
    private String description; // описание
    private boolean like;       // флажок
    private Date date;          // Дата

    public NoteEntity(String title, String description, boolean like, Date date){
        this.title = title;
        this.description=description;
        this.like=like;
        this.date = date;
    }

    protected NoteEntity(Parcel in) {
        title = in.readString();
        description = in.readString();
        like = in.readByte() != 0;
        date = new Date(in.readLong());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeByte((byte) (like ? 1 : 0));
        dest.writeLong(date.getTime());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NoteEntity> CREATOR = new Creator<NoteEntity>() {
        @Override
        public NoteEntity createFromParcel(Parcel in) {
            return new NoteEntity(in);
        }

        @Override
        public NoteEntity[] newArray(int size) {
            return new NoteEntity[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isLike() {
        return like;
    }

    public Date getDate() {
        return date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}