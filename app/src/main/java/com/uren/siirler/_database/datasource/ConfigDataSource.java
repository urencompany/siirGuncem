package com.uren.siirler._database.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.uren.siirler._database.DatabaseHelper;
import com.uren.siirler._model.Config;
import com.uren.siirler._model.Sair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.uren.siirler.Constants.StringConstants.CONFIG_DATABASE;
import static com.uren.siirler.Constants.StringConstants.CONFIG_KEY_DATE;
import static com.uren.siirler.Constants.StringConstants.SAIRLER_DATABASE;

/**
 * Created by topaloglun on 23/2/2019.
 */
public class ConfigDataSource {

    public static final String CONFIG_TABLE_NAME = "Config";
    public static final String CONFIG_KEY = "configKey";
    public static final String CONFIG_VALUE = "configValue";

    private static Cursor cursor;
    private DatabaseHelper databaseHelper;

    public ConfigDataSource(Context context) {
        databaseHelper = new DatabaseHelper(context, CONFIG_DATABASE);
    }

    public ArrayList<Config> getConfigList() {

        ArrayList<Config> configArrayList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        cursor =
                db.rawQuery(
                        "SELECT Config.configKey, Config.configValue FROM Config",
                        null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            Config config = new Config();
            config.setKey(cursor.getString(cursor.getColumnIndex(CONFIG_KEY)));
            config.setValue(cursor.getString(cursor.getColumnIndex(CONFIG_VALUE)));

            configArrayList.add(config);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return configArrayList;
    }

    public void updateConfigDate() {

        try {

            Date current = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            String formattedDate = df.format(current);

            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            String key = "'" + CONFIG_KEY_DATE + "'";
            values.put(CONFIG_VALUE, formattedDate);
            db.update(CONFIG_TABLE_NAME, values, CONFIG_KEY + " = " + key, null);
            db.close();
        } catch (Exception e) {
            Log.e("updateError", e.toString());
        }

    }

}
