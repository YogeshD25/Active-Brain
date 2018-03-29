package com.music.yog.ebmusic;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
//import android.graphics.Camera;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.microsoft.projectoxford.emotion.EmotionServiceClient;
import com.microsoft.projectoxford.emotion.EmotionServiceRestClient;
import com.microsoft.projectoxford.emotion.contract.RecognizeResult;
import com.microsoft.projectoxford.emotion.contract.Scores;
import com.microsoft.projectoxford.emotion.rest.EmotionServiceException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static android.widget.Toast.*;
import com.mindorks.paracamera.Camera;
import static com.mindorks.paracamera.Camera.REQUEST_TAKE_PHOTO;

public class EmotionActivity extends AppCompatActivity {

    ImageView imageView;
    Button btnT,btnP,btn;
    Camera camera;
    int TAKE_PICTURE_CODE=100,REQUEST_PERMISSION_CODE=101;
    Bitmap bitmap;
    final Context context = this;
    public static int happy=0,sad=0,angry=0,contempt=0,disgust=0,surprise=0,neutral=0,fear=0;




    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        if (requestCode == REQUEST_PERMISSION_CODE) {
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                makeText(this,"Permission Granted", LENGTH_SHORT).show();

            }
            else
            {
                makeText(this,"Permission Denied", LENGTH_SHORT).show();
            }
        }
    }

    EmotionServiceClient restClient=new EmotionServiceRestClient("fd9cfd517e7c4879bcec256a052a92d8");


    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("NewApi")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion);

        initViews();

        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_CONTACTS)!=PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]
                    {
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.INTERNET
                    },REQUEST_PERMISSION_CODE);

        }


    }
    private void initViews()
    {
        btnP=(Button)findViewById(R.id.btnProcess);
        btn=(Button)findViewById(R.id.openc);
        imageView =(ImageView)findViewById(R.id.imgView);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto(view);

            }
        });
        btnP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(imageView.getDrawable() == null)
                {
                 /*   Toast toast=Toast.makeText(getApplicationContext(),"Image is not selected",Toast.LENGTH_SHORT);
                    toast.setMargin(50,50);
                    toast.show();*/
                    // Setting Dialog Title
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            context);

                    // set title
                    alertDialogBuilder.setTitle("Must select an image before processing");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Press back and click Open Camera")
                            .setCancelable(true);

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }
                else {
                    processImage();
                }

            }
        });
    }

    public void takePhoto(View view) {
        camera = new Camera.Builder()
                .setDirectory("pics")
                .setName("ali_" + System.currentTimeMillis())
                .setImageFormat(Camera.IMAGE_JPG)
                .setCompression(55)
                .setImageHeight(600)
                .build(this);
        try {
            camera.takePicture();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }








    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {

        if (requestCode == Camera.REQUEST_TAKE_PHOTO) {


            bitmap = camera.getCameraBitmap();
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                Toast.makeText(getApplicationContext(), "Picture not taken!", Toast.LENGTH_SHORT).show();
            }
        }


    }



    public void  processImage()

    {
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,outputStream);


        ByteArrayInputStream inputStream=new ByteArrayInputStream(outputStream.toByteArray());
        @SuppressLint("StaticFieldLeak") AsyncTask<InputStream,String,List<RecognizeResult>>processAsync=new AsyncTask<InputStream, String, List<RecognizeResult>>()
        {


            ProgressDialog mDialog=new ProgressDialog(EmotionActivity.this);

            protected void onPreExecute()
            {
                mDialog.show();
            }

            protected void onProgressUpdate(String... values)
            {
                mDialog.setMessage(values[0]);
            }

            protected List<RecognizeResult> doInBackground(InputStream... params) {
                publishProgress("Please Wait");
                List<RecognizeResult> result = null;
                try {

                    result = restClient.recognizeImage(params[0]);
                } catch (EmotionServiceException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            protected void onPostExecute(List<RecognizeResult> recognizeResults) {
                mDialog.dismiss();
                for (RecognizeResult res : recognizeResults) {
                    String status = getEmotion(res);
                    imageView.setImageBitmap(ImageHelper.drawRectOnBitmap(bitmap, res.faceRectangle, status));

                    if (happy >0) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Intent i = new Intent(EmotionActivity.this, HappyActivity.class);
                                startActivity(i);
                                happy--;
                            }
                        }, 3000);

                    }
                    else if (neutral >0) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Intent i = new Intent(EmotionActivity.this, NeutralActivity.class);
                                startActivity(i);
                                neutral--;
                            }
                        }, 3000);

                    }
                    else if (surprise >0) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Intent i = new Intent(EmotionActivity.this, SurpriseActivity.class);
                                startActivity(i);
                                surprise--;
                            }
                        }, 3000);

                    }
                    else if (sad >0) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Intent i = new Intent(EmotionActivity.this, SadActivity.class);
                                startActivity(i);
                                sad--;
                            }
                        }, 3000);

                    }
                    else if (angry >0) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Intent i = new Intent(EmotionActivity.this, AngryActivity.class);
                                startActivity(i);
                                angry--;
                            }
                        }, 3000);

                    }
                    else if (contempt >0) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Intent i = new Intent(EmotionActivity.this, ContemptActivity.class);
                                startActivity(i);
                                contempt--;
                            }
                        }, 3000);

                    }
                    else if (fear >0) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Intent i = new Intent(EmotionActivity.this, FearActivity.class);
                                startActivity(i);
                                fear--;
                            }
                        }, 3000);

                    }
                    else  {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Intent i = new Intent(EmotionActivity.this, DisguetActivity.class);
                                startActivity(i);
                                disgust--;
                            }
                        }, 3000);

                    }

                }
            }
        };

        processAsync.execute(inputStream);
    }

    public String getEmotion(RecognizeResult res)
    {

        List<Double> list=new ArrayList<>();
        Scores scores=res.scores;

        list.add(scores.anger);
        list.add(scores.happiness);
        list.add(scores.contempt);
        list.add(scores.disgust);
        list.add(scores.fear);
        list.add(scores.neutral);
        list.add(scores.sadness);
        list.add(scores.surprise);

        Collections.sort(list);

        double maxNum=list.get(list.size()-1);
        if(maxNum==scores.anger)
        {   angry++;
            return "Anger";

        }
        else if(maxNum==scores.happiness)
        {
            happy++;
            return "Happiness";

        }
        else if(maxNum==scores.neutral)
        {
            neutral++;
            return "Neutral";

        }
        else if(maxNum==scores.fear)
        {
            fear++;
            return "Fear";
        }
        else if(maxNum==scores.disgust)
        {
            disgust++;
            return "Disgust";
        }else if(maxNum==scores.surprise)
        {
            surprise++;
            return "Surprise";
        }
        else if(maxNum==scores.sadness)
        {
            sad++;
            return "Sadness";

        }
        else if(maxNum==scores.contempt)
        {
            contempt++;
            return "Contempt";
        }
        else
        {

            return "No Face is Present";
        }

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EmotionActivity.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();

    }
}

