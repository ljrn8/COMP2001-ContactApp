package com.example.contactapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ContactListFragment contactListFragment = new ContactListFragment();

    private ContactDOA dao = null;

    private static final int REQUEST_READ_CONTACT_PERMISSION = 3;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dropdown, menu);
        return true;
    }


//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//
//            case R.id.choose_contact:
//                Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
//                return true;
//
//            case R.id.all_contacts:
//                Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    public ContactDOA getDao() { return dao; }

}