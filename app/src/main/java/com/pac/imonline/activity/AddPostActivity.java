package com.pac.imonline.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.pac.imonline.R;
import com.pac.imonline.activity.Database.AppDatabase;
import com.pac.imonline.activity.Entities.PostEntity;

import java.io.IOException;

public class AddPostActivity extends AppCompatActivity {

    private AppDatabase appDatabase;
    private SharedPreferences sharedPreferences;

    private Button btnPost;
    private ImageView imgPost;
    private EditText txtDesc;
    private Bitmap bitmap = null;
    private static final int GALLERY_CHANGE_POST = 3;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        init();

        sharedPreferences = getApplicationContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        appDatabase = AppDatabase.getAppDatabase(this);

        // Log the user ID retrieved from SharedPreferences
        int userId = sharedPreferences.getInt("userId", -1);
        if (userId != -1) {
            Log.d("AddPostActivity", "User ID retrieved successfully: " + userId);
        } else {
            Log.e("AddPostActivity", "User ID not found in SharedPreferences");
        }
    }


    private void init() {
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);

        btnPost = findViewById(R.id.btnAddPost);
        imgPost = findViewById(R.id.imgAddPost);
        txtDesc = findViewById(R.id.txtDescAddPost);

        imgPost.setImageURI(getIntent().getData());
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), getIntent().getData());
        } catch (IOException e) {
            e.printStackTrace();
        }

        btnPost.setOnClickListener(v -> {
            if (!txtDesc.getText().toString().isEmpty()) {
                post();
            } else {
                Toast.makeText(this, "Description is required", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void post() {
        dialog.setMessage("Posting");
        dialog.show();

        // Retrieve user ID from SharedPreferences
        int userId = sharedPreferences.getInt("userId", -1);

        // Check if user ID is valid
        if (userId != -1) {
            // Save post to Room Database asynchronously using AsyncTask
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    // Insert post into Room Database
                    PostEntity postEntity = new PostEntity();
                    postEntity.setDescription(txtDesc.getText().toString().trim());
                    postEntity.setImageUri("");
                    postEntity.setUserId(userId); // Set the user ID for the post
                    appDatabase.postDao().insert(postEntity);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    // After posting, navigate to HomeActivity
                    startActivity(new Intent(AddPostActivity.this, HomeActivity.class));
                    finish();
                    dialog.dismiss();
                }
            }.execute();
        } else {
            // If user ID is invalid, show an error message
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }

    public void cancelPost(View view) {
        super.onBackPressed();
    }

    public void changePhoto(View view) {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, GALLERY_CHANGE_POST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CHANGE_POST && resultCode == RESULT_OK) {
            Uri imgUri = data.getData();
            imgPost.setImageURI(imgUri);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}