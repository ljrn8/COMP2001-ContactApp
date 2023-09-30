package com.example.contactapp;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class EditContactFragment extends Fragment {


    private String name, email, phone;
    private int position;
    private boolean createContact;
    private long id;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            name = arguments.getString("name");
            email = arguments.getString("email");
            phone = arguments.getString("phone");
            position = arguments.getInt("position");
            id = arguments.getLong("id");
        }
    }

    private EditText nameEdit, phoneEdit, emailEdit;

    private TextView title, error;
    private Button save, photo;
    private ImageView capture;
    private List<Contact> contacts;

    private Bitmap image;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("name", nameEdit.getText().toString());
        outState.putString("phone", phoneEdit.getText().toString());
        outState.putString("email", emailEdit.getText().toString());

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            ((MainActivity) requireActivity()).loadFragment(new ContactListFragment(), R.id.contact_list);
            return true;

        } else if (item.getItemId() == R.id.choose_contact
                || item.getItemId() == R.id.all_contacts ) {
            Toast.makeText(requireContext(), "return to the contact list before importing", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // View view =  inflater.inflate(R.layout.fragment_edit_contact, container, false);

        View view;
        // int orientation = getResources().getConfiguration().orientation;
        view = inflater.inflate(R.layout.fragment_edit_contact, container, false);
        setHasOptionsMenu(true);

        ContactDOA dao = ((MainActivity) requireActivity()).getDao();
        List<Contact> contacts = dao.getAllContacts();

        createContact = (name == null);

        title = view.findViewById(R.id.title);
        error = view.findViewById(R.id.error);
        error.setVisibility(View.INVISIBLE);

        nameEdit = view.findViewById(R.id.enter_name);
        phoneEdit = view.findViewById(R.id.enter_phone);
        emailEdit = view.findViewById(R.id.enter_email);
        save = view.findViewById(R.id.save);

        capture = view.findViewById(R.id.capture);

        // menu toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        MainActivity activity = (MainActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        // update contact
        if (!createContact) {
            Contact contact = dao.getById(id);
            toolbar.setTitle(contact.getName());
            phoneEdit.setText(contact.getPhone());
            emailEdit.setText(contact.getEmail());
            nameEdit.setText(contact.getName());
            capture.setImageBitmap(contact.getPicture());

            // get the pfp
            image = contact.getPicture();
            if (image != null) capture.setImageBitmap(image);
            else { capture.setImageResource(R.drawable.baseline_person_24); }

            save.setOnClickListener(v -> {
                if (validateInput(contacts)) {
                    Contact current = dao.getById(id);
                    current.setName(nameEdit.getText().toString());
                    current.setEmail(emailEdit.getText().toString());
                    current.setPhone(phoneEdit.getText().toString());
                    current.setPicture(image);
                    dao.update(current);

                    ((MainActivity) requireActivity()).loadFragment(new ContactListFragment(), R.id.contact_list);
                }
            });

        // add a contact
        } else {
            toolbar.setTitle("Create Contact");
            capture.setImageResource(R.drawable.baseline_person_24);
            save.setOnClickListener(v -> {
                if (validateInput(contacts)) {
                    Contact contact = new Contact(
                            nameEdit.getText().toString(),
                            phoneEdit.getText().toString(),
                            emailEdit.getText().toString()
                    );
                    contact.setPicture(image);
                    dao.insert(contact);
                    ((MainActivity) requireActivity()).loadFragment(new ContactListFragment(), R.id.contact_list);
                }
            });
        }

        // image capture display (lecture slides)
        ActivityResultLauncher<Intent> photoLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    image = (Bitmap) data.getExtras().get("data");
                    if (image != null) {
                        capture.setImageBitmap(image);
                    }
                }
            }
        );

        // image capture button
        photo = view.findViewById(R.id.get_picture);
        photo.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            photoLauncher.launch(intent);
        });

        return view;
    }


    private boolean validateInput(List<Contact> contacts) {
        String inName = nameEdit.getText().toString();
        String inPhone = phoneEdit.getText().toString();
        String inEmail = emailEdit.getText().toString();

        String checkInputs[] = { inName, inPhone };
        for (String input: checkInputs) {
            if (input == null || input.equals("")) {
                Toast.makeText(requireContext(),
                        "name and phone are required",
                        Toast.LENGTH_LONG).show();
                return false;
            }
            if (input.length() > 30) {
                Toast.makeText(requireContext(),
                        "keep name and phone number below 30 characters",
                        Toast.LENGTH_LONG).show();
                return false;
            }
        }
        for (Contact contact: contacts) {
            if (contact.getName().equals(inName) && !contact.getName().equals(name)) {
                Toast.makeText(requireContext(),
                        "name '" + contact.getName() + "' is taken",
                        Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }
}