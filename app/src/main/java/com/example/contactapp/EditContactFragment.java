package com.example.contactapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class EditContactFragment extends Fragment {


    private String name, email, phone;
    private int position;
    private boolean createContact;

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

    private EditText nameEdit, phoneEdit, emailEdit;
    private TextView title, error;
    private Button save;
    private List<Contact> contacts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_edit_contact, container, false);

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


        // update contact
        if (!createContact) {
            title.setText(name);
            nameEdit.setText(name);
            phoneEdit.setText(phone);
            emailEdit.setText(email);

            save.setOnClickListener(v -> {
                if (validateInput(contacts)) {
                    Contact current = dao.getByName(name);
                    current.setName(nameEdit.getText().toString());
                    current.setEmail(emailEdit.getText().toString());
                    current.setPhone(phoneEdit.getText().toString());

                    dao.update(current); // TODO ????

                    /*Log.i("kys", "==============" + current.toString() + "================");
                    for (Contact contact: contacts) {
                        Log.i("kys", contact.toString());
                    }*/


                    ((MainActivity) requireActivity()).loadFragment(new ContactListFragment(), R.id.contact_list);
                }
            });

        // add a contact
        } else {
            title.setText("Create Contact");
            save.setOnClickListener(v -> {
                if (validateInput(contacts)) {
                    Contact contact = new Contact(
                            nameEdit.getText().toString(),
                            phoneEdit.getText().toString(),
                            emailEdit.getText().toString()
                    );
                    dao.insert(contact);
                    ((MainActivity) requireActivity()).loadFragment(new ContactListFragment(), R.id.contact_list);
                }
            });
        }
        return view;
    }


    boolean validateInput(List<Contact> contacts) {
        String inName = nameEdit.getText().toString();
        String inPhone = phoneEdit.getText().toString();
        String inEmail = emailEdit.getText().toString();

        String inputs[] = { inName, inPhone, inEmail };
        for (String input: inputs) {
            if (input.equals("") || input.equals(null)) {
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