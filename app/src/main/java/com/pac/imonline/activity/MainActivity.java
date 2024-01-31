package com.pac.imonline.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.pac.imonline.R;
import com.pac.imonline.activity.Fragments.CommunityListFragment;
import com.pac.imonline.activity.Fragments.CreateCommunityFragment;
import com.pac.imonline.activity.Fragments.LoginFragment;
import com.pac.imonline.activity.Fragments.LoginRegisterFragment;
import com.pac.imonline.activity.Fragments.RegisterFragment;
import com.pac.imonline.activity.Fragments.WalkthroughPage1Fragment;
import com.pac.imonline.activity.Fragments.WalkthroughPage2Fragment;
import com.pac.imonline.activity.Fragments.WalkthroughPage3Fragment;
import com.pac.imonline.activity.Fragments.WalkthroughPagesAnimFragment;

public class MainActivity extends AppCompatActivity {

    // Fragments
    private LoginRegisterFragment loginRegisterFragment;
    private CommunityListFragment communityListFragment;
    private CreateCommunityFragment createCommunityFragment;
    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;
    private WalkthroughPage1Fragment walkthroughPage1Fragment;
    private WalkthroughPage2Fragment walkthroughPage2Fragment;
    private WalkthroughPage3Fragment walkthroughPage3Fragment;
    private WalkthroughPagesAnimFragment walkthroughPagesAnimFragment;

    ImageView imageBtnProfile;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize fragments
        loginRegisterFragment = new LoginRegisterFragment();
        communityListFragment = new CommunityListFragment();
        createCommunityFragment = new CreateCommunityFragment();
        loginFragment = new LoginFragment();
        registerFragment = new RegisterFragment();
        walkthroughPage1Fragment = new WalkthroughPage1Fragment();
        walkthroughPage2Fragment = new WalkthroughPage2Fragment();
        walkthroughPage3Fragment = new WalkthroughPage3Fragment();
        walkthroughPagesAnimFragment = new WalkthroughPagesAnimFragment();

        // Set the default fragment
        setFragment(loginRegisterFragment);

    }

    private void isFirstTime(){

        //checking if the app is running for the first time
        SharedPreferences preferences = getApplication().getSharedPreferences("onBoard", Context.MODE_PRIVATE);
        boolean isFirstTime = preferences.getBoolean("isFirstTime", true);
        //default value is true
        if(isFirstTime){

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isFirstTime", false);
            editor.apply();

            //start onBoard activity
            startActivity(new Intent(MainActivity.this,WalkthroughPagesAnimFragment.class));
            finish();

        }else {
            startActivity(new Intent(MainActivity.this,AuthActivity.class));
            finish();
        }

    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void switchToCommunityListFragment() {
        setFragment(communityListFragment);
    }

    public void switchToLoginRegisterFragment() {
        setFragment(loginRegisterFragment);
    }

    public void switchToCreateCommunityFragment() {
        setFragment(createCommunityFragment);
    }

    public void switchToLoginFragment() {
        setFragment(loginFragment);
    }

    public void switchToRegisterFragment() {
        setFragment(registerFragment);
    }

    public void switchToWalkthroughPage1Fragment() {
        setFragment(walkthroughPage1Fragment);
    }

    public void switchToWalkthroughPage2Fragment() {
        setFragment(walkthroughPage2Fragment);
    }

    public void switchToWalkthroughPage3Fragment() {
        setFragment(walkthroughPage3Fragment);
    }

    public void switchToWalkthroughPagesAnimFragment() {
        setFragment(walkthroughPagesAnimFragment);
    }

}
