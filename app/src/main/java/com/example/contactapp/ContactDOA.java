package com.example.contactapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.contactapp.Contact;

import java.util.List;

@Dao
public interface ContactDOA {

    @Insert(onConflict = OnConflictStrategy.REPLACE) // TODO check for dup
    void insert(Contact contact);

    @Insert
    void insertMultiple(List<Contact> contacts);

    @Update(entity = Contact.class)
    void update(Contact contact);

    @Delete
    void delete(Contact contact);

    @Query("SELECT * FROM contacts")
    List<Contact> getAllContacts();

    @Query("SELECT * FROM contacts WHERE name= :inName")
    Contact getByName(String inName);

    @Query("UPDATE contacts SET name= :newName WHERE name= :oldName")
    void updateName(String oldName, String newName);

    @Query("DELETE FROM contacts")
    void deleteAll();
}