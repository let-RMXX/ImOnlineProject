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
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.pac.imonline.R;
import com.pac.imonline.activity.Api.ApiService;
import com.pac.imonline.activity.Api.RetrofitClient;
import com.pac.imonline.activity.Database.AppDatabase;
import com.pac.imonline.activity.Entities.UserEntity;
import com.pac.imonline.activity.Fragments.WalkthroughPagesAnimFragment;
import com.pac.imonline.R;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoActivity extends AppCompatActivity {

    private TextInputLayout layoutName, layoutLastName;
    private TextInputEditText txtName, txtLastName;
    private TextView txtSelectPhoto;
    private Button btnContinue;
    private CircleImageView circleImageView;
    private static final int GALLERY_ADD_PROFILE = 1;
    private Bitmap bitmap = null;
    private SharedPreferences userPref;
    private ProgressDialog dialog;
    private ApiService apiService;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private AppDatabase appDatabase;

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
        apiService = RetrofitClient.createService();
        appDatabase = AppDatabase.getAppDatabase(this);

        layoutLastName = findViewById(R.id.txtLayoutLastNameUserInfo);
        layoutName = findViewById(R.id.txtLayoutNameUserInfo);
        txtName = findViewById(R.id.txtNameUserInfo);
        txtLastName = findViewById(R.id.txtLastNameUserInfo);
        txtSelectPhoto = findViewById(R.id.txtSelectPhoto);
        btnContinue = findViewById(R.id.btnContinue);
        circleImageView = findViewById(R.id.imgUserInfo);

        txtSelectPhoto.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            startActivityForResult(i, GALLERY_ADD_PROFILE);
        });

        btnContinue.setOnClickListener(v -> {
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
        if (txtName.getText().toString().isEmpty()) {
            layoutName.setErrorEnabled(true);
            layoutName.setError("Name is Required");
            return false;
        }

        if (txtLastName.getText().toString().isEmpty()) {
            layoutLastName.setErrorEnabled(true);
            layoutLastName.setError("Last Name is required");
            return false;
        }

        return true;
    }

    private void saveUserInfo() {
        dialog.setMessage("Saving");
        dialog.show();

        String name = txtName.getText().toString().trim();
        String lastName = txtLastName.getText().toString().trim();

        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody lastNameBody = RequestBody.create(MediaType.parse("text/plain"), lastName);

        MultipartBody.Part photoPart = null;
        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] array = byteArrayOutputStream.toByteArray();
            String encodedImage = Base64.encodeToString(array, Base64.DEFAULT);
            RequestBody photoBody = RequestBody.create(MediaType.parse("multipart/form-data"), encodedImage);
            photoPart = MultipartBody.Part.createFormData("photo", "photo.jpg", photoBody);
        }

        UserEntity user = new UserEntity();
        user.setName(name);
        user.setLastName(lastName);
        user.setEmail("");
        user.setPassword("");

        // Save user info to Room database
        saveUserToRoomDatabase(user);
    }

    private void saveUserToRoomDatabase(UserEntity userEntity) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                appDatabase.userDao().registerUser(userEntity);

                // Create a new instance of WalkthroughPagesAnimFragment
                WalkthroughPagesAnimFragment walkthroughAnimFragment = new WalkthroughPagesAnimFragment();

                // Use getSupportFragmentManager() if you are using AppCompatActivity
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, walkthroughAnimFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}