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
import com.pac.imonline.activity.Database.AppDatabase;
import com.pac.imonline.activity.Database.UserDao;
import com.pac.imonline.activity.Entities.UserEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterFragment extends Fragment {

    private EditText enterUsernameRegister, enterEmailRegister, enterPasswordRegister;
    private Button signUpButton;
    private ImageView backButton;

    private UserDao userDao;

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
        boolean userExists = userDao.getUserByUsername(userEntity.getUsername()) != null ||
                userDao.getUserByEmail(userEntity.getEmail()) != null;

        if (userExists) {
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(requireContext(), "Username or Email already exists locally!", Toast.LENGTH_SHORT).show();
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
        return !userEntity.getUsername().isEmpty() &&
                !userEntity.getEmail().isEmpty() &&
                !userEntity.getPassword().isEmpty();
    }
}
