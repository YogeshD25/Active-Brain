package com.music.yog.ebmusic;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import dyanamitechetan.vusikview.VusikView;

public class PlayerActivity extends AppCompatActivity {
    static MediaPlayer mp;
    int position;
    SeekBar sb;
    ArrayList<File> mySongs;
    Thread updateSeekBar;
    Button forward,reverse,next,previous,pause;
    SeekBar positionBar;
    SeekBar volumeBar;
    TextView elapsedTimeLabel;
   TextView remainingTimeLabel;
  int totalTime;
    VusikView vusikView;



   /* ContentResolver musicResolve = getContentResolver();
    Uri smusicUri = android.provider.MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
    Cursor music =musicResolve.query(smusicUri,null,null, null, null);*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        pause = (Button) findViewById(R.id.pause);
        forward = (Button) findViewById(R.id.forward);
        previous = (Button) findViewById(R.id.previous);
        next = (Button) findViewById(R.id.next);
        reverse = (Button) findViewById(R.id.reverse);
        sb = (SeekBar) findViewById(R.id.seekBar);
       // elapsedTimeLabel = (TextView) findViewById(R.id.elapsedTimeLabel);
       // remainingTimeLabel = (TextView) findViewById(R.id.remainingTimeLabel);

        vusikView = (VusikView) findViewById(R.id.musicView);
        vusikView.start();



        updateSeekBar=new Thread(){
            @Override
            public void run(){
                int totalDuration = mp.getDuration();
                int currentPosition = 0;
                //sb.setMax(totalDuration);
                while(currentPosition < totalDuration){
                    try{
                        sleep(800);
                        currentPosition=mp.getCurrentPosition();
                        sb.setProgress(currentPosition);
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }

        };
        if(mp != null){
            mp.stop();
            mp.release();
        }
        Intent i = getIntent();
        Bundle b = i.getExtras();
        mySongs = (ArrayList) b.getParcelableArrayList("songs");
        position = b.getInt("pos", 0);
        Uri u = Uri.parse(mySongs.get(position).toString());
        mp = MediaPlayer.create(getApplicationContext(), u);

      /*
        ContentResolver res = getContentResolver();
        Uri smusicUri = android.provider.MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        Cursor music =res.query(smusicUri,null,null, null, null);
        music.moveToFirst();            //i put only one song in my external storage to keep things simple
        int x=music.getColumnIndex(android.provider.MediaStore.Audio.Albums.ALBUM_ART);
        String thisArt = music.getString(x);
        Bitmap bm= BitmapFactory.decodeFile(thisArt);
        ImageView image=(ImageView)findViewById(R.id.imgThumb);
        image.setImageBitmap(bm);*/
        mp.start();
        sb.setMax(mp.getDuration());
        updateSeekBar.start();
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());
            }

        });


        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.setMax(mp.getDuration());

                if (mp.isPlaying()) {
                    // Stopping
                   // sb.setProgress(0);
                    mp.pause();
                    pause.setBackgroundResource(R.drawable.stop);
                    //Intent switchIntent = new Intent("com.example.app.ACTION_PLAY");
                   // PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(this, 100, switchIntent, 0);
                   // sb.setMax(mp.getDuration());
                   // updateSeekBar.start();

                } else {
                    // Playing
                    mp.start();
                   // vusikView.pauseNotesFall();
                    pause.setBackgroundResource(R.drawable.play);
                }

            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.setMax(mp.getDuration());

                mp.seekTo(mp.getCurrentPosition() + 5000);
            }
        });
        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.setMax(mp.getDuration());

                mp.seekTo(mp.getCurrentPosition() - 5000);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                mp.release();
                position = ((position + 1) % mySongs.size());
                Uri u = Uri.parse(mySongs.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(), u);
                mp.start();
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                mp.release();
                position = ((position - 1) < 0) ? (mySongs.size() - 1) : (position - 1);
                Uri u = Uri.parse(mySongs.get(position).toString());//%mysongs so that it do not go to invalid position
                mp = MediaPlayer.create(getApplicationContext(), u);
                mp.start();
            }
        });


        // Volume Bar
        volumeBar = (SeekBar) findViewById(R.id.volumeBar);
        volumeBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        float volumeNum = progress / 100f;
                        mp.setVolume(volumeNum, volumeNum);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );



    }
   // Intent switchIntent = new Intent("com.example.app.ACTION_PLAY");

   /*public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;
            // Update positionBar.
            positionBar.setProgress(currentPosition);

            // Update Labels.
            String elapsedTime = createTimeLabel(currentPosition);
            elapsedTimeLabel.setText(elapsedTime);

            String remainingTime = createTimeLabel(totalTime-currentPosition);
            remainingTimeLabel.setText("- " + remainingTime);
        }
    };*/

   /* public String createTimeLabel(int time) {
        String timeLabel = "";
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;

        timeLabel = min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;

        return timeLabel;
    }*/
/*public void onBackPressed()
{
   // mp.stop();
    super.onBackPressed();
}*/


    }