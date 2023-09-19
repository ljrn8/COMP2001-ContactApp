package com.example.contactapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ContactListAdapter extends RecyclerView.Adapter<ContactVH> {

    @NonNull
    @Override
    public ContactVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.contact, parent, false);
        return new ContactVH(view);
    }


    // TODO
    public Contact contacts[] = {
            new Contact("John", "287542387", "kys@gmail.com"),
            new Contact("No", "287542387", "kys@gmail.com"),
            new Contact("Jim", "287542387", "kys@gmail.com"),
            new Contact("John", "287542387", "kys@gmail.com"),
            new Contact("No", "287542387", "kys@gmail.com"),
            new Contact("Jim", "287542387", "kys@gmail.com"),
            new Contact("John", "287542387", "kys@gmail.com"),
            new Contact("No", "287542387", "kys@gmail.com"),
            new Contact("Jim", "287542387", "kys@gmail.com"),
            new Contact("John", "287542387", "kys@gmail.com"),
            new Contact("No", "287542387", "kys@gmail.com"),
            new Contact("Jim", "287542387", "kys@gmail.com"),
            new Contact("Ethan", "287542387", "kys@gmail.com")
    };

    @Override
    public void onBindViewHolder(@NonNull ContactVH holder, int position) {
        Contact contact = contacts[position];

        holder.email.setText(contact.email);
        holder.name.setText(contact.name);
        holder.phone.setText(contact.phone);
    }

    @Override
    public int getItemCount() {
        return contacts.length;
    }
}
