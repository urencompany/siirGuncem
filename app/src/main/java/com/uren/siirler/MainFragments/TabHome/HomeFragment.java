package com.uren.siirler.MainFragments.TabHome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.uren.siirler.MainFragments.BaseFragment;
import com.uren.siirler.R;

import butterknife.ButterKnife;

public class HomeFragment extends BaseFragment {

    View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        getActivity().findViewById(R.id.tabMainLayout).setVisibility(View.VISIBLE);
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_main_home, container, false);
            ButterKnife.bind(this, mView);

        }

        return mView;
    }


}
