package com.uren.siirler.MainFragments.TabHome.Adapters;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uren.siirler.MainFragments.BaseFragment;
import com.uren.siirler.MainFragments.TabHome.Interfaces.PoemFeaturesCallback;
import com.uren.siirler.MainFragments.TabHome.JavaClasses.PoemHelper;
import com.uren.siirler.R;
import com.uren.siirler.Utils.UserDataUtil;
import com.uren.siirler.Utils.Utils;
import com.uren.siirler._database.datasource.SairDataSource;
import com.uren.siirler._model.Sair;
import com.uren.siirler._model.Siir;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.uren.siirler.Constants.NumericConstants.COMING_FROM_DAILY_POPULAR_POEM;
import static com.uren.siirler.Constants.NumericConstants.COMING_FROM_NORMAL_POEM;
import static com.uren.siirler.Constants.StringConstants.USERNAME_FONT_TYPE;

public class HorizontalPoetsAdapter extends RecyclerView.Adapter {


    private Activity mActivity;
    private Context mContext;
    private BaseFragment.FragmentNavigation fragmentNavigation;
    private List<Object> objectList;
    private List<Siir> headerSiirList;
    private List<Siir> verticalSiirList;
    private List<Sair> sairList;
    private HashMap<Integer, Sair> sairHashmap;

    public HorizontalPoetsAdapter(Activity activity, Context context,
                                  BaseFragment.FragmentNavigation fragmentNavigation, HashMap<Integer, Sair> sairHashmap) {
        this.mActivity = activity;
        this.mContext = context;
        this.fragmentNavigation = fragmentNavigation;
        this.objectList = new ArrayList<Object>();
        this.headerSiirList = new ArrayList<Siir>();
        this.verticalSiirList = new ArrayList<Siir>();
        this.sairList = new ArrayList<Sair>();
        this.sairHashmap = sairHashmap;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_horizontal_item, parent, false);

        RecyclerView.ViewHolder viewHolder = new MyViewHolder(itemView);

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
        ((MyViewHolder) holder).setData(siir, position);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        View mView;
        private int position;
        private Siir siir;

        ImageView imgProfilePic;
        TextView txtProfilePic;
        TextView txtUserName;
        LinearLayout llContent;

        public MyViewHolder(View view) {
            super(view);

            try {
                mView = view;
                imgProfilePic = (ImageView) view.findViewById(R.id.imgProfilePic);
                txtProfilePic = (TextView) view.findViewById(R.id.txtProfilePic);
                txtUserName = (TextView) view.findViewById(R.id.txtUserName);
                llContent = (LinearLayout) view.findViewById(R.id.llContent);

                setListeners();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void setListeners() {

            llContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setSinglePoemFragmentItems();
                    siir.setIsDailyClicked(true);
                    updateView();
                }


            });

        }


        public void setData(Siir siir, int position) {

            this.position = position;
            this.siir = siir;

            //profile picture
            SairDataSource sairDataSource = new SairDataSource(mContext);
            Sair sair = sairDataSource.getSair(siir.getSairId());

            if (siir.getIsDailyClicked()) {
                int colorCode = mContext.getResources().getColor(R.color.transparent);
                UserDataUtil.setProfilePictureWithStroke(mContext, sair, txtProfilePic, imgProfilePic, colorCode);
            } else {
                int colorCode = mContext.getResources().getColor(R.color.red);
                UserDataUtil.setProfilePictureWithStroke(mContext, sair, txtProfilePic, imgProfilePic, colorCode);
            }
            imgProfilePic.setPadding(3, 3, 3, 3);

            //Name
            if (sair.getAd() != null && !sair.getAd().isEmpty()) {
                this.txtUserName.setText(sair.getAd());
                txtUserName.setTypeface(Typeface.createFromAsset(mContext.getAssets(), USERNAME_FONT_TYPE));
            }

        }

        private void setSinglePoemFragmentItems() {

            PoemHelper.SinglePoemClicked singlePoemClicked = PoemHelper.SinglePoemClicked.getInstance();
            singlePoemClicked.setSinglePoemItems(mContext, fragmentNavigation, siir.getId(), position, COMING_FROM_DAILY_POPULAR_POEM);
            singlePoemClicked.setPoemFeaturesCallback(new PoemFeaturesCallback() {
                @Override
                public void onPoemFavoriteClicked(boolean isFavorite, int position) {

                }
            });

            singlePoemClicked.startSinglePoemProcess();
        }

        private void updateView() {
            //profile picture
            SairDataSource sairDataSource = new SairDataSource(mContext);
            Sair sair = sairDataSource.getSair(siir.getSairId());

            if (siir.getIsDailyClicked()) {
                int colorCode = mContext.getResources().getColor(R.color.transparent);
                UserDataUtil.setProfilePictureWithStroke(mContext, sair, txtProfilePic, imgProfilePic, colorCode);
            } else {
                int colorCode = mContext.getResources().getColor(R.color.orange);
                UserDataUtil.setProfilePictureWithStroke(mContext, sair, txtProfilePic, imgProfilePic, colorCode);
            }
            imgProfilePic.setPadding(3, 3, 3, 3);
        }

    }


    /********************************************************************************/


    public void addHeaderItems(ArrayList<Siir> arrayListSiir, ArrayList<Sair> arrayListSair) {
        try {
            if (arrayListSiir != null && arrayListSair != null) {
                headerSiirList = arrayListSiir;
                sairList = arrayListSair;
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