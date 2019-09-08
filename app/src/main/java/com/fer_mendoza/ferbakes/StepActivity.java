package com.fer_mendoza.ferbakes;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import com.fer_mendoza.ferbakes.models.Step;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class StepActivity extends AppCompatActivity {

    private Step step;
    private int position = 0;
    private MediaController mediaControls;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        step = (Step) getIntent().getExtras().getSerializable("step");
        setContentView(R.layout.activity_step);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(step.getShortDescription());
        setSupportActionBar(toolbar);

        TextView description = findViewById(R.id.step_instruction);
        videoView = findViewById(R.id.videoView);
        description.setText(step.getDescription());

        if(!step.getVideoURL().isEmpty()){
            renderVideo();
        }else{
            videoView.setVisibility(View.INVISIBLE);
        }
    }

    private void renderVideo() {

        if (mediaControls == null) {
            mediaControls = new MediaController(this);
        }

        try {
            //set the media controller in the VideoView
            videoView.setMediaController(mediaControls);

            //set the uri of the video to be played
            videoView.setVideoURI(Uri.parse(step.getVideoURL()));

        } catch (Exception e) {
            e.printStackTrace();
        }

        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {
                //if we have a position on savedInstanceState, the video playback should start from here
                videoView.seekTo(position);
                if (position == 0) {
                    videoView.start();
                } else {
                    //if we come from a resumed activity, video playback will be paused
                    videoView.pause();
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("Position", videoView.getCurrentPosition());
        videoView.pause();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        position = savedInstanceState.getInt("Position");
        videoView.seekTo(position);
    }

}
