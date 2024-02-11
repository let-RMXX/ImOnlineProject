package com.pac.imonline.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pac.imonline.R;
import com.pac.imonline.activity.Database.AppDatabase;
import com.pac.imonline.activity.Entities.PostEntity;
import com.pac.imonline.activity.Fragments.HomeFragment;

import java.util.List;

public class EditPostActivity extends AppCompatActivity {

    private int position = 0, id = 0;
    private EditText txtDesc;
    private Button btnSave;
    private ProgressDialog dialog;
    private SharedPreferences sharedPreferences;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        init();
        appDatabase = AppDatabase.getAppDatabase(this);
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

        btnSave.setOnClickListener(view -> {
            if (!txtDesc.getText().toString().isEmpty()) {
                savePost();
            }
        });
    }

    private void savePost() {
        dialog.setMessage("Saving");
        dialog.show();

        // Save post to Room database
        PostEntity postEntity = new PostEntity();
        postEntity.setId(id);
        postEntity.setDescription(txtDesc.getText().toString());
        appDatabase.postDao().insert(postEntity);

        // Observe the LiveData<List<PostEntity>> from the database
        appDatabase.postDao().getAllPosts().observe(this, new Observer<List<PostEntity>>() {
            @Override
            public void onChanged(List<PostEntity> postEntities) {
                if (postEntities != null) {
                    // Access the post at the specified position
                    PostEntity postEntity = postEntities.get(position);
                    postEntity.setDescription(txtDesc.getText().toString());
                    appDatabase.postDao().update(postEntity);

                    dialog.dismiss();
                    finish();
                }
            }
        });
    }

    public void cancelEdit(View view) {
        super.onBackPressed();
    }
}
