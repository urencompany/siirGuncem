package com.uren.siirler.MainFragments.TabHome.Adapters;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
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
import com.uren.siirler.Utils.Utils;
import com.uren.siirler._model.Sair;
import com.uren.siirler._model.Siir;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.uren.siirler.Constants.NumericConstants.COMING_FROM_NORMAL_POEM;

public class SinglePoemAdapter extends RecyclerView.Adapter {

    private Activity mActivity;
    private Context mContext;
    private BaseFragment.FragmentNavigation fragmentNavigation;
    private List<Object> objectList;
    private List<Siir> siirList;
    private List<Sair> sairList;
    private HashMap<Integer, Sair> sairHashmap;

    public SinglePoemAdapter(Activity activity, Context context,
                             BaseFragment.FragmentNavigation fragmentNavigation) {
        this.mActivity = activity;
        this.mContext = context;
        this.fragmentNavigation = fragmentNavigation;
        this.objectList = new ArrayList<Object>();
        this.siirList = new ArrayList<Siir>();
        this.sairList = new ArrayList<Sair>();
        this.sairHashmap = new HashMap<>();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_suggestion, parent, false);

        RecyclerView.ViewHolder viewHolder = new SinglePoemAdapter.MyViewHolder(itemView);

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
        ((SinglePoemAdapter.MyViewHolder) holder).setData(siir, position);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        View mView;
        ImageView imgProfilePic;
        TextView txtProfilePic;
        TextView txtSair;
        TextView txtSiir;
        RelativeLayout llRvRow;

        private int position;
        private Siir siir;

        public MyViewHolder(View view) {
            super(view);

            try {
                mView = view;
                imgProfilePic = (ImageView) view.findViewById(R.id.imgProfilePic);
                txtProfilePic = (TextView) view.findViewById(R.id.txtProfilePic);
                txtSair = (TextView) view.findViewById(R.id.txtSair);
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

            //profile picture
            Sair sair = sairHashmap.get(siir.getSairId());

            if (sair != null) {
                int colorCode = mContext.getResources().getColor(R.color.Black);
                UserDataUtil.setProfilePictureRectangle(mContext, sairHashmap.get(siir.getSairId()), txtProfilePic, imgProfilePic, colorCode);
                imgProfilePic.setPadding(1, 1, 1, 1);
                //Name
                if (sair.getAd() != null && !sair.getAd().isEmpty()) {
                    this.txtSair.setText(sair.getAd());
                }
                //Siir
                if (siir.getAd() != null && !siir.getAd().isEmpty()) {
                    this.txtSiir.setText(siir.getAd());
                }

            } else {
                Toast.makeText(mContext, "sair Null", Toast.LENGTH_SHORT).show();
                Log.i("sairIdNull", String.valueOf(siir.getSairId()));
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

    public void addAll(ArrayList<Siir> arrayListSiir, ArrayList<Sair> arrayListSair) {
        try {
            if (arrayListSiir != null && arrayListSair != null) {
                siirList = arrayListSiir;
                sairList = arrayListSair;
                setSairHashMap();
                objectList.addAll(arrayListSiir);
                notifyItemRangeInserted(0, arrayListSiir.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSairHashMap() {
        for (int i = 0; i < sairList.size(); i++) {
            sairHashmap.put(sairList.get(i).getId(), sairList.get(i));
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