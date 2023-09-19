package com.example.contactapp;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ContactVH extends RecyclerView.ViewHolder {

    public TextView name, phone, email;
    public ContactVH(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.name);
        phone = itemView.findViewById(R.id.phone);
        email = itemView.findViewById(R.id.email);

    }
}
