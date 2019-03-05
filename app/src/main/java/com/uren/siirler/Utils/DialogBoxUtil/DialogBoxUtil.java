package com.uren.siirler.Utils.DialogBoxUtil;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.uren.siirler.MainFragments.RecordManagement.Adapters.CustomListAdapter;
import com.uren.siirler.MainFragments.RecordManagement.Interfaces.RecordOptionsCallback;
import com.uren.siirler.MainFragments.RecordManagement.Interfaces.RecordSavedCallback;
import com.uren.siirler.R;
import com.uren.siirler.Utils.CommonUtils;
import com.uren.siirler.Utils.DialogBoxUtil.Interfaces.YesNoDialogBoxCallback;

import java.io.File;
import java.util.ArrayList;

import static com.uren.siirler.Constants.StringConstants.APP_NAME;

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

    public static void recordOptionsDialogBox(final Activity activity, final Context context, final RecordOptionsCallback recordOptionsCallback) {

        try {
            CommonUtils.hideKeyBoard(context);
            final ArrayList<String> myList = new ArrayList<>();
            myList.add(context.getString(R.string.delete));
            myList.add(context.getString(R.string.share2));

            LayoutInflater inflater = activity.getLayoutInflater();
            View convertView = (View) inflater.inflate(R.layout.record_options_list_item, null);

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setView(convertView);

            ListView listView = (ListView) convertView.findViewById(R.id.mylistview);

            final AlertDialog alert = alertDialog.create();
            final CustomListAdapter myadapter = new CustomListAdapter(context, R.layout.list_view_item, myList);

            listView.setAdapter(myadapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                    Resources resources = context.getResources();
                    String selectedItem = myList.get(position);

                    if (selectedItem.equals(resources.getString(R.string.share2))) {
                        recordOptionsCallback.onShareRecord();
                    } else if (selectedItem.equals(resources.getString(R.string.delete))) {
                        String message = context.getResources().getString(R.string.deleteSure);
                        DialogBoxUtil.showYesNoDialog(context, null, message, new YesNoDialogBoxCallback() {
                            @Override
                            public void yesClick() {
                                recordOptionsCallback.onDeleteRecord();
                            }

                            @Override
                            public void noClick() {
                            }
                        });

                    }

                    alert.cancel();
                }
            });

            // show dialog
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void renameFile(final Activity activity, final Context context, final File file, final View llAll, final RecordSavedCallback recordSavedCallback) {

        try {
            CommonUtils.hideKeyBoard(context);

            LayoutInflater inflater = activity.getLayoutInflater();
            View convertView = (View) inflater.inflate(R.layout.rename_record, null);
            final EditText edtRename = (EditText) convertView.findViewById(R.id.edtRename);
            Button btnApply = (Button) convertView.findViewById(R.id.btnApply);
            Button btnCancel = (Button) convertView.findViewById(R.id.btnCancel);

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setView(convertView);

            final String defaultFileName = "AUD";
            edtRename.setText(defaultFileName);

            final AlertDialog alert = alertDialog.create();
            alert.show();
            alert.setCanceledOnTouchOutside(false);

            btnApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(APP_NAME), Environment.DIRECTORY_DOCUMENTS);
                    if (mediaStorageDir.exists()) {
                        File from = new File(mediaStorageDir + "/" + file.getName());
                        File to = new File(mediaStorageDir + "/" + file.getName().replace("AUD", edtRename.getText()));
                        if (from.exists())
                            from.renameTo(to);
                        recordSavedSnackBar(llAll, context);
                        if (recordSavedCallback != null) {
                            recordSavedCallback.recordSaved();
                        }
                    }

                    alert.dismiss();

                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alert.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void recordSavedSnackBar(View view, Context context) {
        try {
            Snackbar snackbar = Snackbar.make(view,
                    context.getResources().getString(R.string.recordSuccessful),
                    Snackbar.LENGTH_SHORT);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(context.getResources().getColor(R.color.style_color_accent));
            TextView tv = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(context.getResources().getColor(R.color.White));
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
