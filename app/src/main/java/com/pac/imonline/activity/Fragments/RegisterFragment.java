package com.pac.imonline.activity.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.pac.imonline.R;
import com.pac.imonline.activity.Constant;
import com.pac.imonline.activity.Database.AppDatabase;
import com.pac.imonline.activity.Database.UserDao;
import com.pac.imonline.activity.Entities.UserEntity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.pac.imonline.activity.Api.UserService;

public class RegisterFragment extends Fragment {

    private EditText enterUsernameRegister, enterEmailRegister, enterPasswordRegister;
    private Button signUpButton;
    private ImageView backButton;

    private UserDao userDao;
    private UserService userService;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public RegisterFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_register, container, false);

        enterUsernameRegister = view.findViewById(R.id.enterUsernameRegister);
        enterEmailRegister = view.findViewById(R.id.enterEmailRegister);
        enterPasswordRegister = view.findViewById(R.id.enterPasswordRegister);
        signUpButton = view.findViewById(R.id.signUpButton);
        backButton = view.findViewById(R.id.registerBackButton);

        userDao = AppDatabase.getAppDatabase(requireContext()).userDao();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.HOME)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userService = retrofit.create(UserService.class);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final UserEntity userEntity = new UserEntity();
                userEntity.setName(enterUsernameRegister.getText().toString());
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
                    Toast.makeText(requireContext(), "Fill All Fields!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new LoginRegisterFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    private void checkLocalUserExistence(final UserEntity userEntity) {
        boolean userExists = userDao.getUserByUsername(userEntity.getName()) != null ||
                userDao.getUserByEmail(userEntity.getEmail()) != null;

        if (userExists) {
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(requireContext(), "Username or Email already exists locally!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // If the username and email don't exist locally, proceed with remote registration
            registerUserRemotely(userEntity);
        }
    }

    private void registerUserRemotely(final UserEntity userEntity) {
        Call<Void> call = userService.registerUser(userEntity);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Registration successful remotely, proceed with local registration
                    registerUserLocally(userEntity);
                } else {
                    // Handle remote registration failure
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(requireContext(), "Remote Registration Failed. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle network error
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(requireContext(), "Network Error. Please check your connection.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void registerUserLocally(final UserEntity userEntity) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                userDao.registerUser(userEntity);

                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(requireContext(), "User Successfully Registered Locally!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private boolean validateInput(UserEntity userEntity) {
        return !userEntity.getName().isEmpty() &&
                !userEntity.getEmail().isEmpty() &&
                !userEntity.getPassword().isEmpty();
    }
}