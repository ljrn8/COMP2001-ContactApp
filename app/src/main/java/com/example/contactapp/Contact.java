package com.example.contactapp;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;

@Entity(tableName = "Contacts")
public class Contact {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "name")
    private String name = "john"; // TODO

    @ColumnInfo(name = "number")
    private String phone;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "picture")
    byte[] picture;

    // Byte array to bitmap conversion from
    // https://stackoverflow.com/questions/4989182/converting-java-bitmap-to-byte-array
    public Bitmap getPicture() {
        if (picture == null) return null;
        return BitmapFactory.decodeByteArray(picture, 0, picture.length);
    }
    public void setPicture(Bitmap picture) {
        if (picture == null) {
            this.picture = null;
        } else {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            picture.compress(Bitmap.CompressFormat.PNG, 100, stream);
            this.picture = stream.toByteArray();
        }
    }

    public Contact(@NotNull String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NonNull @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", picture=" + picture +
                '}';
    }
}
