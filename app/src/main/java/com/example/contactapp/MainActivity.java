package com.example.contactapp;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ContactListFragment contactListFragment = new ContactListFragment();

    private ContactDOA dao = null;

    public static final int REQUEST_READ_CONTACT_PERMISSION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        dao = ContactDataBase
                .getDataBase(getApplicationContext())
                .contactDOA();


        loadFragment(contactListFragment, R.id.contact_list);

    }



    private void restartDB() {
        dao.deleteAll();
        dao.insert(new Contact("John", "9764373468", "John@gmail.com"));
        dao.insert(new Contact("Jim", "946097235", "Jim@gmail.com"));
        dao.insert(new Contact("Samantha", "4344363468", "Sam@gmail.com"));
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_CONTACT_PERMISSION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Contact Reading Permission Granted",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public ContactDOA getDao() { return this.dao; }

}