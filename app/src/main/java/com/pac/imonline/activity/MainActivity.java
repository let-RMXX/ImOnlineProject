package com.pac.imonline.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pac.imonline.R;


public class MainActivity extends AppCompatActivity {

    ImageView imageBtnProfile;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerViewMain);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Toolbar Config
        Toolbar toolbar = findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);

        imageBtnProfile = findViewById(R.id.imageViewProfile);
        imageBtnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Intent intent = new Intent(MainActivity.this, ProfileActivityMain.class);
                //startActivity(intent);

            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){

                case R.id.botton_communities:
                    startActivity(new Intent(getApplicationContext(), CommunityListActivity.class));
                    overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                    finish();
                    return true;

                case R.id.botton_profile:
                    //startActivity(new Intent(getApplicationContext(),ProfileActivityMain.class));
                    overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.botton_chat:
                    //startActivity(new Intent(getApplicationContext(), ContactMain.class));
                    overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                    finish();
                    return true;
            }
            return false;
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);

    }
}