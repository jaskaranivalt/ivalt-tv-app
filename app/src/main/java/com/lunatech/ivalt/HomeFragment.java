package com.lunatech.ivalt;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button watchVideoButton = view.findViewById(R.id.watch);

        watchVideoButton.setOnClickListener(v -> startViewMoveActivity());

        return view;
    }

    private void startViewMoveActivity() {
        Intent intent = new Intent(requireContext(), ViewMove.class);
        startActivity(intent);
    }
}