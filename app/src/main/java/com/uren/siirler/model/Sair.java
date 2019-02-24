package com.uren.siirler.model;


/**
 * Created by topaloglun on 23/2/2019.
 */
public class Sair {
    private int id;
    private String ad;
    private String dogumTarihi;
    private String olumTarihi;

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
}
