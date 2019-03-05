package com.uren.siirler._model;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by topaloglun on 23/2/2019.
 */
public class Sair {
    private int id;
    private String ad;
    private boolean isDisplayed;
    private boolean isPopular;


    private String dogumTarihi;
    private String olumTarihi;
    private int profilePicIndex;

    public int getProfilePicIndex() {
        return profilePicIndex;
    }

    public void setProfilePicIndex(int profilePicIndex) {
        this.profilePicIndex = profilePicIndex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getDogumTarihi() {
        return dogumTarihi;
    }

    public void setDogumTarihi(String dogumTarihi) {
        this.dogumTarihi = dogumTarihi;
    }

    public String getOlumTarihi() {
        return olumTarihi;
    }

    public void setOlumTarihi(String olumTarihi) {
        this.olumTarihi = olumTarihi;
    }


    public boolean isDisplayed() {
        return isDisplayed;
    }

    public void setDisplayed(boolean displayed) {
        isDisplayed = displayed;
    }

    public boolean isPopular() {
        return isPopular;
    }

    public void setPopular(boolean popular) {
        isPopular = popular;
    }



}
