package com.example.contactapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactVH> {

    private MainActivity activity;
    private ContactDOA dao;


    public ContactListAdapter(MainActivity activity, ContactDOA dao) {
        this.activity = activity;
        this.dao = dao;
    }

    @NonNull
    @Override
    public ContactVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.contact, parent, false);
        return new ContactVH(view);
    }


    private List<Contact> contacts;
    @Override
    public void onBindViewHolder(@NonNull ContactVH holder, int position) {


        contacts = dao.getAllContacts();

        Contact contact = contacts.get(position); // TODO get adapter pos

        holder.email.setText(contact.getEmail());
        holder.name.setText(contact.getName());
        holder.phone.setText(contact.getPhone());

        holder.itemView.setOnClickListener(v -> {

            if (activity != null) {
                Log.i("contacts", "pressed on user " + position);

                Bundle bundle = new Bundle();
                bundle.putString("name", holder.name.getText().toString());
                bundle.putString("email", holder.email.getText().toString());
                bundle.putString("phone", holder.phone.getText().toString());
                bundle.putInt("position", position);

                EditContactFragment editContactFragment = new EditContactFragment();
                editContactFragment.setArguments(bundle);

                activity.loadFragment(editContactFragment, R.id.contact_list); // smaller ??
            }
        });
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (contacts != null) {
            size = contacts.size();
        }
        return size;
    }

}
