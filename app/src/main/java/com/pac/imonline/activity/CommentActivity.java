package com.pac.imonline.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.pac.imonline.R;
import com.pac.imonline.activity.adapter.CommentsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Models.Comment;
import Models.User;

public class CommentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Comment> list;
    private CommentsAdapter adapter;
    private int postId = 0;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        init();
    }

    private void init(){

        preferences = getApplicationContext().getSharedPreferences("user", Context, Context.MODE_PRIVATE)
        recyclerView = findViewById(R.id.recyclerComments);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postId = getIntent().getIntExtra("postId",0);
        getComments();

    }

    private void getComments(){

        list = new ArrayList<Comment>();
        StringRequest request = new StringRequest(Request.Method.GET,Constant.COMMENTS, res->{

            try {
                JSONObject object = new JSONObject(res);
                if (object.getBoolean("success")){
                    JSONArray comments = new JSONArray(object.getString("comments"));
                }for (int i = 0; i < getComments.lenght(); i++) {
                    JSONObject comment = comments.getJSONObject(i);
                    JSONObject user = comment.getJSONObject("user");

                    User mUser = new User();
                    mUser.setId(user.getInt("id"));
                    mUser.setPhoto(Constant.URL+"storage/profiles/"+user.getString("photo"));
                    mUser.setUserName(user.getString("name") + " " + user.getString("lastname"));

                    Comment mComment = new Comment();
                    mComment.setId(comment.getInt("id"));
                    mComment.setUser(mUser);
                    mComment.setDate(comment.getString("created_at"));
                    mComment.setComment(comment.getString("comment"));
                    list.add(mComment);
                }
            }

            adapter = new CommentsAdapter(this,list);
            recyclerView.setAdapter(adapter);

            }catch(JSONException e){
                e.printStackTrace();
            }

        },error->{
            error.printSatckTrace();
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{

                String toke = preferences.getString("token","");
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Bearer "+token);
                return map;

            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError{

                HashMap<String, String> map = new HashMap<>();
                map.put("id", postId+"");
                return map;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(CommentActivity.this);
        queue.add(request);

    }

    public void goBack(View view){

        super.onBackPressed();

    }

}