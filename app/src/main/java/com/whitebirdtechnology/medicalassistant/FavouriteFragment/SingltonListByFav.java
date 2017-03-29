package com.whitebirdtechnology.medicalassistant.FavouriteFragment;

import java.util.ArrayList;

/**
 * Created by dell on 29/3/17.
 */

public class SingltonListByFav {
    private static SingltonListByFav ourInstance = new SingltonListByFav();
    public static void reset() {
        ourInstance = new SingltonListByFav();
    }
    ArrayList<FeedListByFavourite> arrayListByExpert = new ArrayList<>();

    public static SingltonListByFav getInstance() {
        return ourInstance;
    }

    private SingltonListByFav() {
    }
}
