package com.whitebirdtechnology.medicalassistant.ExpertFragment.ByCategoriesFragment.ListBySpecializationAndName.BySpecialization.SubCategoryExpertList;

import java.util.ArrayList;

/**
 * Created by dell on 30/3/17.
 */

public class SingltonByExpertSubCat {
    private static SingltonByExpertSubCat ourInstance = new SingltonByExpertSubCat();
    public static void reset() {
        ourInstance = new SingltonByExpertSubCat();
    }

    public static SingltonByExpertSubCat getInstance() {
        return ourInstance;
    }

    ArrayList<FeedListBySubCatExpert> arrayListByExpertName = new ArrayList<>();
    private SingltonByExpertSubCat() {
    }
}
