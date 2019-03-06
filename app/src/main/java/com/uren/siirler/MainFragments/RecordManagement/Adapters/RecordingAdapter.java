package com.uren.siirler.MainFragments.RecordManagement.Adapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uren.siirler.MainFragments.BaseFragment;
import com.uren.siirler.MainFragments.RecordManagement.Interfaces.PlayRecordCallback;
import com.uren.siirler.MainFragments.RecordManagement.Interfaces.RecordOptionsCallback;
import com.uren.siirler.R;
import com.uren.siirler.Utils.DialogBoxUtil.DialogBoxUtil;
import com.uren.siirler._model.Sair;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class RecordingAdapter extends RecyclerView.Adapter {

    private Activity mActivity;
    private Context mContext;
    private BaseFragment.FragmentNavigation fragmentNavigation;
    private ArrayList<HashMap<String, String>> recordList;
    private PlayRecordCallback playRecordCallback;

    public RecordingAdapter(Activity activity, Context context,
                            BaseFragment.FragmentNavigation fragmentNavigation) {
        this.mActivity = activity;
        this.mContext = context;
        this.fragmentNavigation = fragmentNavigation;
        this.recordList = new ArrayList<HashMap<String, String>>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_recording, parent, false);

        RecyclerView.ViewHolder viewHolder = new RecordingAdapter.MyViewHolder(itemView);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List payloads) {
        super.onBindViewHolder(holder, position, payloads);

        if (payloads.isEmpty()) {
            // Perform a full update
            onBindViewHolder(holder, position);
        } else {
            // Perform a partial update
            for (Object payload : payloads) {
            }
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        HashMap<String, String> record = (HashMap<String, String>) recordList.get(position);
        ((MyViewHolder) holder).setData(record, position);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        View mView;
        TextView txtRecordName;
        TextView txtLength;
        TextView txtDate;

        LinearLayout llRecording;
        private HashMap<String, String> record;
        int position;
        String fileName, filePath;


        public MyViewHolder(View view) {
            super(view);

            try {
                mView = view;
                txtRecordName = (TextView) view.findViewById(R.id.txtRecordName);
                txtLength = (TextView) view.findViewById(R.id.txtLength);
                txtDate = (TextView) view.findViewById(R.id.txtDate);
                llRecording = (LinearLayout) view.findViewById(R.id.llRecording);

                setListeners();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void setListeners() {

            llRecording.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (playRecordCallback != null) {
                        playRecordCallback.onPlayRecord(filePath);
                    }
                }
            });

            llRecording.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    openOptionsDialogBox();
                    return false;
                }
            });

        }

        private void openOptionsDialogBox() {

            DialogBoxUtil.recordOptionsDialogBox(mActivity, mContext, new RecordOptionsCallback() {
                @Override
                public void onDeleteRecord() {
                    deleteClicked();
                }

                @Override
                public void onShareRecord() {
                    shareClicked();
                }
            });
        }

        private void deleteClicked() {
            File file = new File(filePath);
            file.delete();
            recordList.remove(position);
            notifyItemRemoved(position);
            notifyDataSetChanged();
        }

        private void shareClicked() {

            try {
                String sharePath = filePath;
                Uri uri = Uri.parse(sharePath);
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("audio/*");
                share.setPackage("com.whatsapp");
                share.putExtra(Intent.EXTRA_STREAM, uri);
                mActivity.startActivity(Intent.createChooser(share, "Share with"));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(mContext, "Whatsapp not found", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(mContext, "Teknik Hata", Toast.LENGTH_SHORT).show();
            }

        }


        public void setData(HashMap<String, String> record, int position) {

            this.position = position;
            this.record = record;
            this.fileName = record.get("file_name");
            this.filePath = record.get("file_path");


            //profile pic
            if (record != null) {
                txtRecordName.setText(fileName);
                setMetaData(filePath);
            }

        }


        private void setMetaData(String filePath) {

            File file = new File(filePath);

            if (file.exists()) {

                MediaPlayer mediaPlayer = null;
                try {
                    mediaPlayer = MediaPlayer.create(mContext, Uri.parse(file.getPath()));
                    if(mediaPlayer != null){
                        txtLength.setText(formatTime((int) mediaPlayer.getDuration()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Date lastModDate = new Date(file.lastModified());
                txtDate.setText(formatDate(lastModDate));

                // close object
                if(mediaPlayer != null){
                    mediaPlayer.release();
                }

            }

        }

        private String formatTime(int mTimeLeftInMillis) {
            int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
            int minutes = (int) ((mTimeLeftInMillis / (1000 * 60)) % 60);
            int hours = (int) ((mTimeLeftInMillis / (1000 * 60 * 60)) % 24);

            String length = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
            return length;
        }

        private String formatDate(Date date) {
            SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            String formattedDate = df.format(date);
            return formattedDate;
        }


    }

    /**
     *
     ******************************************************************************/

    public void addAll(ArrayList<HashMap<String, String>> recordList) {
        try {
            if (recordList != null) {
                this.recordList = recordList;
                notifyItemRangeInserted(0, recordList.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /******************************************************************************/
    public void updateItems() {
        /**/
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (recordList != null ? recordList.size() : 0);
    }


    public void setPlayRecordCallback(PlayRecordCallback playRecordCallback) {
        this.playRecordCallback = playRecordCallback;
    }

}