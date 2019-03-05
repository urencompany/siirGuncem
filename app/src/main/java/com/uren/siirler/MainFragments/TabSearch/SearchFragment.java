package com.uren.siirler.MainFragments.TabSearch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.uren.siirler.MainActivity;
import com.uren.siirler.MainFragments.BaseFragment;
import com.uren.siirler.MainFragments.TabPoets.Adapters.PoetAdapter;
import com.uren.siirler.MainFragments.TabPoets.PoetsFragment;
import com.uren.siirler.MainFragments.TabSearch.Adapters.SearchResultAdapter;
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
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

import static com.uren.siirler.Constants.StringConstants.ANIMATE_LEFT_TO_RIGHT;


public class SearchFragment extends BaseFragment
        implements View.OnClickListener {

    View mView;
    SearchResultAdapter searchResultAdapter;
    LinearLayoutManager mLayoutManager;

    String searchText = "";
    String tempSearchText = "";

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.imgCancel)
    ClickableImageView imgCancel;
    @BindView(R.id.imgBack)
    ClickableImageView imgBack;

    @BindView(R.id.edtSearch)
    EditText edtSearch;
    @BindView(R.id.txtResult)
    TextView txtResult;
    @BindView(R.id.llContent)
    LinearLayout llContent;
    @BindView(R.id.adView)
    AdView adView;

    private Timer timer;

    public SearchFragment() {
    }

    public static SearchFragment newInstance() {
        Bundle args = new Bundle();
        SearchFragment fragment = new SearchFragment();
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
            mView = inflater.inflate(R.layout.fragment_main_search, container, false);
            ButterKnife.bind(this, mView);


            edtSearch.requestFocus();
            showKeyboard(true);
            showResultView(true, getString(R.string.searchDetail));
            initListeners();
            initRecyclerView();

        }

        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initListeners() {

        MobileAds.initialize(getContext(), getActivity().getResources().getString(R.string.ADMOB_APP_ID));
        AdMobUtils.loadBannerAd(adView);

        imgCancel.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        edtSearch.setOnClickListener(this);
        llContent.setOnClickListener(this);

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edtSearch.clearFocus();
                showKeyboard(false);
                imgCancel.setVisibility(View.GONE);
                return false;
            }

        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // user is typing: reset already started timer (if existing)
                if (timer != null) {
                    timer.cancel();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

                // user typed: start the timer
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {

                        try {
                            // do your actual work here
                            tempSearchText = edtSearch.getText().toString();

                            if (tempSearchText.matches("")) {

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        searchText = tempSearchText;
                                        searchResultAdapter.clearList();
                                        showResultView(true, getString(R.string.search));
                                        imgCancel.setVisibility(View.GONE);
                                    }
                                });

                                return;
                            }

                            if (!tempSearchText.matches(searchText)) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        searchText = tempSearchText;
                                        imgCancel.setVisibility(View.VISIBLE);
                                        getSearchResult();
                                    }
                                });

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 800);

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
        searchResultAdapter = new SearchResultAdapter(getActivity(), getContext(), mFragmentNavigation);
        recyclerView.setAdapter(searchResultAdapter);
        recyclerView.setItemViewCacheSize(50);
        recyclerView.setItemAnimator(new ItemAnimator());
    }

    private void showKeyboard(boolean showKeyboard) {

        try {
            if (showKeyboard) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            } else {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtSearch.getWindowToken(), 0);
                edtSearch.setFocusable(false);
                edtSearch.setFocusableInTouchMode(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showResultView(boolean isShowResult, @Nullable String msg) {
        try {
            if (isShowResult) {
                if (msg != null) {
                    txtResult.setText(msg);
                    txtResult.setVisibility(View.VISIBLE);
                }
            } else {
                txtResult.setText("");
                txtResult.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {

        if (view == imgCancel) {
            showKeyboard(false);
            imgCancel.setVisibility(View.GONE);
            //getActivity().onBackPressed();
        }

        if (view == recyclerView) {
            showKeyboard(false);
        }

        if (view == edtSearch) {
            edtSearch.requestFocus();
            imgCancel.setVisibility(View.VISIBLE);
            showKeyboard(true);
        }

        if (view == imgBack) {
            getActivity().onBackPressed();
        }

        if (view == llContent) {
            edtSearch.clearFocus();
            showKeyboard(false);
            imgCancel.setVisibility(View.GONE);
        }

    }

    public void getSearchResult() {
        new AsyncInsertData().execute();
    }


    private class AsyncInsertData extends AsyncTask<Void, Void, Void> {

        ArrayList<Sair> sairArrayList;
        ArrayList<Siir> siirArrayList;
        ArrayList<Siir> siirWithDetailArrayList;

        @Override
        protected void onPreExecute() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //searchResultAdapter.addProgressLoading();
                }
            });
        }

        @Override
        protected Void doInBackground(Void... params) {

            getSairList();
            getSiirList();
            getSiirWithDetailList();

            return null;
        }

        private void getSairList() {
            SairDataSource sairDataSource = new SairDataSource(getContext());
            sairArrayList = sairDataSource.getSairList();
        }

        private void getSiirList() {
            SiirDataSource siirDataSource = new SiirDataSource(getContext());
            siirArrayList = siirDataSource.getFilteredSiirList(searchText);
        }

        private void getSiirWithDetailList() {
            SiirDataSource siirDataSource = new SiirDataSource(getContext());
            siirWithDetailArrayList = siirDataSource.getFilteredDetailSiirList(searchText);
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.d("MainActivity", "Data Inserted ");


            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //searchResultAdapter.removeProgressLoading();
                    setUpRecyclerView(sairArrayList, siirArrayList, siirWithDetailArrayList);
                }
            });

        }
    }

    private void setUpRecyclerView(ArrayList<Sair> sairArrayList, ArrayList<Siir> siirArrayList, ArrayList<Siir> siirWithDetailArrayList) {

        searchResultAdapter.clearList();

        if (siirArrayList.size() == 0 && siirWithDetailArrayList.size() == 0) {
            showResultView(true, getString(R.string.THERE_IS_NO_SEARCH_RESULT));

            return;
        } else {
            showResultView(false, null);

            for (int i = 0; i < siirArrayList.size(); i++) {
                siirArrayList.get(i).setConsistsDetail(false);
            }

            for (int i = 0; i < siirWithDetailArrayList.size(); i++) {
                siirWithDetailArrayList.get(i).setConsistsDetail(true);
                siirArrayList.add(siirWithDetailArrayList.get(i));
            }

            searchResultAdapter.setSearchText(searchText);
            searchResultAdapter.addSairList(sairArrayList);
            searchResultAdapter.addMatchingSiirList(siirArrayList);
            //searchResultAdapter.addDetailMatchingSiirList(siirWithDetailArrayList);

        }

    }

    @Override
    public void onDestroyView() {
        showKeyboard(false);
        super.onDestroyView();
    }
}