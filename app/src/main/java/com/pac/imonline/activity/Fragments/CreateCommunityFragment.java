package com.pac.imonline.activity.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pac.imonline.R;
import com.pac.imonline.activity.Community;
import com.pac.imonline.activity.CommunityDao;
import com.pac.imonline.activity.Database.AppDatabase;

public class CreateCommunityFragment extends Fragment {

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

    public CreateCommunityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_create_community, container, false);

        communityNameEditText = view.findViewById(R.id.communityNameEditText);
        photoImageView = view.findViewById(R.id.photoImageView);
        selectPhotoButton = view.findViewById(R.id.selectPhotoButton);
        bannerImageView = view.findViewById(R.id.bannerImageView);
        selectBannerButton = view.findViewById(R.id.selectBannerButton);
        createCommunityButton = view.findViewById(R.id.createCommunityButton);

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
                new InsertCommunityTask().execute(community);
            }
        });

        return view;
    }

    private void openGallery(boolean isSelectingPhoto) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, isSelectingPhoto ? REQUEST_SELECT_GALLERY : REQUEST_SELECT_BANNER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
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
            AppDatabase appDatabase = AppDatabase.getAppDatabase(requireContext());
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
        Toast.makeText(requireContext(), "Community was successfully created", Toast.LENGTH_SHORT).show();
    }

    private void setResultAndFinish() {
        if (getActivity() != null) {
            Intent resultIntent = new Intent();
            getActivity().setResult(RESULT_COMMUNITY_CREATED, resultIntent);
            getActivity().finish();
        }
    }
}
