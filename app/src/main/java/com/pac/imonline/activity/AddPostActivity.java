package com.pac.imonline.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.pac.imonline.R;
import com.pac.imonline.activity.Constant;
import com.pac.imonline.activity.Fragments.HomeFragment;

import java.io.ByteArrayOutputStream;

import Models.Posts;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public class AddPostActivity extends AppCompatActivity {

    private Button btnPost;
    private ImageView imgPost;
    private EditText txtDesc;
    private Bitmap bitmap = null;
    private static final int GALLERY_CHANGE_POST = 3;
    private ProgressDialog dialog;
    private SharedPreferences preferences;

    private Retrofit retrofit;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        init();
    }

    private void init() {
        preferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);

        btnPost = findViewById(R.id.btnAddPost);
        imgPost = findViewById(R.id.imgAddPost);
        txtDesc = findViewById(R.id.txtDescAddPost);

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);

        imgPost.setImageURI(getIntent().getData());
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), getIntent().getData());
        } catch (Exception e) {
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

        String token = preferences.getString("token", "");

        RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), txtDesc.getText().toString().trim());

        MultipartBody.Part photoPart = null;
        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] array = byteArrayOutputStream.toByteArray();
            RequestBody photoBody = RequestBody.create(MediaType.parse("image/*"), array);
            photoPart = MultipartBody.Part.createFormData("photo", "photo.jpg", photoBody);
        }

        apiService = getRetrofitInstance();

        Call<Posts> call = apiService.addPost("Bearer " + token, descBody, photoPart);
        call.enqueue(new Callback<Posts>() {
            @Override
            public void onResponse(Call<Posts> call, Response<Posts> response) {
                if (response.isSuccessful()) {
                    Posts post = response.body();

                    HomeFragment.arrayList.add(0, post);
                    HomeFragment.recyclerView.getAdapter().notifyItemInserted(0);
                    HomeFragment.recyclerView.getAdapter().notifyDataSetChanged();
                    Toast.makeText(AddPostActivity.this, "Posted", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // Handle error
                    Toast.makeText(AddPostActivity.this, "Failed to post", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<Posts> call, Throwable t) {
                // Handle failure
                Toast.makeText(AddPostActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private ApiService getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }

    private interface ApiService {
        @Multipart
        @POST(Constant.ADD_POST)
        Call<Posts> addPost(
                @Header("Authorization") String authorization,
                @Part("desc") RequestBody desc,
                @Part MultipartBody.Part photo
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CHANGE_POST && resultCode == RESULT_OK) {
            Uri imgUri = data.getData();
            imgPost.setImageURI(imgUri);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void cancelPost() {
        super.onBackPressed();
    }

    public void changePhoto() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, GALLERY_CHANGE_POST);
    }
}
