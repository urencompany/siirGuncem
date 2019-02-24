package com.uren.siirler.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.InputStream;

public class BitmapConversion extends AppCompatActivity {

    public static Bitmap getRoundedShape(Bitmap scaleBitmapImage, int Width, int Height) {

        Bitmap targetBitmap = null;

        int targetWidth = Width;
        int targetHeight = Height;
        targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        canvas.drawBitmap(scaleBitmapImage,
                new Rect(0, 0, scaleBitmapImage.getWidth(),
                        scaleBitmapImage.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);

        return targetBitmap;
    }

    public static Bitmap getScreenShot(View view) {
        Bitmap bitmap = null;
        view.setDrawingCacheEnabled(true);
        bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return bitmap;
    }

    public static void setBlurBitmap(Context context, View view, int drawableItem, float bitmapScale,
                                     float blurRadius, Bitmap mBitmap) {
        Bitmap bitmap;
        if (mBitmap != null)
            bitmap = mBitmap;
        else if (drawableItem != 0) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            bitmap = BitmapFactory.decodeResource(context.getResources(), drawableItem, options);
        } else return;

        Bitmap blurBitmap = BlurBuilder.blur(context, bitmap, bitmapScale, blurRadius);
        Drawable dr = new BitmapDrawable(context.getResources(), blurBitmap);
        view.setBackground(dr);
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        Bitmap resizedBitmap = null;

        int width = bm.getWidth();
        int height = bm.getHeight();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);

        if (bm != null && !bm.isRecycled())
            bm.recycle();

        return resizedBitmap;
    }

    public static Bitmap getBitmapFromInputStream(InputStream input, Context context,
                                                  int width, int height) {
        Bitmap myBitmap = null;
        myBitmap = BitmapFactory.decodeStream(input);
        Bitmap roundedBitmap = BitmapConversion.getRoundedShape(myBitmap, width, height);
        return roundedBitmap;
    }
}