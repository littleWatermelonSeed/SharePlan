package com.sayhellototheworld.littlewatermelon.shareplan.view.base_activity;

import android.app.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 123 on 2017/3/16.
 */

public class MyActivityManager {
    private static List<Activity> list;
    private static Map<String,Activity> sMap;

    private static MyActivityManager destroyedAllActivity = null;

    private MyActivityManager(){
        list = new ArrayList<Activity>();
        sMap = new HashMap<String,Activity>();
    }

    public static MyActivityManager getDestoryed(){
        if(destroyedAllActivity == null){
            destroyedAllActivity = new MyActivityManager();
            return destroyedAllActivity;
        }else {
            return destroyedAllActivity;
        }
    }

    public void addActivityToList(Activity activity){
        list.add(activity);
    }

    public void destroyedListActivity(){
        for (Activity activity:list){
            activity.finish();
        }
        list.clear();
    }

    public void addActivityToMap(String activityName,Activity activity){
        sMap.put(activityName,activity);
    }

    public void destroyedMapActivity(String activityName){
        if(sMap.containsKey(activityName)){
            sMap.remove(activityName);
        }
    }

}
