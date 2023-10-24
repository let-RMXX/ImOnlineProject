package com.pac.imonline.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pac.imonline.R;

import java.util.ArrayList;
import java.util.List;



public class CommunityListActivity extends AppCompatActivity {

    private static final int REQUEST_CREATE_COMMUNITY = 1;
    private static final int RESULT_COMMUNITY_CREATED = 3;

    private RecyclerView communityRecyclerView;
    private CommunityAdapter communityAdapter;
    private List<Community> communityList;
    private CommunityDao communityDao;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_list);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){

                case R.id.botton_home:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                    finish();
                    return true;

            }

            return false;
        });

        communityRecyclerView = findViewById(R.id.communityRecyclerView);
        communityRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        communityList = new ArrayList<>();
        communityAdapter = new CommunityAdapter(communityList);
        communityRecyclerView.setAdapter(communityAdapter);

        appDatabase = AppDatabase.getAppDatabase(this);
        communityDao = appDatabase.getCommunityDao();

        communityDao.getAllCommunities().observe(this, new Observer<List<Community>>() {
            @Override
            public void onChanged(List<Community> communities) {
                communityList.clear();
                communityList.addAll(communities);
                communityAdapter.notifyDataSetChanged();
            }
        });
    }

    public void onCreateCommunityClicked(View view) {
        Intent intent = new Intent(this, CreateCommunityActivity.class);
        startActivityForResult(intent, REQUEST_CREATE_COMMUNITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CREATE_COMMUNITY && resultCode == RESULT_COMMUNITY_CREATED && data != null) {
            Community community = data.getParcelableExtra("community");
            if (community != null) {
                communityList.add(community);
                communityAdapter.notifyItemInserted(communityList.size() - 1);
            }
        }
    }
}
