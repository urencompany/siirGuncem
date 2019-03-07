package com.uren.siirler.MainFragments.ShareManagement;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;

import com.uren.siirler.R;

public class ShareContent {

    int[] TEXT_COLORS = new int[]{
            R.color.Black,
            R.color.Orange,
            R.color.LightSalmon,
            R.color.DarkOrange,
            R.color.Coral,
            R.color.HotPink,
            R.color.Tomato,
            R.color.OrangeRed,
            R.color.DeepPink,
            R.color.Fuchsia,
            R.color.DarkRed,
            R.color.BlueViolet,
            R.color.LightSkyBlue,
            R.color.SkyBlue,
            R.color.Gray,
            R.color.Olive,
            R.color.Purple,
            R.color.Maroon,
            R.color.Aquamarine,
            R.color.DarkCyan,
            R.color.Teal,
            R.color.Green,
            R.color.DarkGreen,
            R.color.Blue,
            R.color.MediumBlue,
            R.color.DarkBlue,
            R.color.White
    };


    public ShareContent() {

    }

    public int[] getTextColors() {
        return TEXT_COLORS;
    }

    public Typeface[] getTypeFace(Context context) {

        Resources resources = context.getResources();

        Typeface[] typeFaceList = new Typeface[0];
        try {
            typeFaceList = new Typeface[]{

                    // Typeface.createFromAsset(context.getAssets(), "fonttypes/JMH Typewriter-Bold.ttf"),
                    // Typeface.createFromAsset(context.getAssets(), "fonttypes/Cabin-Medium.ttf"),
                    // Typeface.createFromAsset(context.getAssets(), "fonttypes/Cabin-Regular.ttf"),
                    //  Typeface.createFromAsset(context.getAssets(), "fonttypes/Comfortaa-Regular.ttf"),//not sure
                    Typeface.createFromAsset(context.getAssets(), "fonttypes/Roboto-Regular.ttf"),
                    Typeface.createFromAsset(context.getAssets(), "fonttypes/Sansation_Regular.ttf"), //ok
                    Typeface.createFromAsset(context.getAssets(), "fonttypes/Young.ttf"),
                    //Typeface.createFromAsset(context.getAssets(), "fonttypes/nimbusmono-bold.otf"),
                    Typeface.createFromAsset(context.getAssets(), "fonttypes/Courier Prime.ttf"),
                    Typeface.createFromAsset(context.getAssets(), "fonttypes/Aristotelica Display DemiBold Trial.ttf")// ok// ok
            };
        } catch (Exception e) {
            e.printStackTrace();
        }

        return typeFaceList;
    }
}
