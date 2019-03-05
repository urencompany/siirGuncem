package com.uren.siirler.Utils;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.uren.siirler.Constants.NumericConstants;
import com.uren.siirler.R;
import com.uren.siirler._model.Sair;

public class UserDataUtil {

    public static String getShortenUserName(String name) {
        String returnValue = "";
        try {
            if (name != null && !name.trim().isEmpty()) {
                String[] seperatedName = name.trim().split(" ");
                for (String word : seperatedName) {
                    if (returnValue.length() < 3)
                        returnValue = returnValue + word.substring(0, 1).toUpperCase();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnValue;
    }

    public static void setProfilePicture(Context context, Sair sair, TextView shortNameTv, ImageView profilePicImgView) {
        try {
            if (context == null) return;


            if (sair.getProfilePicIndex() != -1) {
                shortNameTv.setVisibility(View.GONE);
                Glide.with(context)
                        .load(sair.getProfilePicIndex())
                        .apply(RequestOptions.circleCropTransform())
                        .into(profilePicImgView);
                profilePicImgView.setPadding(1, 1, 1, 1); // degerler asagidaki imageShape strokeWidth ile aynı tutulmalı
            } else {
                if (sair.getAd() != null && !sair.getAd().trim().isEmpty()) {
                    shortNameTv.setVisibility(View.VISIBLE);
                    shortNameTv.setText(UserDataUtil.getShortenUserName(sair.getAd()));
                    profilePicImgView.setImageDrawable(null);
                } else {
                    shortNameTv.setVisibility(View.GONE);
                    Glide.with(context)
                            .load(context.getResources().getIdentifier("icon_user_profile", "mipmap", context.getPackageName()))
                            .apply(RequestOptions.circleCropTransform())
                            .into(profilePicImgView);
                }
            }

            GradientDrawable imageShape = ShapeUtil.getShape(context.getResources().getColor(R.color.DodgerBlue),
                    context.getResources().getColor(R.color.White),
                    GradientDrawable.OVAL, 50, 3);
            profilePicImgView.setBackground(imageShape);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void setProfilePictureWithStroke(Context context, Sair sair, TextView shortNameTv, ImageView profilePicImgView, int colorCode) {
        try {
            if (context == null) return;


            if (sair.getProfilePicIndex() != -1) {
                shortNameTv.setVisibility(View.GONE);
                Glide.with(context)
                        .load(sair.getProfilePicIndex())
                        .apply(RequestOptions.circleCropTransform())
                        .into(profilePicImgView);
                profilePicImgView.setPadding(1, 1, 1, 1); // degerler asagidaki imageShape strokeWidth ile aynı tutulmalı
            } else {
                if (sair.getAd() != null && !sair.getAd().trim().isEmpty()) {
                    shortNameTv.setVisibility(View.VISIBLE);
                    shortNameTv.setText(UserDataUtil.getShortenUserName(sair.getAd()));
                    profilePicImgView.setImageDrawable(null);
                } else {
                    shortNameTv.setVisibility(View.GONE);
                    Glide.with(context)
                            .load(context.getResources().getIdentifier("icon_user_profile", "mipmap", context.getPackageName()))
                            .apply(RequestOptions.circleCropTransform())
                            .into(profilePicImgView);
                }
            }

            GradientDrawable imageShape = ShapeUtil.getShape(context.getResources().getColor(R.color.DodgerBlue),
                    colorCode,
                    GradientDrawable.OVAL, 50, 3);
            profilePicImgView.setBackground(imageShape);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void setProfilePictureRectangle(Context context, Sair sair, TextView shortNameTv, ImageView profilePicImgView, int colorCode) {
        try {
            if (context == null) return;


            if (sair.getProfilePicIndex() != -1) {
                shortNameTv.setVisibility(View.GONE);
                Glide.with(context)
                        .load(sair.getProfilePicIndex())
                        .apply(RequestOptions.centerCropTransform())
                        .into(profilePicImgView);
                profilePicImgView.setPadding(1, 1, 1, 1); // degerler asagidaki imageShape strokeWidth ile aynı tutulmalı
            } else {
                if (sair.getAd() != null && !sair.getAd().trim().isEmpty()) {
                    shortNameTv.setVisibility(View.VISIBLE);
                    shortNameTv.setText(UserDataUtil.getShortenUserName(sair.getAd()));
                    profilePicImgView.setImageDrawable(null);
                } else {
                    shortNameTv.setVisibility(View.GONE);
                    Glide.with(context)
                            .load(context.getResources().getIdentifier("icon_user_profile", "mipmap", context.getPackageName()))
                            .apply(RequestOptions.circleCropTransform())
                            .into(profilePicImgView);
                }
            }

            GradientDrawable imageShape = ShapeUtil.getShape(context.getResources().getColor(R.color.DodgerBlue),
                    colorCode,
                    GradientDrawable.RECTANGLE, 5, 1);
            profilePicImgView.setBackground(imageShape);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
