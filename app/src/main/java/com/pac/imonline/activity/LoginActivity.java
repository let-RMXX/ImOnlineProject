package com.pac.imonline.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pac.imonline.R;

public class LoginActivity extends AppCompatActivity {

    EditText enterEmailLogin, enterPasswordLogin;
    Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        enterEmailLogin = findViewById(R.id.enterEmailLogin);
        enterPasswordLogin = findViewById(R.id.enterPasswordLogin);
        signInButton = findViewById(R.id.signInButton);

        ImageView backButton = findViewById(R.id.loginBackButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = enterEmailLogin.getText().toString().trim();
                String password = enterPasswordLogin.getText().toString().trim();

                if (validateInput(email, password)) {
                    AppDatabase appDatabase = AppDatabase.getAppDatabase(getApplicationContext());
                    final UserDao userDao = appDatabase.userDao();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            UserEntity user = userDao.loginUser(email, password);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (user != null) {
                                        Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LoginActivity.this, SwipePagesAnimation.class));
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Invalid credentials", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }).start();

                } else {
                    Toast.makeText(getApplicationContext(), "Fill in all fields!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, LoginRegisterActivity.class));
                finish();
            }
        });
    }

    private boolean validateInput(String email, String password) {
        return !email.isEmpty() && !password.isEmpty();
    }
}
