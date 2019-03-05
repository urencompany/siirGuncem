package com.uren.siirler;

import android.graphics.PorterDuff;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uren.siirler.FragmentControllers.FragNavController;
import com.uren.siirler.FragmentControllers.FragNavTransactionOptions;
import com.uren.siirler.FragmentControllers.FragmentHistory;
import com.uren.siirler.MainFragments.BaseFragment;
import com.uren.siirler.MainFragments.TabPoets.PoetsFragment;
import com.uren.siirler.MainFragments.TabHome.HomeFragment;
import com.uren.siirler.MainFragments.TabSearch.SearchFragment;
import com.uren.siirler.Utils.Utils;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.uren.siirler.Constants.StringConstants.ANIMATE_DOWN_TO_UP;
import static com.uren.siirler.Constants.StringConstants.ANIMATE_LEFT_TO_RIGHT;
import static com.uren.siirler.Constants.StringConstants.ANIMATE_RIGHT_TO_LEFT;
import static com.uren.siirler.Constants.StringConstants.ANIMATE_UP_TO_DOWN;

public class MainActivity extends FragmentActivity
        implements BaseFragment.FragmentNavigation,
        FragNavController.TransactionListener,
        FragNavController.RootFragmentListener {

    @BindView(R.id.content_frame)
    FrameLayout contentFrame;
    @BindArray(R.array.tab_name)
    String[] TABS;
    @BindView(R.id.bottom_tab_layout)
    TabLayout bottomTabLayout;
    @BindView(R.id.tabMainLayout)
    LinearLayout tabMainLayout;


    private int[] mTabIconsSelected = {
            //R.drawable.tab_quran,
            R.drawable.tab_quran
            //R.drawable.tab_quran
    };

    private FragNavController mNavController;
    private FragmentHistory fragmentHistory;
    private TextView tabDescription;

    private int initialTabIndex = 0;
    private int selectedTabColor, unSelectedTabColor;
    public static String PACKAGE_NAME;

    public String ANIMATION_TAG;
    public FragNavTransactionOptions transactionOptions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        PACKAGE_NAME = getApplicationContext().getPackageName();
        unSelectedTabColor = this.getResources().getColor(R.color.DarkGray);
        selectedTabColor = this.getResources().getColor(R.color.fab_color_pressed);

        initToolbar();
        initTab();
        showTabLayout(false);

        fragmentHistory = new FragmentHistory();

        mNavController = FragNavController.newBuilder(savedInstanceState, getSupportFragmentManager(), R.id.content_frame)
                .transactionListener(this)
                .rootFragmentListener(this, TABS.length)
                .selectedTabIndex(FragNavController.TAB1)
                .build();

        bottomTabLayout.getTabAt(initialTabIndex).select();
        switchTab(initialTabIndex);
        updateTabSelection(initialTabIndex);

        bottomTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabSelectionControl(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                mNavController.clearStack();
                tabSelectionControl(tab);
            }
        });


    }

    private void initToolbar() {
        /*
        setSupportActionBar(toolbar);
        */
    }

    public void tabSelectionControl(TabLayout.Tab tab) {
        fragmentHistory.push(tab.getPosition());
        switchAndUpdateTabSelection(tab.getPosition());
    }

    private void initTab() {
        if (bottomTabLayout != null) {
            for (int i = 0; i < TABS.length; i++) {
                bottomTabLayout.addTab(bottomTabLayout.newTab());
                TabLayout.Tab tab = bottomTabLayout.getTabAt(i);

                if (tab != null) {
                    tab.setIcon(mTabIconsSelected[i]);
                    tab.setText(TABS[i]);
                }
/*
                if (tab != null)
                    tab.setCustomView(getTabView(i));*/
            }
            bottomTabLayout.getTabAt(initialTabIndex).getIcon().setColorFilter(selectedTabColor, PorterDuff.Mode.SRC_IN);
            //bottomTabLayout.setTabTextColors(unSelectedTabColor, selectedTabColor);

        }

    }


    private View getTabView(int position) {
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.tab_item_bottom, null);
        ImageView icon = (ImageView) view.findViewById(R.id.tab_icon);
        icon.setImageDrawable(Utils.setDrawableSelector(MainActivity.this, mTabIconsSelected[position], mTabIconsSelected[position]));
        tabDescription = (TextView) view.findViewById(R.id.tabDesc);
        tabDescription.setText(TABS[position]);

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void switchTab(int position) {
        mNavController.switchTab(position);

        updateTab(position);
//      updateToolbarTitle(position);
    }

    private void updateTab(int position) {

        for (int i = 0; i < TABS.length; i++) {
            TabLayout.Tab selectedTab = bottomTabLayout.getTabAt(i);
/*
            View customView = selectedTab.getCustomView();
            TextView tabDescription = (TextView) customView.findViewById(R.id.tabDesc);
            ImageView icon = (ImageView) customView.findViewById(R.id.tab_icon);

            if (position != i) {
                icon.setColorFilter(ContextCompat.getColor(this, R.color.gray), android.graphics.PorterDuff.Mode.SRC_IN);
                tabDescription.setTextColor(getResources().getColor(R.color.gray));
            } else {
                icon.setColorFilter(ContextCompat.getColor(this, R.color.fab_color_pressed), android.graphics.PorterDuff.Mode.SRC_IN);
                tabDescription.setTextColor(getResources().getColor(R.color.fab_color_pressed));
            }

            */
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (!mNavController.isRootFragment()) {
            setTransactionOption();
            mNavController.popFragment(transactionOptions);
        } else {

            if (fragmentHistory.isEmpty()) {
                super.onBackPressed();
            } else {
                if (fragmentHistory.getStackSize() > 1) {
                    int position = fragmentHistory.popPrevious();
                    switchAndUpdateTabSelection(position);
                } else {
                    switchAndUpdateTabSelection(initialTabIndex);
                    fragmentHistory.emptyStack();
                }
            }
        }
    }

    public void switchAndUpdateTabSelection(int position) {
        switchTab(position);
        updateTabSelection(position);
    }

    private void updateTabSelection(int currentTab) {

        for (int i = 0; i < TABS.length; i++) {
            TabLayout.Tab selectedTab = bottomTabLayout.getTabAt(i);

            if (currentTab != i) {
                //selectedTab.getCustomView().setSelected(false);
                selectedTab.getIcon().setColorFilter(unSelectedTabColor, PorterDuff.Mode.SRC_IN);
            } else {
                //selectedTab.getCustomView().setSelected(true);
                selectedTab.getIcon().setColorFilter(selectedTabColor, PorterDuff.Mode.SRC_IN);
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mNavController != null) {
            mNavController.onSaveInstanceState(outState);
        }
    }

    @Override
    public void pushFragment(Fragment fragment) {
        if (mNavController != null) {
            mNavController.pushFragment(fragment);
        }
    }

    @Override
    public void pushFragment(Fragment fragment, String animationTag) {
        ANIMATION_TAG = animationTag;
        setTransactionOption();

        if (mNavController != null) {
            mNavController.pushFragment(fragment, transactionOptions);
        }
    }

    private void setTransactionOption() {
        if (transactionOptions == null) {
            transactionOptions = FragNavTransactionOptions.newBuilder().build();
        }

        if (ANIMATION_TAG != null) {
            switch (ANIMATION_TAG) {
                case ANIMATE_RIGHT_TO_LEFT:
                    transactionOptions.enterAnimation = R.anim.slide_from_right;
                    transactionOptions.exitAnimation = R.anim.slide_to_left;
                    transactionOptions.popEnterAnimation = R.anim.slide_from_left;
                    transactionOptions.popExitAnimation = R.anim.slide_to_right;
                    break;
                case ANIMATE_LEFT_TO_RIGHT:
                    transactionOptions.enterAnimation = R.anim.slide_from_left;
                    transactionOptions.exitAnimation = R.anim.slide_to_right;
                    transactionOptions.popEnterAnimation = R.anim.slide_from_right;
                    transactionOptions.popExitAnimation = R.anim.slide_to_left;
                    break;
                case ANIMATE_DOWN_TO_UP:
                    transactionOptions.enterAnimation = R.anim.slide_from_down;
                    transactionOptions.exitAnimation = R.anim.slide_to_up;
                    transactionOptions.popEnterAnimation = R.anim.slide_from_up;
                    transactionOptions.popExitAnimation = R.anim.slide_to_down;
                    break;
                case ANIMATE_UP_TO_DOWN:
                    transactionOptions.enterAnimation = R.anim.slide_from_up;
                    transactionOptions.exitAnimation = R.anim.slide_to_down;
                    transactionOptions.popEnterAnimation = R.anim.slide_from_down;
                    transactionOptions.popExitAnimation = R.anim.slide_to_up;
                    break;
                default:
                    transactionOptions = null;
            }
        } else
            transactionOptions = null;
    }

    @Override
    public void onTabTransaction(Fragment fragment, int index) {
        // If we have a backstack, show the back button
        /*
        if (getSupportActionBar() != null && mNavController != null) {
            updateToolbar();
        }
        */
    }

    private void updateToolbar() {
        /*
        getSupportActionBar().setDisplayHomeAsUpEnabled(!mNavController.isRootFragment());
        getSupportActionBar().setDisplayShowHomeEnabled(!mNavController.isRootFragment());
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        */
    }

    @Override
    public void onFragmentTransaction(Fragment fragment, FragNavController.TransactionType transactionType) {
        //do fragmentty stuff. Maybe change title, I'm not going to tell you how to live your life
        // If we have a backstack, show the back button
        /*
        if (getSupportActionBar() != null && mNavController != null) {
            updateToolbar();
        }
        */
    }

    @Override
    public Fragment getRootFragment(int index) {
        switch (index) {

            //case FragNavController.TAB1:
            //return new SearchFragment();
            case FragNavController.TAB1:
                return new HomeFragment();
            //case FragNavController.TAB3:
            //return new PoetsFragment();

        }
        throw new IllegalStateException("Need to send an index that we know");
    }

//    private void updateToolbarTitle(int position){
//        getSupportActionBar().setTitle(TABS[position]);
//    }

    public void updateToolbarTitle(String title) {
        /*
        getSupportActionBar().setTitle(title);
        */
    }

    public void reSelectCurrentTab() {
        TabLayout.Tab tabAt = bottomTabLayout.getTabAt(bottomTabLayout.getSelectedTabPosition());
        mNavController.clearStack();
        tabSelectionControl(tabAt);
    }

    public void showTabLayout(boolean isShow) {
        if(isShow){
            //tabMainLayout.setVisibility(View.VISIBLE);
        }else{
            tabMainLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        //SingletonSiirList.getInstance().reset();
        //SingletonSairList.getInstance().reset();
        super.onDestroy();
    }
}


