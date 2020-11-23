package com.example.exercise;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView TextView1, TextView2, TextView3, TextView4;
    Button Button1, Button2, Button3, Button4;
    SeekBar mySeekBar;
    MediaPlayer myMediaPlayer;
    SimpleDateFormat SimpleDateFormatObj;
    Thread myThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myMediaPlayer=new MediaPlayer();
        myMediaPlayer= MediaPlayer.create(this,R.raw.chasani_ringtone);

        mySeekBar=findViewById(R.id.song_progressSeekBar);
        mySeekBar.setMax(myMediaPlayer.getDuration());
        mySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b){
                    myMediaPlayer.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Button1=findViewById(R.id.prevButton);
        Button1.setOnClickListener(this);

        Button2=findViewById(R.id.playButton);
        Button2.setOnClickListener(this);

        Button3=findViewById(R.id.pauseButton);
        Button3.setOnClickListener(this);

        Button4=findViewById(R.id.nextButton);
        Button4.setOnClickListener(this);

        SimpleDateFormatObj = new SimpleDateFormat("mm:ss");

        TextView1 = findViewById(R.id.song_current_timeTextView);
        TextView1.setText(SimpleDateFormatObj.format(myMediaPlayer.getCurrentPosition()));

        TextView2 = findViewById(R.id.song_lengthTextView);
        TextView2.setText(SimpleDateFormatObj.format(myMediaPlayer.getDuration()));


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.prevButton:
                Log.d("prevButton", "pressed");
                PreviousSong();
                break;
            case R.id.playButton:
                Log.d("playButton", "pressed");
                PlaySong();
                break;
            case R.id.pauseButton:
                Log.d("pauseButton", "pressed");
                PauseSong();
                break;
            case R.id.nextButton:
                Log.d("nextButton", "pressed");
                NextSong();
                break;
        }
    }

    private void NextSong() {
        if (myMediaPlayer != null) {
            myMediaPlayer.seekTo(myMediaPlayer.getDuration());
            mySeekBar.setProgress(myMediaPlayer.getDuration());
        }
    }

    private void PauseSong() {
        if (myMediaPlayer != null && myMediaPlayer.isPlaying()) {
            myMediaPlayer.pause();
        }
    }

    private void PlaySong() {
        if (myMediaPlayer != null && !myMediaPlayer.isPlaying()) {
            myMediaPlayer.start();
            sync();
        }
    }

    private void PreviousSong() {
        if (myMediaPlayer != null) {
            myMediaPlayer.seekTo(0);
            mySeekBar.setProgress(0);
            myMediaPlayer.pause();
        }
    }

    private void sync() {
        myThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (myMediaPlayer != null) {
                        while (myMediaPlayer.isPlaying()) {
                            Thread.sleep(500);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    TextView1.setText(SimpleDateFormatObj.format(myMediaPlayer.getCurrentPosition()));
                                    mySeekBar.setProgress(myMediaPlayer.getCurrentPosition());
                                }
                            });
                        }
                    }
                } catch (Exception e) {

                }
            }
        });
        myThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myMediaPlayer.stop();
        myMediaPlayer.release();
        myMediaPlayer=null;
    }
}