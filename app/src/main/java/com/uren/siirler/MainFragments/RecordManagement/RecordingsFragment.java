package com.uren.siirler.MainFragments.RecordManagement;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.uren.siirler.MainActivity;
import com.uren.siirler.MainFragments.BaseFragment;
import com.uren.siirler.MainFragments.RecordManagement.Adapters.RecordingAdapter;
import com.uren.siirler.MainFragments.RecordManagement.Interfaces.PlayRecordCallback;
import com.uren.siirler.MainFragments.RecordManagement.JavaClasses.FileDialog.*;
import com.uren.siirler.R;
import com.uren.siirler.Utils.AdMobUtil.AdMobUtils;
import com.uren.siirler.Utils.ClickableImage.ClickableImageView;
import com.uren.siirler.Utils.ItemAnimator;
import com.uren.siirler.Utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.uren.siirler.Constants.StringConstants.ANIMATE_LEFT_TO_RIGHT;
import static com.uren.siirler.Constants.StringConstants.APP_NAME;
import static com.uren.siirler.Constants.StringConstants.TOOLBAR_FONT_TYPE;


public class RecordingsFragment extends BaseFragment
        implements View.OnClickListener, PlayRecordCallback {

    View mView;
    private LinearLayoutManager mLayoutManager;
    private RecordingAdapter recordingAdapter;

    @BindView(R.id.toolbarLayout)
    Toolbar toolbarLayout;
    @BindView(R.id.txtToolbarTitle)
    TextView txtToolbarTitle;
    @BindView(R.id.imgBack)
    ClickableImageView imgBack;
    @BindView(R.id.imgRight)
    ImageView imgOptions;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.adView)
    AdView adView;

    Dialog dialog;
    private ImageButton btnPlay;
    private SeekBar seekBar;
    private MediaPlayer mediaPlayer;
    private Runnable runnable;
    private Handler handler;
    private TextView txtCurrentTime, txtEndTime;


    public RecordingsFragment() {
    }

    public static RecordingsFragment newInstance() {
        Bundle args = new Bundle();
        RecordingsFragment fragment = new RecordingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        ((MainActivity) getActivity()).showTabLayout(true);
        ((MainActivity) getActivity()).ANIMATION_TAG = ANIMATE_LEFT_TO_RIGHT;
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.recording_fragment, container, false);
            ButterKnife.bind(this, mView);

            setToolbar();
            init();

            initRecyclerView();

            if (checkPermission()) {
                startAsyncTask();
            }

        }

        return mView;
    }

    private boolean checkPermission() {
        boolean perm3 = isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (perm3)
            return true;
        else
            return false;

    }

    public boolean isPermissionGranted(String askedPermission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getContext(), askedPermission)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                requestPermissions(new String[]{askedPermission}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //resume tasks needing this permission
            startAsyncTask();
        }
    }

    private void startAsyncTask() {

        try {
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(APP_NAME), Environment.DIRECTORY_DOCUMENTS);
            ArrayList<HashMap<String, String>> playList = getPlayList(mediaStorageDir.getPath());
            setUpRecyclerView(playList);
        } catch (Exception e) {
            Log.i("fileReadError", e.toString());
        }

    }

    ArrayList<HashMap<String, String>> getPlayList(String rootPath) {
        ArrayList<HashMap<String, String>> fileList = new ArrayList<>();

        try {

            File rootFolder = new File(rootPath);
            File[] files = rootFolder.listFiles(); //here you will get NPE if directory doesn't contains  any file,handle it like this.
            for (File file : files) {
                if (file.isDirectory()) {
                    if (getPlayList(file.getAbsolutePath()) != null) {
                        fileList.addAll(getPlayList(file.getAbsolutePath()));
                    } else {
                        break;
                    }
                } else if (file.getName().endsWith(".mp3")) {
                    HashMap<String, String> song = new HashMap<>();
                    song.put("file_path", file.getAbsolutePath());
                    song.put("file_name", file.getName());
                    fileList.add(song);
                }
            }
            return fileList;
        } catch (Exception e) {
            return null;
        }
    }

    private void setToolbar() {
        txtToolbarTitle.setText(getString(R.string.myRecordings));
        txtToolbarTitle.setTextColor(getResources().getColor(R.color.black));
        txtToolbarTitle.setTypeface(Typeface.createFromAsset(getContext().getAssets(), TOOLBAR_FONT_TYPE), Typeface.BOLD);
    }

    private void init() {
        MobileAds.initialize(getContext(), getActivity().getResources().getString(R.string.ADMOB_APP_ID));
        AdMobUtils.loadBannerAd(adView);
        //AdMobUtils.loadInterstitialAd(getContext());
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(this);
        imgOptions.setVisibility(View.GONE);
    }

    private void initRecyclerView() {
        setLayoutManager();
        setAdapter();
    }

    private void setLayoutManager() {
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);

    }

    private void setAdapter() {
        recordingAdapter = new RecordingAdapter(getActivity(), getContext(), mFragmentNavigation);
        recyclerView.setAdapter(recordingAdapter);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setItemAnimator(new ItemAnimator());
        recordingAdapter.setPlayRecordCallback(this);
    }

    private void setUpRecyclerView(ArrayList<HashMap<String, String>> sairArrayList) {

        recordingAdapter.addAll(sairArrayList);

    }

    @Override
    public void onClick(View view) {

        if (view == imgBack) {
            getActivity().onBackPressed();
        }

    }


    @Override
    public void onPlayRecord(String path) {

        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.audio_player_dialog);
        initItems(path);

        dialog.show();

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    if (handler != null && runnable != null) {
                        handler.removeCallbacks(runnable);
                    }
                }
            }
        });

    }

    private void initItems(String path) {
        btnPlay = (ImageButton) dialog.findViewById(R.id.btnPlay);
        btnPlay.requestFocus();
        handler = new Handler();
        seekBar = (SeekBar) dialog.findViewById(R.id.mediacontroller_progress);
        mediaPlayer = MediaPlayer.create(getContext(), Uri.parse(path));
        txtCurrentTime = (TextView) dialog.findViewById(R.id.time_current);
        txtEndTime = (TextView) dialog.findViewById(R.id.player_end_time);

        if (mediaPlayer != null) {
            //setSeekBar();
            txtCurrentTime.setText(stringForTime((int) mediaPlayer.getCurrentPosition()));
            txtEndTime.setText(stringForTime((int) mediaPlayer.getDuration()));

            btnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        btnPlay.setImageResource(android.R.drawable.ic_media_play);
                    } else {
                        mediaPlayer.start();
                        btnPlay.setImageResource(android.R.drawable.ic_media_pause);
                        changeSeekBar();
                    }
                }
            });

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    seekBar.setMax(mediaPlayer.getDuration());
                    //mediaPlayer.start();
                    //changeSeekBar();
                }
            });

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    if (b) {
                        mediaPlayer.seekTo(i);
                        txtCurrentTime.setText(stringForTime((int) mediaPlayer.getCurrentPosition()));
                        txtEndTime.setText(stringForTime((int) mediaPlayer.getDuration()));
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }

    }

    private String stringForTime(int timeMs) {
        StringBuilder mFormatBuilder;
        Formatter mFormatter;
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    private void changeSeekBar() {
        if (mediaPlayer != null) {
            seekBar.setProgress(mediaPlayer.getCurrentPosition());

            if (mediaPlayer.isPlaying()) {
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        changeSeekBar();
                        txtCurrentTime.setText(stringForTime((int) mediaPlayer.getCurrentPosition()));
                        txtEndTime.setText(stringForTime((int) mediaPlayer.getDuration()));
                    }

                };

                handler.postDelayed(runnable, 1000);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

    }
}
