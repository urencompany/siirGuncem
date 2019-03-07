package com.uren.siirler.Utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.uren.siirler.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by f22labs on 07/03/17.
 */

public class Utils {

    private static int screenWidth = 0;
    private static int screenHeight = 0;

    public static final void showToast(Context context, String message) {

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    public static final String getDeviceID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }


    public static final String getVersionName(Context context) {

        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pInfo.versionName;

    }


    public static void setButtonBackgroundColor(Context context, Button button, int color) {

        if (Build.VERSION.SDK_INT >= 23) {
            button.setBackgroundColor(context.getResources().getColor(color, null));
        } else {
            button.setBackgroundColor(context.getResources().getColor(color));
        }
    }

    public static void setButtonBackgroundColor(Context context, TextView textView, int color) {

        if (Build.VERSION.SDK_INT >= 23) {
            textView.setBackgroundColor(context.getResources().getColor(color, null));
        } else {
            textView.setBackgroundColor(context.getResources().getColor(color));
        }
    }


    public static Drawable setDrawableSelector(Context context, int normal, int selected) {


        Drawable state_normal = ContextCompat.getDrawable(context, normal);

        Drawable state_pressed = ContextCompat.getDrawable(context, selected);


        Bitmap state_normal_bitmap = ((BitmapDrawable) state_normal).getBitmap();

        // Setting alpha directly just didn't work, so we draw a new bitmap!
        Bitmap disabledBitmap = null;
        try {
            disabledBitmap = Bitmap.createBitmap(
                    state_normal.getIntrinsicWidth(),
                    state_normal.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Canvas canvas = new Canvas(disabledBitmap);

        Paint paint = new Paint();
        paint.setAlpha(126);
        canvas.drawBitmap(state_normal_bitmap, 0, 0, paint);

        BitmapDrawable state_normal_drawable = new BitmapDrawable(context.getResources(), disabledBitmap);


        StateListDrawable drawable = new StateListDrawable();

        drawable.addState(new int[]{android.R.attr.state_selected},
                state_pressed);
        drawable.addState(new int[]{android.R.attr.state_enabled},
                state_normal_drawable);

        return drawable;
    }


    public static StateListDrawable selectorRadioImage(Context context, Drawable normal, Drawable pressed) {
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_checked}, pressed);
        states.addState(new int[]{}, normal);
        //                imageView.setImageDrawable(states);
        return states;
    }

