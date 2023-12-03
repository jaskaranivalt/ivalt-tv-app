package com.lunatech.ivalt;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.lunatech.ivalt.databinding.ActivityViewMoveBinding;

public class ViewMove extends AppCompatActivity {

    Button button;
    VideoView videoView;

    ActivityViewMoveBinding viewMoveBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewMoveBinding = ActivityViewMoveBinding.inflate(getLayoutInflater());
        setContentView(viewMoveBinding.getRoot());


        button = findViewById(R.id.button2);
        videoView = findViewById(R.id.videoView);
        videoView.setVideoURI(Uri.parse("https://ivalt.com/videos/videoplayback.mp4"));
        videoView.start();

        MediaController mediaControls = new MediaController(this);
        mediaControls.setAnchorView(videoView);
        videoView.setMediaController(mediaControls);

        videoView.setOnFocusChangeListener((v, hasFocus) -> {
            if (videoView.isPlaying()) {
                button.setVisibility(Button.GONE);
            } else {
                button.setVisibility(Button.VISIBLE);
            }
        });

        mediaControls.setOnClickListener((v) -> {
            if (videoView.isPlaying()) {
                button.setVisibility(Button.GONE);
            } else {
                button.setVisibility(Button.VISIBLE);
            }
        });


        button.setOnClickListener(v -> {
            finish();
        });
    }
}