package com.pac.imonline.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pac.imonline.R;
import com.pac.imonline.activity.Fragments.HomeFragment;
import com.pac.imonline.activity.Models.Posts;
import com.pac.imonline.activity.adapter.CommentsAdapter;
import com.pac.imonline.activity.Api.ApiService;
import com.pac.imonline.activity.Api.RetrofitClient;
import com.pac.imonline.activity.Models.Comment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Comment> list;
    private CommentsAdapter adapter;
    private int postId = 0;
    public static int postPosition = 0;
    private SharedPreferences preferences;
    private EditText txtAddComment;
    private ProgressDialog dialog;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        init();
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
        apiService = RetrofitClient.createService();
        getComments();
    }

    private void getComments() {
        list = new ArrayList<>();
        apiService.getComments("Bearer " + preferences.getString("token", ""), postId)
                .enqueue(new Callback<List<Comment>>() {
                    @Override
                    public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                        if (response.isSuccessful()) {
                            list.addAll(response.body());
                            adapter = new CommentsAdapter(CommentActivity.this, list);
                            recyclerView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Comment>> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    public void goBack(View view) {
        super.onBackPressed();
    }

    public void addComment(View view) {
        String commentText = txtAddComment.getText().toString();
        dialog.setMessage("Adding Comment");
        dialog.show();
        if (commentText.length() > 0) {
            apiService.addComment("Bearer " + preferences.getString("token", ""), postId, commentText)
                    .enqueue(new Callback<Comment>() {
                        @Override
                        public void onResponse(Call<Comment> call, Response<Comment> response) {
                            if (response.isSuccessful()) {
                                Comment newComment = response.body();
                                list.add(newComment);
                                adapter.notifyDataSetChanged();
                                txtAddComment.setText("");

                                // Update post comments count
                                Posts posts = HomeFragment.arrayList.get(postPosition);
                                posts.setComments(posts.getComments() + 1);
                                HomeFragment.arrayList.set(postPosition, posts);
                                HomeFragment.recyclerView.getAdapter().notifyDataSetChanged();
                            }
                            dialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<Comment> call, Throwable t) {
                            t.printStackTrace();
                            dialog.dismiss();
                        }
                    });
        }
    }
}
