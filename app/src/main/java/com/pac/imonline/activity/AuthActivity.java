package com.pac.imonline.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.pac.imonline.R;
import com.pac.imonline.activity.Fragments.LoginFragment;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameAuthContainer,new LoginFragment()).commit();
    }
}