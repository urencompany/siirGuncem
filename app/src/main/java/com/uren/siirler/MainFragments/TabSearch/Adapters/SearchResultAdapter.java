package com.uren.siirler.MainFragments.TabSearch.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uren.siirler.MainFragments.BaseFragment;
import com.uren.siirler.MainFragments.TabHome.Interfaces.PoemFeaturesCallback;
import com.uren.siirler.MainFragments.TabHome.JavaClasses.PoemHelper;
import com.uren.siirler.R;
import com.uren.siirler.Utils.UserDataUtil;
import com.uren.siirler.Utils.Utils;
import com.uren.siirler._model.Sair;
import com.uren.siirler._model.Siir;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.uren.siirler.Constants.NumericConstants.COMING_FROM_NORMAL_POEM;
import static com.uren.siirler.Constants.StringConstants.USERNAME_FONT_TYPE;


public class SearchResultAdapter extends RecyclerView.Adapter {

    public static final int VIEW_POEM = 0;
    public static final int VIEW_PROG = 1;

    private Activity mActivity;
    private Context mContext;
    private BaseFragment.FragmentNavigation fragmentNavigation;
    private ArrayList<Object> objectList;
    private ArrayList<Sair> sairArrayList;
    private ArrayList<Siir> siirArrayList;
    private ArrayList<Siir> siirWitDetalList;
    private ArrayList<String> siirDetalList;
    private HashMap<Integer, Sair> sairHashmap;
    private String searchText;

    public SearchResultAdapter(Activity activity, Context context,
                               BaseFragment.FragmentNavigation fragmentNavigation) {
        this.mActivity = activity;
        this.mContext = context;
        this.fragmentNavigation = fragmentNavigation;
        this.objectList = new ArrayList<Object>();
        this.siirArrayList = new ArrayList<Siir>();
        this.siirWitDetalList = new ArrayList<Siir>();
        this.siirDetalList = new ArrayList<String>();
        this.sairArrayList = new ArrayList<Sair>();
        this.sairHashmap = new HashMap<>();
    }

    @Override
    public int getItemViewType(int position) {

        if (objectList.get(position) instanceof Siir) {
            return VIEW_POEM;
        } else if (objectList.get(position) instanceof ProgressBar) {
            return VIEW_PROG;
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

        if (viewType == VIEW_POEM) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_poem_with_detail, parent, false);

            viewHolder = new PoemViewHolder(itemView);
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
            if (holder instanceof PoemViewHolder) {
                Siir siir = (Siir) objectList.get(position);
                ((PoemViewHolder) holder).setData(siir, position);
            } else {
                ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class PoemViewHolder extends RecyclerView.ViewHolder {

        View mView;
        ImageView imgProfilePic;
        TextView txtProfilePic;
        TextView txtSair;
        TextView txtBaslik;
        TextView txtSiir;
        RelativeLayout llRvRow;

        private int position;
        private Siir siir;

        public PoemViewHolder(View view) {
            super(view);

            try {
                mView = view;
                imgProfilePic = (ImageView) view.findViewById(R.id.imgProfilePic);
                txtProfilePic = (TextView) view.findViewById(R.id.txtProfilePic);
                txtSair = (TextView) view.findViewById(R.id.txtSair);
                txtBaslik = (TextView) view.findViewById(R.id.txtBaslik);
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

            Sair sair = sairHashmap.get(siir.getSairId());
            int colorCode = mContext.getResources().getColor(R.color.Black);
            UserDataUtil.setProfilePictureRectangle(mContext, sairHashmap.get(siir.getSairId()), txtProfilePic, imgProfilePic, colorCode);
            imgProfilePic.setPadding(1, 1, 1, 1);//Name
            if (sair.getAd() != null && !sair.getAd().isEmpty()) {
                this.txtSair.setText(sair.getAd());
                txtSair.setTypeface(Typeface.createFromAsset(mContext.getAssets(), USERNAME_FONT_TYPE));
            }

            //Siir ad
            if (siir.getAd() != null && !siir.getAd().isEmpty()) {
                this.txtBaslik.setText(siir.getAd());
            }

            //siir metin
            if (siir.isConsistsDetail()) {
                txtSiir.setVisibility(View.VISIBLE);
                String displayText = setSiir();
                txtSiir.setText("..." + displayText + "...");
            } else {
                txtSiir.setVisibility(View.GONE);
            }

            txtBaslik.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonttypes/Sansation_Regular.ttf"));
            txtSiir.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonttypes/Sansation_Regular.ttf"));

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

        private String setSiir() {

            String siirText = null;
            try {
                siirText = new String(siir.getMetin(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                Log.e("encodeError", e.toString());

            }
            int index = 0;
            if (searchText != null) {
                index = siirText.toLowerCase().indexOf(searchText.toLowerCase());
            }

            String displayText;
            if (index < 75) {
                if (siirText.length() > index + 50) {
                    displayText = siirText.substring(0, index + 50);
                } else {
                    displayText = siirText.substring(0, index + searchText.length());
                }
            } else {
                if (siirText.length() > index + 50) {
                    displayText = siirText.substring(index - 75, index + 50);
                } else {
                    displayText = siirText.substring(index - 75, index + searchText.length());
                }
            }
            return displayText;
        }

    }


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBarLoading);
        }
    }


    /********************************************************************************/

    public void addSairList(ArrayList<Sair> arrayListSair) {
        try {
            if (arrayListSair != null) {
                sairArrayList = arrayListSair;
                setSairHashMap();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addMatchingSiirList(ArrayList<Siir> arrayListSiir) {
        try {
            if (arrayListSiir != null && arrayListSiir.size() > 0) {
                siirArrayList = arrayListSiir;
                int initialSize = objectList.size();
                objectList.addAll(arrayListSiir);
                notifyItemRangeInserted(initialSize, arrayListSiir.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addDetailMatchingSiirList(ArrayList<Siir> arrayListSiir) {
        try {
            if (arrayListSiir != null && arrayListSiir.size() > 0) {
                siirWitDetalList = arrayListSiir;
                int initialSize = objectList.size();
                for (int i = 0; i < arrayListSiir.size(); i++) {
                    boolean isDetailItem = true;
                    objectList.add(isDetailItem);
                }
                notifyItemRangeInserted(initialSize, arrayListSiir.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setSairHashMap() {
        for (int i = 0; i < sairArrayList.size(); i++) {
            sairHashmap.put(sairArrayList.get(i).getId(), sairArrayList.get(i));
        }
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
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

    public void clearList() {
        try {
            int initialSize = objectList.size();
            objectList.clear();
            sairArrayList.clear();
            siirArrayList.clear();
            siirWitDetalList.clear();
            siirDetalList.clear();
            sairHashmap.clear();
            notifyItemRangeRemoved(0, initialSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


