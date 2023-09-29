package com.example.contactapp;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.Manifest;
import android.widget.Toast;


import java.util.List;
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
                        Contact newContact = processPickContactResult(data);

                        boolean duplicate = false;
                        ContactDOA dao = activity.getDao();

                        for (Contact contact: dao.getAllContacts()) {
                            if (Objects.equals(contact.getName(), newContact.getName())) {
                                Toast.makeText(requireContext(),
                                        "Imported contact has same name as existing contact " + contact.getName(),
                                        Toast.LENGTH_LONG).show();
                                duplicate = true;
                            }
                        }

                        if (newContact == null) {
                            Toast.makeText(requireContext(),
                                    "Something when wrong and the contact wasn't imported",
                                    Toast.LENGTH_LONG).show();
                        } else if (!duplicate) {
                            activity.getDao().insert(newContact);
                            adapter.notifyDataSetChanged();
                        }
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


    private Contact processPickContactResult(Intent data) {
        String tag = "importing";
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[] { Manifest.permission.READ_CONTACTS }, 0);
        }

        Uri contactUri = data.getData();
        String[] queryFields = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
        };

        // String whereAddressClause = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?";

        int id = 0;
        Contact newContact = null;
        Cursor c = requireActivity().getContentResolver().query(
                contactUri, queryFields, null, null, null
        );

        try {
            if (c.getCount() > 0) {
                c.moveToFirst();

                Log.i(tag, "\n--- got contact ---");
                Log.i(tag, c.getInt(0) + "");
                Log.i(tag, c.getString(1));

                id = c.getInt(0);
                String name = c.getString(1);
                String email = getSelectedAddress(id);
                String phone = getSelectedPhoneNumber(id);
                newContact = new Contact(name, phone, email);

            }
        } finally {
            c.close();
        }
        return newContact;
    }

    private String getSelectedAddress(int id) {
        String tag = "importing";


        Uri emailUri = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String[] queryFields = new String[] {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
        };

        String whereClause = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?";
        String[] whereValues = new String [] {
                String.valueOf(id),
        };

        Cursor c = requireActivity().getContentResolver().query(
                emailUri, queryFields, whereClause, whereValues, null
        );

        if (c == null || !c.moveToFirst()) {
            c.close();
            Log.i(tag, "\n--- stopped ---");
            return null;
        }

        String address;
        try {
            c.moveToFirst();

            do {
                Log.i(tag, "\n--- got contact Email ---");
                Log.i(tag, c.getString(0));

                address = c.getString(0);

            } while(c.moveToNext());

        } finally {
            if (c != null) {
                c.close();
            }
        }
        return address;
    }


    private String getSelectedPhoneNumber(int id) {
        String[] phoneProjection = new String[] {
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };
        String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?";
        String[] selectionArgs = new String[] { String.valueOf( id ) };

        Cursor phoneCursor = null;
        String phone = null;
        try {
            phoneCursor = requireActivity().getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    phoneProjection,
                    selection,
                    selectionArgs,
                    null
            );
            if (phoneCursor != null && phoneCursor.moveToFirst()) {
                do {
                    String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Log.i("importing", "Phone Number: " + phoneNumber);
                    phone = phoneNumber;

                } while (phoneCursor.moveToNext());
            }
        } finally {
            if (phoneCursor != null) {
                phoneCursor.close();
            }
        }
        return phone;
    }

    /*
    private void getContacts() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[] { Manifest.permission.READ_CONTACTS }, 0);
        }

        ContentResolver contentResolver = requireActivity().getContentResolver();
        Cursor cursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        if (cursor == null) { return; }
        if (cursor.moveToFirst()) {
            do  {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                // Retrieve email addresses using a separate query
                // https://stackoverflow.com/questions/11669069/get-email-address-from-contact-list
                String contactID = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                Cursor emailCursor = requireContext().getContentResolver().query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=?",
                        new String[]{ contactID }, null
                );

                int emailIdx = cursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.Email.DATA);

                String emailAddress = null;
                if (emailCursor.moveToFirst()) {
                    emailAddress = emailCursor.getString(emailIdx);
                    emailCursor.close();
                }

                // Do something with name, phone number, and email
                Log.d("ContactInfo", "Name: " + name + ", Phone: " + phoneNumber + ", Email: " + emailAddress);
                if (emailAddress != null) Log.d("ContactInfo", "found an aaddress !!!");

            } while (cursor.moveToNext());
        }

        cursor.close();


    } // Toast for errors
    */
}