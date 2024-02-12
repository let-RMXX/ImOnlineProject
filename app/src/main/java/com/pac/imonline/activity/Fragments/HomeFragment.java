package com.pac.imonline.activity.Fragments;

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
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.pac.imonline.R;
import com.pac.imonline.activity.Database.AppDatabase;
import com.pac.imonline.activity.Entities.PostEntity;
import com.pac.imonline.activity.HomeActivity;
import com.pac.imonline.activity.Models.Posts;
import com.pac.imonline.activity.adapter.PostsAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private PostsAdapter postsAdapter;
    private MaterialToolbar toolbar;
    private AppDatabase appDatabase;
    private List<PostEntity> postEntities;

    @NonNull
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_home, container, false);
        init();
        return view;
    }

    private void init() {
        appDatabase = AppDatabase.getAppDatabase(getContext());
        recyclerView = view.findViewById(R.id.recyclerHome);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        refreshLayout = view.findViewById(R.id.swipeHome);
        toolbar = view.findViewById(R.id.toolbarHome);

        // Set toolbar if the parent activity is HomeActivity
        if (getActivity() instanceof HomeActivity) {
            ((HomeActivity) getActivity()).setSupportActionBar(toolbar);
        }

        setHasOptionsMenu(true);

        refreshLayout.setOnRefreshListener(this::getPostsFromDatabase);
        getPostsFromDatabase();
    }

    private void getPostsFromDatabase() {
        refreshLayout.setRefreshing(true);

        // Observe changes in postEntities
        appDatabase.postDao().getAllPosts().observe(getViewLifecycleOwner(), new Observer<List<PostEntity>>() {
            @Override
            public void onChanged(List<PostEntity> entities) {
                // Update the UI with the new data
                postEntities = entities;

                // Convert PostEntity objects to Posts objects
                ArrayList<Posts> postsList = convertToPostsList(postEntities);

                // Update RecyclerView with fetched posts
                if (postsAdapter == null) {
                    postsAdapter = new PostsAdapter(getContext(), postsList);
                    recyclerView.setAdapter(postsAdapter);
                } else {
                    postsAdapter.updateList(postsList);
                }

                refreshLayout.setRefreshing(false);
            }
        });
    }

    private ArrayList<Posts> convertToPostsList(List<PostEntity> postEntities) {
        ArrayList<Posts> postsList = new ArrayList<>();
        for (PostEntity entity : postEntities) {
            Posts post = new Posts();
            post.setId(entity.getId());
            post.setDescription(entity.getDescription());
            postsList.add(post);
        }
        return postsList;
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
