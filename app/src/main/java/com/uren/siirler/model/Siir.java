package com.uren.siirler.model;


/**
 * Created by topaloglun on 23/2/2019.
 */
public class Siir {
    private int id;
    private int sairId;
    private String ad;
    private byte[] metin;

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
}
