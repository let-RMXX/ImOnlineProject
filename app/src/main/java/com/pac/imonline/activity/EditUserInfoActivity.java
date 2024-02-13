package com.pac.imonline.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.pac.imonline.R;
import com.pac.imonline.activity.Database.AppDatabase;
import com.pac.imonline.activity.Entities.UserEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditUserInfoActivity extends AppCompatActivity {

    private TextInputLayout layoutName, layoutLastName;
    private TextInputEditText txtName, txtLastName;
    private TextView txtSelectPhoto;
    private Button btnSave;
    private CircleImageView circleImageView;
    private static final int GALLERY_CHANGE_PROFILE = 5;
    private Bitmap bitmap = null;
    private ProgressDialog dialog;
    private AppDatabase appDatabase;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);
        init();

        // Initialize Room database
        appDatabase = AppDatabase.getAppDatabase(this);

        // Retrieve user ID from intent extras
        userId = getIntent().getIntExtra("user_id", -1);
        Log.d("EditUserInfoActivity", "User ID: " + userId); // Log the value of userId
        if (userId == -1) {
            // If user ID is not passed, display an error message and finish the activity
            Toast.makeText(this, "User ID not provided", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            // Load user data based on the user ID
            loadUserData(userId);
        }
    }

    private void init() {
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        layoutLastName = findViewById(R.id.txtEditLayoutLastNameUserInfo);
        layoutName = findViewById(R.id.txtEditLayoutNameUserInfo);
        txtName = findViewById(R.id.txtEditNameUserInfo);
        txtLastName = findViewById(R.id.txtEditLastNameUserInfo);
        txtSelectPhoto = findViewById(R.id.txtEditSelectPhoto);
        btnSave = findViewById(R.id.btnEditSave);
        circleImageView = findViewById(R.id.imgEditUserInfo);

        // Click listener for selecting photo
        txtSelectPhoto.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            startActivityForResult(i, GALLERY_CHANGE_PROFILE);
        });

        // Click listener for save button
        btnSave.setOnClickListener(v -> {
            if (validate()) {
                updateProfile();
            }
        });
    }

    private void loadUserData(int userId) {
        // Load user data from the database based on the user ID
        new Thread(() -> {
            UserEntity userEntity = appDatabase.userDao().getUserById(userId);
            if (userEntity != null) {
                // Update UI with user data
                runOnUiThread(() -> {
                    txtName.setText(userEntity.getName());
                    txtLastName.setText(userEntity.getLastName());

                });
            } else {
                runOnUiThread(() -> Toast.makeText(EditUserInfoActivity.this, "User not found", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void updateProfile() {
        dialog.setMessage("Updating");
        dialog.show();

        String name = txtName.getText().toString().trim();
        String lastName = txtLastName.getText().toString().trim();

        // Update user data in the database
        new Thread(() -> {
            UserEntity userEntity = appDatabase.userDao().getUserById(userId);
            if (userEntity != null) {
                userEntity.setName(name);
                userEntity.setLastName(lastName);

                appDatabase.userDao().updateUser(userEntity);

                // Simulate network request delay with Handler
                new Handler(getMainLooper()).postDelayed(() -> {
                    dialog.dismiss();
                    Toast.makeText(EditUserInfoActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();

                    // Start HomeActivity
                    Intent intent = new Intent(EditUserInfoActivity.this, HomeActivity.class);
                    startActivity(intent);

                    finish();
                }, 1500);
            } else {
                runOnUiThread(() -> {
                    dialog.dismiss();
                    Toast.makeText(EditUserInfoActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CHANGE_PROFILE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            circleImageView.setImageURI(uri);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
