package com.lunatech.ivalt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;

import com.lunatech.ivalt.databinding.ActivityHomeBinding;

public class Home extends AppCompatActivity {

    FrameLayout frameLayout;
    Fragment HomeFragment;
    Fragment SplashFragment;

    ActivityHomeBinding homeBinding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(homeBinding.getRoot());

        frameLayout = findViewById(R.id.fragment_container);

        HomeFragment = new dashboard();
        SplashFragment = new HomeFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, SplashFragment).commit();

        homeBinding.cross.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                homeBinding.cross.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.focusedColor));
            } else {
                homeBinding.cross.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.unFocusedColor));
            }
        });
        homeBinding.cross.setOnClickListener(v -> {
            finish();
        });

        homeBinding.home.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                homeBinding.home.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.focusedColor));
            } else {
                homeBinding.home.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.unFocusedColor));
            }
        });

        homeBinding.settings.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                homeBinding.settings.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.focusedColor));
            } else {
                homeBinding.settings.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.unFocusedColor));
            }
        });


        homeBinding.logout.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                homeBinding.logout.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.focusedColor));
            } else {
                homeBinding.logout.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.unFocusedColor));
            }
        });

    }
    protected void openHomeFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, HomeFragment).commit();
    }
}