package com.uren.siirler._database;

import android.content.Context;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;


/** Created by topaloglun on 23/2/2019. */
public class DatabaseHelper extends SQLiteAssetHelper {

  public static final int DATABASE_VERSION = 8;

  public DatabaseHelper(Context context, String DATABASE_NAME) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    this.setForcedUpgrade(); // just because it is read only database so ForceUpgrade change or
    // remove it is not read only
    Log.d("DatabaseHelper  ", "constructor");
  }
}
