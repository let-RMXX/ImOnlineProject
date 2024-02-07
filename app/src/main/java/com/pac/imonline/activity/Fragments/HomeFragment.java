package com.pac.imonline.activity.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.pac.imonline.activity.Api.ApiService;
import com.pac.imonline.activity.HomeActivity;
import com.pac.imonline.activity.Models.Posts;
import com.pac.imonline.activity.Api.RetrofitClient;
import com.pac.imonline.activity.adapter.PostsAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private View view;
    public static RecyclerView recyclerView;
    public static ArrayList<Posts> arrayList;
    private SwipeRefreshLayout refreshLayout;
    private PostsAdapter postsAdapter;
    private MaterialToolbar toolbar;
    private SharedPreferences sharedPreferences;
    private ApiService apiService;

    public HomeFragment() {}

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

        // Dynamically check and set toolbar if the parent activity is HomeActivity
        if (getActivity() instanceof HomeActivity) {
            ((HomeActivity) getActivity()).setSupportActionBar(toolbar);
        }

        setHasOptionsMenu(true);
        apiService = RetrofitClient.createService();
        getPosts();

        refreshLayout.setOnRefreshListener(() -> getPosts());
    }

    private void getPosts() {
        arrayList = new ArrayList<>();
        refreshLayout.setRefreshing(true);

        Call<List<Posts>> call = apiService.getPosts("Bearer " + sharedPreferences.getString("token", ""));
        call.enqueue(new Callback<List<Posts>>() {
            @Override
            public void onResponse(Call<List<Posts>> call, Response<List<Posts>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    arrayList.addAll(response.body());
                    postsAdapter = new PostsAdapter(getContext(), arrayList);
                    recyclerView.setAdapter(postsAdapter);
                }
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Posts>> call, Throwable t) {
                t.printStackTrace();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (postsAdapter != null) {
                    postsAdapter.getFilter().filter(newText);
                }
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }
}
