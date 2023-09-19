package com.example.contactapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditContactFragment extends Fragment {


    private String name, email, phone;
    private int position;
    private boolean createUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            name = arguments.getString("name");
            email = arguments.getString("email");
            phone = arguments.getString("phone");
            position = arguments.getInt("position");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_edit_contact, container, false);


        createUser = (name == null);

        TextView title = view.findViewById(R.id.title);
        EditText nameEdit = view.findViewById(R.id.enter_name);
        EditText phoneEdit = view.findViewById(R.id.enter_phone);
        EditText emailEdit = view.findViewById(R.id.enter_email);
        Button save = view.findViewById(R.id.save);

        if (createUser) {
            title.setText("Create Contact");

        } else {
            title.setText(name);
            nameEdit.setText(name);
            phoneEdit.setText(phone);
            emailEdit.setText(email);
        }

        save.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).loadFragment(new ContactListFragment(), R.id.contact_list);
        });

        return view;
    }
}