package com.uren.siirler.MainFragments.TabHome.Adapters;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdView;
import com.uren.siirler.MainFragments.BaseFragment;
import com.uren.siirler.MainFragments.RecordManagement.RecordAudioFragment;
import com.uren.siirler.MainFragments.TabHome.Interfaces.PoemFeaturesCallback;
import com.uren.siirler.MainFragments.TabHome.JavaClasses.PoemHelper;
import com.uren.siirler.MainFragments.ShareManagement.ShareFragment;
import com.uren.siirler.MainFragments.TabHome.SubFragments.PoetProfileFragment;
import com.uren.siirler.R;
import com.uren.siirler.Utils.AdMobUtil.AdMobUtils;
import com.uren.siirler.Utils.UserDataUtil;
import com.uren.siirler.Utils.Utils;
import com.uren.siirler._model.Sair;
import com.uren.siirler._model.Siir;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static com.uren.siirler.Constants.NumericConstants.COMING_FROM_NORMAL_POEM;
import static com.uren.siirler.Constants.NumericConstants.ITEM_PER_AD;
import static com.uren.siirler.Constants.NumericConstants.NUMBER_OF_HORIZONTAL_POEMS;
import static com.uren.siirler.Constants.StringConstants.ANIMATE_RIGHT_TO_LEFT;
import static com.uren.siirler.Constants.StringConstants.USERNAME_FONT_TYPE;

public class FeedAdapter extends RecyclerView.Adapter {

    public static final int VIEW_HEADER = 0;
    public static final int VIEW_ITEM = 1;
    public static final int VIEW_PROG = 2;
    public static final int VIEW_LAST_ITEM = 3;
    public static final int VIEW_BANNER_AD = 4;

    private Activity mActivity;
    private Context mContext;
    private BaseFragment.FragmentNavigation fragmentNavigation;
    private ArrayList<Object> objectList;
    private ArrayList<Siir> headerSiirList;
    private ArrayList<Siir> verticalSiirList;
    private ArrayList<Sair> sairList;
    private HashMap<Integer, Sair> sairHashmap;

    public FeedAdapter(Activity activity, Context context,
                       BaseFragment.FragmentNavigation fragmentNavigation) {
        this.mActivity = activity;
        this.mContext = context;
        this.fragmentNavigation = fragmentNavigation;
        this.objectList = new ArrayList<Object>();
        this.headerSiirList = new ArrayList<Siir>();
        this.verticalSiirList = new ArrayList<Siir>();
        this.sairList = new ArrayList<Sair>();
        this.sairHashmap = new HashMap<>();
    }

    @Override
    public int getItemViewType(int position) {

        if (position % ITEM_PER_AD == 0 && position != 0) {
            return VIEW_BANNER_AD;
        } else if (objectList.get(position) instanceof Boolean) {
            return VIEW_HEADER;
        } else if (objectList.get(position) instanceof Siir) {
            return VIEW_ITEM;
        } else if (objectList.get(position) instanceof ProgressBar) {
            return VIEW_PROG;
        } else if (objectList.get(position) instanceof String) {
            return VIEW_LAST_ITEM;
        } else {
            return VIEW_PROG;
        }

    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;

        if (viewType == VIEW_BANNER_AD) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.banner_ad_container, parent, false);

