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

public class AngryActivity extends AppCompatActivity implements MediaPlayer.OnBufferingUpdateListener,MediaPlayer.OnCompletionListener{

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
        setContentView(R.layout.activity_angry);

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
                final ProgressDialog mDialog = new ProgressDialog(AngryActivity.this);


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

                mp3Play.execute("https://demolat.sunproxy.net/file/VnE1alZsa213K0VjZ3JqSkNIL1RHRGRiZ2xDT3N6cFlKN1hDOTcrbWFudEwzUkcyK0lMVkVtbDhLbEd1OWxHT0xnNkRQZy9tdkNvKzZmdXliQ2puajhkSDZNTDFRV0RURWsrZlYxTGFMczA9/The_Cranberries_-_Zombie_-_The_Cranberries_-_Zombie_(DemoLat.com).mp3"); // direct link mp3 file
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
                    textView.setText(String.format("%d:%d",TimeUnit.MILLISECONDS.toMinutes(realtimeLength),
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
        //musicView.stopNotesFall();

    }
    public void next_track() {
        mediaPlayer = new MediaPlayer();

        i = (int) (Math.random() * 6);
        a[0] = "https://songolum.com/file/kh1ImqRxXC88eKgYN4yiMw3H1PK6LumyICWB5cVwcvs/In%2BThe%2BEnd%2B-%2BLinkin%2BPark.mp3?r=idz&dl=311&ref=linkin-park";
        a[1] = "https://songolum.com/file/2tcGMd-WzmwOgyX26ujKX1edt4yoDVBunPlSz2O7Z5s/Numb%2B-%2BLinkin%2BPark.mp3?r=idz&dl=311&ref=linkin-park";
        a[2] = "https://songolum.com/file/9BbuvB2LgYHR-QaBf3TqaPYKiXztF-EEYaHSyl-NE3E/Linkin%2BPark.-%2BNew%2BDivide.mp3?r=idz&dl=311&ref=linkin-park";
        a[3] = "https://songolum.com/file/eakneYGOU16ZjWQ5318td2KYd5fEb-i54Bw9iI9Bn5Q/One%2BMore%2BLight%2B-%2BLinkin%2BPark.mp3?r=idz&dl=311&ref=linkin-park";
        a[4] = "https://songolum.com/file/BZYW64aSmUZ72lxiL0sjjS9wHW9KqYk1j7k4fym_-LI/BURN%2BIT%2BDOWN.mp3?r=idz&dl=311&ref=linkin-park";
        a[5] = "https://songolum.com/file/9an-tRMaYIYk2Ph3vvYQxW9hvJZFvNF_jRrNOtJxIDo/Marshmello%2B%26%2BAnne-Marie%2B-%2BFRIENDS.mp3?r=idz&dl=311&ref=marshmello-anne-marie-friends";
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
        a[0] = "https://songolum.com/file/kh1ImqRxXC88eKgYN4yiMw3H1PK6LumyICWB5cVwcvs/In%2BThe%2BEnd%2B-%2BLinkin%2BPark.mp3?r=idz&dl=311&ref=linkin-park";
        a[1] = "https://songolum.com/file/2tcGMd-WzmwOgyX26ujKX1edt4yoDVBunPlSz2O7Z5s/Numb%2B-%2BLinkin%2BPark.mp3?r=idz&dl=311&ref=linkin-park";
        a[2] = "https://songolum.com/file/9BbuvB2LgYHR-QaBf3TqaPYKiXztF-EEYaHSyl-NE3E/Linkin%2BPark.-%2BNew%2BDivide.mp3?r=idz&dl=311&ref=linkin-park";
        a[3] = "https://songolum.com/file/eakneYGOU16ZjWQ5318td2KYd5fEb-i54Bw9iI9Bn5Q/One%2BMore%2BLight%2B-%2BLinkin%2BPark.mp3?r=idz&dl=311&ref=linkin-park";
        a[4] = "https://songolum.com/file/BZYW64aSmUZ72lxiL0sjjS9wHW9KqYk1j7k4fym_-LI/BURN%2BIT%2BDOWN.mp3?r=idz&dl=311&ref=linkin-park";
        a[5] = "https://songolum.com/file/9an-tRMaYIYk2Ph3vvYQxW9hvJZFvNF_jRrNOtJxIDo/Marshmello%2B%26%2BAnne-Marie%2B-%2BFRIENDS.mp3?r=idz&dl=311&ref=marshmello-anne-marie-friends";

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
        //
        //musicView.stopNotesFall();
        super.onBackPressed();
        finish();
    }
}
