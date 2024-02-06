package com.pac.imonline.activity.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.pac.imonline.activity.Api.RetrofitClient;
import com.pac.imonline.activity.AuthActivity;
import com.pac.imonline.activity.Constant;
import com.pac.imonline.activity.EditUserInfoActivity;
import com.pac.imonline.activity.HomeActivity;
import com.pac.imonline.activity.Models.MyPostResponse;
import com.pac.imonline.activity.Models.Posts;
import com.pac.imonline.activity.Models.Posts;
import com.pac.imonline.activity.Models.User;
import com.pac.imonline.activity.adapter.AccountPostAdapter;
import com.pac.imonline.activity.Api.ApiService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private ApiService apiService;

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
        preferences = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        apiService = RetrofitClient.createService();
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

        btnEditAccount.setOnClickListener(v -> {
            Intent i = new Intent(((HomeActivity) getContext()), EditUserInfoActivity.class);
            i.putExtra("imgUrl", imgUrl);
            startActivity(i);
        });
    }

    private void getData() {
        apiService.getMyPosts("Bearer " + preferences.getString("token", "")).enqueue(new Callback<MyPostResponse>() {
            @Override
            public void onResponse(Call<MyPostResponse> call, Response<MyPostResponse> response) {
                if (response.isSuccessful()) {
                    MyPostResponse myPostResponse = response.body();
                    if (myPostResponse != null && myPostResponse.isSuccess()) {
                        List<Posts> posts = myPostResponse.getPosts();
                        updateUI(posts, myPostResponse.getUser());
                    }
                }
            }


            @Override
            public void onFailure(Call<MyPostResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void updateUI(List<Posts> posts, User user) {
        arrayList = new ArrayList<>(posts);
        txtName.setText(user.getName() + " " + user.getLastName());
        txtPostsCount.setText(String.valueOf(arrayList.size()));
        Picasso.get().load(Constant.URL + "storage/profiles/" + user.getPhoto()).into(imgProfile);
        adapter = new AccountPostAdapter(getContext(), arrayList);
        recyclerView.setAdapter(adapter);
        imgUrl = Constant.URL + "storage/profiles/" + user.getPhoto();
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
        apiService.logout("Bearer " + preferences.getString("token", "")).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    clearSharedPreferences();
                    navigateToAuthActivity();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            getData();
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }
}
