package com.example.contactapp;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ContactVH extends RecyclerView.ViewHolder {

    public TextView name, phone, email;
    public ImageView pfp, delete;
    public ContactVH(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.name);
        phone = itemView.findViewById(R.id.phone);
        email = itemView.findViewById(R.id.email);
        pfp = itemView.findViewById(R.id.pfp);
        delete = itemView.findViewById(R.id.delete);

        TextView[] fields = new TextView[] {
                name, phone, email
        };

    }
}
