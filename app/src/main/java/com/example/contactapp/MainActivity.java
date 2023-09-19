package com.example.contactapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(new ContactListFragment(), R.id.contact_list);
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

}