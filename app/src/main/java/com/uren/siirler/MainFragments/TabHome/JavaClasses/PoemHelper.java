package com.uren.siirler.MainFragments.TabHome.JavaClasses;

import android.content.Context;

import com.uren.siirler.MainFragments.BaseFragment;
import com.uren.siirler.MainFragments.TabHome.Interfaces.PoemFeaturesCallback;
import com.uren.siirler.MainFragments.TabHome.SubFragments.SinglePoemFragment;
import com.uren.siirler._database.datasource.SiirDataSource;

import java.util.ArrayList;
import java.util.List;

import static com.uren.siirler.Constants.StringConstants.ANIMATE_RIGHT_TO_LEFT;

public class PoemHelper {

    public static class FavoriteClicked {

        static int poemId;
        static boolean isFavorite;

        public static final void startProcess(Context context, int poemID, boolean isFavorite) {
            FavoriteClicked.poemId = poemID;
            FavoriteClicked.isFavorite = isFavorite;

            FavoriteClicked favoriteClicked = new FavoriteClicked(context);
        }

        private FavoriteClicked(Context context) {
            poemFavoriteClickedProcess(context);
        }

        private void poemFavoriteClickedProcess(final Context context) {

            //update DB operation
            SiirDataSource siirDataSource = new SiirDataSource(context);
            siirDataSource.updateSiirFavorite(poemId, isFavorite);
        }

    }

    public static class ProfileClicked {
/*
        static BaseFragment.FragmentNavigation fragmentNavigation;
        static UserInfoListItem userInfoListItem;

        public static final void startProcess(Context context, BaseFragment.FragmentNavigation fragmNav, UserInfoListItem userInfoListItem) {

            fragmentNavigation = fragmNav;
            ProfileClicked.userInfoListItem = userInfoListItem;

            ProfileClicked commentListClicked = new ProfileClicked(context);
        }

        private ProfileClicked(Context context) {
            postProfileClickedProcess(context);
        }

        private void postProfileClickedProcess(Context context) {
            if (fragmentNavigation != null) {
                if (userInfoListItem.getUser().getUserid().equals(AccountHolderInfo.getUserID())) {
                    //clicked own profile
                    fragmentNavigation.pushFragment(ProfileFragment.newInstance(false), ANIMATE_RIGHT_TO_LEFT);
                } else {
                    //clicked others profile
                    fragmentNavigation.pushFragment(new OtherProfileFragment(userInfoListItem), ANIMATE_RIGHT_TO_LEFT);
                }
            }
        }
*/
    }

    public static class SinglePoemClicked {

        /**
         * Bu fonksiyon çağrılırken sırasıyla
         * getInstance()
         * setSinglePoemItems()
         * setPoemFeaturesCallback
         * startSinglePoemProcess() fonksiyonları zorunlu olarak çağrılmalıdır.
         */

        private static SinglePoemClicked instance = null;
        private static List<PoemFeaturesCallback> poemFeaturesCallbackList;

        private static BaseFragment.FragmentNavigation fragmentNavigation;
        private static int poemId;
        private static int position;
        private static int comingFrom;
        private static int numberOfCallback;

        public SinglePoemClicked() {
            poemFeaturesCallbackList = new ArrayList<PoemFeaturesCallback>();
            numberOfCallback = -1;
        }

        public static SinglePoemClicked getInstance() {
            if (instance == null)
                instance = new SinglePoemClicked();

            return instance;
        }


        public void setSinglePoemItems(Context context, BaseFragment.FragmentNavigation fragmentNavigation,
                                       int siirID, int position, int comingFrom) {

            SinglePoemClicked.fragmentNavigation = fragmentNavigation;
            SinglePoemClicked.poemId = siirID;
            SinglePoemClicked.position = position;
            SinglePoemClicked.comingFrom = comingFrom;

        }

        public void startSinglePoemProcess() {
            if (fragmentNavigation != null) {
                numberOfCallback++;
                fragmentNavigation.pushFragment(SinglePoemFragment.newInstance(poemId, position, numberOfCallback, comingFrom), ANIMATE_RIGHT_TO_LEFT);
            }
        }

        public void setPoemFeaturesCallback(PoemFeaturesCallback poemFeaturesCallback) {
            poemFeaturesCallbackList.add(poemFeaturesCallback);
        }

        public static void poemFavoriteStatusChanged(boolean isFavorite, int position, int _numberOfCallback) {
            poemFeaturesCallbackList.get(_numberOfCallback).onPoemFavoriteClicked(isFavorite, position);
        }

    }


}
