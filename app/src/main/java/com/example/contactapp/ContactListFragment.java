package com.example.contactapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ContactListFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public ContactListAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);

        MainActivity activity = (MainActivity) requireActivity();

        /*ContactDOA dao = ContactDataBase
                .getDataBase(activity.getApplicationContext())
                .contactDOA();
        */

        RecyclerView rv = view.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ContactListAdapter(activity);
        rv.setAdapter(adapter);


        Button addContact = view.findViewById(R.id.addContact);
        addContact.setOnClickListener(v -> ((MainActivity) requireActivity()).loadFragment(
                new EditContactFragment(), R.id.contact_list)
        );

        return view;
    }
}