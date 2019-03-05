package com.uren.siirler.MainFragments.TabHome;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.uren.siirler.MainActivity;
import com.uren.siirler.MainFragments.BaseFragment;
import com.uren.siirler.MainFragments.Favorite.FavoriteFragment;
import com.uren.siirler.MainFragments.TabHome.Adapters.FeedAdapter;
import com.uren.siirler.MainFragments.RecordManagement.RecordingsFragment;
import com.uren.siirler.MainFragments.TabPoets.PoetsFragment;
import com.uren.siirler.MainFragments.TabSearch.SearchFragment;
import com.uren.siirler.R;
import com.uren.siirler.Utils.ClickableImage.ClickableImageView;
import com.uren.siirler.Utils.ItemAnimator;
import com.uren.siirler._database.datasource.ConfigDataSource;
import com.uren.siirler._database.datasource.SairDataSource;
import com.uren.siirler._database.datasource.SiirDataSource;
import com.uren.siirler._model.Config;
import com.uren.siirler._model.Sair;
import com.uren.siirler._model.Siir;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.uren.siirler.Constants.NumericConstants.ITEM_PER_AD;
import static com.uren.siirler.Constants.NumericConstants.NUMBER_OF_HORIZONTAL_POEMS;
import static com.uren.siirler.Constants.NumericConstants.NUMBER_OF_VERTICAL_POEMS;
import static com.uren.siirler.Constants.StringConstants.CONFIG_KEY_DATE;
import static com.uren.siirler.Constants.StringConstants.TOOLBAR_FONT_TYPE;

