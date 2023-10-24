package com.pac.imonline.activity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pac.imonline.R;

public class EditProfileActivity extends AppCompatActivity {


    ImageView imgViewAvatar;
    FloatingActionButton floatingActionButton;
    int[] avatarList;
    int position;
    private static final int REQUEST_IMAGE_GALLERY = 1;

    public static void start(Context context) {
        Intent intent = new Intent(context, EditProfileActivity.class);
        context.startActivity(intent);
    }

    EditText editTextName;
    EditText editTextAbout;
    EditText editTextPhone;
    EditText editTextEmail;
    EditText editTextLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        imgViewAvatar = findViewById(R.id.imageViewAvatar);
        floatingActionButton = findViewById(R.id.imgbutton);
        avatarList = avatarList = new int[]{R.drawable.photo_1, R.drawable.photo_2, R.drawable.photo_3, R.drawable.photo_4,};
        position = 0;
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position++;
                if (position >= avatarList.length) {
                    position = 0;
                }
                imgViewAvatar.setImageResource(avatarList[position]);
            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        this.editTextName = findViewById(R.id.editTextName);
        this.editTextAbout = findViewById(R.id.editTextAbout);
        this.editTextPhone = findViewById(R.id.editTextPhone);
        this.editTextEmail = findViewById(R.id.editTextEmail);
        this.editTextLocation = findViewById(R.id.editTextLocation);
        this.imgViewAvatar = findViewById(R.id.imageViewAvatar);
    }

    private void showPhotoSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Escolha uma foto");

        final String[] photoOptions = new String[]{"foto1", "foto2", "me"};

        builder.setItems(photoOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedPhoto = photoOptions[which];
                loadImageFromDrawable(selectedPhoto);
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void loadImageFromDrawable(String photoName) {
        int resourceId = getResources().getIdentifier(photoName, "drawable", getPackageName());

        if (resourceId != 0) {
            Glide.with(this)
                    .load(resourceId)
                    .apply(new RequestOptions().centerCrop())
                    .into(imgViewAvatar);
        } else {
            imgViewAvatar.setImageResource(R.drawable.default_icon);
        }
    }

    public void editProfile(View view) {
        String name = this.editTextName.getText().toString();
        String about = this.editTextAbout.getText().toString();
        String phone = this.editTextPhone.getText().toString();
        String email = this.editTextEmail.getText().toString();
        String location = this.editTextLocation.getText().toString();
        String avatar = Integer.toString(avatarList[position]);

        int profileId = 1;

        ProfileActivity currentProfile = AppDatabase.getInstance(this).getProfileDAO().getById(profileId);

        if ((currentProfile != null)) {
            currentProfile.updateProfile(name, about, phone, email, location, avatar);

            if ((currentProfile != null)) {
                currentProfile.updateProfile(name, about, phone, email, location, "");

                AppDatabase.getInstance(this).getProfileDAO().updateProfile(currentProfile);
                finish();
            } else {
                AppDatabase.getInstance(this).getProfileDAO().getById(profileId);
            }
        }
    }
}