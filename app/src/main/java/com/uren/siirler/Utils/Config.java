package com.uren.siirler.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class Config {

    public static final int FONT_QALAM_MAJEED = 0;
    public static final int FONT_HAFS = 1;
    public static final int FONT_NOOREHUDA = 2;
    public static final int FONT_ME_QURAN = 3;
    public static final int FONT_MAX = 3;

    //shared preferences KEYS
    public static final String LANG = "lang";
    public static final String TRANSLITERATION_LANG = "transliterationLang";
    public static final String SHOW_TRANSLATION = "showTranslation";
    public static final String SHOW_TRANSLITERATION = "showTransliteration";
    public static final String FONT_ARABIC = "fontArabic";
    public static final String FONT_SIZE_ARABIC = "fontSizeArabic";
    public static final String FONT_SIZE_TRANSLITERATION = "fontSizeTransliteration";
    public static final String FONT_SIZE_TRANSLATION = "fontSizeTranslation";
    public static final String COUNTRY = "country";
    public static final String CITY = "city";
    public static final String COUNTY = "county";
    public static final String COUNTY_CODE = "countyCode";
    public static final String TARGET_PRAYER_TIME = "targetPrayerTime";

    public static final String NOTIF_ENABLED = "notifEnabled";
    public static final String NOTIF_BEFORE_IMSAK = "notifBeforeImsak";
    public static final String NOTIF_EXACT_IMSAK = "notifExactImsak";
    public static final String NOTIF_BEFORE_GUNES = "notifBeforeGunes";
    public static final String NOTIF_EXACT_GUNES_ = "notifExactGunes";
    public static final String NOTIF_BEFORE_OGLE = "notifBeforeOgle";
    public static final String NOTIF_EXACT_OGLE = "notifExactOgle";
    public static final String NOTIF_BEFORE_IKINDI = "notifBeforeIkindi";
    public static final String NOTIF_EXACT_IKINDI = "notifExactIkindi";
    public static final String NOTIF_BEFORE_AKSAM = "notifBeforeAksam";
    public static final String NOTIF_EXACT_AKSAM = "notifExactAksam";
    public static final String NOTIF_BEFORE_YATSI = "notifBeforeYatsi";
    public static final String NOTIF_EXACT_YATSI = "notifExactYatsi";
    public static final String TIME_NOTIF_BEFORE_IMSAK = "timeNotifBeforeImsak";
    public static final String TIME_NOTIF_BEFORE_GUNES = "timeNotifBeforeGunes";
    public static final String TIME_NOTIF_BEFORE_OGLE = "timeNotifBeforeOgle";
    public static final String TIME_NOTIF_BEFORE_IKINDI = "timeNotifBeforeIkindi";
    public static final String TIME_NOTIF_BEFORE_AKSAM = "timeNotifBeforeAksam";
    public static final String TIME_NOTIF_BEFORE_YATSI = "timeNotifBeforeYatsi";
    public static final String MELODY_BEFORE_IMSAK = "melodyBeforeImsak";
    public static final String MELODY_IMSAK = "melodyImsak";
    public static final String MELODY_BEFORE_GUNES = "melodyBeforeGunes";
    public static final String MELODY_GUNES = "melodyGunes";
    public static final String MELODY_BEFORE_OGLE = "melodyBeforeOgle";
    public static final String MELODY_OGLE = "melodyOgle";
    public static final String MELODY_BEFORE_IKINDI = "melodyBeforeIkindi";
    public static final String MELODY_IKINDI = "melodyIkindi";
    public static final String MELODY_BEFORE_AKSAM = "melodyBeforeAksam";
    public static final String MELODY_AKSAM = "melodyAksam";
    public static final String MELODY_BEFORE_YATSI = "melodyBeforeYatsi";
    public static final String MELODY_YATSI = "melodyYatsi";


    //default values
    public static final String defaultLang = "tr.diyanet";
    public static final String defaultTransliterationLang = "tr";
    public static final boolean defaultShowTransliteration = true;
    public static final boolean defaultShowTranslation = true;
    public static final String defaultFontArabic = "PDMS_IslamicFont.ttf";
    public static final int defaultFontSizeArabic = 28;
    public static final int defaultFontSizeTransliteration = 12;
    public static final int defaultFontSizeTranslation = 15;
    public static final String defaultCountry = "";
    public static final String defaultCity = "";
    public static final String defaultCounty = "";
    public static final String defaultCountyCode = "";
    public static final int defaultTargetPrayerTime = 0;

    public static final boolean defaultNotifEnabled = false;
    public static final boolean defaultNotifBefore = false;
    public static final boolean defaultNotifExact = false;
    public static final int defaultTimeBefore = 5;
    public static final int defaultMelodyIndex = 0;

    // current variables-bunlar uzerınden ilerlenmeli
    public static String lang; //translation lang
    public static String transliterationlang;
    public static boolean showTransliteration;
    public static boolean showTranslation;
    public static String fontArabic;
    public static int fontSizeArabic;
    public static int fontSizeTransliteration;
    public static int fontSizeTranslation;
    public static String country;
    public static String city;
    public static String county;
    public static String countyCode;
    public static int targetPrayerTime;

    public static boolean notifEnabled;
    public static boolean notifBeforeImsak;
    public static boolean notifExactImsak;
    public static boolean notifBeforeGunes;
    public static boolean notifExactGunes;
    public static boolean notifBeforeOgle;
    public static boolean notifExactOgle;
    public static boolean notifBeforeIkindi;
    public static boolean notifExactIkindi;
    public static boolean notifBeforeAksam;
    public static boolean notifExactAksam;
    public static boolean notifBeforeYatsi;
    public static boolean notifExactYatsi;
    public static int timeBeforeImsak;
    public static int timeBeforeGunes;
    public static int timeBeforeOgle;
    public static int timeBeforeIkindi;
    public static int timeBeforeAksam;
    public static int timeBeforeYatsi;
    public static int melodyBeforeImsak;
    public static int melodyImsak;
    public static int melodyBeforeGunes;
    public static int melodyGunes;
    public static int melodyBeforeOgle;
    public static int melodyOgle;
    public static int melodyBeforeIkindi;
    public static int melodyIkindi;
    public static int melodyBeforeAksam;
    public static int melodyAksam;
    public static int melodyBeforeYatsi;
    public static int melodyYatsi;


    public static Context context;

    public void load(Context context) {
        this.context = context;
        Log.d("Config", "Load");
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            loadDefault();
            lang = sp.getString(Config.LANG, Config.defaultLang);
            transliterationlang = sp.getString(Config.TRANSLITERATION_LANG, Config.defaultTransliterationLang);
            showTransliteration = sp.getBoolean(Config.SHOW_TRANSLITERATION, Config.defaultShowTransliteration);
            showTranslation = sp.getBoolean(Config.SHOW_TRANSLATION, Config.defaultShowTranslation);
            fontArabic = sp.getString(Config.FONT_ARABIC, Config.defaultFontArabic);
            fontSizeArabic = sp.getInt(Config.FONT_SIZE_ARABIC, Config.defaultFontSizeArabic);
            fontSizeTransliteration = sp.getInt(Config.FONT_SIZE_TRANSLITERATION, Config.defaultFontSizeTransliteration);
            fontSizeTranslation = sp.getInt(Config.FONT_SIZE_TRANSLATION, Config.defaultFontSizeTranslation);
            country = sp.getString(Config.COUNTRY, Config.defaultCountry);
            city = sp.getString(Config.CITY, Config.defaultCity);
            county = sp.getString(Config.COUNTY, Config.defaultCounty);
            countyCode = sp.getString(Config.COUNTY_CODE, Config.defaultCountyCode);
            targetPrayerTime = sp.getInt(Config.TARGET_PRAYER_TIME, Config.defaultTargetPrayerTime);

            //Notif items
            notifEnabled = sp.getBoolean(Config.NOTIF_ENABLED, Config.defaultNotifEnabled);

            notifBeforeImsak = sp.getBoolean(Config.NOTIF_BEFORE_IMSAK, Config.defaultNotifBefore);
            notifBeforeGunes = sp.getBoolean(Config.NOTIF_BEFORE_GUNES, Config.defaultNotifBefore);
            notifBeforeOgle = sp.getBoolean(Config.NOTIF_BEFORE_OGLE, Config.defaultNotifBefore);
            notifBeforeIkindi = sp.getBoolean(Config.NOTIF_BEFORE_IKINDI, Config.defaultNotifBefore);
            notifBeforeAksam = sp.getBoolean(Config.NOTIF_BEFORE_AKSAM, Config.defaultNotifBefore);
            notifBeforeYatsi = sp.getBoolean(Config.NOTIF_BEFORE_YATSI, Config.defaultNotifBefore);
            notifExactImsak = sp.getBoolean(Config.NOTIF_EXACT_IMSAK, Config.defaultNotifExact);
            notifExactGunes = sp.getBoolean(Config.NOTIF_EXACT_GUNES_, Config.defaultNotifExact);
            notifExactOgle = sp.getBoolean(Config.NOTIF_EXACT_OGLE, Config.defaultNotifExact);
            notifExactIkindi = sp.getBoolean(Config.NOTIF_EXACT_IKINDI, Config.defaultNotifExact);
            notifExactAksam = sp.getBoolean(Config.NOTIF_EXACT_AKSAM, Config.defaultNotifExact);
            notifExactYatsi = sp.getBoolean(Config.NOTIF_EXACT_YATSI, Config.defaultNotifExact);

            timeBeforeImsak = sp.getInt(Config.TIME_NOTIF_BEFORE_IMSAK, Config.defaultTimeBefore);
            timeBeforeGunes = sp.getInt(Config.TIME_NOTIF_BEFORE_GUNES, Config.defaultTimeBefore);
            timeBeforeOgle = sp.getInt(Config.TIME_NOTIF_BEFORE_OGLE, Config.defaultTimeBefore);
            timeBeforeIkindi = sp.getInt(Config.TIME_NOTIF_BEFORE_IKINDI, Config.defaultTimeBefore);
            timeBeforeAksam = sp.getInt(Config.TIME_NOTIF_BEFORE_AKSAM, Config.defaultTimeBefore);
            timeBeforeYatsi = sp.getInt(Config.TIME_NOTIF_BEFORE_YATSI, Config.defaultTimeBefore);

            melodyBeforeImsak = sp.getInt(Config.MELODY_BEFORE_IMSAK, Config.defaultMelodyIndex);
            melodyImsak = sp.getInt(Config.MELODY_IMSAK, Config.defaultMelodyIndex);
            melodyBeforeGunes = sp.getInt(Config.MELODY_BEFORE_GUNES, Config.defaultMelodyIndex);
            melodyGunes = sp.getInt(Config.MELODY_GUNES, Config.defaultMelodyIndex);
            melodyBeforeOgle = sp.getInt(Config.MELODY_BEFORE_OGLE, Config.defaultMelodyIndex);
            melodyOgle = sp.getInt(Config.MELODY_OGLE, Config.defaultMelodyIndex);
            melodyBeforeIkindi = sp.getInt(Config.MELODY_BEFORE_IKINDI, Config.defaultMelodyIndex);
            melodyIkindi = sp.getInt(Config.MELODY_IKINDI, Config.defaultMelodyIndex);
            melodyBeforeAksam = sp.getInt(Config.MELODY_BEFORE_AKSAM, Config.defaultMelodyIndex);
            melodyAksam = sp.getInt(Config.MELODY_AKSAM, Config.defaultMelodyIndex);
            melodyBeforeYatsi = sp.getInt(Config.MELODY_BEFORE_YATSI, Config.defaultMelodyIndex);
            melodyYatsi = sp.getInt(Config.MELODY_YATSI, Config.defaultMelodyIndex);

            Log.d("Config", "Loading Custom");
            //loadDefault();
        } catch (Exception e) {
            loadDefault();
            Log.d("Config", "Exception Loading Defaults");
        }
    }

    public void loadDefault() {
        lang = defaultLang;
        transliterationlang = defaultTransliterationLang;
        showTransliteration = defaultShowTransliteration;
        showTranslation = defaultShowTranslation;
        fontArabic = defaultFontArabic;
        fontSizeArabic = defaultFontSizeArabic;
        fontSizeTransliteration = defaultFontSizeTransliteration;
        fontSizeTranslation = defaultFontSizeTranslation;
        country = defaultCountry;
        city = defaultCity;
        county = defaultCounty;
        countyCode = defaultCountyCode;
        targetPrayerTime = defaultTargetPrayerTime;
    }

    public static void update(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Config.context);
        SharedPreferences.Editor ed = sp.edit();
        ed.clear();
        ed.putString(LANG, lang);
        ed.putString(TRANSLITERATION_LANG, transliterationlang);
        ed.putBoolean(SHOW_TRANSLITERATION, showTransliteration);
        ed.putBoolean(SHOW_TRANSLATION, showTranslation);
        ed.putString(FONT_ARABIC, "" + fontArabic);
        ed.putInt(FONT_SIZE_ARABIC, fontSizeArabic);
        ed.putInt(FONT_SIZE_TRANSLITERATION, fontSizeTransliteration);
        ed.putInt(FONT_SIZE_TRANSLATION, fontSizeTranslation);
        ed.putString(COUNTRY, country);
        ed.putString(CITY, city);
        ed.putString(COUNTY, county);
        ed.putString(COUNTY_CODE, countyCode);
        ed.commit();
    }

    public static void updateNotif(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Config.context);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt(TARGET_PRAYER_TIME, targetPrayerTime);

        ed.putBoolean(NOTIF_ENABLED, notifEnabled);
        ed.putBoolean(NOTIF_BEFORE_IMSAK, notifBeforeImsak);
        ed.putBoolean(NOTIF_BEFORE_GUNES, notifBeforeGunes);
        ed.putBoolean(NOTIF_BEFORE_OGLE, notifBeforeOgle);
        ed.putBoolean(NOTIF_BEFORE_IKINDI, notifBeforeIkindi);
        ed.putBoolean(NOTIF_BEFORE_AKSAM, notifBeforeAksam);
        ed.putBoolean(NOTIF_BEFORE_YATSI, notifBeforeYatsi);

        ed.putBoolean(NOTIF_EXACT_IMSAK, notifExactImsak);
        ed.putBoolean(NOTIF_EXACT_GUNES_, notifExactGunes);
        ed.putBoolean(NOTIF_EXACT_OGLE, notifExactOgle);
        ed.putBoolean(NOTIF_EXACT_IKINDI, notifExactIkindi);
        ed.putBoolean(NOTIF_EXACT_AKSAM, notifExactAksam);
        ed.putBoolean(NOTIF_EXACT_YATSI, notifExactYatsi);

        ed.putInt(TIME_NOTIF_BEFORE_IMSAK, timeBeforeImsak);
        ed.putInt(TIME_NOTIF_BEFORE_GUNES, timeBeforeGunes);
        ed.putInt(TIME_NOTIF_BEFORE_OGLE, timeBeforeOgle);
        ed.putInt(TIME_NOTIF_BEFORE_IKINDI, timeBeforeIkindi);
        ed.putInt(TIME_NOTIF_BEFORE_AKSAM, timeBeforeAksam);
        ed.putInt(TIME_NOTIF_BEFORE_YATSI, timeBeforeYatsi);

        ed.putInt(MELODY_BEFORE_IMSAK, melodyBeforeImsak);
        ed.putInt(MELODY_IMSAK, melodyImsak);
        ed.putInt(MELODY_BEFORE_GUNES, melodyBeforeGunes);
        ed.putInt(MELODY_GUNES, melodyGunes);
        ed.putInt(MELODY_BEFORE_OGLE, melodyBeforeOgle);
        ed.putInt(MELODY_OGLE, melodyOgle);
        ed.putInt(MELODY_BEFORE_IKINDI, melodyBeforeIkindi);
        ed.putInt(MELODY_IKINDI, melodyIkindi);
        ed.putInt(MELODY_BEFORE_AKSAM, melodyBeforeAksam);
        ed.putInt(MELODY_AKSAM, melodyAksam);
        ed.putInt(MELODY_BEFORE_YATSI, melodyBeforeYatsi);
        ed.putInt(MELODY_YATSI, melodyYatsi);

        ed.commit();
    }

    /*public void save(Context context) {
        Log.d("Config","Save");
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor ed = sp.edit();
        ed.clear();
        ed.putString(LANG, lang);
        ed.putBoolean(SHOW_TRANSLATION, showTranslation);
        ed.putBoolean(WORD_BY_WORD, wordByWord);
        ed.putBoolean(KEEP_SCREEN_ON, keepScreenOn);
        ed.putString(FONT_SIZE_ARABIC, "" + fontSizeArabic);
        ed.putString(FONT_SIZE_TRANSLATION, "" + fontSizeTranslation);
        ed.commit();
    }*/
    private int getStringInt(SharedPreferences sp, String key, int defValue) {
        return Integer.parseInt(sp.getString(key, Integer.toString(defValue)));
    }

  /*  public boolean loadFont() {
      if (loadedFont != Config.fontArabic) {
          String name;
          switch (config.fontArabic) {
              case Config.FONT_NASKH:
                  name = "naskh.otf";
                  break;
              case Config.FONT_NOOREHUDA:
                  name = "noorehuda.ttf";
                  break;
              case Config.FONT_ME_QURAN:
                  name = "me_quran.ttf";
                  break;
              default:
                  name = "qalam.ttf";
          }
          try {
              NativeRenderer.loadFont(getAssets().open(name));
              loadedFont = config.fontArabic;
          } catch (IOException e) {
              e.printStackTrace();
              loadedFont = -1;
              return false;
          }
      }
      return true;
  }*/

}
