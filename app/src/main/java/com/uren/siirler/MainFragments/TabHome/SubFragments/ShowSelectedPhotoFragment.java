package com.uren.siirler.MainFragments.TabHome.SubFragments;


import android.annotation.SuppressLint;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.uren.siirler.MainActivity;
import com.uren.siirler.MainFragments.BaseFragment;
import com.uren.siirler.R;
import com.uren.siirler.Utils.ClickableImage.ClickableImageView;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoViewAttacher;

@SuppressLint("ValidFragment")
public class ShowSelectedPhotoFragment extends BaseFragment {

    View mView;

    @BindView(R.id.photoSelectImgv)
    ImageView photoSelectImgv;
    @BindView(R.id.imgCancel)
    ClickableImageView imgCancel;


    Matrix initMatrix;
    int imageIndex;

    public ShowSelectedPhotoFragment(int imageIndex) {
        this.imageIndex = imageIndex;
    }

    @Override
    public void onStart() {
        ((MainActivity) getActivity()).showTabLayout(false);
        ((MainActivity) getActivity()).ANIMATION_TAG = null;
        super.onStart();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            mView = inflater.inflate(R.layout.show_selected_photo_fragment, container, false);
            ButterKnife.bind(this, mView);
            initVariables();
            setImage();
            setListeners();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mView;
    }


    private void initVariables() {
        initMatrix = new Matrix();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        try {
            PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(photoSelectImgv);
            photoViewAttacher.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setImage() {

        try {
            Glide.with(getContext())
                    .load(imageIndex)
                    .apply(RequestOptions.fitCenterTransform())
                    .into(photoSelectImgv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setListeners() {

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

    }
}