package com.pac.imonline.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pac.imonline.R;
import com.pac.imonline.activity.Api.ApiService;
import com.pac.imonline.activity.Api.RetrofitClient;
import com.pac.imonline.activity.Fragments.HomeFragment;
import com.pac.imonline.activity.Models.Posts;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPostActivity extends AppCompatActivity {

    private int position = 0, id = 0;
    private EditText txtDesc;
    private Button btnSave;
    private ProgressDialog dialog;
    private SharedPreferences sharedPreferences;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        init();
    }

    private void init() {
        sharedPreferences = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
        txtDesc = findViewById(R.id.txtDescEditPost);
        btnSave = findViewById(R.id.btnEditPost);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        position = getIntent().getIntExtra("position", 0);
        id = getIntent().getIntExtra("postId", 0);
        txtDesc.setText(getIntent().getStringExtra("text"));
        apiService = RetrofitClient.createService();

        btnSave.setOnClickListener(view -> {
            if (!txtDesc.getText().toString().isEmpty()) {
                savePost();
            }
        });
    }

    private void savePost() {
        dialog.setMessage("Saving");
        dialog.show();

        apiService.updatePost("Bearer " + sharedPreferences.getString("token", ""), id, txtDesc.getText().toString())
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            // Update the post in the recycler view
                            Posts post = HomeFragment.arrayList.get(position);
                            post.setDesc(txtDesc.getText().toString());
                            HomeFragment.arrayList.set(position, post);
                            HomeFragment.recyclerView.getAdapter().notifyItemChanged(position);
                            HomeFragment.recyclerView.getAdapter().notifyDataSetChanged();
                            finish();
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        t.printStackTrace();
                        dialog.dismiss();
                    }
                });
    }

    public void cancelEdit(View view) {
        super.onBackPressed();
    }
}
