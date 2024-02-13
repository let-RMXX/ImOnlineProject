package com.pac.imonline.activity.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.pac.imonline.activity.HomeActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginFragment extends Fragment {

    private EditText enterEmailLogin, enterPasswordLogin;
    private Button signInButton;
    private SharedPreferences sharedPreferences;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public LoginFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login, container, false);

        enterEmailLogin = view.findViewById(R.id.enterEmailLogin);
        enterPasswordLogin = view.findViewById(R.id.enterPasswordLogin);
        signInButton = view.findViewById(R.id.signInButton);

        ImageView backButton = view.findViewById(R.id.loginBackButton);
        sharedPreferences = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = enterEmailLogin.getText().toString().trim();
                String password = enterPasswordLogin.getText().toString().trim();

                if (validateInput(email, password)) {
                    AppDatabase appDatabase = AppDatabase.getAppDatabase(requireContext());
                    final UserDao userDao = appDatabase.userDao();

                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            UserEntity user = userDao.loginUser(email, password);
                            requireActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (user != null) {
                                        Toast.makeText(requireContext(), "Login Successful!", Toast.LENGTH_SHORT).show();

                                        // Save user data to SharedPreferences
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("username", user.getUsername());
                                        editor.putString("email", user.getEmail());
                                        editor.putInt("userId", user.getId());
                                        editor.apply();

                                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    } else {
                                        Toast.makeText(requireContext(), "Invalid credentials", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });

                } else {
                    Toast.makeText(requireContext(), "Fill in all fields!", Toast.LENGTH_SHORT).show();
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

    private boolean validateInput(String email, String password) {
        return !email.isEmpty() && !password.isEmpty();
    }
}

