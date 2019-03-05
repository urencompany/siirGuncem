package com.uren.siirler._model;


/**
 * Created by topaloglun on 23/2/2019.
 */
public class Siir {
    private int id;
    private int sairId;
    private String ad;
    private byte[] metin;
    private boolean isFavorite;
    private int displayCount;
    private boolean isDailyPoem;
    private boolean isDailyClicked;

    private boolean showFull;
    private boolean consistsDetail;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSairId() {
        return sairId;
    }

    public void setSairId(int sairId) {
        this.sairId = sairId;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public byte[] getMetin() {
        return metin;
    }

    public void setMetin(byte[] data) {
        this.metin = data;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isShowFull() {
        return showFull;
    }

    public void setShowFull(boolean showFull) {
        this.showFull = showFull;
    }

    public int getDisplayCount() {
        return displayCount;
    }

    public void setDisplayCount(int displayCount) {
        this.displayCount = displayCount;
    }

    public boolean getIsDailyPoem() {
        return isDailyPoem;
    }

    public void setIsDailyPoem(boolean isDailyPoem) {
        this.isDailyPoem = isDailyPoem;
    }

    public boolean getIsDailyClicked() {
        return isDailyClicked;
    }

    public void setIsDailyClicked(boolean isDailyClicked) {
        this.isDailyClicked = isDailyClicked;
    }

    public boolean isConsistsDetail() {
        return consistsDetail;
    }

    public void setConsistsDetail(boolean consistsDetail) {
        this.consistsDetail = consistsDetail;
    }
}
