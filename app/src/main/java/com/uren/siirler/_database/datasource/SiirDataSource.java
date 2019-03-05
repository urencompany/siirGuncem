package com.uren.siirler._database.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.uren.siirler._database.DatabaseHelper;
import com.uren.siirler._model.Siir;

import java.util.ArrayList;

import static com.uren.siirler.Constants.StringConstants.SIIRLER_DATABASE;

/**
 * Created by topaloglun on 23/2/2019.
 */
public class SiirDataSource {

    public static final String SIIR_TABLE_NAME = "Siirler";
    public static final String SIIR_ID = "id";
    public static final String SIIR_SAIR_ID = "sairId";
    public static final String SIIR_AD = "ad";
    public static final String SIIR_METIN = "metin";
    public static final String SIIR_ISFAVORITE = "isFavorite";
    public static final String SIIR_DISPLAY_COUNT = "display_count";
    public static final String SIIR_IS_DAILY_POEM = "isDailyPoem";
    public static final String SIIR_IS_DAILY_CLICKED = "isDailyClicked";

    private static Cursor cursor;
    private DatabaseHelper databaseHelper;

    public SiirDataSource(Context context) {
        databaseHelper = new DatabaseHelper(context, SIIRLER_DATABASE);
    }

    public ArrayList<Siir> getSiirList() {

        ArrayList<Siir> siirArrayList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        cursor =
                db.rawQuery(
                        "SELECT Siirler.id," +
                                " Siirler.sairId," +
                                " Siirler.ad," +
                                " Siirler.metin," +
                                " Siirler.isFavorite," +
                                " Siirler.display_count," +
                                " Siirler.isDailyPoem," +
                                " Siirler.isDailyClicked" +
                                " FROM Siirler",
                        null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Siir siir = setSiirColumns(cursor);
            siirArrayList.add(siir);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return siirArrayList;
    }

    public Siir getSiir(int siirId) {

        Siir siir = new Siir();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        cursor =
                db.rawQuery(
                        "SELECT Siirler.id," +
                                " Siirler.sairId," +
                                " Siirler.ad," +
                                " Siirler.metin," +
                                " Siirler.isFavorite," +
                                " Siirler.display_count," +
                                " Siirler.isDailyPoem," +
                                " Siirler.isDailyClicked" +
                                " FROM Siirler" +
                                " WHERE Siirler.id = " + siirId,
                        null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            siir = setSiirColumns(cursor);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();

        return siir;
    }

    public ArrayList<Siir> getSairSiirList(int sairId) {

        ArrayList<Siir> siirArrayList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        cursor =
                db.rawQuery(
                        "SELECT Siirler.id," +
                                " Siirler.sairId," +
                                " Siirler.ad," +
                                " Siirler.metin," +
                                " Siirler.isFavorite," +
                                " Siirler.display_count," +
                                " Siirler.isDailyPoem," +
                                " Siirler.isDailyClicked" +
                                " FROM Siirler" +
                                " WHERE Siirler.sairId = " + sairId,
                        null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            Siir siir = setSiirColumns(cursor);
            siirArrayList.add(siir);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return siirArrayList;
    }

    public void updateSiirFavorite(int siirId, boolean isFavorite) {

        try {
            int value = 0;
            if (isFavorite) {
                value = 1;
            }

            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(SIIR_ISFAVORITE, value);
            db.update(SIIR_TABLE_NAME, values, SIIR_ID + " = " + siirId, null);
            db.close();
        } catch (Exception e) {
            Log.e("updateError", e.toString());
        }

    }

    public void updateSiirDisplayCount(int siirId) {

        try {
            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            cursor =
                    db.rawQuery(
                            "SELECT Siirler.display_count FROM Siirler " +
                                    "WHERE Siirler.id = " + siirId,
                            null);

            cursor.moveToFirst();
            int display_count = cursor.getInt(cursor.getColumnIndex(SIIR_DISPLAY_COUNT));

            ContentValues values = new ContentValues();
            values.put(SIIR_DISPLAY_COUNT, display_count + 1);
            db.update(SIIR_TABLE_NAME, values, SIIR_ID + " = " + siirId, null);
            db.close();
        } catch (Exception e) {
            Log.e("updateError", e.toString());
        }

    }

    public void updateSiirDailyClicked(int siirId) {

        try {
            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(SIIR_IS_DAILY_CLICKED, 1);
            db.update(SIIR_TABLE_NAME, values, SIIR_ID + " = " + siirId, null);
            db.close();
        } catch (Exception e) {
            Log.e("updateError", e.toString());
        }

    }

    public ArrayList<Siir> getFilteredSiirList(String searchText) {

        ArrayList<Siir> siirArrayList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        cursor =
                db.rawQuery(
                        "SELECT Siirler.id," +
                                " Siirler.sairId," +
                                " Siirler.ad," +
                                " Siirler.metin," +
                                " Siirler.isFavorite," +
                                " Siirler.display_count," +
                                " Siirler.isDailyPoem," +
                                " Siirler.isDailyClicked" +
                                " FROM Siirler" +
                                " WHERE Siirler.ad LIKE '%" + searchText + "%'",
                        null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            Siir siir = setSiirColumns(cursor);
            siirArrayList.add(siir);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return siirArrayList;
    }

    public ArrayList<Siir> getFilteredDetailSiirList(String searchText) {

        ArrayList<Siir> siirArrayList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        cursor =
                db.rawQuery(
                        "SELECT Siirler.id," +
                                " Siirler.sairId," +
                                " Siirler.ad," +
                                " Siirler.metin," +
                                " Siirler.isFavorite," +
                                " Siirler.display_count," +
                                " Siirler.isDailyPoem," +
                                " Siirler.isDailyClicked" +
                                " FROM Siirler" +
                                " WHERE Siirler.metin LIKE '%" + searchText + "%'",
                        null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            Siir siir = setSiirColumns(cursor);
            siirArrayList.add(siir);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return siirArrayList;
    }

    public Siir getRandomSiirFromSair(int sairId) {

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        cursor =
                db.rawQuery(
                        "SELECT Siirler.id," +
                                " Siirler.sairId," +
                                " Siirler.ad," +
                                " Siirler.metin," +
                                " Siirler.isFavorite," +
                                " Siirler.display_count," +
                                " Siirler.isDailyPoem," +
                                " Siirler.isDailyClicked" +
                                " FROM Siirler" +
                                " WHERE Siirler.sairId = " + sairId + " ORDER BY RANDOM()",
                        null);

        cursor.moveToFirst();

        Siir siir = setSiirColumns(cursor);
        cursor.close();
        db.close();
        return siir;
    }

    public ArrayList<Siir> getDailySiirList() {

        ArrayList<Siir> siirArrayList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        cursor =
                db.rawQuery(
                        "SELECT Siirler.id," +
                                " Siirler.sairId," +
                                " Siirler.ad," +
                                " Siirler.metin," +
                                " Siirler.isFavorite," +
                                " Siirler.display_count," +
                                " Siirler.isDailyPoem," +
                                " Siirler.isDailyClicked" +
                                " FROM Siirler" +
                                " WHERE Siirler.isDailyPoem = 1" +
                                " ORDER BY Siirler.isDailyClicked ASC",
                        null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            Siir siir = setSiirColumns(cursor);
            siirArrayList.add(siir);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return siirArrayList;
    }

    public void updateDailySiirList(ArrayList<Siir> siirArrayList) {

        initAllSiir();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(SIIR_IS_DAILY_POEM, 1);

        for (int i = 0; i < siirArrayList.size(); i++) {
            db.update(SIIR_TABLE_NAME, values, SIIR_ID + " = " + siirArrayList.get(i).getId(), null);
        }

        db.close();

    }

    private void initAllSiir() {
        try {
            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(SIIR_IS_DAILY_POEM, 0);
            values.put(SIIR_IS_DAILY_CLICKED, 0);
            db.update(SIIR_TABLE_NAME, values, "1=1", null);
            db.close();
        } catch (Exception e) {
            Log.e("updateError", e.toString());
        }
    }

    public ArrayList<Siir> getFavoriteSiirList() {

        ArrayList<Siir> siirArrayList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        cursor =
                db.rawQuery(
                        "SELECT Siirler.id," +
                                " Siirler.sairId," +
                                " Siirler.ad," +
                                " Siirler.metin," +
                                " Siirler.isFavorite," +
                                " Siirler.display_count," +
                                " Siirler.isDailyPoem," +
                                " Siirler.isDailyClicked" +
                                " FROM Siirler " +
                                " WHERE Siirler.isFavorite = 1",
                        null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Siir siir = setSiirColumns(cursor);
            siirArrayList.add(siir);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return siirArrayList;
    }

    /***********************************************************************/
    private Siir setSiirColumns(Cursor cursor) {

        Siir siir = new Siir();
        siir.setId(SiirDataSource.cursor.getInt(SiirDataSource.cursor.getColumnIndex(SIIR_ID)));
        siir.setSairId(SiirDataSource.cursor.getInt(SiirDataSource.cursor.getColumnIndex(SIIR_SAIR_ID)));
        siir.setAd(SiirDataSource.cursor.getString(SiirDataSource.cursor.getColumnIndex(SIIR_AD)));
        siir.setMetin(SiirDataSource.cursor.getBlob(SiirDataSource.cursor.getColumnIndex(SIIR_METIN)));
        siir.setDisplayCount(SiirDataSource.cursor.getInt(SiirDataSource.cursor.getColumnIndex(SIIR_DISPLAY_COUNT)));

        //isFavorite
        if (SiirDataSource.cursor.getInt(SiirDataSource.cursor.getColumnIndex(SIIR_ISFAVORITE)) == 1) {
            siir.setFavorite(true);
        } else {
            siir.setFavorite(false);
        }

        //isDisplayPoem
        if (SiirDataSource.cursor.getInt(SiirDataSource.cursor.getColumnIndex(SIIR_IS_DAILY_POEM)) == 1) {
            siir.setIsDailyPoem(true);
        } else {
            siir.setIsDailyPoem(false);
        }

        //isDailyClicked
        if (SiirDataSource.cursor.getInt(SiirDataSource.cursor.getColumnIndex(SIIR_IS_DAILY_CLICKED)) == 1) {
            siir.setIsDailyClicked(true);
        } else {
            siir.setIsDailyClicked(false);
        }

        return siir;
    }


}
