package com.pac.imonline.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.pac.imonline.R;
import com.pac.imonline.activity.Api.ApiService;
import com.pac.imonline.activity.Api.RetrofitClient;
import com.pac.imonline.activity.Models.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPostActivity extends AppCompatActivity {

    private ApiService apiService;
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

        apiService = RetrofitClient.createService();
        sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
    }

    private void init() {
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);

        btnPost = findViewById(R.id.btnAddPost);
        imgPost = findViewById(R.id.imgAddPost);
        txtDesc = findViewById(R.id.txtDescAddPost);
        dialog.setCancelable(false);

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

    private void post() {
        dialog.setMessage("Posting");
        dialog.show();

        Call<Void> call = apiService.addPost("Bearer " + sharedPreferences.getString("token", ""),
                txtDesc.getText().toString().trim(), bitmapToString(bitmap));

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    User user = getCurrentUser();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("photo", user.getPhoto());
                    editor.apply();
                    startActivity(new Intent(AddPostActivity.this, HomeActivity.class));
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

    private User getCurrentUser() {
        User currentUser = new User();
        // Retrieve user information from SharedPreferences
        currentUser.setId(sharedPreferences.getInt("userId", 0));
        currentUser.setUserName(sharedPreferences.getString("userName", ""));
        currentUser.setPhoto(sharedPreferences.getString("userPhoto", ""));
        return currentUser;
    }


    private String bitmapToString(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] array = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(array, Base64.DEFAULT);
        }
        return "";
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
