package com.pac.imonline.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.pac.imonline.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiDbListActivity extends AppCompatActivity {
    private TextView textViewResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        textViewResult = findViewById(R.id.textViewResult);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/api/usersinfo/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ImOnlineApi imOnlineApi = retrofit.create(ImOnlineApi.class);

        Call<List<Post>> call = imOnlineApi.getAllUsers();

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }

                List<Post> usersInfo = response.body();

                for (Post post : usersInfo) {
                    String content = "";
                    content += "ID: " + post.getId() + "\n";
                    content += "Name: " + post.getName() + "\n";
                    content += "Email: " + post.getEmail();

                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }
}
