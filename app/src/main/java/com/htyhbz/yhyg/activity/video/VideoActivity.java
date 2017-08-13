package com.htyhbz.yhyg.activity.video;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;
import com.htyhbz.yhyg.R;
import com.htyhbz.yhyg.activity.BaseActivity;

/**
 * Created by zongshuo on 2017/8/13 0013.
 */
public class VideoActivity extends BaseActivity{
    private VideoView videoView = null;
    private MediaController mediaController = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        videoView = (VideoView) findViewById(R.id.videoView);
        mediaController = new MediaController(VideoActivity.this);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(Uri.parse(getIntent().getExtras().getString("url")));
        videoView.requestFocus();
        videoView.start();
        findViewById(R.id.backImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                findViewById(R.id.progressBar).setVisibility(View.GONE);
            }
        });

    }
}
