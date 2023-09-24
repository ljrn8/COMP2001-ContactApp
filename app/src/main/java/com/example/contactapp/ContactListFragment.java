package com.example.contactapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Objects;

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


        RecyclerView rv = view.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ContactListAdapter(activity);
        rv.setAdapter(adapter);


        Button addContact = view.findViewById(R.id.addContact);
        addContact.setOnClickListener(v -> ((MainActivity) requireActivity()).loadFragment(
                new EditContactFragment(), R.id.contact_list)
        );


        ActivityResultLauncher<Intent> pickContactLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;

                        int id = processPickContactResult(data);
                        getAddress(data, id);
                    }
                }
        );

        Button importContacts = view.findViewById(R.id.import_contacts);
        importContacts.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setData(ContactsContract.Contacts.CONTENT_URI);
            pickContactLauncher.launch(intent);
        });

        return view;
    }

    // from lecture slides week 7
    private int processPickContactResult(Intent data) {
        String tag = "importing";

        Uri contactUri = data.getData();
        String[] queryFields = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                // ContactsContract.CommonDataKinds.Email.ADDRESS,
                //ContactsContract.CommonDataKinds.Phone.NUMBER
        };

        // String whereAddressClause = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?";

        assert contactUri != null;
        Cursor c = requireActivity().getContentResolver().query(contactUri, queryFields, null, null, null);

        int id = 0;
        try {
            if (c.getCount() > 0) {
                c.moveToFirst();

                Log.i(tag, "\n--- got contact ---");
                Log.i(tag, c.getInt(0) + "");
                Log.i(tag, c.getString(1));
                id = c.getInt(0);
                // Log.i(tag, c.getString(2));
                // Log.i(tag, c.getString(3));

                // this.contactID = c.getInt(0); // id
                String contactName = c.getString(1); // name
            }
        } finally {
            c.close();
        }
        return id;
    }

    private void getAddress(Intent data, int id) {
        String tag = "importing";

        Uri contactUri = data.getData();


        Uri emailUri = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String[] queryFields = new String[] {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
        };

        String whereClause = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?";
        String[] whereValues = new String [] {
                String.valueOf(id), // id <-
        };

        assert contactUri != null;
        Cursor c = requireActivity().getContentResolver().query(
                emailUri, queryFields, whereClause, whereValues, null
        );

        try {
            assert c != null;
            c.moveToFirst();

            do {
                Log.i(tag, "\n--- got contact Email ---");
                Log.i(tag, c.getString(0));
            } while(c.moveToNext());

        } finally {
            c.close();
        }
    }


}