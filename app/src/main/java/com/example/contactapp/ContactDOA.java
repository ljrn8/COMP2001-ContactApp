package com.example.contactapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.contactapp.Contact;

import java.util.List;

@Dao
public interface ContactDOA {

    @Insert
    void insert(Contact contact);

    @Insert
    void insertMultiple(List<Contact> contacts);

    @Update
    void update(Contact contact);

    @Delete
    void delete(Contact contact);

    @Query("SELECT * FROM contacts")
    List<Contact> getAllContacts();


    @Query("SELECT * FROM contacts WHERE name= :inName")
    Contact getByName(String inName);
}