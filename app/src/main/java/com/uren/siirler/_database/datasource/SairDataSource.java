package com.uren.siirler._database.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

import com.uren.siirler.Constants.NumericConstants;
import com.uren.siirler.Utils.Utils;
import com.uren.siirler._database.DatabaseHelper;
import com.uren.siirler._model.Sair;

import java.util.ArrayList;
import java.util.HashMap;

import static com.uren.siirler.Constants.StringConstants.SAIRLER_DATABASE;

/**
 * Created by topaloglun on 23/2/2019.
 */
public class SairDataSource {

    public static final String SAIR_TABLE_NAME = "Sairler";
    public static final String SAIR_ID = "id";
    public static final String SAIR_AD = "ad";
    public static final String SAIR_IS_DISPLAYED = "isDisplayed";
    public static final String SAIR_IS_POPULAR = "isPopular";
    public static final String SAIR_DOGUM_TARIHI = "dogum_tarihi";
    public static final String SAIR_OLUM_TARIHI = "olum_tarihi";
    private static Cursor cursor;
    private DatabaseHelper databaseHelper;

    public SairDataSource(Context context) {
        databaseHelper = new DatabaseHelper(context, SAIRLER_DATABASE);
        //profile picture
    }

    public ArrayList<Sair> getSairList() {

        ArrayList<Sair> sairArrayList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        cursor =
                db.rawQuery(
                        "SELECT Sairler.id,Sairler.ad,Sairler.isDisplayed,Sairler.isPopular FROM Sairler",
                        null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Sair sair = setSairColumns(cursor);
            sairArrayList.add(sair);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return sairArrayList;
    }

    public Sair getSair(int sairId) {

        Sair sair = null;
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        cursor =
                db.rawQuery(
                        "SELECT Sairler.id,Sairler.ad,Sairler.isDisplayed,Sairler.isPopular FROM Sairler " +
                                "WHERE Sairler.id = " + sairId,
                        null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            sair = setSairColumns(cursor);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();

        return sair;
    }

    public void updateSairDisplayed(int sairId, boolean isDisplayed) {

        try {
            int value = 0;
            if (isDisplayed) {
                value = 1;
            }

            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(SAIR_IS_DISPLAYED, value);
            db.update(SAIR_TABLE_NAME, values, SAIR_ID + " = " + sairId, null);
            db.close();
        } catch (Exception e) {
            Log.e("updateError", e.toString());
        }

    }

    public ArrayList<Sair> getDisplayedSairList(Boolean isDisplayed) {

        int value = 0;
        if (isDisplayed) {
            value = 1;
        }

        ArrayList<Sair> sairArrayList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        cursor =
                db.rawQuery(
                        "SELECT Sairler.id,Sairler.ad,Sairler.isDisplayed,Sairler.isPopular FROM Sairler " +
                                "WHERE Sairler.isDisplayed = " + value + " ORDER BY RANDOM()",
                        null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Sair sair = setSairColumns(cursor);
            sairArrayList.add(sair);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return sairArrayList;
    }

    public ArrayList<Sair> getPopularSairList() {

        int value = 1;
        ArrayList<Sair> sairArrayList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        cursor =
                db.rawQuery(
                        "SELECT Sairler.id,Sairler.ad,Sairler.isDisplayed,Sairler.isPopular FROM Sairler " +
                                "WHERE Sairler.isPopular = " + value + " ORDER BY RANDOM()",
                        null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Sair sair = setSairColumns(cursor);
            sairArrayList.add(sair);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return sairArrayList;
    }

    public ArrayList<Sair> getFilteredSairList(String searchText) {

        ArrayList<Sair> sairArrayList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        cursor =
                db.rawQuery(
                        "SELECT Sairler.id,Sairler.ad,Sairler.isDisplayed,Sairler.isPopular " +
                                " FROM Sairler" +
                                " WHERE Sairler.ad LIKE '%" + searchText + "%'",
                        null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Sair sair = setSairColumns(cursor);
            sairArrayList.add(sair);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return sairArrayList;
    }


    /***********************************************************************/
    private Sair setSairColumns(Cursor cursor) {

        Sair sair = new Sair();
        sair.setId(cursor.getInt(cursor.getColumnIndex(SAIR_ID)));
        sair.setAd(cursor.getString(cursor.getColumnIndex(SAIR_AD)));
        //sair.setDogumTarihi(cursor.getString(cursor.getColumnIndex(SAIR_DOGUM_TARIHI)));
        //sair.setOlumTarihi(cursor.getString(cursor.getColumnIndex(SAIR_OLUM_TARIHI)));

        if (cursor.getInt(cursor.getColumnIndex(SAIR_IS_DISPLAYED)) == 1) {
            sair.setDisplayed(true);
        } else {
            sair.setDisplayed(false);
        }

        if (cursor.getInt(cursor.getColumnIndex(SAIR_IS_POPULAR)) == 1) {
            sair.setPopular(true);
        } else {
            sair.setPopular(false);
        }


        //sair.setProfilePicBitmap(idPhotoHashmap.get(sair.getId()));
        sair.setProfilePicIndex(NumericConstants.profileSoruceArray[sair.getId() - 1]);

        return sair;
    }


}
