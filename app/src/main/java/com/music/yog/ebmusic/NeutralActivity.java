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

public class NeutralActivity extends AppCompatActivity implements MediaPlayer.OnBufferingUpdateListener,MediaPlayer.OnCompletionListener{

    private ImageButton btn_play_pause;
    private SeekBar seekBar;
    private TextView textView;
    SeekBar volumeBar;

    private VusikView musicView;

    private MediaPlayer mediaPlayer;
    private int mediaFileLength;
    private int realtimeLength;
    Button forward,previous,play;
    String a[]=new String[12];
    Uri myUri;
    int i;

    final Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neutral);
//        musicView.start();

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
                final ProgressDialog mDialog = new ProgressDialog(NeutralActivity.this);


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

                mp3Play.execute("http://myxy.co/mp3/128/Malayalam/40706/Tu-Buddhi-De-(RaagSong.Com).mp3"); // direct link mp3 file
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
       musicView.stopNotesFall();

    }
    public void next_track() {
        mediaPlayer = new MediaPlayer();

        i = (int) (Math.random() * 12);
        a[0] = "https://downpwnew.com/11755/Darasal%20-%20Raabta%20(Atif%20Aslam)%20190Kbps.mp3";
        a[1] = "http://cdn1.jatt.link/fa9d413d8b64607056fadfa042818e84/lfyvv/Mendichyaa%20Paanaawara-(Mr-Jatt.com).mp3";
        a[2] = "http://cdn1.jatt.link/fa9d413d8b64607056fadfa042818e84/nfyvv/Airanichya%20Dewa%20Tula-(Mr-Jatt.com).mp3";
        a[3] = "https://downpwnew.com/12737/01%20Jogi-%20Yasser%20Desai%20190Kbps.mp3";
        a[4] = "https://downpwnew.com/12737/04%20Tu%20Banja%20Gali%20Benaras%20Ki%20-%20Asit%20Tripathy%20190Kbps.mp3";
        a[5] = "https://downpwnew.com/11755/Darasal%20-%20Raabta%20(Atif%20Aslam)%20190Kbps.mp3";
        a[6]="https://downpwnew.com/11053/04%20Dariya%20-%20Baar%20Baar%20Dekho%20(Arko)%20320Kbps.mp3";
        a[7]="https://downpwnew.com/11053/02%20Kho%20Gaye%20Hum%20Kahan%20-%20Baar%20Baar%20Dekho%20190Kbps.mp3";
        a[8]="https://downpwnew.com/14050/01%20Sanu%20Ek%20Pal%20Chain%20-%20Raid%20190Kbps.mp3";
        a[9]="https://downpwnew.com/12875/Tujse%20Door%20Jo%20Hota%20Hun%20-%20Gajendra%20Verma%20320Kbps.mp3";
        a[10]="https://downpwnew.com/11755/Hoor%20-%20Hindi%20Medium%20(Atif%20Aslam)%20320Kbps.mp3";
        a[11]="https://downpwnew.com/11755/Darasal%20-%20Raabta%20(Atif%20Aslam)%20190Kbps.mp3";


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
        mediaPlayer = new MediaPlayer();
        i = (int) (Math.random() * 12);
        a[0] = "https://downpwnew.com/12737/01%20Jogi-%20Yasser%20Desai%20190Kbps.mp3";
        a[1] = "http://cdn1.jatt.link/fa9d413d8b64607056fadfa042818e84/lfyvv/Mendichyaa%20Paanaawara-(Mr-Jatt.com).mp3";
        a[2] = "http://cdn1.jatt.link/fa9d413d8b64607056fadfa042818e84/nfyvv/Airanichya%20Dewa%20Tula-(Mr-Jatt.com).mp3";
        a[3] = "http://cdn1.jatt.link/fa9d413d8b64607056fadfa042818e84/gfyvv/Chapha%20Bolena-(Mr-Jatt.com).mp3";
        a[4] = "https://downpwnew.com/12737/04%20Tu%20Banja%20Gali%20Benaras%20Ki%20-%20Asit%20Tripathy%20190Kbps.mp3";
        a[5] = "https://downpwnew.com/11755/Darasal%20-%20Raabta%20(Atif%20Aslam)%20190Kbps.mp3";
        a[6]="https://downpwnew.com/11053/04%20Dariya%20-%20Baar%20Baar%20Dekho%20(Arko)%20320Kbps.mp3";
        a[7]="https://downpwnew.com/11053/02%20Kho%20Gaye%20Hum%20Kahan%20-%20Baar%20Baar%20Dekho%20190Kbps.mp3";
        a[8]="https://downpwnew.com/14050/01%20Sanu%20Ek%20Pal%20Chain%20-%20Raid%20190Kbps.mp3";
        a[9]="https://downpwnew.com/12875/Tujse%20Door%20Jo%20Hota%20Hun%20-%20Gajendra%20Verma%20320Kbps.mp3";
        a[10]="https://downpwnew.com/11755/Hoor%20-%20Hindi%20Medium%20(Atif%20Aslam)%20320Kbps.mp3";
        a[11]="https://downpwnew.com/11755/Darasal%20-%20Raabta%20(Atif%20Aslam)%20190Kbps.mp3";
        myUri = Uri.parse(a[i]);

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
        //musicView.start();
        //musicView.stopNotesFall();
        super.onBackPressed();
        finish();

    }
}

