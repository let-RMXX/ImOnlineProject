package com.pac.imonline.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.pac.imonline.R;

public class CreateCommunityActivity extends AppCompatActivity {

    private static final int REQUEST_SELECT_GALLERY = 2;
    private static final int REQUEST_SELECT_BANNER = 3;
    private static final int RESULT_COMMUNITY_CREATED = 3;

    private EditText communityNameEditText;
    private ImageView photoImageView;
    private Button selectPhotoButton;
    private ImageView bannerImageView;
    private Button selectBannerButton;
    private Button createCommunityButton;

    private Uri selectedPhotoUri;
    private Uri selectedBannerUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_community);

        communityNameEditText = findViewById(R.id.communityNameEditText);
        photoImageView = findViewById(R.id.photoImageView);
        selectPhotoButton = findViewById(R.id.selectPhotoButton);
        bannerImageView = findViewById(R.id.bannerImageView);
        selectBannerButton = findViewById(R.id.selectBannerButton);
        createCommunityButton = findViewById(R.id.createCommunityButton);

        selectPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(true);
            }
        });

        selectBannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(false);
            }
        });

        createCommunityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String communityName = communityNameEditText.getText().toString().trim();
                String photoUrl = selectedPhotoUri != null ? selectedPhotoUri.toString() : null;
                String bannerUrl = selectedBannerUri != null ? selectedBannerUri.toString() : null;

                Community community = new Community(communityName, photoUrl, bannerUrl);

                // Save the community to the database
                AppDatabase appDatabase = AppDatabase.getAppDatabase(CreateCommunityActivity.this);
                CommunityDao communityDao = appDatabase.getCommunityDao();
                new InsertCommunityTask().execute(community);
            }
        });
}

    private void openGallery(boolean isSelectingPhoto) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, isSelectingPhoto ? REQUEST_SELECT_GALLERY : REQUEST_SELECT_BANNER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_SELECT_GALLERY) {
                selectedPhotoUri = data.getData();
                photoImageView.setImageURI(selectedPhotoUri);
            } else if (requestCode == REQUEST_SELECT_BANNER) {
                selectedBannerUri = data.getData();
                bannerImageView.setImageURI(selectedBannerUri);
            }
        }
    }

    private class InsertCommunityTask extends AsyncTask<Community, Void, Void> {
        @Override
        protected Void doInBackground(Community... communities) {
            AppDatabase appDatabase = AppDatabase.getAppDatabase(CreateCommunityActivity.this);
            CommunityDao communityDao = appDatabase.getCommunityDao();
            communityDao.insertCommunity(communities[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            showSuccessMessage();
            setResultAndFinish();
        }
    }

    private void showSuccessMessage() {
        Toast.makeText(CreateCommunityActivity.this, "Community was successfully created", Toast.LENGTH_SHORT).show();
    }

    private void setResultAndFinish() {
        Intent resultIntent = new Intent();
        setResult(RESULT_COMMUNITY_CREATED, resultIntent);
        finish();
    }
}
