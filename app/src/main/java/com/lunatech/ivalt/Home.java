package com.lunatech.ivalt;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.lunatech.ivalt.databinding.ActivityHomeBinding;

public class Home extends AppCompatActivity {

    private Fragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.lunatech.ivalt.databinding.ActivityHomeBinding homeBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(homeBinding.getRoot());

        initializeViews();
        setInitialFragment();

        setupButton(homeBinding.cross);
        setupButton(homeBinding.home);
        setupButton(homeBinding.settings);
        setupButton(homeBinding.logout);
    }

    private void initializeViews() {
        homeFragment = new DashboardFragment();
    }

    private void setInitialFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, homeFragment)
                .commit();
    }

    private void setupButton(Button button) {
        button.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                button.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.focusedColor));
            } else {
                button.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.unFocusedColor));
            }
        });
    }

}