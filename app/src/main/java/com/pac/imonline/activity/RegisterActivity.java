package com.pac.imonline.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pac.imonline.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText enterUsernameRegister, enterEmailRegister, enterPasswordRegister;
    private Button signUpButton;
    private ImageView backButton;

    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        enterUsernameRegister = findViewById(R.id.enterUsernameRegister);
        enterEmailRegister = findViewById(R.id.enterEmailRegister);
        enterPasswordRegister = findViewById(R.id.enterPasswordRegister);
        signUpButton = findViewById(R.id.signUpButton);
        backButton = findViewById(R.id.registerBackButton);

        userDao = AppDatabase.getAppDatabase(getApplicationContext()).userDao();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final UserEntity userEntity = new UserEntity();
                userEntity.setUsername(enterUsernameRegister.getText().toString());
                userEntity.setEmail(enterEmailRegister.getText().toString());
                userEntity.setPassword(enterPasswordRegister.getText().toString());

                if (validateInput(userEntity)) {
                    // Check if username and email exist locally
                    checkLocalUserExistence(userEntity);
                } else {
                    Toast.makeText(getApplicationContext(), "Fill All Fields!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginRegisterActivity.class));
                finish();
            }
        });
    }

    private void checkLocalUserExistence(final UserEntity userEntity) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                return userDao.getUserByUsername(userEntity.getUsername()) != null ||
                        userDao.getUserByEmail(userEntity.getEmail()) != null;
            }

            @Override
            protected void onPostExecute(Boolean userExists) {
                if (userExists) {
                    Toast.makeText(getApplicationContext(), "Username or Email already exists locally!", Toast.LENGTH_SHORT).show();
                } else {
                    // If the username and email don't exist, proceed with registration
                    registerUserLocally(userEntity);
                }
            }
        }.execute();
    }

    private void registerUserLocally(final UserEntity userEntity) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                userDao.registerUser(userEntity);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Toast.makeText(getApplicationContext(), "User Successfully Registered Locally!", Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    private boolean validateInput(UserEntity userEntity) {
        return !userEntity.getUsername().isEmpty() &&
                !userEntity.getEmail().isEmpty() &&
                !userEntity.getPassword().isEmpty();
    }
}
