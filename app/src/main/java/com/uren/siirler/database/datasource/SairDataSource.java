package com.uren.siirler.database.datasource;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.uren.siirler.database.DatabaseHelper;
import com.uren.siirler.model.Sair;

import java.util.ArrayList;

/**
 * Created by topaloglun on 23/2/2019.
 */
public class SairDataSource {

    public static final String SAIR_TABLE_NAME = "Sairler";
    public static final String SAIR_ID = "id";
    public static final String SAIR_AD = "ad";
    public static final String SAIR_DOGUM_TARIHI = "dogum_tarihi";
    public static final String SAIR_OLUM_TARIHI = "olum_tarihi";
    private static Cursor cursor;
    private DatabaseHelper databaseHelper;

    public SairDataSource(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public ArrayList<Sair> getSairList() {

        ArrayList<Sair> sairArrayList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        cursor =
                db.rawQuery(
                        "SELECT Sairler.id,Sairler.ad,Sairler.dogum_tarihi,Sairler.olum_tarihi FROM Sairler",
                        null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            Sair sair = new Sair();
            sair.setId(cursor.getInt(cursor.getColumnIndex(SAIR_ID)));
            sair.setAd(cursor.getString(cursor.getColumnIndex(SAIR_AD)));
            sair.setDogumTarihi(cursor.getString(cursor.getColumnIndex(SAIR_DOGUM_TARIHI)));
            sair.setOlumTarihi(cursor.getString(cursor.getColumnIndex(SAIR_OLUM_TARIHI)));
            sairArrayList.add(sair);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return sairArrayList;
    }

}
