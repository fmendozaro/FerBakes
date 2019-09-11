package com.fer_mendoza.ferbakes;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import com.fer_mendoza.ferbakes.models.Recipe;
import com.fer_mendoza.ferbakes.models.Step;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class StepActivity extends AppCompatActivity {

    private Step step;
    private int position = 0;
    private MediaController mediaControls;
    private VideoView videoView;
    private Recipe recipe;
    private ImageView placeholder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipe = (Recipe) getIntent().getExtras().getSerializable("recipe");
        step = recipe.getSteps().get((int) getIntent().getExtras().getLong("stepId"));
        setContentView(R.layout.activity_step);
        loadStepDetail(step);
    }

    private void loadStepDetail(final Step step) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView description = findViewById(R.id.step_instruction);
        Button btn_next = findViewById(R.id.nav_btn_next);
        Button btn_prev = findViewById(R.id.nav_btn_prev);
        videoView = findViewById(R.id.videoView);
        placeholder = findViewById(R.id.vidPlaceholder);

        toolbar.setTitle(step.getShortDescription());
        setSupportActionBar(toolbar);
        description.setText(step.getDescription());

        if(step.getId() == 0){
            btn_prev.setVisibility(View.INVISIBLE);
        }else{
            btn_prev.setVisibility(View.VISIBLE);
        }

        if(step.isLast()){
            btn_next.setVisibility(View.INVISIBLE);
        }else{
            btn_next.setVisibility(View.VISIBLE);
        }
        if(!step.getVideoURL().isEmpty()){
            placeholder.setVisibility(View.INVISIBLE);
            videoView.setVisibility(View.VISIBLE);
            renderVideo(step.getVideoURL());
        }else{
            placeholder.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.INVISIBLE);
        }

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadStepDetail(recipe.getSteps().get((int) step.getId() + 1));
            }
        });

        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadStepDetail(recipe.getSteps().get((int) step.getId() - 1));
            }
        });
    }

    private void renderVideo(String videoUrl) {

        if (mediaControls == null) {
            mediaControls = new MediaController(this);
        }

        try {
            mediaControls.setAnchorView(videoView);
            videoView.setMediaController(mediaControls);
            videoView.setVideoURI(Uri.parse(videoUrl));
        } catch (Exception e) {
            e.printStackTrace();
        }

        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                videoView.seekTo(position);
                if (position == 0) {
                    videoView.start();
                } else {
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
