package com.pac.imonline.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pac.imonline.R;
import com.pac.imonline.activity.AddPostActivity;
import com.pac.imonline.activity.Database.AppDatabase;
import com.pac.imonline.activity.Entities.UserEntity;
import com.pac.imonline.activity.Fragments.AccountFragment;
import com.pac.imonline.activity.Fragments.HomeFragment;

public class HomeActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private SharedPreferences sharedPreferences;

    private FloatingActionButton fab;
    private BottomNavigationView navigationView;
    private static final int GALLERY_ADD_POST = 2;

    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d("HomeActivity", "onCreate: Creating HomeActivity");

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frameHomeContainer, new HomeFragment(), HomeFragment.class.getSimpleName()).commit();

        sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);

        appDatabase = AppDatabase.getAppDatabase(this);

        if (appDatabase == null) {
            Log.e("HomeActivity", "onCreate: AppDatabase is null");
        } else {
            Log.d("HomeActivity", "onCreate: AppDatabase initialized successfully");
        }

        init();
        loadUserData();
    }

    private void init() {
        navigationView = findViewById(R.id.bottom_nav);
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            startActivityForResult(i, GALLERY_ADD_POST);
        });

        navigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_home: {
                        Fragment account = fragmentManager.findFragmentByTag(AccountFragment.class.getSimpleName());
                        if (account != null) {
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(AccountFragment.class.getSimpleName())).commit();
                            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(HomeFragment.class.getSimpleName())).commit();
                        }
                        break;
                    }

                    case R.id.item_account: {
                        Fragment account = fragmentManager.findFragmentByTag(AccountFragment.class.getSimpleName());
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(HomeFragment.class.getSimpleName())).commit();

                        if (account != null) {
                            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(AccountFragment.class.getSimpleName())).commit();
                        } else {
                            // Pass sharedPreferences data to AccountFragment
                            Bundle bundle = new Bundle();
                            bundle.putString("name", sharedPreferences.getString("name", ""));
                            bundle.putString("lastname", sharedPreferences.getString("lastname", ""));
                            bundle.putString("photoUrl", sharedPreferences.getString("photoUrl", ""));
                            AccountFragment accountFragment = new AccountFragment();
                            accountFragment.setArguments(bundle);

                            fragmentManager.beginTransaction().add(R.id.frameHomeContainer, accountFragment, AccountFragment.class.getSimpleName()).commit();
                        }
                        break;
                    }
                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void loadUserData() {
        new AsyncTask<Void, Void, UserEntity>() {
            @Override
            protected UserEntity doInBackground(Void... voids) {
                try {
                    return appDatabase.userDao().getUserByEmail(sharedPreferences.getString("email", ""));
                } catch (Exception e) {
                    Log.e("HomeActivity", "Error retrieving user data", e);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(UserEntity user) {
                if (user != null) {
                    int userId = user.getId();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("userId", userId);
                    editor.apply();
                    boolean applyResult = editor.commit();
                    Log.d("HomeActivity", "SharedPreferences apply result: " + applyResult);
                    Log.d("HomeActivity", "User ID retrieved and stored successfully: " + userId);
                    Log.d("HomeActivity", "Retrieved user's email: " + user.getEmail());
                } else {
                    Log.e("HomeActivity", "Failed to retrieve user ID");
                }
            }
        }.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_ADD_POST && resultCode == RESULT_OK) {
            Uri imgUri = data.getData();
            Intent i = new Intent(HomeActivity.this, AddPostActivity.class);
            i.setData(imgUri);
            startActivity(i);
        }
    }
}