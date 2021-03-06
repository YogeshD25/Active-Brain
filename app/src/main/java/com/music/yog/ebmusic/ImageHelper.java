package com.music.yog.ebmusic;

/**
 * Created by Yogesh Dande on 18/01/2018.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.microsoft.projectoxford.emotion.contract.FaceRectangle;

public class ImageHelper {
    public static Bitmap drawRectOnBitmap(Bitmap mBitmap, FaceRectangle faceRectangle, String status)
    {
        Bitmap bitmap=mBitmap.copy(Bitmap.Config.ARGB_8888,true);
        Canvas canvas=new Canvas(bitmap);

        Paint paint=new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(8);

        canvas.drawRect(faceRectangle.left,faceRectangle.top,faceRectangle.left+faceRectangle.width,faceRectangle.top+faceRectangle.height,paint);
        int cX=faceRectangle.left+faceRectangle.width;
        int cY=faceRectangle.top+faceRectangle.height;
        drawRectOnBitmap(canvas,50,cX/2+cX/5,cY+70,Color.WHITE,status);

        return bitmap;

    }

    private static void drawRectOnBitmap(Canvas canvas, int textsize, int cX, int cY, int color, String status) {
        Paint paint=new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        paint.setTextSize(textsize);

        canvas.drawText(status,cX,cY,paint);



    }



}
