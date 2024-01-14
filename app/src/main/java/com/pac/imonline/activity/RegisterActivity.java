package com.pac.imonline.activity;

import static com.pac.imonline.activity.ApiDbListActivity.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pac.imonline.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText enterUsernameRegister, enterEmailRegister, enterPasswordRegister;
    private Button signUpButton;
    private ImageView backButton;

    private UserDao userDao;
    private ImOnlineApi imOnlineApi;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

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
        imOnlineApi = ApiClient.getClient().create(ImOnlineApi.class);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final UserEntity userEntity = new UserEntity();
                userEntity.setUsername(enterUsernameRegister.getText().toString());
                userEntity.setEmail(enterEmailRegister.getText().toString());
                userEntity.setPassword(enterPasswordRegister.getText().toString());

                if (validateInput(userEntity)) {
                    // Check if username and email exist locally
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            checkLocalUserExistence(userEntity);
                        }
                    });
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
        boolean userExists = userDao.getUserByUsername(userEntity.getUsername()) != null ||
                userDao.getUserByEmail(userEntity.getEmail()) != null;

        if (userExists) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Username or Email already exists locally!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // If the username and email don't exist, proceed with registration
            registerUserLocally(userEntity);
        }
    }

    private void registerUserLocally(final UserEntity userEntity) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                userDao.registerUser(userEntity);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "User Successfully Registered Locally!", Toast.LENGTH_SHORT).show();
                        // Register the user remotely (via API)
                        registerUserRemotely(userEntity);
                    }
                });
            }
        });
    }

    private void registerUserRemotely(UserEntity userEntity) {
        Call<Void> call = imOnlineApi.registerUser(
                userEntity.getUsername(),
                userEntity.getEmail(),
                userEntity.getPassword()
        );

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "User Successfully Registered Remotely!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Failed to register user remotely. User is registered locally.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Network error during registration! User is registered locally.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private boolean validateInput(UserEntity userEntity) {
        return !userEntity.getUsername().isEmpty() &&
                !userEntity.getEmail().isEmpty() &&
                !userEntity.getPassword().isEmpty();
    }
}
