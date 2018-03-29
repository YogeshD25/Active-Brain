package com.music.yog.ebmusic;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import dyanamitechetan.vusikview.VusikView;

public class FearActivity extends AppCompatActivity implements MediaPlayer.OnBufferingUpdateListener,MediaPlayer.OnCompletionListener{

    private ImageButton btn_play_pause;
    private SeekBar seekBar;
    private TextView textView;
    SeekBar volumeBar;

    private VusikView musicView;

    private MediaPlayer mediaPlayer;
    private int mediaFileLength;
    private int realtimeLength;
    Button forward,previous,play;
    String a[]=new String[6];
    Uri myUri;
    int i;

    final Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fear);

        musicView = (VusikView)findViewById(R.id.musicView);
        forward=(Button)findViewById(R.id.next);
        previous=(Button)findViewById(R.id.previous);

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next_track();
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previous_track();
            }
        });



        seekBar = (SeekBar)findViewById(R.id.seekbar);
        seekBar.setMax(99); // 100% (0~99)
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(mediaPlayer.isPlaying())
                {
                    SeekBar seekBar = (SeekBar)v;
                    int playPosition = (mediaFileLength/100)*seekBar.getProgress();
                    mediaPlayer.seekTo(playPosition);
                }
                return false;
            }
        });

        textView = (TextView)findViewById(R.id.textTimer);

        btn_play_pause = (ImageButton) findViewById(R.id.btn_play_pause);
        btn_play_pause.setOnClickListener(new View.OnClickListener() {




            @Override
            public void onClick(View v) {
                final ProgressDialog mDialog = new ProgressDialog(FearActivity.this);


                AsyncTask<String,String,String> mp3Play = new AsyncTask<String, String, String>() {

                    @Override
                    protected void onPreExecute() {
                        mDialog.setMessage("Please wait");
                        mDialog.show();
                    }

                    @Override
                    protected String doInBackground(String... params) {
                        try{
                            mediaPlayer.setDataSource(params[0]);
                            mediaPlayer.prepare();
                        }
                        catch (Exception ex)
                        {

                        }
                        return "";
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        mediaFileLength = mediaPlayer.getDuration();
                        realtimeLength = mediaFileLength;
                        if(!mediaPlayer.isPlaying())
                        {
                            mediaPlayer.start();
                            btn_play_pause.setImageResource(R.drawable.ic_pause);
                        }
                        else
                        {
                            mediaPlayer.pause();
                            btn_play_pause.setImageResource(R.drawable.ic_play);
                        }

                        updateSeekBar();
                        mDialog.dismiss();
                    }
                };

                mp3Play.execute("http://sv21.onlinevideoconverter.com/download?file=a0i8c2d3h7f5j9f5"); // direct link mp3 file
                // mp3Play.execute("http://download2086.mediafire.com/h3rhn94gxurg/0s2z5sw1mwtia1t/Bawara+Mann-%28Mr-Jatt.com%29.mp3");

                musicView.start();
            }
        });

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);
        volumeBar = (SeekBar) findViewById(R.id.volumeBar);
        volumeBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        float volumeNum = progress / 100f;
                        mediaPlayer.setVolume(volumeNum, volumeNum);
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

    private void updateSeekBar() {
        seekBar.setProgress((int)(((float)mediaPlayer.getCurrentPosition() / mediaFileLength)*100));
        if(mediaPlayer.isPlaying())
        {
            Runnable updater = new Runnable() {
                @Override
                public void run() {
                    updateSeekBar();
                    realtimeLength-=1000; // declare 1 second
                    textView.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes(realtimeLength),
                            TimeUnit.MILLISECONDS.toSeconds(realtimeLength) -
                                    TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes(realtimeLength))));

                }

            };
            handler.postDelayed(updater,1000); // 1 second
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        seekBar.setSecondaryProgress(percent);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        btn_play_pause.setImageResource(R.drawable.ic_play);
       // musicView.stopNotesFall();

    }
    public void next_track() {
        mediaPlayer = new MediaPlayer();

        i = (int) (Math.random() * 6);
        a[0] = "http://mic.duytan.edu.vn:86/ncs.mp3";
        a[1] = "https://downpwnew.com/14088/02%20O%20Saathi%20-%20Baaghi%202%20-%20Atif%20Aslam%20190Kbps.mp3";
        a[2] = "http://hd.jatt.link/7a4562960c3c06676ecbee134f52e0f6/pfrzv/Khol%20De%20Par-(Mr-Jatt.com).mp3";
        a[3] = "https://downpwnew.com/14087/variation/190K/08%20Weekend%20-%20Diljit%20Dosanjh%20320Kbps.mp3";
        a[4] = "https://downpwnew.com/14084/02%20Patola%20-%20Blackmail%20-%20Guru%20Randhawa%20190Kbps.mp3";
        a[5] = "http://download2086.mediafire.com/h3rhn94gxurg/0s2z5sw1mwtia1t/Bawara+Mann-%28Mr-Jatt.com%29.mp3";
        myUri = Uri.parse(a[i]);
        if (!mediaPlayer.isPlaying()) {
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(this, myUri);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.prepare(); //don't use prepareAsync for mp3 playback
                mediaPlayer.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mediaPlayer.stop();
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(this, myUri);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.prepare(); //don't use prepareAsync for mp3 playback
                mediaPlayer.start();


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }



    public void previous_track()
    {
        i=(int)(Math.random()*6);
        a[0]="http://mic.duytan.edu.vn:86/ncs.mp3";
        a[1]="https://downpwnew.com/14088/02%20O%20Saathi%20-%20Baaghi%202%20-%20Atif%20Aslam%20190Kbps.mp3";
        a[2]="http://hd.jatt.link/7a4562960c3c06676ecbee134f52e0f6/pfrzv/Khol%20De%20Par-(Mr-Jatt.com).mp3";
        a[3]="https://downpwnew.com/14087/variation/190K/08%20Weekend%20-%20Diljit%20Dosanjh%20320Kbps.mp3";
        a[4]="https://downpwnew.com/14084/02%20Patola%20-%20Blackmail%20-%20Guru%20Randhawa%20190Kbps.mp3";
        a[5]="http://download2086.mediafire.com/h3rhn94gxurg/0s2z5sw1mwtia1t/Bawara+Mann-%28Mr-Jatt.com%29.mp3";

        myUri=Uri.parse(a[i]);

        if(!mediaPlayer.isPlaying()) {
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(this, myUri);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.prepare(); //don't use prepareAsync for mp3 playback
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            mediaPlayer.stop();
            try
            {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(this, myUri);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.prepare(); //don't use prepareAsync for mp3 playback
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    public void onBackPressed() {
        mediaPlayer.stop();
       // musicView.stopNotesFall();
        super.onBackPressed();
        finish();

    }
}

