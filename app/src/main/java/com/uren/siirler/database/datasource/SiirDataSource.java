package com.uren.siirler.database.datasource;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.uren.siirler.database.DatabaseHelper;
import com.uren.siirler.model.Sair;
import com.uren.siirler.model.Siir;

import java.util.ArrayList;

/**
 * Created by topaloglun on 23/2/2019.
 */
public class SiirDataSource {

    public static final String SIIR_TABLE_NAME = "Siirler";
    public static final String SIIR_ID = "id";
    public static final String SIIR_SAIR_ID = "sairId";
    public static final String SIIR_AD = "ad";
    public static final String SIIR_METIN = "metin";

    private static Cursor cursor;
    private DatabaseHelper databaseHelper;

    public SiirDataSource(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public ArrayList<Siir> getSiirList() {

        ArrayList<Siir> siirArrayList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        cursor =
                db.rawQuery(
                        "SELECT Siirler.id,Siirler.sairId,Siirler.ad,Siirler.metin FROM Siirler",
                        null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            Siir siir = new Siir();
            siir.setId(cursor.getInt(cursor.getColumnIndex(SIIR_ID)));
            siir.setSairId(cursor.getInt(cursor.getColumnIndex(SIIR_SAIR_ID)));
            siir.setAd(cursor.getString(cursor.getColumnIndex(SIIR_AD)));
            siir.setMetin(cursor.getBlob(cursor.getColumnIndex(SIIR_METIN)));

            siirArrayList.add(siir);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return siirArrayList;
    }

}
