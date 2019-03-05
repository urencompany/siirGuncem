package com.uren.siirler.MainFragments.RecordManagement.JavaClasses;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
import static com.uren.siirler.Constants.StringConstants.APP_NAME;

public class FileAdapter {

    public static File getOutputMediaFile(int type) {

        String directoryChild = null;

        try {
            switch (type) {
                case MEDIA_TYPE_IMAGE:
                    directoryChild = Environment.DIRECTORY_PICTURES;
                    break;

                case MEDIA_TYPE_VIDEO:
                    directoryChild = Environment.DIRECTORY_MOVIES;
                    break;
                case MEDIA_TYPE_AUDIO:
                    directoryChild = Environment.DIRECTORY_DOCUMENTS;
                    break;
                default:
                    break;
            }

            if (directoryChild != null && !directoryChild.isEmpty()) {

                File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(APP_NAME), directoryChild);

                if (!mediaStorageDir.exists()) {

                    try {
                        boolean mkdirs = mediaStorageDir.mkdirs();
                        if (!mkdirs) {
                            Log.d(APP_NAME, "failed to create directory");
                            return null;
                        }
                    } catch (Exception e) {
                        Log.e("mkdir", "directory create Error");
                    }


                }

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                File mediaFile;
                if (type == MEDIA_TYPE_IMAGE) {
                    mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                            "IMG_" + timeStamp + ".jpg");
                    return mediaFile;
                } else if (type == MEDIA_TYPE_VIDEO) {
                    mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                            "VID_" + timeStamp + ".mp4");
                    return mediaFile;
                } else if (type == MEDIA_TYPE_AUDIO) {
                    mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                            "AUD_" + timeStamp + ".mp3");
                    return mediaFile;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static File getCropMediaFile() {
        File mediaFile = null;
        try {
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(APP_NAME), Environment.DIRECTORY_PICTURES);

            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d(APP_NAME, "failed to create directory");
                    return null;
                }
            }
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_CROP.jpg");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mediaFile;
    }
}