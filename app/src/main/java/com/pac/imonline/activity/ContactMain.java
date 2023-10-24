package com.pac.imonline.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pac.imonline.R;

import java.util.List;

public class ContactMain extends AppCompatActivity implements ContactAdapter.ContactAdapterEventListener{
    private ContactAdapter contactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewContacts);
        AppDatabase db = AppDatabase.getInstance(this);
        ContactDao contactDao = db.getContactDao();
        this.contactAdapter = new ContactAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(this.contactAdapter);
        recyclerView.setLayoutManager(layoutManager);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
        switch (item.getItemId()){

            case R.id.botton_home:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                finish();
                return true;

            case R.id.botton_communities:
                startActivity(new Intent(getApplicationContext(), CommunityListActivity.class));
                overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                finish();
                return true;

            case R.id.botton_profile:
                startActivity(new Intent(getApplicationContext(), ProfileActivityMain.class));
                overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                finish();
                return true;
        }

        return false;
    });
    }

    @Override
    public void onStart(){
        super.onStart();
        List<Contact> newContactList = AppDatabase.getInstance(this).getContactDao().getAll();
        this.contactAdapter.refreshContactList(newContactList);
    }

    public void onContactClicked(long contactId){
        ContactDetailsActivity.startActivity(this, contactId);
    }
}