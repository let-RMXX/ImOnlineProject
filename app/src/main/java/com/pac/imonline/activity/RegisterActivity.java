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

public class RegisterActivity extends AppCompatActivity {

    EditText enterUsernameRegister, enterEmailRegister, enterPasswordRegister;
    Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        enterUsernameRegister = findViewById(R.id.enterUsernameRegister);
        enterEmailRegister = findViewById(R.id.enterEmailRegister);
        enterPasswordRegister = findViewById(R.id.enterPasswordRegister);
        signUpButton = findViewById(R.id.signUpButton);
        ImageView BackButton = findViewById(R.id.registerBackButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserEntity userEntity = new UserEntity();
                userEntity.setUsername(enterUsernameRegister.getText().toString());
                userEntity.setEmail(enterEmailRegister.getText().toString());
                userEntity.setPassword(enterPasswordRegister.getText().toString());

                if (validateInput(userEntity)) {
                    AppDatabase appDatabase = AppDatabase.getAppDatabase(getApplicationContext());
                    final UserDao userDao = appDatabase.userDao();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // Check if username exists
                            UserEntity existingUserByUsername = userDao.getUserByUsername(userEntity.getUsername());
                            if (existingUserByUsername != null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Username already exists!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return;
                            }

                            // Check if email exists
                            UserEntity existingUserByEmail = userDao.getUserByEmail(userEntity.getEmail());
                            if (existingUserByEmail != null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Email already exists!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return;
                            }

                            // Register the user
                            userDao.registerUser(userEntity);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "User Successfully Registered!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                    finish();
                                }
                            });
                        }
                    }).start();
                } else {
                    Toast.makeText(getApplicationContext(), "Fill All Fields!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginRegisterActivity.class));
                finish();
            }
        });
    }

    private Boolean validateInput(UserEntity userEntity) {
        if (userEntity.getUsername().isEmpty() ||
                userEntity.getEmail().isEmpty() ||
                userEntity.getPassword().isEmpty()) {
            return false;
        }
        return true;
    }
}
