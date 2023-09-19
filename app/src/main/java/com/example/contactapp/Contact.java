package com.example.contactapp;


import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "contacts")
public class Contact {


    @PrimaryKey
    @NotNull
    private String name = "john"; // TODO

    @ColumnInfo(name = "phone_number")
    private String phone;

    @ColumnInfo(name = "contact_email")
    private String email;

    @Ignore
    Bitmap picture;

    public Contact(@NotNull String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }


    @NonNull
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
}
