package com.pac.imonline.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import com.pac.imonline.R;
import com.pac.imonline.activity.Fragments.HomeFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import Models.User;

public class AddPostActivity extends AppCompatActivity {

    private Button btnPost;
    private ImageView imgPost;
    private EditText txtDesc;
    private Bitmap bitmap = null;
    private static final int GALLERY_CHANGE_POST = 3;
    private ProgressDialog dialog;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        init();
    }

    private void init(){

        sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        btnPost = findViewById(R.id.btnAddPost);
        imgPost = findViewById(R.id.imgAddPost);
        txtDesc = findViewById(R.id.txtDescAddPost);
        dialog.setCancelable(false);

        imgPost.setImageURI(getIntent().getData());
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),getIntent().getData());
        }catch(IOException e){

            e.printStackTrace();

        }

        btnPost.setOnClickListener(v->{

            if(!txtDesc.getText().toString().isEmpty()){

                post();

            }else{

                Toast.makeText(this, "Description is required", Toast.LENGTH_SHORT).show();

            }

        });

    }

    private void post(){

        dialog.setCancelMessage("Posting");
        dialog.show();

        StringRequest request = new StringRequest(Request.Method.POST,Constant.ADD_POST,response ->{

            try {

                JSONObject object = new JSONObject(response);

                if (object.getBoolean("success")){

                    JSONObject postObject = object.getJSONObject("post");
                    JSONObject userObject = object.getJSONObject("user");

                    User user = new User();
                    user.setId(userObject.getInt("id"));
                    user.setUserName(userObject.getString("name")+" "+userObject.getString("lastname"));
                    user.setPhoto(userObject.getString("photo"));

                    Post post = new Post();
                    post.setUser(user);
                    post.setId(postObject.getInt("id"));
                    post.setSelfLike(false);
                    post.setPhoto(postObject.getString("photo"));
                    post.setDesc(postObject.getString("desc"));
                    post.setComments(0);
                    post.setLikes(0);
                    post.setDate(postObject.getString("created_at"));

                    HomeFragment.arrayList.add(0, post);
                    HomeFragment.recyclerView.getAdapter().notifyItemInserted(0);
                    HomeFragment.recyclerView.getAdapter().notifyDataSetChanged();
                    Toast.makeText(this, "Posted", Toast.LENGTH_SHORT).show();
                    finish();

                }

            }catch(JSONException e){

                e.printStackTrace();

            }

            dialog.dismiss();

        },error -> {

            error.printStackTrace();
            dialog.dismiss();

        }){

            //add token to header
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{

                String token = preferences.getString("token", "");
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Bearer "+token);
                return map;

            }

            //add params
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{

                HashMap<String, String> map = new HashMap<>();
                map.put("desc", txtDesc.getText().toString().trim());
                map.put("photo", bitmapToString(bitmap));
                return map;

            }

        };

        RequestQueued queue = Volley.nevRequestQueue(AddPostActivity.this);
        queue.add(request);

    }

    private String bitmapToString(Bitmap bitmap){

        if(bitmap != null){

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayInputStream);
            byte [] array = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(array,Base64.DEFAULT);
        }
        return "";
    }


    public void cancelPost(View view){

        super.onBackPressed();

    }

    public void changePhoto(View view){

       Intent i = new Intent(Intent.ACTION_PICK);
       i.setType("image/*");
       startActivityForResult(i,GALLERY_CHANGE_POST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_CHANGE_POST && resultCode==RESULT_OK){

            Uri imgUri =data.getData();
            imgPost.setImageURI(imgUri);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imgUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

}