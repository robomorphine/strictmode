package com.robomorphine.log.logger;

public class AndroidLogger implements Logger {

    @Override
    public void print(int level, String tag, String msg) {

        int androidLevel = android.util.Log.ERROR;
        switch (level) {
            case com.robomorphine.log.Log.VERBOSE:
                androidLevel = android.util.Log.VERBOSE;
                break;
            case com.robomorphine.log.Log.DEBUG:
                androidLevel = android.util.Log.DEBUG;
                break;
            case com.robomorphine.log.Log.INFO:
                androidLevel = android.util.Log.INFO;
                break;
            case com.robomorphine.log.Log.WARN:
                androidLevel = android.util.Log.WARN;
                break;
            case com.robomorphine.log.Log.ERROR:
                androidLevel = android.util.Log.ERROR;
                break;
            case com.robomorphine.log.Log.ASSERT:
                androidLevel = android.util.Log.ASSERT;
                break;
            default:
                androidLevel = android.util.Log.ERROR;
        }
        android.util.Log.println(androidLevel, tag, msg);
    }
}
