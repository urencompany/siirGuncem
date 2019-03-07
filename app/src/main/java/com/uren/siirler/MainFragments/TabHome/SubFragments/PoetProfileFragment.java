package com.uren.siirler.MainFragments.TabHome.SubFragments;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.uren.siirler.Constants.NumericConstants;
import com.uren.siirler.MainActivity;
import com.uren.siirler.MainFragments.BaseFragment;
import com.uren.siirler.MainFragments.TabHome.Adapters.PoemAdapter;
import com.uren.siirler.MainFragments.TabHome.Adapters.SinglePoemAdapter;
import com.uren.siirler.R;
import com.uren.siirler.Utils.AdMobUtil.AdMobUtils;
import com.uren.siirler.Utils.ClickableImage.ClickableImageView;
import com.uren.siirler.Utils.ItemAnimator;
import com.uren.siirler._database.datasource.SairDataSource;
import com.uren.siirler._database.datasource.SiirDataSource;
import com.uren.siirler._model.Sair;
import com.uren.siirler._model.Siir;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.uren.siirler.Constants.StringConstants.ANIMATE_LEFT_TO_RIGHT;
import static com.uren.siirler.Constants.StringConstants.USERNAME_FONT_TYPE;

public class PoetProfileFragment extends BaseFragment
        implements View.OnClickListener {

    private View mView;
    private int poetId;

    private LinearLayoutManager mLayoutManager;
    private PoemAdapter poemAdapter;

    @BindView(R.id.imgProfile)
    ImageView imgProfile;
    @BindView(R.id.imgBack)
    ClickableImageView imgBack;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsing_toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.adView)
    AdView adView;

    Sair sair;
    ArrayList<Siir> sairSiirList;

    public static PoetProfileFragment newInstance(int poetId) {
        Bundle args = new Bundle();
        args.putInt("poetId", poetId);
        PoetProfileFragment fragment = new PoetProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        ((MainActivity) getActivity()).showTabLayout(true);
        super.onStart();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.poet_profile_fragment, container, false);
            ButterKnife.bind(this, mView);

            //input for the fragment
            getItemsFromBundle();

            //setting content
            init();
            setVariables();

        }

        return mView;
    }

    private void getItemsFromBundle() {
        Bundle args = getArguments();
        if (args != null) {
            poetId = (Integer) args.getInt("poetId");
        }
    }

    private void init() {
        MobileAds.initialize(getContext(), getActivity().getResources().getString(R.string.ADMOB_APP_ID));
        AdMobUtils.loadBannerAd(adView);
        //AdMobUtils.loadInterstitialAd(getContext());
        imgBack.setOnClickListener(this);

        //imgRecordIcon
        Glide.with(getContext())
                .load(R.drawable.icon_left_arrow)
                .apply(RequestOptions.fitCenterTransform())
                .into(imgBack);

    }

    private void setVariables() {
        setUI();
        initRecyclerView();
        new AsyncInsertData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void setUI() {

        SairDataSource sairDataSource = new SairDataSource(getContext());
        sair = sairDataSource.getSair(poetId);

        setToolbar();
        setProfilePicture();

    }

    private void setToolbar() {
        collapsing_toolbar.setTitle(sair.getAd());
        collapsing_toolbar.setExpandedTitleColor(getResources().getColor(R.color.clearWhite));
        collapsing_toolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.black));
        collapsing_toolbar.setExpandedTitleTypeface(Typeface.createFromAsset(getContext().getAssets(), USERNAME_FONT_TYPE));
        collapsing_toolbar.setCollapsedTitleTypeface(Typeface.createFromAsset(getContext().getAssets(), USERNAME_FONT_TYPE));
    }

    private void setProfilePicture() {

        imgProfile.setOnClickListener(this);

        if (sair.getProfilePicIndex() != -1) {
            Glide.with(getContext())
                    .load(sair.getProfilePicIndex())
                    .apply(RequestOptions.centerCropTransform().centerCrop())
                    .into(imgProfile);
        } else {
            Glide.with(getContext())
                    .load(R.drawable.icon_poem)
                    .apply(RequestOptions.centerCropTransform().centerCrop())
                    .into(imgProfile);
        }
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
        poemAdapter = new PoemAdapter(getActivity(), getContext(), mFragmentNavigation);
        recyclerView.setAdapter(poemAdapter);
        recyclerView.setItemAnimator(new ItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void setUpRecyclerView(ArrayList<Siir> siirArrayList) {

        poemAdapter.addAll(siirArrayList);

    }

    @Override
    public void onClick(View view) {

        if (view == imgBack) {
            getActivity().onBackPressed();
        }

        if (view == imgProfile) {
            mFragmentNavigation.pushFragment(new ShowSelectedPhotoFragment(sair.getProfilePicIndex()));
        }

    }

    private class AsyncInsertData extends AsyncTask<Void, Void, Void> {

        ArrayList<Siir> siirArrayList;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {

            Log.d("onInBackground()", "Data Inserting ");
            SiirDataSource siirDataSource = new SiirDataSource(getContext());
            siirArrayList = siirDataSource.getSairSiirList(poetId);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.d("MainActivity", "Data Inserted ");

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // WORK on UI thread here
                    setUpRecyclerView(siirArrayList);
                }
            });

        }
    }

}
