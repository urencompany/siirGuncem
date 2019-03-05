package com.uren.siirler.MainFragments.Favorite;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.uren.siirler.MainActivity;
import com.uren.siirler.MainFragments.BaseFragment;
import com.uren.siirler.MainFragments.Favorite.Adapters.FavoriteAdapter;
import com.uren.siirler.MainFragments.TabPoets.Adapters.PoetAdapter;
import com.uren.siirler.R;
import com.uren.siirler.Utils.AdMobUtil.AdMobUtils;
import com.uren.siirler.Utils.ClickableImage.ClickableImageView;
import com.uren.siirler.Utils.ItemAnimator;
import com.uren.siirler._database.datasource.SairDataSource;
import com.uren.siirler._database.datasource.SiirDataSource;
import com.uren.siirler._model.Sair;
import com.uren.siirler._model.Siir;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


import static com.uren.siirler.Constants.StringConstants.ANIMATE_LEFT_TO_RIGHT;
import static com.uren.siirler.Constants.StringConstants.TOOLBAR_FONT_TYPE;


public class FavoriteFragment extends BaseFragment
        implements View.OnClickListener {

    View mView;
    private LinearLayoutManager mLayoutManager;
    private FavoriteAdapter favoriteAdapter;

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
    @BindView(R.id.txtNoFavorite)
    TextView txtNoFavorite;
    @BindView(R.id.adView)
    AdView adView;

    public FavoriteFragment() {
    }

    public static FavoriteFragment newInstance() {
        Bundle args = new Bundle();
        FavoriteFragment fragment = new FavoriteFragment();
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
            mView = inflater.inflate(R.layout.favorite_fragment, container, false);
            ButterKnife.bind(this, mView);

            setToolbar();
            init();

            initRecyclerView();
            startAsyncTask();


        }

        return mView;
    }

    private void startAsyncTask() {
        new AsyncInsertData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void setToolbar() {
        txtToolbarTitle.setText(getString(R.string.myFavorites));
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
        favoriteAdapter = new FavoriteAdapter(getActivity(), getContext(), mFragmentNavigation);
        recyclerView.setAdapter(favoriteAdapter);
        recyclerView.setItemViewCacheSize(50);
        recyclerView.setItemAnimator(new ItemAnimator());
    }

    private void setUpRecyclerView(ArrayList<Siir> siirArrayList, ArrayList<Sair> sairArrayList) {
        if (siirArrayList != null && siirArrayList.size() > 0) {
            txtNoFavorite.setVisibility(View.GONE);
            favoriteAdapter.addAll(sairArrayList, siirArrayList);
        } else {
            txtNoFavorite.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onClick(View view) {

        if (view == imgBack) {
            getActivity().onBackPressed();
        }

    }

    private class AsyncInsertData extends AsyncTask<Void, Void, Void> {

        ArrayList<Sair> sairArrayList;
        ArrayList<Siir> siirArrayList;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {

            //Tüm şairler
            SairDataSource sairDataSource = new SairDataSource(getContext());
            sairArrayList = sairDataSource.getSairList();

            //Tüm şiirler
            SiirDataSource siirDataSource = new SiirDataSource(getContext());
            siirArrayList = siirDataSource.getFavoriteSiirList();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.d("MainActivity", "Data Inserted ");

            progressBar.setVisibility(View.GONE);

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
