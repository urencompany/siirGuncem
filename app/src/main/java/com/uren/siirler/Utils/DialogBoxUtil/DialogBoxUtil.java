package com.uren.siirler.Utils.DialogBoxUtil;

import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;

import com.uren.siirler.R;
import com.uren.siirler.Utils.CommonUtils;
import com.uren.siirler.Utils.DialogBoxUtil.Interfaces.YesNoDialogBoxCallback;

public class DialogBoxUtil {

    public static void showYesNoDialog(Context context, String title, String message, final YesNoDialogBoxCallback yesNoDialogBoxCallback) {
        CommonUtils.hideKeyBoard(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setCancelable(false);

        if (title != null && !title.isEmpty())
            builder.setTitle(title);

        builder.setPositiveButton(context.getResources().getString(R.string.UPPERYES), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                yesNoDialogBoxCallback.yesClick();
            }
        });

        builder.setNegativeButton(context.getResources().getString(R.string.UPPERNO), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                yesNoDialogBoxCallback.noClick();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }


}
