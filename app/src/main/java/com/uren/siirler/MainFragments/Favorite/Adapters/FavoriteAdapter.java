package com.uren.siirler.MainFragments.Favorite.Adapters;


import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
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
import com.uren.siirler.MainFragments.BaseFragment;
import com.uren.siirler.MainFragments.TabHome.Interfaces.PoemFeaturesCallback;
import com.uren.siirler.MainFragments.TabHome.JavaClasses.PoemHelper;
import com.uren.siirler.MainFragments.TabHome.SubFragments.PoetProfileFragment;
import com.uren.siirler.MainFragments.TabHome.SubFragments.SinglePoemFragment;
import com.uren.siirler.R;
import com.uren.siirler.Utils.UserDataUtil;
import com.uren.siirler._model.Sair;
import com.uren.siirler._model.Siir;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.uren.siirler.Constants.NumericConstants.COMING_FROM_NORMAL_POEM;
import static com.uren.siirler.Constants.StringConstants.ANIMATE_RIGHT_TO_LEFT;
import static com.uren.siirler.Constants.StringConstants.USERNAME_FONT_TYPE;

public class FavoriteAdapter extends RecyclerView.Adapter {

    private Activity mActivity;
    private Context mContext;
    private BaseFragment.FragmentNavigation fragmentNavigation;
    private List<Object> objectList;
    private List<Sair> sairList;
    private List<Siir> siirList;
    private HashMap<Integer, Sair> sairHashmap;

    public FavoriteAdapter(Activity activity, Context context,
                           BaseFragment.FragmentNavigation fragmentNavigation) {
        this.mActivity = activity;
        this.mContext = context;
        this.fragmentNavigation = fragmentNavigation;
        this.objectList = new ArrayList<Object>();
        this.sairList = new ArrayList<Sair>();
        this.siirList = new ArrayList<Siir>();
        this.sairHashmap = new HashMap<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_favorite, parent, false);

        RecyclerView.ViewHolder viewHolder = new FavoriteAdapter.MyViewHolder(itemView);

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

        try {
            Siir siir = (Siir) objectList.get(position);
            ((MyViewHolder) holder).setData(siir, position);
        } catch (UnsupportedEncodingException e) {
            Log.e("error", e.toString());
        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        View mView;
        ImageView imgProfilePic;
        TextView txtProfilePic;
        TextView txtSair;
        TextView txtBaslik;
        TextView txtSiir;
        ImageView imgFavorite;
        RelativeLayout llRvRow;

        private int position;
        private Siir siir;
        boolean isFavorite = false;

        public MyViewHolder(View view) {
            super(view);

            try {
                mView = view;
                imgProfilePic = (ImageView) view.findViewById(R.id.imgProfilePic);
                txtProfilePic = (TextView) view.findViewById(R.id.txtProfilePic);
                txtSair = (TextView) view.findViewById(R.id.txtSair);
                txtBaslik = (TextView) view.findViewById(R.id.txtBaslik);
                txtSiir = (TextView) view.findViewById(R.id.txtSiir);
                imgFavorite = (ImageView) view.findViewById(R.id.imgFavorite);
                llRvRow = (RelativeLayout) view.findViewById(R.id.llRvRow);

                setListeners();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void setListeners() {

            //imgFavorite
            imgFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    imgFavorite.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.image_click));

                    if (siir.isFavorite()) {
                        isFavorite = false;
                        siir.setFavorite(false);
                        setLikeIconUI(R.color.black, R.drawable.icon_star_emtpy, true);
                    } else {
                        isFavorite = true;
                        siir.setFavorite(true);
                        setLikeIconUI(R.color.likeButtonColor, R.drawable.icon_star_full, true);
                    }

                    PoemHelper.FavoriteClicked.startProcess(mContext, siir.getId(), isFavorite);

                }

            });


            llRvRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setSinglePoemFragmentItems();
                }
            });

        }


        public void setData(Siir siir, int position) throws UnsupportedEncodingException {

            this.position = position;
            this.siir = siir;
            this.isFavorite = siir.isFavorite();

            Sair sair = sairHashmap.get(siir.getSairId());
            int colorCode = mContext.getResources().getColor(R.color.Black);
            UserDataUtil.setProfilePictureRectangle(mContext, sairHashmap.get(siir.getSairId()), txtProfilePic, imgProfilePic, colorCode);
            imgProfilePic.setPadding(1, 1, 1, 1);//Name
            if (sair.getAd() != null && !sair.getAd().isEmpty()) {
                this.txtSair.setText(sair.getAd());
                txtSair.setTypeface(Typeface.createFromAsset(mContext.getAssets(), USERNAME_FONT_TYPE));
            }

            //Siir
            if (siir.getAd() != null && !siir.getAd().isEmpty()) {
                this.txtBaslik.setText(siir.getAd());
            }

            if (siir.getMetin() != null) {

                int length = siir.getMetin().length;
                if(length<100){
                    String response = new String(siir.getMetin(), "UTF-8");
                    txtSiir.setText(response);
                }else{
                    byte[] result = new byte[100];
                    for (int i = 0; i < 100; i++) {
                        result[i] = siir.getMetin()[i];
                    }

                    String response = new String(result, "UTF-8");
                    txtSiir.setText(response + "...");
                }

            }

            txtBaslik.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonttypes/Sansation_Regular.ttf"));
            txtSiir.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonttypes/Sansation_Regular.ttf"));

            //Like
            if (siir.isFavorite()) {
                setLikeIconUI(R.color.likeButtonColor, R.drawable.icon_star_full, false);
            } else {
                setLikeIconUI(R.color.black, R.drawable.icon_star_emtpy, false);
            }

        }

        private void setLikeIconUI(int color, int icon, boolean isClientOperation) {
            //imgFavorite.setColorFilter(ContextCompat.getColor(mContext, color), android.graphics.PorterDuff.Mode.SRC_IN);

            Glide.with(mContext)
                    .load(icon)
                    .into(imgFavorite);

        }

        private void setSinglePoemFragmentItems() {

            PoemHelper.SinglePoemClicked singlePoemClicked = PoemHelper.SinglePoemClicked.getInstance();
            singlePoemClicked.setSinglePoemItems(mContext, fragmentNavigation, siir.getId(), position, COMING_FROM_NORMAL_POEM);
            singlePoemClicked.setPoemFeaturesCallback(new PoemFeaturesCallback() {
                @Override
                public void onPoemFavoriteClicked(boolean isFavorite, int position) {
                    ((Siir) objectList.get(position)).setFavorite(isFavorite);
                    notifyItemChanged(position);
                }
            });

            singlePoemClicked.startSinglePoemProcess();
        }


    }

    /********************************************************************************/

    public void addAll(ArrayList<Sair> arrayListSair, ArrayList<Siir> siirArrayList) {
        try {
            if (arrayListSair != null && siirArrayList != null) {
                sairList = arrayListSair;
                setSairHashMap();
                siirList = siirArrayList;
                objectList.addAll(siirArrayList);
                notifyItemRangeInserted(0, siirArrayList.size());
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