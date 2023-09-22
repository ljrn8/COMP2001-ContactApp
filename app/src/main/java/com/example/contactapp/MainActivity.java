package com.example.contactapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    ContactListFragment contactListFragment = new ContactListFragment();

    private ContactDOA dao = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dao = ContactDataBase
                .getDataBase(getApplicationContext())
                .contactDOA();

        // restartDB();

        loadFragment(contactListFragment, R.id.contact_list);
    }

    // default users - change later
    private void restartDB() {
        dao.deleteAll();
        dao.insert(new Contact("john", "64373468", "kys@gmail.com"));
        dao.insert(new Contact("john2", "64373468", "kys@gmail.com"));
        dao.insert(new Contact("jim", "64363468", "kys@gmail.com"));
        dao.insert(new Contact("no", "64363468", "kys@gmail.com"));
    }

    public void loadFragment(Fragment frag, int container){
        FragmentManager fm = getSupportFragmentManager();
        Fragment frame = fm.findFragmentById(container);
        if(frame == null) {
            fm.beginTransaction().add(container, frag).commit();
        } else {
            fm.beginTransaction().replace(container,frag).commit();
        }
    }

    public ContactDOA getDao() { return dao; }

}