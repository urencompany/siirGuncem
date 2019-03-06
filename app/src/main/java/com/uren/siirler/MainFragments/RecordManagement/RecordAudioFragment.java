package com.uren.siirler.MainFragments.RecordManagement;


import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cleveroad.audiovisualization.GLAudioVisualizationView;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.uren.siirler.MainActivity;
import com.uren.siirler.MainFragments.BaseFragment;
import com.uren.siirler.MainFragments.RecordManagement.Interfaces.RecordSavedCallback;
import com.uren.siirler.MainFragments.RecordManagement.JavaClasses.FileAdapter;
import com.uren.siirler.MainFragments.RecordManagement.JavaClasses.Util;
import com.uren.siirler.MainFragments.RecordManagement.JavaClasses.VisualizerHandler;

import com.uren.siirler.MainFragments.RecordManagement.model.AudioChannel;
import com.uren.siirler.MainFragments.RecordManagement.model.AudioSampleRate;
import com.uren.siirler.MainFragments.RecordManagement.model.AudioSource;
import com.uren.siirler.R;
import com.uren.siirler.Utils.AdMobUtil.AdMobUtils;
import com.uren.siirler.Utils.ClickableImage.ClickableImageView;
import com.uren.siirler.Utils.DialogBoxUtil.DialogBoxUtil;
import com.uren.siirler.Utils.Utils;
import com.uren.siirler._database.datasource.SairDataSource;
import com.uren.siirler._database.datasource.SiirDataSource;
import com.uren.siirler._model.Sair;
import com.uren.siirler._model.Siir;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import omrecorder.AudioChunk;
import omrecorder.OmRecorder;
import omrecorder.PullTransport;
import omrecorder.Recorder;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO;
import static com.uren.siirler.Constants.StringConstants.ANIMATE_LEFT_TO_RIGHT;


