package com.uren.siirler.MainFragments.TabHome.Adapters;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uren.siirler.MainFragments.BaseFragment;
import com.uren.siirler.MainFragments.TabHome.Interfaces.PoemFeaturesCallback;
import com.uren.siirler.MainFragments.TabHome.JavaClasses.PoemHelper;
import com.uren.siirler.R;
import com.uren.siirler.Utils.UserDataUtil;
import com.uren.siirler._model.Sair;
import com.uren.siirler._model.Siir;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.uren.siirler.Constants.NumericConstants.COMING_FROM_NORMAL_POEM;

public class PoemAdapter extends RecyclerView.Adapter {

    private Activity mActivity;
    private Context mContext;
    private BaseFragment.FragmentNavigation fragmentNavigation;
    private List<Object> objectList;
    private List<Siir> siirList;

    public PoemAdapter(Activity activity, Context context,
                       BaseFragment.FragmentNavigation fragmentNavigation) {
        this.mActivity = activity;
        this.mContext = context;
        this.fragmentNavigation = fragmentNavigation;
        this.objectList = new ArrayList<Object>();
        this.siirList = new ArrayList<Siir>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_poem, parent, false);

        RecyclerView.ViewHolder viewHolder = new PoemAdapter.MyViewHolder(itemView);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List payloads) {
        super.onBindViewHolder(holder, position, payloads);

        if (payloads.isEmpty()) {
            // Perform a full update
            onBindViewHolder(holder, position);
        } else {
            // Perform a partial update
            for (Object payload : payloads) {
            }
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Siir siir = (Siir) objectList.get(position);
        ((PoemAdapter.MyViewHolder) holder).setData(siir, position);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        View mView;
        TextView txtSiir;
        RelativeLayout llRvRow;

        private int position;
        private Siir siir;

        public MyViewHolder(View view) {
            super(view);

            try {
                mView = view;
                txtSiir = (TextView) view.findViewById(R.id.txtSiir);
                llRvRow = (RelativeLayout) view.findViewById(R.id.llRvRow);

                setListeners();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void setListeners() {

            llRvRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setSinglePoemFragmentItems();
                }
            });

        }


        public void setData(Siir siir, int position) {

            this.position = position;
            this.siir = siir;

            //Siir
            if (siir.getAd() != null && !siir.getAd().isEmpty()) {
                this.txtSiir.setText(siir.getAd());
            }

        }

        private void setSinglePoemFragmentItems() {

            PoemHelper.SinglePoemClicked singlePoemClicked = PoemHelper.SinglePoemClicked.getInstance();
            singlePoemClicked.setSinglePoemItems(mContext, fragmentNavigation, siir.getId(), position, COMING_FROM_NORMAL_POEM);
            singlePoemClicked.setPoemFeaturesCallback(new PoemFeaturesCallback() {
                @Override
                public void onPoemFavoriteClicked(boolean isFavorite, int position) {

                }
            });

            singlePoemClicked.startSinglePoemProcess();
        }

    }

    /********************************************************************************/

    public void addAll(ArrayList<Siir> arrayListSiir) {
        try {
            if (arrayListSiir != null) {
                siirList = arrayListSiir;
                objectList.addAll(arrayListSiir);
                notifyItemRangeInserted(0, arrayListSiir.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /******************************************************************************/
    public void updateItems() {
        /**/
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (objectList != null ? objectList.size() : 0);
    }


}