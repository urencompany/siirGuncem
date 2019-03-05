package com.uren.siirler.Singleton;

import com.uren.siirler._model.Sair;
import com.uren.siirler._model.Siir;

import java.util.ArrayList;

public class SingletonSairList {

    private static SingletonSairList single_instance = null; // static variable single_instance of type Singleton
    private ArrayList<Sair> sairArrayList; // variable of type String


    private SingletonSairList() {
        sairArrayList = new ArrayList<>();
    }

    public static SingletonSairList getInstance() {
        if (single_instance == null)
            single_instance = new SingletonSairList();

        return single_instance;
    }

    public ArrayList<Sair> getSairArrayList() {
        return sairArrayList;
    }

    public void setSairArrayList(ArrayList<Sair> siirArrayList) {
        this.sairArrayList = siirArrayList;
    }

    public static void reset() {
        single_instance = null;
    }

}