public class RecordAudioFragment extends BaseFragment
        implements PullTransport.OnAudioChunkPulledListener, View.OnClickListener {

    View mView;
    private int poemId;
    private int imageIndex = 0;
    private int textTypeIndex = 1;
    private Siir siir;
    private Sair sair;

    private MediaPlayer player;
    private MediaPlayer backgroundMediaPlayer;
    private MediaPlayer chooseMelodyplayer;
    private Recorder recorder;
    private VisualizerHandler visualizerHandler;

    private Timer timer;
    private int recorderSecondsElapsed;
    private int playerSecondsElapsed;
    private boolean isRecording;
    private GLAudioVisualizationView visualizerView;

    //Recorder variables
    private String filePath = Environment.getExternalStorageDirectory() + "/recorded_audio.wav";
    private AudioSource source = AudioSource.MIC;
    private AudioChannel channel = AudioChannel.STEREO;
    private AudioSampleRate sampleRate = AudioSampleRate.HZ_44100;
    private int color = Color.parseColor("#546E7A");
    private int requestCode = 0;
    private boolean autoStart = false;
    private boolean keepDisplayOn = false;
    private File outputMediaFile;

    @BindView(R.id.llAll)
    LinearLayout llAll;
    @BindView(R.id.content)
    LinearLayout contentLayout;
    @BindView(R.id.status)
    TextView statusView;
    @BindView(R.id.timer)
    TextView timerView;
    @BindView(R.id.restart)
    ImageButton restartView;
    @BindView(R.id.record)
    ImageButton recordView;
    @BindView(R.id.play)
    ImageButton playView;

    @BindView(R.id.imgCancel)
    ClickableImageView imgCancel;
    @BindView(R.id.imgSave)
    ClickableImageView imgSave;
    @BindView(R.id.imgBackground)
    ImageView imgBackground;
    @BindView(R.id.imgShare)
    ClickableImageView imgShare;
    @BindView(R.id.imgMusic)
    ClickableImageView imgMusic;

    @BindView(R.id.txtBaslik)
    TextView txtBaslik;
    @BindView(R.id.txtSiir)
    TextView txtSiir;
    @BindView(R.id.txtSair)
    TextView txtSair;
    @BindView(R.id.adView)
    AdView adView;

    ArrayList<File> listOutputFile = new ArrayList<>();

    @BindArray(R.array.sounds)
    String[] melodies;
    int selectedFontMusic = 0;

    private static final String AUDIO_FILE_PATH =
            Environment.getExternalStorageDirectory().getPath() + "/recorded_audio.wav";


    public RecordAudioFragment() {
    }

    public static RecordAudioFragment newInstance(int poemId, int backgroundIndex, int typeFaceIndex) {
        Bundle args = new Bundle();
        args.putInt("poemId", poemId);
        args.putInt("backgroundIndex", backgroundIndex);
        args.putInt("typeFaceIndex", typeFaceIndex);
        RecordAudioFragment fragment = new RecordAudioFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        ((MainActivity) getActivity()).showTabLayout(true);
        ((MainActivity) getActivity()).ANIMATION_TAG = ANIMATE_LEFT_TO_RIGHT;
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.audio_reacording_fragment, container, false);
            ButterKnife.bind(this, mView);

            getItemsFromBundle();

            setListeners();
            setRecordingVariables();
            setUIElements();
            setPoem();

            checkPermission();

        }

        return mView;
    }

    private void setPoem() {
        SiirDataSource siirDataSource = new SiirDataSource(getContext());
        siir = siirDataSource.getSiir(poemId);

        SairDataSource sairDataSource = new SairDataSource(getContext());
        sair = sairDataSource.getSair(siir.getSairId());

        txtBaslik.setText(siir.getAd());
        txtSair.setText(sair.getAd());

        String response = null;
        try {
            response = new String(siir.getMetin(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("EncodingError", e.toString());
        }
        txtSiir.setText(response);

        //Yazı tipleri
        Typeface[] typeFace = Utils.getTypeFaceList(getContext());
        txtBaslik.setTypeface(typeFace[textTypeIndex]);
        txtSiir.setTypeface(typeFace[textTypeIndex]);
        txtSair.setTypeface(typeFace[textTypeIndex]);
    }

    private void getItemsFromBundle() {
        Bundle args = getArguments();
        if (args != null) {
            poemId = (Integer) args.getInt("poemId");
            imageIndex = (Integer) args.getInt("backgroundIndex");
            textTypeIndex = (Integer) args.getInt("typeFaceIndex");
        }
    }

    private boolean checkPermission() {
        boolean perm1 = isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        boolean perm2 = isPermissionGranted(Manifest.permission.RECORD_AUDIO);


        if (perm1 && perm2)
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
        }
    }

    private void setListeners() {
        MobileAds.initialize(getContext(), getActivity().getResources().getString(R.string.ADMOB_APP_ID));
        AdMobUtils.loadBannerAd(adView);

        restartView.setOnClickListener(this);
        recordView.setOnClickListener(this);
        playView.setOnClickListener(this);
        imgCancel.setOnClickListener(this);
        imgSave.setOnClickListener(this);
        imgShare.setOnClickListener(this);
        imgMusic.setOnClickListener(this);
        imgSave.setVisibility(View.GONE);
        imgShare.setVisibility(View.GONE);
        imgMusic.setVisibility(View.VISIBLE);
    }

    private void setRecordingVariables() {
        filePath = AUDIO_FILE_PATH;
        color = ContextCompat.getColor(getContext(), R.color.red);
        source = AudioSource.MIC;
        channel = AudioChannel.STEREO;
        sampleRate = AudioSampleRate.HZ_11025;
        autoStart = false;
        keepDisplayOn = true;

    }

    private void setUIElements() {

        /*
        visualizerView = new GLAudioVisualizationView.Builder(getActivity())
                .setLayersCount(1)
                .setWavesCount(6)
                .setWavesHeight(R.dimen.aar_wave_height)
                .setWavesFooterHeight(R.dimen.aar_footer_height)
                .setBubblesPerLayer(20)
                .setBubblesSize(R.dimen.aar_bubble_size)
                .setBubblesRandomizeSize(true)
                .setBackgroundColor(Util.getDarkerColor(color))
                .setLayerColors(new int[]{color})
                .build();

        contentLayout.addView(visualizerView, 0);
        */
        //imgCancel.setColorFilter(ContextCompat.getColor(getContext(), R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
        //imgSave.setColorFilter(ContextCompat.getColor(getContext(), R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
        statusView.setTextColor(getResources().getColor(R.color.black));
        timerView.setTextColor(getResources().getColor(R.color.black));
        restartView.setColorFilter(ContextCompat.getColor(getContext(), R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
        restartView.setColorFilter(ContextCompat.getColor(getContext(), R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
        recordView.setColorFilter(ContextCompat.getColor(getContext(), R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
        playView.setColorFilter(ContextCompat.getColor(getContext(), R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);

        //contentLayout.setBackgroundColor(Util.getDarkerColor(color));
        Glide.with(getContext())
                .load(Utils.getBackgroundList(getContext())[imageIndex])
                .apply(RequestOptions.centerCropTransform())
                .into(imgBackground);


        restartView.setVisibility(View.INVISIBLE);
        playView.setVisibility(View.INVISIBLE);

        if (Util.isBrightColor(color)) {
            ContextCompat.getDrawable(getContext(), R.drawable.aar_ic_clear)
                    .setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
            ContextCompat.getDrawable(getContext(), R.drawable.aar_ic_check)
                    .setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
            statusView.setTextColor(Color.BLACK);
            timerView.setTextColor(Color.BLACK);
            restartView.setColorFilter(Color.BLACK);
            recordView.setColorFilter(Color.BLACK);
            playView.setColorFilter(Color.BLACK);
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        try {
            // visualizerView.onResume();
        } catch (Exception e) {
        }
    }


    @Override
    public void onAudioChunkPulled(AudioChunk audioChunk) {
        // float amplitude = isRecording ? (float) audioChunk.maxAmplitude() : 0f;
        // visualizerHandler.onDataReceived(amplitude);
    }

    private void selectAudio() {
        stopRecording();
        //setResult(RESULT_OK);
        // finish();
    }

    public void toggleRecording() {
        stopPlaying();
        Util.wait(100, new Runnable() {
            @Override
            public void run() {
                if (isRecording) {
                    pauseRecording();
                } else {
                    resumeRecording();
                }
            }
        });
    }

    public void togglePlaying() {
        pauseRecording();
        Util.wait(100, new Runnable() {
            @Override
            public void run() {
                if (isPlaying()) {
                    stopPlaying();
                } else {
                    startPlaying();
                }
            }
        });
    }

    public void restartRecording() {

        if (isRecording) {
            stopRecording();
        } else if (isPlaying()) {
            stopPlaying();
        } else {
            visualizerHandler = new VisualizerHandler();
            //visualizerView.linkTo(visualizerHandler);
            //visualizerView.release();
            if (visualizerHandler != null) {
                visualizerHandler.stop();
            }
        }

        statusView.setVisibility(View.INVISIBLE);
        restartView.setVisibility(View.INVISIBLE);
        playView.setVisibility(View.INVISIBLE);
        recordView.setImageResource(R.drawable.aar_ic_rec);
        timerView.setText("00:00:00");
        recorderSecondsElapsed = 0;
        playerSecondsElapsed = 0;
        imgSave.setVisibility(View.GONE);
        imgShare.setVisibility(View.GONE);
        imgMusic.setVisibility(View.VISIBLE);

        if (recorder != null) {
            recorder.stopRecording();
            recorder = null;
        }

        if (backgroundMediaPlayer != null) {
            if (selectedFontMusic != 0) {
                backgroundMediaPlayer.reset();
            }
            backgroundMediaPlayer = null;
        }
    }

    private void resumeRecording() {
        isRecording = true;

        statusView.setText(R.string.aar_recording);
        statusView.setVisibility(View.VISIBLE);
        restartView.setVisibility(View.INVISIBLE);
        playView.setVisibility(View.INVISIBLE);
        recordView.setImageResource(R.drawable.aar_ic_pause);
        playView.setImageResource(R.drawable.aar_ic_play);
        imgSave.setVisibility(View.GONE);
        imgShare.setVisibility(View.GONE);
        imgMusic.setVisibility(View.VISIBLE);

        visualizerHandler = new VisualizerHandler();
        //visualizerView.linkTo(visualizerHandler);

        if (recorder == null) {
            timerView.setText("00:00:00");

            outputMediaFile = FileAdapter.getOutputMediaFile(MEDIA_TYPE_AUDIO);
            listOutputFile.add(outputMediaFile);

            recorder = OmRecorder.wav(
                    new PullTransport.Default(Util.getMic(source, channel, sampleRate), this),
                    outputMediaFile);
        }

        if (backgroundMediaPlayer == null) {
            if (selectedFontMusic != 0) {
                backgroundMediaPlayer = MediaPlayer.create(getContext(), selectedFontMusic);
                backgroundMediaPlayer.setLooping(true);
            }
        }

        recorder.resumeRecording();
        if (selectedFontMusic != 0) {
            backgroundMediaPlayer.start();
        }


        startTimer();
    }

    private void pauseRecording() {
        isRecording = false;

        statusView.setText(R.string.aar_paused);
        statusView.setVisibility(View.VISIBLE);
        restartView.setVisibility(View.VISIBLE);
        playView.setVisibility(View.VISIBLE);
        recordView.setImageResource(R.drawable.aar_ic_rec);
        playView.setImageResource(R.drawable.aar_ic_play);
        imgSave.setVisibility(View.VISIBLE);
        imgShare.setVisibility(View.VISIBLE);
        imgMusic.setVisibility(View.GONE);

        //visualizerView.release();
        if (visualizerHandler != null) {
            visualizerHandler.stop();
        }

        if (recorder != null) {
            recorder.pauseRecording();
        }

        if (backgroundMediaPlayer != null) {
            if (selectedFontMusic != 0) {
                backgroundMediaPlayer.pause();
            }
        }

        stopTimer();
    }

    private void stopRecording() {
        //visualizerView.release();
        if (visualizerHandler != null) {
            visualizerHandler.stop();
        }

        recorderSecondsElapsed = 0;
        if (recorder != null) {
            recorder.stopRecording();
            recorder = null;
        }

        if (backgroundMediaPlayer != null) {
            if (selectedFontMusic != 0) {
                backgroundMediaPlayer.stop();
            }
            backgroundMediaPlayer = null;
        }

        stopTimer();
    }

    private void startPlaying() {
        try {
            stopRecording();
            player = new MediaPlayer();
            player.setDataSource(outputMediaFile.getPath());
            try {
                player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            player.start();

            //visualizerView.linkTo(DbmHandler.Factory.newVisualizerHandler(this, player));
            /*visualizerView.post(new Runnable() {
                @Override
                public void run() {
                    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            stopPlaying();
                        }
                    });
                }
            });*/

            timerView.setText("00:00:00");
            statusView.setText(R.string.aar_playing);
            statusView.setVisibility(View.VISIBLE);
            playView.setImageResource(R.drawable.aar_ic_stop);
            imgSave.setVisibility(View.VISIBLE);
            imgShare.setVisibility(View.VISIBLE);
            imgMusic.setVisibility(View.GONE);

            playerSecondsElapsed = 0;
            startTimer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopPlaying() {
        statusView.setText("");
        statusView.setVisibility(View.INVISIBLE);
        playView.setImageResource(R.drawable.aar_ic_play);

        //visualizerView.release();
        if (visualizerHandler != null) {
            visualizerHandler.stop();
        }

        if (player != null) {
            try {
                player.stop();
                player.reset();
            } catch (Exception e) {
            }
        }

        stopTimer();
    }

    private boolean isPlaying() {
        try {
            return player != null && player.isPlaying() && !isRecording;
        } catch (Exception e) {
            return false;
        }
    }

    private void startTimer() {
        stopTimer();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateTimer();
            }
        }, 0, 1000);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    private void updateTimer() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isRecording) {
                    recorderSecondsElapsed++;
                    timerView.setText(Util.formatSeconds(recorderSecondsElapsed));
                } else if (isPlaying()) {
                    playerSecondsElapsed++;
                    timerView.setText(Util.formatSeconds(playerSecondsElapsed));
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

        if (view == restartView) {
            restartRecording();
        }

        if (view == recordView) {
            if (checkPermission())
                toggleRecording();
        }

        if (view == playView) {
            togglePlaying();
        }

        if (view == imgCancel) {
            stopRecording();
            getActivity().onBackPressed();
        }

        if (view == imgSave) {
            saveClicked();
        }

        if (view == imgShare) {
            shareClicked();
        }

        if (view == imgMusic) {
            musicClicked();
        }

    }

    private void musicClicked() {

        showMusicList();


    }

    private void showMusicList() {

        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.radiobutton_dialog);
        ScrollView scrollView = (ScrollView) dialog.findViewById(R.id.scrollView);
        final RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio_group);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        Button btnApply = (Button) dialog.findViewById(R.id.btnApply);

        for (int i = 0; i < melodies.length; i++) {

            String melody = melodies[i];
            RadioButton rb = new RadioButton(getContext());
            rb.setText(melody);
            rb.setId(i);

            rg.addView(rb);

            if (i == selectedFontMusic) {
                rb.setChecked(true);
            }


        }

        dialog.show();

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int index = rg.getCheckedRadioButtonId();

                if (chooseMelodyplayer != null) {
                    chooseMelodyplayer.reset();// stops any current playing song
                }
                if (index != 0) {
                    chooseMelodyplayer = MediaPlayer.create(getContext(), Utils.getRawItem(index));
                    chooseMelodyplayer.start();
                }

            }
        });

        //set Listeners
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (chooseMelodyplayer != null) {
                    chooseMelodyplayer.reset();// stops any current playing song
                }
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedIndex = rg.getCheckedRadioButtonId();
                selectedFontMusic = Utils.getRawItem(selectedIndex);

                dialog.dismiss();
                if (chooseMelodyplayer != null) {
                    chooseMelodyplayer.reset();// stops any current playing song
                }
            }
        });

    }

    private void saveClicked() {
        stopRecording();
        for (int i = 0; i < listOutputFile.size(); i++) {

            if (i != listOutputFile.size() - 1) {
                listOutputFile.get(i).delete();
            }

        }
        listOutputFile.clear();

        DialogBoxUtil.recordSavedSnackBar(llAll, getContext());
    }

    private void shareClicked() {

        stopRecording();
        try {
            String sharePath = outputMediaFile.getPath();
            Uri uri = Uri.parse(sharePath);
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("audio/*");
            share.setPackage("com.whatsapp");
            share.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(share, "Share with"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "Whatsapp not found", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Teknik Hata", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDestroyView() {
        for (int i = 0; i < listOutputFile.size(); i++) {
            listOutputFile.get(i).delete();
        }
        listOutputFile.clear();

        if (backgroundMediaPlayer != null) {
            backgroundMediaPlayer.stop();
            backgroundMediaPlayer.release();
        }

        if (chooseMelodyplayer != null) {
            chooseMelodyplayer.stop();
            chooseMelodyplayer.release();
        }

        super.onDestroyView();
    }
}