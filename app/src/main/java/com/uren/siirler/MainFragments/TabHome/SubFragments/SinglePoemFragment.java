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
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.uren.siirler.MainActivity;
import com.uren.siirler.MainFragments.BaseFragment;
import com.uren.siirler.MainFragments.RecordManagement.RecordAudioFragment;
import com.uren.siirler.MainFragments.TabHome.Adapters.SinglePoemAdapter;
import com.uren.siirler.MainFragments.TabHome.JavaClasses.PoemHelper;
import com.uren.siirler.MainFragments.ShareManagement.ShareFragment;
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

import static com.uren.siirler.Constants.NumericConstants.COMING_FROM_DAILY_POPULAR_POEM;
import static com.uren.siirler.Constants.NumericConstants.NUMBER_OF_SUGGESTED_POEMS;
import static com.uren.siirler.Constants.StringConstants.ANIMATE_LEFT_TO_RIGHT;
import static com.uren.siirler.Constants.StringConstants.ANIMATE_RIGHT_TO_LEFT;
import static com.uren.siirler.Constants.StringConstants.USERNAME_FONT_TYPE;

public class SinglePoemFragment extends BaseFragment
        implements View.OnClickListener {

    private View mView;
    private int poemId;
    private int position;
    private int numberOfCallback;
    private int comingFrom;

    private LinearLayoutManager mLayoutManager;
    private SinglePoemAdapter singlePoemAdapter;

    private Siir siir;
    private Sair sair;

    @BindView(R.id.imgProfile)
    ImageView imgProfile;
    @BindView(R.id.imgBack)
    ClickableImageView imgBack;
    @BindView(R.id.txtBaslik)
    TextView txtBaslik;
    @BindView(R.id.txtPoem)
    TextView txtPoem;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsing_toolbar;
    @BindView(R.id.txtAllPoems)
    TextView txtAllPoems;
    @BindView(R.id.imgFavorite)
    ClickableImageView imgFavorite;
    @BindView(R.id.imgShare)
    ImageView imgShare;
    @BindView(R.id.imgRecord)
    ImageView imgRecord;
    @BindView(R.id.rlShare)
    RelativeLayout rlShare;
    @BindView(R.id.rlRecord)
    RelativeLayout rlRecord;
    @BindView(R.id.adView)
    AdView adView;


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    public static SinglePoemFragment newInstance(int poemId, int position, int numberOfCallback, int comingFrom) {
        Bundle args = new Bundle();
        args.putInt("poemId", poemId);
        args.putInt("position", position);
        args.putInt("numberOfCallback", numberOfCallback);
        args.putInt("comingFrom", comingFrom);
        SinglePoemFragment fragment = new SinglePoemFragment();
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
            mView = inflater.inflate(R.layout.single_poem_fragment, container, false);
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
            poemId = (Integer) args.getInt("poemId");
            position = (Integer) args.getInt("position");
            numberOfCallback = (Integer) args.getInt("numberOfCallback");
            comingFrom = (Integer) args.getInt("comingFrom");
        }
    }

    private void init() {
        MobileAds.initialize(getContext(), getActivity().getResources().getString(R.string.ADMOB_APP_ID));
        AdMobUtils.loadBannerAd(adView);
        //AdMobUtils.loadInterstitialAd(getContext());
        imgBack.setOnClickListener(this);
        txtAllPoems.setOnClickListener(this);
        imgFavorite.setOnClickListener(this);
    }


    private void setVariables() {
        getPoem();
        if (siir != null && sair != null) {
            setUI();
        }

    }

    private void getPoem() {
        SiirDataSource siirDataSource = new SiirDataSource(getContext());
        siir = siirDataSource.getSiir(poemId);
        updateSiirVariables(siirDataSource);


        SairDataSource sairDataSource = new SairDataSource(getContext());
        sair = sairDataSource.getSair(siir.getSairId());
        if (sair != null) {
            sairDataSource.updateSairDisplayed(sair.getId(), true);
        }

    }

    private void updateSiirVariables(SiirDataSource siirDataSource) {
        siirDataSource.updateSiirDisplayCount(poemId);

        if (comingFrom == COMING_FROM_DAILY_POPULAR_POEM) {
            siirDataSource.updateSiirDailyClicked(poemId);
        }

    }


    private void setUI() {

        setToolbar();
        setProfilePicture();

        //Siir baslik
        txtBaslik.setText(siir.getAd());
        //Siir metin
        String response = null;
        try {
            response = new String(siir.getMetin(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("EncodingError", e.toString());
        }
        txtPoem.setText(response);
        txtPoem.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonttypes/Sansation_Regular.ttf"));
        txtBaslik.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonttypes/Sansation_Regular.ttf"));

        //All poems
        txtAllPoems.setText(sair.getAd() + " " + getString(R.string.allPoems));

        //Favorite
        if (siir.isFavorite()) {
            setLikeIconUI(R.color.likeButtonColor, R.drawable.icon_star_full, false);
        } else {
            setLikeIconUI(R.color.black, R.drawable.icon_star_emtpy, false);
        }


        setSuggestionLayout();

    }

    private void setToolbar() {
        collapsing_toolbar.setTitle(sair.getAd());
        collapsing_toolbar.setExpandedTitleColor(getResources().getColor(R.color.clearWhite));
        collapsing_toolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.black));
        collapsing_toolbar.setExpandedTitleTypeface(Typeface.createFromAsset(getContext().getAssets(), USERNAME_FONT_TYPE));
        collapsing_toolbar.setCollapsedTitleTypeface(Typeface.createFromAsset(getContext().getAssets(), USERNAME_FONT_TYPE));
    }

    private void setLikeIconUI(int color, int icon, boolean isClientOperation) {
        //imgFavorite.setColorFilter(ContextCompat.getColor(mContext, color), android.graphics.PorterDuff.Mode.SRC_IN);

        Glide.with(getContext())
                .load(icon)
                .into(imgFavorite);

    }

    private void setProfilePicture() {

        imgProfile.setOnClickListener(this);
        //imgShare.setOnClickListener(this);
        // imgRecord.setOnClickListener(this);
        rlShare.setOnClickListener(this);
        rlRecord.setOnClickListener(this);


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

    private void setSuggestionLayout() {

        initRecyclerView();
        new AsyncInsertData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

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
        singlePoemAdapter = new SinglePoemAdapter(getActivity(), getContext(), mFragmentNavigation);
        recyclerView.setAdapter(singlePoemAdapter);
        recyclerView.setItemAnimator(new ItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void setUpRecyclerView(ArrayList<Siir> siirArrayList, ArrayList<Sair> sairArrayList) {

        Collections.shuffle(siirArrayList);
        ArrayList<Siir> siirArrayList1 = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_SUGGESTED_POEMS; i++) {
            siirArrayList1.add(siirArrayList.get(i));
        }

        singlePoemAdapter.addAll(siirArrayList1, sairArrayList);

    }

    @Override
    public void onClick(View view) {

        if (view == imgBack) {
            getActivity().onBackPressed();
        }

        if (view == txtAllPoems) {
            mFragmentNavigation.pushFragment(PoetProfileFragment.newInstance(sair.getId()), ANIMATE_RIGHT_TO_LEFT);
        }

        if (view == imgFavorite) {
            favoriteClicked();
        }

        if (view == imgProfile) {
            mFragmentNavigation.pushFragment(new ShowSelectedPhotoFragment(sair.getProfilePicIndex()));
        }

        if (view == rlShare) {
            mFragmentNavigation.pushFragment(ShareFragment.newInstance(siir.getId(), 0, 1));
        }

        if (view == rlRecord) {
            mFragmentNavigation.pushFragment(RecordAudioFragment.newInstance(siir.getId(), 0, 1));
        }
    }

    private void favoriteClicked() {

        imgFavorite.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.image_click));

        if (siir.isFavorite()) {
            siir.setFavorite(false);
            setLikeIconUI(R.color.black, R.drawable.icon_star_emtpy, true);
        } else {
            siir.setFavorite(true);
            setLikeIconUI(R.color.likeButtonColor, R.drawable.icon_star_full, true);
        }

        PoemHelper.FavoriteClicked.startProcess(getContext(), siir.getId(), siir.isFavorite());
        PoemHelper.SinglePoemClicked.poemFavoriteStatusChanged(siir.isFavorite(), position, numberOfCallback);

    }

    private class AsyncInsertData extends AsyncTask<Void, Void, Void> {

        ArrayList<Siir> siirArrayList;
        ArrayList<Sair> sairArrayList;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {

            Log.d("onInBackground()", "Data Inserting ");
            SiirDataSource siirDataSource = new SiirDataSource(getContext());
            siirArrayList = siirDataSource.getSiirList();

            SairDataSource sairDataSource = new SairDataSource(getContext());
            sairArrayList = sairDataSource.getSairList();

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
                    setUpRecyclerView(siirArrayList, sairArrayList);
                }
            });

        }
    }

}
