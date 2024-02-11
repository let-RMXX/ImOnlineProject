package com.pac.imonline.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pac.imonline.R;
import com.pac.imonline.activity.Database.AppDatabase;
import com.pac.imonline.activity.Entities.PostEntity;
import com.pac.imonline.activity.adapter.CommentsAdapter;
import com.pac.imonline.activity.Models.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Comment> list;
    private CommentsAdapter adapter;
    private int postId = 0;
    public static int postPosition = 0;
    private SharedPreferences preferences;
    private EditText txtAddComment;
    private ProgressDialog dialog;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        init();
        getPostsFromDatabase();
    }

    private void init() {
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        postPosition = getIntent().getIntExtra("postPosition", -1);
        preferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        recyclerView = findViewById(R.id.recyclerComments);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postId = getIntent().getIntExtra("postId", 0);
        appDatabase = AppDatabase.getAppDatabase(this);
    }

    private void getPostsFromDatabase() {
        appDatabase.postDao().getAllPosts().observe(this, new Observer<List<PostEntity>>() {
            @Override
            public void onChanged(List<PostEntity> posts) {
                // Update UI or perform actions with the posts data
                // For now, we just log the posts size
                if (posts != null) {
                    // Adjust this to your requirement
                    for (PostEntity post : posts) {
                        if (post.getId() == postId) {
                            // Found the post, now update UI or perform actions
                            // For example, you can get comments for this post from API
                            // getComments();
                            break;
                        }
                    }
                }
            }
        });
    }

    // Method to fetch comments from API (if needed)

    public void goBack(View view) {
        super.onBackPressed();
    }

    public void addComment(View view) {
        // Method to add a new comment
    }
}
