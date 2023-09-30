package com.example.contactapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactVH> {

    private MainActivity activity;

    public ContactListAdapter(MainActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public ContactVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.contact, parent, false);
        return new ContactVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactVH holder, int position) {

        List<Contact> contacts = activity.getDao().getAllContacts();

        Contact contact = contacts.get(holder.getAdapterPosition());

        holder.email.setText(contact.getEmail());
        holder.name.setText(contact.getName());
        holder.phone.setText(contact.getPhone());

        Configuration configuration = activity.getResources().getConfiguration();
        if ((configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
            holder.delete.setImageResource(R.drawable.trash_light);
        }

        holder.delete.setOnClickListener(v -> {
            activity.getDao().delete(contact);
            notifyItemRemoved(holder.getAdapterPosition());
        });

        contact.loadPicture(holder.pfp);

        holder.itemView.setOnClickListener(v -> {

            if (activity != null) {
                Log.i("contacts", "pressed on user " + position);

                Bundle bundle = new Bundle();
                bundle.putString("name", holder.name.getText().toString());
                bundle.putString("email", holder.email.getText().toString());
                bundle.putString("phone", holder.phone.getText().toString());
                bundle.putInt("position", position);
                bundle.putLong("id", contact.getId());

                EditContactFragment editContactFragment = new EditContactFragment();
                editContactFragment.setArguments(bundle);

                activity.loadFragment(editContactFragment, R.id.contact_list); // smaller ??
            }
        });
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (activity.getDao().getAllContacts() != null) {
            size = activity.getDao().getAllContacts().size();
        }
        return size;
    }

}
