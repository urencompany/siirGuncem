package com.uren.siirler.MainFragments.TabPoets.Adapters;


import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uren.siirler.MainFragments.BaseFragment;
import com.uren.siirler.MainFragments.TabHome.Interfaces.PoemFeaturesCallback;
import com.uren.siirler.MainFragments.TabHome.JavaClasses.PoemHelper;
import com.uren.siirler.MainFragments.TabHome.SubFragments.PoetProfileFragment;
import com.uren.siirler.R;
import com.uren.siirler.Utils.UserDataUtil;
import com.uren.siirler._model.Sair;
import com.uren.siirler._model.Siir;

import java.util.ArrayList;
import java.util.List;

import static com.uren.siirler.Constants.StringConstants.ANIMATE_RIGHT_TO_LEFT;
import static com.uren.siirler.Constants.StringConstants.USERNAME_FONT_TYPE;

public class PoetAdapter extends RecyclerView.Adapter {

    private Activity mActivity;
    private Context mContext;
    private BaseFragment.FragmentNavigation fragmentNavigation;
    private List<Object> objectList;
    private List<Sair> sairList;

    public PoetAdapter(Activity activity, Context context,
                       BaseFragment.FragmentNavigation fragmentNavigation) {
        this.mActivity = activity;
        this.mContext = context;
        this.fragmentNavigation = fragmentNavigation;
        this.objectList = new ArrayList<Object>();
        this.sairList = new ArrayList<Sair>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_poet, parent, false);

        RecyclerView.ViewHolder viewHolder = new PoetAdapter.MyViewHolder(itemView);

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

        Sair sair = (Sair) objectList.get(position);
        ((PoetAdapter.MyViewHolder) holder).setData(sair, position);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        View mView;
        TextView txtSair;
        RelativeLayout llRvRow;
        ImageView imgProfilePic;
        TextView txtProfilePic;

        private int position;
        private Sair sair;

        public MyViewHolder(View view) {
            super(view);

            try {
                mView = view;
                txtSair = (TextView) view.findViewById(R.id.txtSair);
                llRvRow = (RelativeLayout) view.findViewById(R.id.llRvRow);
                imgProfilePic = (ImageView) view.findViewById(R.id.imgProfilePic);
                txtProfilePic = (TextView) view.findViewById(R.id.txtProfilePic);


                setListeners();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void setListeners() {

            llRvRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentNavigation.pushFragment(PoetProfileFragment.newInstance(sair.getId()), ANIMATE_RIGHT_TO_LEFT);
                }
            });

        }


        public void setData(Sair sair, int position) {

            this.position = position;
            this.sair = sair;

            //profile pic
            int colorCode = mContext.getResources().getColor(R.color.Black);
            UserDataUtil.setProfilePictureRectangle(mContext, sair, txtProfilePic, imgProfilePic, colorCode);
            imgProfilePic.setPadding(1, 1, 1, 1);//Name
            //sair ad
            if (sair.getAd() != null && !sair.getAd().isEmpty()) {
                this.txtSair.setText(sair.getAd());
                txtSair.setTypeface(Typeface.createFromAsset(mContext.getAssets(), USERNAME_FONT_TYPE));
            }


        }


    }

    /********************************************************************************/

    public void addAll(ArrayList<Sair> arrayListSair) {
        try {
            if (arrayListSair != null) {
                sairList = arrayListSair;
                objectList.addAll(arrayListSair);
                notifyItemRangeInserted(0, arrayListSair.size());
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