public class HomeFragment extends BaseFragment
        implements View.OnClickListener {

    View mView;
    private LinearLayoutManager mLayoutManager;
    private FeedAdapter feedAdapter;
    boolean mDrawerState;

    @BindView(R.id.toolbarLayout)
    Toolbar toolbarLayout;
    @BindView(R.id.txtToolbarTitle)
    TextView txtToolbarTitle;
    @BindView(R.id.imgLeft)
    ClickableImageView imgMenu;
    @BindView(R.id.imgRight)
    ImageView imgOptions;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navViewLayout)
    NavigationView navViewLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        ((MainActivity) getActivity()).showTabLayout(true);
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_main_home, container, false);
            ButterKnife.bind(this, mView);

            //Menu Layout
            setNavViewItems();
            setDrawerListeners();

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
        txtToolbarTitle.setText(getString(R.string.poems));
        txtToolbarTitle.setTextColor(getResources().getColor(R.color.black));
        txtToolbarTitle.setTypeface(Typeface.createFromAsset(getContext().getAssets(), TOOLBAR_FONT_TYPE), Typeface.BOLD);

        txtToolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.smoothScrollToPosition(0);
            }
        });

    }

    private void init() {
        MobileAds.initialize(getContext(), getActivity().getResources().getString(R.string.ADMOB_APP_ID));
        //AdMobUtils.loadBannerAd(adView);
        imgMenu.setVisibility(View.VISIBLE);
        imgOptions.setVisibility(View.VISIBLE);
        imgOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mFragmentNavigation.pushFragment(RecordAudioFragment.newInstance());
            }
        });
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
        feedAdapter = new FeedAdapter(getActivity(), getContext(), mFragmentNavigation);
        recyclerView.setAdapter(feedAdapter);
        recyclerView.setItemViewCacheSize(50);
        recyclerView.setItemAnimator(new ItemAnimator());
    }

    private void setUpRecyclerView(ArrayList<Siir> siirArrayList, ArrayList<Siir> popularSiirList, ArrayList<Sair> sairArrayList) {

        feedAdapter.addHeader(popularSiirList, sairArrayList);

        ArrayList<Object> bannerAds = getBannerAds(siirArrayList.size());
        feedAdapter.addVerticalItem(siirArrayList, bannerAds, sairArrayList);

    }

    private ArrayList<Object> getBannerAds(int size) {

        ArrayList<Object> objectList = new ArrayList<>();

        for (int i = 0; i < size; i += ITEM_PER_AD) {
            final AdView adView = new AdView(getContext());
            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId(getString(R.string.ADMOB_BANNER_AD_UNIT_ID));
            //adView.loadAd(new AdRequest.Builder().build());
            objectList.add(adView);
        }

        return objectList;


    }

    @Override
    public void onClick(View view) {
    }

    long startTime, endTime, duration;

    private class AsyncInsertData extends AsyncTask<Void, Void, Void> {

        ArrayList<Config> configArrayList;
        ArrayList<Siir> siirArrayList;
        ArrayList<Sair> sairArrayList;
        ArrayList<Sair> displayedSairList;
        ArrayList<Sair> popularSairList;
        ArrayList<Siir> popularSiirList;
        ArrayList<Siir> resultSiirList;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {


            //Tüm şiirler
            setStartTime();
            SiirDataSource siirDataSource = new SiirDataSource(getContext());
            siirArrayList = siirDataSource.getSiirList();
            setEndTime();
            Log.i("tumSiirler", String.valueOf(duration));

            //Tüm şairler
            setStartTime();
            SairDataSource sairDataSource = new SairDataSource(getContext());
            sairArrayList = sairDataSource.getSairList();
            setEndTime();
            Log.i("tumSairler", String.valueOf(duration));

            //Config
            setStartTime();
            ConfigDataSource configDataSource = new ConfigDataSource(getContext());
            configArrayList = configDataSource.getConfigList();
            setEndTime();
            Log.i("Config", String.valueOf(duration));

            boolean isCurrentDate = checkIsCurrentDate(configArrayList);

            if (isCurrentDate) {
                setStartTime();
                //isaretlenen popüler siirleri al.
                popularSiirList = siirDataSource.getDailySiirList();
                setEndTime();
                Log.i("Populer siirler", String.valueOf(duration));
            } else {
                //Populer sairler
                setStartTime();
                popularSairList = sairDataSource.getPopularSairList();
                setEndTime();
                Log.i("Populer sairler", String.valueOf(duration));

                //Populer sairlerden random birer siir alındı
                setStartTime();
                popularSiirList = new ArrayList<>();
                for (int i = 0; i < NUMBER_OF_HORIZONTAL_POEMS; i++) {

                    Siir siir = siirDataSource.getRandomSiirFromSair(popularSairList.get(i).getId());
                    popularSiirList.add(siir);
                }
                setEndTime();
                Log.i("random birer siir", String.valueOf(duration));

                siirDataSource.updateDailySiirList(popularSiirList);
                configDataSource.updateConfigDate();
            }

            //Görüntülenen şairler
            setStartTime();
            displayedSairList = sairDataSource.getDisplayedSairList(true);
            setEndTime();
            Log.i("Görüntülenen şairler", String.valueOf(duration));

            //Görüntülenen şairlerden random birer şiir alındı
            setStartTime();
            resultSiirList = new ArrayList<>();
            for (int i = 0; i < displayedSairList.size(); i++) {
                Siir siir = siirDataSource.getRandomSiirFromSair(displayedSairList.get(i).getId());
                resultSiirList.add(siir);
            }
            setEndTime();
            Log.i("G. şairlerden random", String.valueOf(duration));

            //Liste dolmadıysa normal şiirlerden random şiirler eklendi
            setStartTime();
            Collections.shuffle(siirArrayList);
            int availableItems = resultSiirList.size();
            for (int i = 0; i < NUMBER_OF_VERTICAL_POEMS - availableItems; i++) {
                resultSiirList.add(siirArrayList.get(i));
            }
            setEndTime();
            Log.i("Liste dolmadıysa", String.valueOf(duration));

            return null;
        }


        private void setStartTime() {
            startTime = System.currentTimeMillis();
        }

        private void setEndTime() {
            endTime = System.currentTimeMillis();
            duration = (endTime - startTime);
        }


        private boolean checkIsCurrentDate(ArrayList<Config> configArrayList) {

            for (int i = 0; i < configArrayList.size(); i++) {

                if (configArrayList.get(i).getKey().equals(CONFIG_KEY_DATE)) {
                    Date current = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                    String formattedDate = df.format(current);

                    if (formattedDate.equals(configArrayList.get(i).getValue())) {
                        //aynı gündeyiz.
                        return true;
                    } else {
                        return false;
                    }

                }

            }

            return false;
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
                    setUpRecyclerView(resultSiirList, popularSiirList, sairArrayList);
                }
            });

        }
    }


    /************************************************************************************/
    private void setNavViewItems() {

    }

    public void setDrawerListeners() {
        drawerLayout.addDrawerListener(new ActionBarDrawerToggle(getActivity(),
                drawerLayout,
                null,
                0,
                0) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                mDrawerState = false;
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mDrawerState = true;
            }
        });

        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgMenu.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.image_click));

                if (mDrawerState) {
                    drawerLayout.closeDrawer(Gravity.START);
                } else {
                    drawerLayout.openDrawer(Gravity.START);
                }
            }
        });

        navViewLayout.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.searchItem:
                        drawerLayout.closeDrawer(Gravity.START);
                        startSearchFragment();
                        break;

                    case R.id.allPoets:
                        drawerLayout.closeDrawer(Gravity.START);
                        startPoetsFragment();
                        break;

                    case R.id.myFavorites:
                        drawerLayout.closeDrawer(Gravity.START);
                        startFavoritesFragment();
                        break;
                    case R.id.myRecordings:
                        drawerLayout.closeDrawer(Gravity.START);
                        startRecordingsFragment();
                        break;
                    case R.id.txtRateApp:
                        drawerLayout.closeDrawer(Gravity.START);
                        startRateProcess();
                        break;
                    default:
                        break;
                }

                return false;
            }

        });

    }


    private void startSearchFragment() {
        mFragmentNavigation.pushFragment(SearchFragment.newInstance());
    }

    private void startPoetsFragment() {
        mFragmentNavigation.pushFragment(PoetsFragment.newInstance());
    }

    private void startFavoritesFragment() {
        mFragmentNavigation.pushFragment(FavoriteFragment.newInstance());
    }

    private void startRecordingsFragment() {
        mFragmentNavigation.pushFragment(RecordingsFragment.newInstance());
    }

    private void startRateProcess() {
        Uri uri = Uri.parse("market://details?id=" + getContext().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getContext().getPackageName())));
        }
    }

}
