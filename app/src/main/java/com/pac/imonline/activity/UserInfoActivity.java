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
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.pac.imonline.R;
import com.pac.imonline.activity.Fragments.HomeFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
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

public class UserInfoActivity extends AppCompatActivity {

    private TextView txtSelectPhoto;
    private Button btnContinue;
    private CircleImageView circleImageView;
    private TextInputEditText txtNameUserInfo;
    private TextInputEditText txtLastNameUserInfo;
    private static final int GALLERY_ADD_PROFILE = 1;
    private Bitmap bitmap = null;
    private SharedPreferences userPref;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        init();
    }

    private void init() {
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        txtSelectPhoto = findViewById(R.id.txtSelectPhoto);
        btnContinue = findViewById(R.id.btnContinue);
        circleImageView = findViewById(R.id.imgUserInfo);

        // Initialize the name and last name views
        txtNameUserInfo = findViewById(R.id.txtNameUserInfo);
        txtLastNameUserInfo = findViewById(R.id.txtLastNameUserInfo);

        // Pick a photo from the gallery
        txtSelectPhoto.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            startActivityForResult(i, GALLERY_ADD_PROFILE);
        });

        btnContinue.setOnClickListener(v -> {
            // Validate fields
            if (validate()) {
                saveUserInfo();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_ADD_PROFILE && resultCode == RESULT_OK) {
            Uri imgUri = data.getData();
            circleImageView.setImageURI(imgUri);

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean validate() {
        return true;
    }

    private void saveUserInfo() {
        dialog.setMessage("Saving");
        dialog.show();

        String name = txtNameUserInfo.getText().toString().trim();
        String lastname = txtLastNameUserInfo.getText().toString().trim();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.HOME)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        String token = userPref.getString("token", "");

        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), name);

        MultipartBody.Part photoPart = null;
        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] array = byteArrayOutputStream.toByteArray();
            RequestBody photoBody = RequestBody.create(MediaType.parse("image/*"), array);
            photoPart = MultipartBody.Part.createFormData("photo", "photo.jpg", photoBody);
        }

        Call<UserInfoResponse> call = apiService.saveUserInfo("Bearer " + token, nameBody, photoPart);
        call.enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                if (response.isSuccessful()) {
                    UserInfoResponse userInfoResponse = response.body();
                    if (userInfoResponse != null && userInfoResponse.isSuccess()) {
                        SharedPreferences.Editor editor = userPref.edit();
                        editor.putString("photo", userInfoResponse.getPhoto());
                        editor.apply();
                        startActivity(new Intent(UserInfoActivity.this, HomeFragment.class));
                        finish();
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                t.printStackTrace();
                dialog.dismiss();
            }
        });
    }

    private interface ApiService {
        @Multipart
        @POST(Constant.SAVE_USER_INFO)
        Call<UserInfoResponse> saveUserInfo(
                @Header("Authorization") String authorization,
                @Part("name") RequestBody name,
                @Part MultipartBody.Part photo
        );
    }

    private static class UserInfoResponse {
        private boolean success;
        private String photo;

        public boolean isSuccess() {
            return success;
        }

        public String getPhoto() {
            return photo;
        }
    }
}