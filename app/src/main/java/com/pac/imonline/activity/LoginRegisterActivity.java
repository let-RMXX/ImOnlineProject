package com.pac.imonline.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.pac.imonline.R;

public class LoginRegisterActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private Button signInBtn;

    private Button signUpBtn;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register_page);

        signInBtn = findViewById(R.id.SignInButton1);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginRegisterActivity.this, LoginActivity.class );
                startActivity(intent);
                finish();
            }


        });

        signUpBtn = findViewById(R.id.SignUpButton1);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginRegisterActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}