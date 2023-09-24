package com.example.contactapp;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class EditContactFragment extends Fragment {


    private String name, email, phone;
    private int position;
    private boolean createContact;
    private long id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("kys", "reran onCreate");
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // View view =  inflater.inflate(R.layout.fragment_edit_contact, container, false);
        Log.i("kys", "reran onCreateView");


        View view;
        // int orientation = getResources().getConfiguration().orientation;
        view = inflater.inflate(R.layout.fragment_edit_contact, container, false);

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



        // update contact
        if (!createContact) {
            Contact contact = dao.getById(id);
            title.setText(contact.getName());
            phoneEdit.setText(contact.getPhone());
            emailEdit.setText(contact.getEmail());
            nameEdit.setText(contact.getName());
            capture.setImageBitmap(contact.getPicture());

            // get the pfp
            image = contact.getPicture();
            if (image != null) capture.setImageBitmap(image);
            else { capture.setImageResource(R.drawable.ic_launcher_foreground); }

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
            title.setText("Create Contact");
            capture.setImageResource(R.drawable.ic_launcher_foreground);

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


    boolean validateInput(List<Contact> contacts) {
        String inName = nameEdit.getText().toString();
        String inPhone = phoneEdit.getText().toString();
        String inEmail = emailEdit.getText().toString();

        String inputs[] = { inName, inPhone, inEmail };
        for (String input: inputs) {
            if (input == null || input.equals("")) {
                error.setText("cannot have empty input");
                error.setVisibility(View.VISIBLE);
                return false;
            }
        }


        for (Contact contact: contacts) {
            if (contact.getName().equals(inName) && !contact.getName().equals(name)) {
                error.setText("name is taken");
                error.setVisibility(View.VISIBLE);
                return false;
            }
        }
        return true;
    }
}