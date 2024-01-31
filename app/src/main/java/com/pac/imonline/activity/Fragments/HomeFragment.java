package com.pac.imonline.activity.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.pac.imonline.R;
import com.pac.imonline.activity.Constant;
import com.pac.imonline.activity.HomeActivity;
import com.pac.imonline.activity.adapter.PostsAdapter;

import java.util.ArrayList;

import Models.Posts;
import Models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;

public class HomeFragment extends Fragment {

    private View view;
    public static RecyclerView recyclerView;
    public static ArrayList<Posts> arrayList;
    private SwipeRefreshLayout refreshLayout;
    private PostsAdapter postsAdapter;
    private MaterialToolbar toolbar;
    private SharedPreferences sharedPreferences;

    public HomeFragment() {
    }

    @NonNull
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_home, container, false);
        init();
        return view;
    }

    private void init() {
        sharedPreferences = getContext().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        recyclerView = view.findViewById(R.id.recyclerHome);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        refreshLayout = view.findViewById(R.id.swipeHome);
        toolbar = view.findViewById(R.id.toolbarHome);
        ((HomeActivity) getContext()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        getPosts();

        refreshLayout.setOnRefreshListener(() -> getPosts());
    }

    private void getPosts() {
        arrayList = new ArrayList<>();
        refreshLayout.setRefreshing(true);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.HOME)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        String token = sharedPreferences.getString("token", "");

        Call<ArrayList<Posts>> call = apiService.getPosts("Bearer " + token);
        call.enqueue(new Callback<ArrayList<Posts>>() {
            @Override
            public void onResponse(Call<ArrayList<Posts>> call, Response<ArrayList<Posts>> response) {
                if (response.isSuccessful()) {
                    arrayList = response.body();
                    postsAdapter = new PostsAdapter(getContext(), arrayList);
                    recyclerView.setAdapter(postsAdapter);
                }
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ArrayList<Posts>> call, Throwable t) {
                t.printStackTrace();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                postsAdapter.getFilter().filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    public interface ApiService {
        @GET(Constant.POSTS)
        Call<ArrayList<Posts>> getPosts(@Header("Authorization") String authorization);
    }
}