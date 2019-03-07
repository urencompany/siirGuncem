package com.uren.siirler.MainFragments.ShareManagement;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.uren.siirler.MainActivity;
import com.uren.siirler.MainFragments.BaseFragment;
import com.uren.siirler.R;
import com.uren.siirler.Utils.BitmapConversion;
import com.uren.siirler.Utils.ClickableImage.ClickableImageView;
import com.uren.siirler.Utils.ShapeUtil;
import com.uren.siirler.Utils.Utils;
import com.uren.siirler._database.datasource.SairDataSource;
import com.uren.siirler._database.datasource.SiirDataSource;
import com.uren.siirler._model.Sair;
import com.uren.siirler._model.Siir;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ShareFragment extends BaseFragment
        implements View.OnClickListener {

    View mView;
    private int poemId;
    private Siir siir;
    private Sair sair;

    @BindView(R.id.rlMain)
    RelativeLayout rlMain;
    @BindView(R.id.imgBackground)
    ImageView imgBackground;
    @BindView(R.id.txtBaslik)
    TextView txtBaslik;
    @BindView(R.id.txtSiir)
    TextView txtSiir;
    @BindView(R.id.txtSair)
    TextView txtSair;

    @BindView(R.id.imgCancel)
    ClickableImageView imgCancel;
    @BindView(R.id.seekbarLayout)
    FrameLayout seekbarLayout;
    @BindView(R.id.seekbar)
    SeekBar seekbar;
    @BindView(R.id.imgOptions)
    ClickableImageView imgOptions;

    @BindView(R.id.llSelection)
    LinearLayout llSelection;
    @BindView(R.id.imgBackArrrow)
    ClickableImageView imgBackArrrow;
    @BindView(R.id.imgSelectedOption)
    ImageView imgSelectedOption;
    @BindView(R.id.imgForwardArrow)
    ClickableImageView imgForwardArrow;

    @BindView(R.id.imgForward)
    ClickableImageView imgForward;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    ShareContent shareContent;

    private int textColorIndex = 0;
    private int imageIndex = 0;
    private int textTypeIndex = 1;
    private int selectionId = 0;
    Bitmap bitmap;
    ArrayList<int[]> backgroundColorList;

    private static final int INCREASE = 0;
    private static final int DECREASE = 1;
    private static final int PERM_WRITE_EXT_STORAGE = 901;

    public static ShareFragment newInstance(int poemId, int backgroundIndex, int typeFaceIndex) {
        Bundle args = new Bundle();
        args.putInt("poemId", poemId);
        args.putInt("backgroundIndex", backgroundIndex);
        args.putInt("typeFaceIndex", typeFaceIndex);
        ShareFragment fragment = new ShareFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.share_fragment, container, false);
            ButterKnife.bind(this, mView);

            //input for the fragment
            getItemsFromBundle();

            setShapes();
            init();
            setImages();
            setSeekbar();
            setVariables();
        }

        return mView;
    }

    @Override
    public void onStart() {
        ((MainActivity) getActivity()).showTabLayout(true);
        ((MainActivity) getActivity()).ANIMATION_TAG = null;
        super.onStart();
    }

    private void getItemsFromBundle() {
        Bundle args = getArguments();
        if (args != null) {
            poemId = (Integer) args.getInt("poemId");
            imageIndex = (Integer) args.getInt("backgroundIndex");
            textTypeIndex = (Integer) args.getInt("typeFaceIndex");
        }
    }

    private void setShapes() {
        llSelection.setBackground(ShapeUtil.getShape(getResources().getColor(R.color.transparentBlack),
                0, GradientDrawable.RECTANGLE, 15, 0));
        seekbarLayout.setBackground(ShapeUtil.getShape(getResources().getColor(R.color.transparentBlack),
                0, GradientDrawable.RECTANGLE, 15, 0));
        imgOptions.setBackground(ShapeUtil.getShape(getResources().getColor(R.color.transparentBlack),
                0, GradientDrawable.RECTANGLE, 15, 0));
    }

    private void init() {
        imgCancel.setOnClickListener(this);
        imgOptions.setOnClickListener(this);
        imgForward.setOnClickListener(this);
        imgBackArrrow.setOnClickListener(this);
        imgForwardArrow.setOnClickListener(this);
        rlMain.setOnClickListener(this);

        shareContent = new ShareContent();
        backgroundColorList = Utils.getBackgroundColorList(getContext());
        setAllInvisible();
    }

    private void openMoreMenu() {
        PopupMenu popupMenu = new PopupMenu(getContext(), imgOptions);
        popupMenu.inflate(R.menu.share_menu);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                selectionId = item.getItemId();
                switch (item.getItemId()) {
                    case R.id.itemBackground:
                        setAllInvisible();
                        setSelectionVisibility();
                        setSelectionImage(R.drawable.icon_photo);
                        break;
                    case R.id.itemTextColor:
                        setAllInvisible();
                        setSelectionVisibility();
                        setSelectionImage(R.drawable.icon_painter_palette);
                        break;
                    case R.id.itemTextSize:
                        setAllInvisible();
                        seekbarLayout.setVisibility(View.VISIBLE);
                        break;
                    case R.id.itemTextFont:
                        setAllInvisible();
                        setSelectionVisibility();
                        setSelectionImage(R.drawable.icon_text_color);
                        break;

                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void setSelectionImage(int itemId) {
        Glide.with(getContext())
                .load(itemId)
                .apply(RequestOptions.fitCenterTransform())
                .into(imgSelectedOption);
    }

    private void setSelectionVisibility() {
        if (llSelection.getVisibility() == View.GONE)
            llSelection.setVisibility(View.VISIBLE);
    }

    private void setAllInvisible() {
        llSelection.setVisibility(View.GONE);
        seekbarLayout.setVisibility(View.GONE);
    }

    private void setImages() {
        //background
        int startColor = getContext().getResources().getColor(backgroundColorList.get(imageIndex)[0]);
        int endColor = getContext().getResources().getColor(backgroundColorList.get(imageIndex)[1]);
        Glide.with(getContext())
                .load(ShapeUtil.getGradientBackground(startColor, endColor))
                .apply(RequestOptions.centerCropTransform())
                .into(imgBackground);
        //imgCancel
        Glide.with(getContext())
                .load(R.drawable.icon_black_cancel)
                .apply(RequestOptions.fitCenterTransform())
                .into(imgCancel);
        //imgMore
        Glide.with(getContext())
                .load(R.drawable.icon_more_vertical)
                .apply(RequestOptions.fitCenterTransform())
                .into(imgOptions);
        //imgBackArrrow
        Glide.with(getContext())
                .load(R.drawable.icon_back_white)
                .apply(RequestOptions.fitCenterTransform())
                .into(imgBackArrrow);
        //imgForwardArrow
        Glide.with(getContext())
                .load(R.drawable.icon_forward_white)
                .apply(RequestOptions.fitCenterTransform())
                .into(imgForwardArrow);
        //imgForward
        Glide.with(getContext())
                .load(R.drawable.icon_whatsapp)
                .apply(RequestOptions.fitCenterTransform())
                .into(imgForward);

    }



    private void setVariables() {
        getPoem();
        setUI();
    }

    private void getPoem() {
        SiirDataSource siirDataSource = new SiirDataSource(getContext());
        siir = siirDataSource.getSiir(poemId);

        SairDataSource sairDataSource = new SairDataSource(getContext());
        sair = sairDataSource.getSair(siir.getSairId());
    }

    private void setUI() {

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
        Typeface[] typeFace = shareContent.getTypeFace(getContext());
        txtBaslik.setTypeface(typeFace[textTypeIndex]);
        txtSiir.setTypeface(typeFace[textTypeIndex]);
        txtSair.setTypeface(typeFace[textTypeIndex]);

    }

    private void setSeekbar() {
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtBaslik.setTextSize(progress + 5);
                txtSiir.setTextSize(progress);
                txtSair.setTextSize(progress + 3);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    public void onClick(View view) {

        if (view == imgCancel) {
            getActivity().onBackPressed();
        }

        if (view == imgForward) {

            progressBar.setVisibility(View.VISIBLE);

            llSelection.setVisibility(View.GONE);
            seekbarLayout.setVisibility(View.GONE);
            imgCancel.setVisibility(View.GONE);
            imgOptions.setVisibility(View.GONE);
            imgForward.setVisibility(View.GONE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        progressBar.setVisibility(View.GONE);
                        bitmap = BitmapConversion.getScreenShot(rlMain);
                        checkPermission();
                    } catch (Exception e) {
                        progressBar.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                }
            }, 1500);

        }

        if (view == imgOptions) {
            setAllInvisible();
            openMoreMenu();
        }

        if (view == imgBackArrrow) {

            if (selectionId == R.id.itemBackground) {
                setPhotoImage(DECREASE);
            } else if (selectionId == R.id.itemTextColor) {
                setTextColor(DECREASE);
            } else if (selectionId == R.id.itemTextFont) {
                setTextFont(DECREASE);
            }
        }

        if (view == imgForwardArrow) {

            if (selectionId == R.id.itemBackground) {
                setPhotoImage(INCREASE);
            } else if (selectionId == R.id.itemTextColor) {
                setTextColor(INCREASE);
            } else if (selectionId == R.id.itemTextFont) {
                setTextFont(INCREASE);
            }
        }

        if (view == rlMain) {
            setVisibilities();
        }
    }

    private void setVisibilities() {

        if (imgCancel.getVisibility() == View.VISIBLE) {
            llSelection.setVisibility(View.GONE);
            seekbarLayout.setVisibility(View.GONE);
            imgCancel.setVisibility(View.GONE);
            imgOptions.setVisibility(View.GONE);
            imgForward.setVisibility(View.GONE);
        } else {
            imgCancel.setVisibility(View.VISIBLE);
            imgOptions.setVisibility(View.VISIBLE);
            imgForward.setVisibility(View.VISIBLE);
        }
    }

    private void setTextFont(int type) {

        Typeface[] typeFace = shareContent.getTypeFace(getContext());

        if (type == INCREASE) {
            if (textTypeIndex == (typeFace.length - 1))
                textTypeIndex = 0;
            else
                textTypeIndex++;
        } else if (type == DECREASE) {
            if (textTypeIndex == 0)
                textTypeIndex = typeFace.length - 1;
            else
                textTypeIndex--;
        }

        txtBaslik.setTypeface(typeFace[textTypeIndex]);
        txtSiir.setTypeface(typeFace[textTypeIndex]);
        txtSair.setTypeface(typeFace[textTypeIndex]);

    }

    private void setPhotoImage(int type) {

        if (type == INCREASE) {
            if (imageIndex == (backgroundColorList.size() - 1))
                imageIndex = 0;
            else
                imageIndex++;
        } else if (type == DECREASE) {
            if (imageIndex == 0)
                imageIndex = backgroundColorList.size() - 1;
            else
                imageIndex--;
        }

        //background

        int startColor = getContext().getResources().getColor(backgroundColorList.get(imageIndex)[0]);
        int endColor = getContext().getResources().getColor(backgroundColorList.get(imageIndex)[1]);
        Glide.with(getContext())
                .load(ShapeUtil.getGradientBackground(startColor, endColor))
                .apply(RequestOptions.fitCenterTransform())
                .into(imgBackground);

    }


    private void setTextColor(int type) {
        if (type == INCREASE) {
            if (textColorIndex == (shareContent.getTextColors().length - 1))
                textColorIndex = 0;
            else
                textColorIndex++;
        } else if (type == DECREASE) {
            if (textColorIndex == 0)
                textColorIndex = shareContent.getTextColors().length - 1;
            else
                textColorIndex--;
        }

        int colorCode = shareContent.getTextColors()[textColorIndex];

        txtBaslik.setTextColor(getActivity().getResources().getColor(colorCode));
        txtSiir.setTextColor(getActivity().getResources().getColor(colorCode));
        txtSair.setTextColor(getActivity().getResources().getColor(colorCode));
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            onClickApp("com.whatsapp", bitmap);
        } else
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERM_WRITE_EXT_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == PERM_WRITE_EXT_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onClickApp("com.whatsapp", bitmap);
            }
        }
    }

    public void onClickApp(String pack, Bitmap bitmap) {
        PackageManager pm = getContext().getPackageManager();
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), bitmap, "Title", null);
            Uri imageUri = Uri.parse(path);

            @SuppressWarnings("unused")
            PackageInfo info = pm.getPackageInfo(pack, PackageManager.GET_META_DATA);

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("image/*");
            waIntent.setPackage(pack);
            waIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            getContext().startActivity(Intent.createChooser(waIntent, "Share with"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "Whatsapp not found", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Teknik Hata", Toast.LENGTH_SHORT).show();
        }
    }

}
