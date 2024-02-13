package com.pac.imonline.activity.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.pac.imonline.R;
import com.pac.imonline.activity.AuthActivity;
import com.pac.imonline.activity.Constant;
import com.pac.imonline.activity.Database.AppDatabase;
import com.pac.imonline.activity.EditUserInfoActivity;
import com.pac.imonline.activity.Entities.PostEntity;
import com.pac.imonline.activity.Entities.UserEntity;
import com.pac.imonline.activity.HomeActivity;
import com.pac.imonline.activity.Models.Posts;
import com.pac.imonline.activity.Models.Posts;
import com.pac.imonline.activity.Models.User;
import com.pac.imonline.activity.adapter.AccountPostAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment {

    private View view;
    private MaterialToolbar toolbar;
    private CircleImageView imgProfile;
    private TextView txtName, txtPostsCount;
    private Button btnEditAccount;
    private RecyclerView recyclerView;
    private ArrayList<Posts> arrayList;
    private SharedPreferences preferences;
    private AccountPostAdapter adapter;
    private String imgUrl = "";
    private AppDatabase appDatabase;

    public AccountFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_account, container, false);
        init();
        return view;
    }

    private void init() {
        preferences = getContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String name = preferences.getString("name", "");
        String lastName = preferences.getString("lastname", "");
        String photoUrl = preferences.getString("photoUrl", "");
        int userId = preferences.getInt("userId", -1);

        appDatabase = AppDatabase.getAppDatabase(getContext()); // Initialize AppDatabase here

        toolbar = view.findViewById(R.id.toolbarAccount);
        ((HomeActivity) getContext()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        imgProfile = view.findViewById(R.id.imgAccountProfile);
        txtName = view.findViewById(R.id.txtAccountName);
        txtPostsCount = view.findViewById(R.id.txtAccountPostCount);
        recyclerView = view.findViewById(R.id.recyclerAccount);
        btnEditAccount = view.findViewById(R.id.btnEditAccount);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        txtName.setText(name + " " + lastName);
        // Load profile photo using Picasso or any other image loading library
        if (!photoUrl.isEmpty()) {
            Picasso.get().load(photoUrl).into(imgProfile);
        }

        btnEditAccount.setOnClickListener(v -> {
            Intent i = new Intent(((HomeActivity) getContext()), EditUserInfoActivity.class);
            i.putExtra("userId", userId);
            i.putExtra("imgUrl", imgUrl);
            startActivity(i);
        });

        getData();
    }

    @SuppressLint("StaticFieldLeak")
    private void getData() {
        new AsyncTask<Void, Void, UserEntity>() {
            @Override
            protected UserEntity doInBackground(Void... voids) {
                return appDatabase.userDao().getUserByEmail(preferences.getString("email", ""));
            }

            @Override
            protected void onPostExecute(UserEntity user) {
                if (user != null) {
                    Log.d("AccountFragment", "User data retrieved: " + user.getName() + " " + user.getLastName());
                    new AsyncTask<Void, Void, List<PostEntity>>() {
                        @Override
                        protected List<PostEntity> doInBackground(Void... voids) {
                            return appDatabase.postDao().getPostsByUserId(user.getId());
                        }

                        @Override
                        protected void onPostExecute(List<PostEntity> postEntities) {
                            List<Posts> posts = convertToPosts(postEntities);
                            updateUI(posts, user);
                        }
                    }.execute();
                }
            }
        }.execute();
    }


    private List<Posts> convertToPosts(List<PostEntity> postEntities) {
        List<Posts> posts = new ArrayList<>();
        for (PostEntity postEntity : postEntities) {
            Posts post = new Posts();
            post.setId(postEntity.getId());
            post.setLikes(postEntity.getLikes());
            post.setComments(postEntity.getComments());
            post.setDate(postEntity.getDate());
            post.setDesc(postEntity.getDescription());
            post.setPhoto(postEntity.getPhoto());
            posts.add(post);
        }
        return posts;
    }

    private void updateUI(List<Posts> posts, UserEntity user) {
        arrayList = new ArrayList<>(posts);
        txtName.setText(user.getName() + " " + user.getLastName());
        txtPostsCount.setText(String.valueOf(arrayList.size()));
        Picasso.get().load(Constant.URL + "storage/profiles/" + user.getPhotoData()).into(imgProfile);
        adapter = new AccountPostAdapter(getContext(), arrayList);
        recyclerView.setAdapter(adapter);
        imgUrl = Constant.URL + "storage/profiles/" + user.getPhotoData();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_account, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_logout: {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Do you want to logout?");
                builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        logout();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                    }
                });
                builder.show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        // Clear SharedPreferences and navigate to AuthActivity
        clearSharedPreferences();
        navigateToAuthActivity();
    }

    private void clearSharedPreferences() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    private void navigateToAuthActivity() {
        startActivity(new Intent(((HomeActivity) getContext()), AuthActivity.class));
        ((HomeActivity) getContext()).finish();
    }
}