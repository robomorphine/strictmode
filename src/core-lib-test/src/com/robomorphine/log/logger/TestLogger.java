
package com.robomorphine.log.logger;

public class TestLogger implements Logger {

    private int mCount = 0;
    private int mLastLevel = -1;
    private String mLastTag = null;
    private String mLastMsg = null;

    public TestLogger() {
    }

    @Override
    public void print(int level, String tag, String msg) {
        mCount++;
        mLastLevel = level;
        mLastTag = tag;
        mLastMsg = msg;
    }
    
    public void reset() {
        mCount = 0;
        mLastLevel = -1;
        mLastTag = null;
        mLastMsg = null;
    }
    
    public int getCount() {
        return mCount;
    }
    
    public int getLastLevel() {
        return mLastLevel;
    }
    
    public String getLastTag() {
        return mLastTag;
    }
    
    public String getLastMsg() {
        return mLastMsg;
    }
}