    public static StateListDrawable selectorRadioButton(Context context, int normal, int pressed) {
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_checked}, new ColorDrawable(pressed));
        states.addState(new int[]{}, new ColorDrawable(normal));
        return states;
    }

    public static ColorStateList selectorRadioText(Context context, int normal, int pressed) {
        ColorStateList colorStates = new ColorStateList(new int[][]{new int[]{android.R.attr.state_checked}, new int[]{}}, new int[]{pressed, normal});
        return colorStates;
    }


    public static StateListDrawable selectorRadioDrawable(Drawable normal, Drawable pressed) {
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_checked}, pressed);
        states.addState(new int[]{}, normal);
        return states;
    }

    public static StateListDrawable selectorBackgroundColor(Context context, int normal, int pressed) {
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(pressed));
        states.addState(new int[]{}, new ColorDrawable(normal));
        return states;
    }

    public static StateListDrawable selectorBackgroundDrawable(Drawable normal, Drawable pressed) {
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed}, pressed);
        states.addState(new int[]{}, normal);
        return states;
    }

    public static ColorStateList selectorText(Context context, int normal, int pressed) {
        ColorStateList colorStates = new ColorStateList(new int[][]{new int[]{android.R.attr.state_pressed}, new int[]{}}, new int[]{pressed, normal});
        return colorStates;
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int getScreenHeight(Context c) {
        if (screenHeight == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        }

        return screenHeight;
    }

    public static int getScreenWidth(Context c) {
        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }

        return screenWidth;
    }

    public static boolean isAndroid5() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }


    public static Typeface[] getTypeFaceList(Context context) {

        try {
            Typeface[] typeFaceList = new Typeface[]{

                    // Typeface.createFromAsset(context.getAssets(), "fonttypes/JMH Typewriter-Bold.ttf"),
                    // Typeface.createFromAsset(context.getAssets(), "fonttypes/Cabin-Medium.ttf"),
                    // Typeface.createFromAsset(context.getAssets(), "fonttypes/Cabin-Regular.ttf"),
                    //  Typeface.createFromAsset(context.getAssets(), "fonttypes/Comfortaa-Regular.ttf"),//not sure
                    Typeface.createFromAsset(context.getAssets(), "fonttypes/Roboto-Regular.ttf"),
                    Typeface.createFromAsset(context.getAssets(), "fonttypes/Sansation_Regular.ttf"), //ok
                    Typeface.createFromAsset(context.getAssets(), "fonttypes/Young.ttf"),
                    //Typeface.createFromAsset(context.getAssets(), "fonttypes/nimbusmono-bold.otf"),
                    Typeface.createFromAsset(context.getAssets(), "fonttypes/Courier Prime.ttf"),
                    Typeface.createFromAsset(context.getAssets(), "fonttypes/Aristotelica Display DemiBold Trial.ttf")// ok
            };

            return typeFaceList;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static HashMap<Integer, Bitmap> getIdPhotoHashmap(Context context) {

        AssetManager assetManager = context.getAssets();
        HashMap<Integer, Bitmap> photoHashmap = new HashMap<>();

        ArrayList<Integer> idList = new ArrayList<>();
        ArrayList<Bitmap> bitmapList = new ArrayList<>();

        try {
            String[] files = assetManager.list("profilepics");
            ArrayList<String> listPath = new ArrayList<>();

            for (int i = 0; i < files.length; i++) {

                int endIndex = files[i].indexOf("_");
                String substr = files[i].substring(0, endIndex);
                int id = Integer.valueOf(substr);

                idList.add(id);

                //String pathAssets = "profilepics" + File.separator + files[i];
                //listPath.add(pathAssets);
            }

            bitmapList = profilePicBitmapList(context);

            for (int i = 0; i < idList.size(); i++) {
                photoHashmap.put(idList.get(i), bitmapList.get(i));
            }


            return photoHashmap;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ArrayList<Bitmap> profilePicBitmapList(Context context) {

        AssetManager assetManager = context.getAssets();
        ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
        Bitmap bitmap = null;

        try {
            String[] imgPath = assetManager.list("profilepics");
            for (int i = 0; i < imgPath.length; i++) {
                InputStream is = assetManager.open("profilepics/" + imgPath[i]);
                bitmap = BitmapFactory.decodeStream(is);
                bitmapArrayList.add(bitmap);
            }
            return bitmapArrayList;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static int getRawItem(int soundId) {

        final int[] rawID =
                {0,
                        R.raw.asirlik_fon_muzigi,
                        R.raw.fon_4,
                        R.raw.duygusal_fon_1,
                        R.raw.duygusal_fon_2,
                        R.raw.duygusal_fon_3,
                        R.raw.al_omrumu,
                        R.raw.ay_dusunce,
                        R.raw.bir_kucuk_meyve_icin_dali_incitme_gonul,
                        R.raw.filed_fon_entrumental,
                        R.raw.mona_roza,
                        R.raw.vazgectim_ney
                };

        return rawID[soundId];

    }

    public static ArrayList<int[]> getBackgroundColorList(Context context) {

        ArrayList<int[]> colorList = new ArrayList<>();

        int[] background1 = new int[]{
            R.color.startColor1,
                R.color.endColor1
        };
        int[] background2 = new int[]{
                R.color.startColor2,
                R.color.endColor2
        };
        int[] background3 = new int[]{
                R.color.startColor3,
                R.color.endColor3
        };
        int[] background4 = new int[]{
                R.color.startColor4,
                R.color.endColor4
        };
        int[] background5 = new int[]{
                R.color.startColor5,
                R.color.endColor5
        };
        int[] background6 = new int[]{
                R.color.startColor6,
                R.color.endColor6
        };
        int[] background7 = new int[]{
                R.color.startColor7,
                R.color.endColor7
        };
        int[] background8 = new int[]{
                R.color.startColor8,
                R.color.endColor8
        };
        int[] background9 = new int[]{
                R.color.startColor9,
                R.color.endColor9
        };
        int[] background10 = new int[]{
                R.color.startColor10,
                R.color.endColor10
        };
        int[] background11 = new int[]{
                R.color.startColor11,
                R.color.endColor11
        };

        colorList.add(background1);
        colorList.add(background2);
        colorList.add(background3);
        colorList.add(background4);
        colorList.add(background5);
        colorList.add(background6);
        colorList.add(background7);
        colorList.add(background8);
        colorList.add(background9);
        colorList.add(background10);
        colorList.add(background11);

        return colorList;

    }

}
