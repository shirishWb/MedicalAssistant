package com.whitebirdtechnology.medicalassistant.ExpertFragment.ByCategoriesFragment.ListBySpecializationAndName.ByExpertName;

import com.whitebirdtechnology.medicalassistant.ExpertFragment.ByExpertFragment.SingListByExpert;

import java.util.ArrayList;

/**
 * Created by dell on 27/3/17.
 */

public class SingltonByExpertName {
    private static SingltonByExpertName ourInstance = new SingltonByExpertName();
    public static void reset() {
        ourInstance = new SingltonByExpertName();
    }

    public static SingltonByExpertName getInstance() {
        return ourInstance;
    }

    ArrayList<FeedItemListByExpertName> arrayListByExpertName = new ArrayList<>();
    private SingltonByExpertName() {
    }
}
