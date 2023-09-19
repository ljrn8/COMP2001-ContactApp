package com.example.contactapp;


import android.content.Context;
import android.provider.ContactsContract;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {Contact.class}, version = 1)
public abstract class ContactDataBase extends RoomDatabase {
    public abstract  ContactDOA contactDOA();
    
    
    private static ContactDataBase dataBase;
    public static ContactDataBase getDataBase(Context context) {
        if (dataBase == null) {
            dataBase = Room.databaseBuilder(context, ContactDataBase.class, "app_database")
                    .allowMainThreadQueries()
                    .build();
        }
        return dataBase;
    }
}