package com.uren.siirler.Interfaces;

public interface OptionsCallback {

    public void onLanguageChanged(String language);

    public void onTransliterationLanguageChanged(String language);

    public void onShowTransliterationChanged(boolean isShow);

    public void onShowTranslationChanged(boolean isShow);

    public void onFontArabicChanged(String fontType);

    public void onFontSizeArabicChanged(int fontSize);

    public void onFontSizeTransliterationChanged(int fontSize);

    public void onFontSizeTranslationChanged(int fontSize);

}