            viewHolder = new BannerAdViewHolder(itemView);
        } else if (viewType == VIEW_HEADER) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.feed_header_view, parent, false);

            viewHolder = new HeaderViewHolder(itemView);
        } else if (viewType == VIEW_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.feed_poem_view, parent, false);

            viewHolder = new PoemViewHolder(itemView);
        } else if (viewType == VIEW_LAST_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.feed_last_item_view, parent, false);

            viewHolder = new LastItemViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            viewHolder = new ProgressViewHolder(itemView);
        }

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
            if (holder instanceof BannerAdViewHolder) {
                AdView addView = (AdView) objectList.get(position);
                ((BannerAdViewHolder) holder).setData(addView, position);
            } else if (holder instanceof HeaderViewHolder) {
                boolean isHeaderView = (Boolean) objectList.get(position);
                ((HeaderViewHolder) holder).setData(isHeaderView, position);
            } else if (holder instanceof PoemViewHolder) {
                Siir siir = (Siir) objectList.get(position);
                ((PoemViewHolder) holder).setData(siir, position);
            } else if (holder instanceof LastItemViewHolder) {
                String s = (String) objectList.get(position);
                ((LastItemViewHolder) holder).setData(s, position);
            } else {
                ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        View mView;
        private int position;

        //View items
        RecyclerView horizontalRecyclerView;
        HorizontalPoetsAdapter horizontalPoetsAdapter;
        LinearLayoutManager mLayoutManager;


        public HeaderViewHolder(View view) {
            super(view);

            try {
                mView = view;
                horizontalRecyclerView = (RecyclerView) view.findViewById(R.id.horizontalRecyclerView);


                setLayoutManager();
                setAdapter();
                setHorizontalRecyclerView();

                setListeners();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void setLayoutManager() {
            mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            horizontalRecyclerView.setLayoutManager(mLayoutManager);
        }

        private void setAdapter() {
            horizontalPoetsAdapter = new HorizontalPoetsAdapter(mActivity, mContext, fragmentNavigation, sairHashmap);
            horizontalRecyclerView.setAdapter(horizontalPoetsAdapter);
            horizontalRecyclerView.setNestedScrollingEnabled(false);
            horizontalRecyclerView.setItemViewCacheSize(20);
        }

        private void setHorizontalRecyclerView() {
            horizontalPoetsAdapter.addHeaderItems(headerSiirList, sairList);

        }

        private void setListeners() {

        }


        public void setData(Boolean isHeaderView, int position) {

        }


    }

    public class PoemViewHolder extends RecyclerView.ViewHolder {

        View mView;
        ImageView imgProfilePic;
        TextView txtProfilePic;
        TextView txtUserName;
        CardView cardView;
        LinearLayout profileMainLayout;
        ImageView imgFavorite;
        LinearLayout llPoet;
        TextView txtBaslik;
        TextView txtDetail;
        TextView txtSeeMore;
        TextView txtSair;
        TextView txtShare;
        TextView txtRecord;
        ImageView imgShare, imgRecord;
        private int position;
        boolean isFavorite = false;
        boolean showFull = false;
        private int backgroundIndex, typeFaceIndex;
        Siir siir;

        ArrayList<Bitmap> bitmapArrayList;

        public PoemViewHolder(View view) {
            super(view);

            mView = view;
            cardView = (CardView) view.findViewById(R.id.card_view);
            profileMainLayout = (LinearLayout) view.findViewById(R.id.profileMainLayout);
            imgProfilePic = (ImageView) view.findViewById(R.id.imgProfilePic);
            txtProfilePic = (TextView) view.findViewById(R.id.txtProfilePic);
            txtUserName = (TextView) view.findViewById(R.id.txtUserName);
            imgFavorite = (ImageView) view.findViewById(R.id.imgFavorite);
            llPoet = (LinearLayout) view.findViewById(R.id.llPoet);
            txtDetail = (TextView) view.findViewById(R.id.txtDetail);
            txtBaslik = (TextView) view.findViewById(R.id.txtBaslik);
            txtSeeMore = (TextView) view.findViewById(R.id.txtSeeMore);
            txtSair = (TextView) view.findViewById(R.id.txtSair);
            txtShare = (TextView) view.findViewById(R.id.txtShare);
            txtRecord = (TextView) view.findViewById(R.id.txtRecord);
            imgShare = (ImageView) view.findViewById(R.id.imgShare);
            imgRecord = (ImageView) view.findViewById(R.id.imgRecord);

            setListeners();

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


            //Profile layout
            profileMainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragmentNavigation.pushFragment(PoetProfileFragment.newInstance(siir.getSairId()), ANIMATE_RIGHT_TO_LEFT);
                }
            });

            //Poet
            llPoet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSinglePoemFragmentItems();
                }
            });

            txtSeeMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String response = null;
                    try {
                        response = new String(siir.getMetin(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    txtDetail.setText(response);
                    txtSeeMore.setVisibility(View.GONE);
                    siir.setShowFull(true);
                }
            });

            txtShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentNavigation.pushFragment(ShareFragment.newInstance(siir.getId(), backgroundIndex, typeFaceIndex));
                }
            });

            txtRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentNavigation.pushFragment(RecordAudioFragment.newInstance(siir.getId(), backgroundIndex, typeFaceIndex));
                }
            });

            imgShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentNavigation.pushFragment(ShareFragment.newInstance(siir.getId(), backgroundIndex, typeFaceIndex));
                }
            });

            imgRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentNavigation.pushFragment(RecordAudioFragment.newInstance(siir.getId(), backgroundIndex, typeFaceIndex));
                }
            });

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

        public void setData(Siir siir, int position) throws UnsupportedEncodingException {

            this.position = position;
            this.siir = siir;
            this.isFavorite = siir.isFavorite();
            this.showFull = siir.isShowFull();

            //profile picture
            Sair sair = sairHashmap.get(siir.getSairId());

            int colorCode = mContext.getResources().getColor(R.color.Black);
            UserDataUtil.setProfilePictureRectangle(mContext, sairHashmap.get(siir.getSairId()), txtProfilePic, imgProfilePic, colorCode);
            imgProfilePic.setPadding(1, 1, 1, 1);//Name
            if (sair.getAd() != null && !sair.getAd().isEmpty()) {
                this.txtUserName.setText(sair.getAd());
                txtUserName.setTypeface(Typeface.createFromAsset(mContext.getAssets(), USERNAME_FONT_TYPE));
            }

            //Baslik
            txtBaslik.setText(siir.getAd());
            //Sair
            txtSair.setText(sair.getAd());

            //Text
            if (siir.getMetin() != null) {

                if (siir.getMetin().length < 1000 || showFull) {
                    txtSeeMore.setVisibility(View.GONE);

                    String response = new String(siir.getMetin(), "UTF-8");
                    txtDetail.setText(response);


                } else {

                    byte[] result = new byte[500];
                    for (int i = 0; i < 500; i++) {
                        result[i] = siir.getMetin()[i];
                    }

                    String response = new String(result, "UTF-8");
                    txtDetail.setText(response + "...");

                    txtSeeMore.setVisibility(View.VISIBLE);
                    txtSeeMore.setMovementMethod(LinkMovementMethod.getInstance());
                }

                Drawable[] randoomBackground = Utils.getBackgroundList(mContext);
                Random rand = new Random();
                int index = rand.nextInt(randoomBackground.length);
                backgroundIndex = index;
                llPoet.setBackground(randoomBackground[index]);


                Typeface[] typeFaceList = Utils.getTypeFaceList(mContext);
                index = rand.nextInt(typeFaceList.length);
                typeFaceIndex = index;
                txtBaslik.setTypeface(typeFaceList[index]);
                txtDetail.setTypeface(typeFaceList[index]);
                txtSair.setTypeface(typeFaceList[index]);

            } else {
                this.llPoet.setVisibility(View.GONE);
            }

            //Like
            if (siir.isFavorite()) {
                setLikeIconUI(R.color.likeButtonColor, R.drawable.icon_star_full, false);
            } else {
                setLikeIconUI(R.color.black, R.drawable.icon_star_emtpy, false);
            }

            //share
            Glide.with(mContext)
                    .load(R.drawable.icon_share2)
                    .into(imgShare);

            //record
            Glide.with(mContext)
                    .load(R.drawable.icon_microphone)
                    .into(imgRecord);


        }


        private void setLikeIconUI(int color, int icon, boolean isClientOperation) {
            //imgFavorite.setColorFilter(ContextCompat.getColor(mContext, color), android.graphics.PorterDuff.Mode.SRC_IN);

            Glide.with(mContext)
                    .load(icon)
                    .into(imgFavorite);

        }


    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBarLoading);
        }
    }

    public static class LastItemViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public LastItemViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.tvRv);
        }

        public void setData(String s, int position) {
            textView.setText(s);
            textView.setVisibility(View.GONE);
        }
    }

    public static class BannerAdViewHolder extends RecyclerView.ViewHolder {
        View mView;
        private int position;
        private AdView adView;


        public BannerAdViewHolder(View view) {
            super(view);

            try {
                mView = view;

                adView = (AdView) view.findViewById(R.id.adView);
                AdMobUtils.loadBannerAd(adView);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void setData(AdView adView, int position) {
            this.position = position;
            this.adView = adView;
        }
    }

    /********************************************************************************/


    public void addHeader(ArrayList<Siir> arrayListSiir, ArrayList<Sair> arrayListSair) {
        try {
            if (arrayListSiir != null && arrayListSair != null) {

                for (int i = 0; i < NUMBER_OF_HORIZONTAL_POEMS; i++) {
                    headerSiirList.add(arrayListSiir.get(i));
                }

                sairList = arrayListSair;
                setSairHashMap();
                boolean isHeaderView = true;
                objectList.add(isHeaderView);
                notifyItemRangeInserted(0, 1);
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

    public void addVerticalItem(ArrayList<Siir> arrayListSiir, ArrayList<Sair> arrayListSair) {
        try {
            if (arrayListSiir != null && arrayListSair != null) {
                verticalSiirList = arrayListSiir;
                if (sairList.size() <= 0) {
                    sairList = arrayListSair;
                    setSairHashMap();//header'da eklendi.
                }
                int initialSize = objectList.size();
                objectList.addAll(arrayListSiir);

                notifyItemRangeInserted(initialSize, arrayListSiir.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addAdd(ArrayList<Object> objectArrayList) {
        try {
            if (objectArrayList != null) {
                int initialSize = objectList.size();
                objectList.addAll(objectArrayList);

                notifyItemRangeInserted(initialSize, objectArrayList.size());
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


    public void addProgressLoading() {
        try {
            if (getItemViewType(objectList.size() - 1) != VIEW_PROG) {
                ProgressBar progressBar = new ProgressBar(mContext);
                objectList.add(progressBar);
                notifyItemInserted(objectList.size() - 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addLastItem() {
        try {
            String s = "SON";
            objectList.add(s);
            notifyItemRangeInserted(2, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeProgressLoading() {
        try {
            if (getItemViewType(objectList.size() - 1) == VIEW_PROG) {
                objectList.remove(objectList.size() - 1);
                notifyItemRemoved(objectList.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isShowingProgressLoading() {
        if (getItemViewType(objectList.size() - 1) == VIEW_PROG)
            return true;
        else
            return false;
    }

}