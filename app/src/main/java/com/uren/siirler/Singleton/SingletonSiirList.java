package com.uren.siirler.Singleton;

import com.uren.siirler._model.Siir;

import java.util.ArrayList;

public class SingletonSiirList {

    private static SingletonSiirList single_instance = null; // static variable single_instance of type Singleton
    private ArrayList<Siir> siirArrayList; // variable of type String


    private SingletonSiirList() {
        siirArrayList = new ArrayList<>();
    }

    public static SingletonSiirList getInstance() {
        if (single_instance == null)
            single_instance = new SingletonSiirList();

        return single_instance;
    }

    public ArrayList<Siir> getSiirArrayList() {
        return siirArrayList;
    }

    public void setSiirArrayList(ArrayList<Siir> siirArrayList) {
        this.siirArrayList = siirArrayList;
    }

    public static void reset() {
        single_instance = null;
    }

}
