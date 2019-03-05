package com.uren.siirler.MainFragments.TabPoets;

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
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.uren.siirler.MainActivity;
import com.uren.siirler.MainFragments.BaseFragment;
import com.uren.siirler.MainFragments.TabHome.Adapters.FeedAdapter;
import com.uren.siirler.MainFragments.TabHome.HomeFragment;
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
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

import static com.uren.siirler.Constants.StringConstants.ANIMATE_LEFT_TO_RIGHT;
import static com.uren.siirler.Constants.StringConstants.TOOLBAR_FONT_TYPE;


public class PoetsFragment extends BaseFragment
        implements View.OnClickListener {

    View mView;
    private LinearLayoutManager mLayoutManager;
    private PoetAdapter poetAdapter;

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
    @BindView(R.id.fast_scroller)
    VerticalRecyclerViewFastScroller fastScroller;
    @BindView(R.id.adView)
    AdView adView;

    public PoetsFragment() {
    }

    public static PoetsFragment newInstance() {
        Bundle args = new Bundle();
        PoetsFragment fragment = new PoetsFragment();
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
            mView = inflater.inflate(R.layout.fragment_main_poets, container, false);
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
        txtToolbarTitle.setText(getString(R.string.poets));
        txtToolbarTitle.setTextColor(getResources().getColor(R.color.black));
        txtToolbarTitle.setTypeface(Typeface.createFromAsset(getContext().getAssets(), TOOLBAR_FONT_TYPE), Typeface.BOLD);
    }

    private void init() {
        MobileAds.initialize(getContext(), getActivity().getResources().getString(R.string.ADMOB_APP_ID));
        AdMobUtils.loadBannerAd(adView);
        AdMobUtils.loadInterstitialAd(getContext());
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
        // Connect the recycler to the scroller (to let the scroller scroll the list)
        fastScroller.setRecyclerView(recyclerView);
        recyclerView.setOnScrollListener(fastScroller.getOnScrollListener());
    }

    private void setAdapter() {
        poetAdapter = new PoetAdapter(getActivity(), getContext(), mFragmentNavigation);
        recyclerView.setAdapter(poetAdapter);
        recyclerView.setItemViewCacheSize(50);
        recyclerView.setItemAnimator(new ItemAnimator());
    }

    private void setUpRecyclerView(ArrayList<Sair> sairArrayList) {

        poetAdapter.addAll(sairArrayList);

    }

    @Override
    public void onClick(View view) {

        if (view == imgBack) {
            getActivity().onBackPressed();
        }

    }

    private class AsyncInsertData extends AsyncTask<Void, Void, Void> {

        ArrayList<Sair> sairArrayList;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {

            Log.d("onInBackground()", "Data Inserting ");

            SairDataSource sairDataSource = new SairDataSource(getContext());
            sairArrayList = sairDataSource.getSairList();

            Collections.sort(sairArrayList, new Sortbyroll());

            return null;
        }

        class Sortbyroll implements Comparator<Sair> {
            // Used for sorting in ascending order of
            // roll number
            public int compare(Sair a, Sair b) {
                return a.getAd().compareTo(b.getAd());
            }
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
                    setUpRecyclerView(sairArrayList);
                }
            });

        }
    }

}
