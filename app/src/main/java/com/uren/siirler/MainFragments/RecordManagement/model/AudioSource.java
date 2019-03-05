package com.uren.siirler.MainFragments.RecordManagement.model;

import android.media.MediaRecorder;

public enum AudioSource {
    MIC,
    CAMCORDER,
    VOICE_RECOGNITION;

    public int getSource() {
        switch (this) {
            case CAMCORDER:
                return MediaRecorder.AudioSource.CAMCORDER;
            case VOICE_RECOGNITION:
                return MediaRecorder.AudioSource.VOICE_RECOGNITION;
            default:
                return MediaRecorder.AudioSource.MIC;
        }
    }
}