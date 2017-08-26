package com.sayhellototheworld.littlewatermelon.shareplan.util;

import android.util.Log;

/**
 * Created by 123 on 2017/8/21.
 */

public final class MyLogUtil {

    public final static int LOG_VERBOSE = 0;
    public final static int LOG_DEBUG = 1;
    public final static int LOG_INFO = 2;
    public final static int LOG_WARN = 3;
    public final static int LOG_ERROR = 4;

    public static void logVerbose(String tag,String message){
        Log.v(tag,message);
    }

    public static void logDebug(String tag,String message){
        Log.d(tag,message);
    }

    public static void logInfo(String tag,String message){
        Log.i(tag,message);
    }

    public static void logWarn(String tag,String message){
        Log.w(tag,message);
    }
    public static void logError(String tag,String message){
        Log.e(tag,message);
    }

}
