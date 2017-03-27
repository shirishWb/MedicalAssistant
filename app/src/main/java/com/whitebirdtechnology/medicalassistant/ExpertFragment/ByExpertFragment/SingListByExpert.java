package com.whitebirdtechnology.medicalassistant.ExpertFragment.ByExpertFragment;

import java.util.ArrayList;

/**
 * Created by dell on 23/3/17.
 */

public class SingListByExpert {
    public static void reset() {
        ourInstance = new SingListByExpert();
    }

    private static SingListByExpert ourInstance = new SingListByExpert();
    ArrayList<FeedListByExpert> arrayListByExpert = new ArrayList<>();
    public static SingListByExpert getInstance() {
        return ourInstance;
    }

    private SingListByExpert() {
    }
}
