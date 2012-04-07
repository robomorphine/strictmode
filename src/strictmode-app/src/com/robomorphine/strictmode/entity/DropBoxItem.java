package com.robomorphine.strictmode.entity;

import android.os.Bundle;

public class DropBoxItem {
    
    private static String BUNDLE_TAG = "tag";
    private static String BUNDLE_TIMESTAMPE = "timestamp";
    private static String BUNDLE_DATA = "data";
    
    private final String mTag;
    private final long mTimestamp;
    private final String mData;
    
    public DropBoxItem(String tag, long timestamp, String data) {
        mTag = tag;
        mTimestamp = timestamp;
        mData = data;
    }
    
    public String getTag() {
        return mTag;
    }
    
    public long getTimestamp() {
        return mTimestamp;
    }
    
    public String getData() {
        return mData;
    }
    
    public static DropBoxItem fromBundle(Bundle bundle) {
        String tag = bundle.getString(BUNDLE_TAG);
        long timestamp = bundle.getLong(BUNDLE_TIMESTAMPE, -1);
        String data = bundle.getString(BUNDLE_DATA);
        return new DropBoxItem(tag, timestamp, data);
    }
    
    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TAG, mTag);
        bundle.putLong(BUNDLE_TIMESTAMPE, mTimestamp);
        bundle.putString(BUNDLE_DATA, mData);
        return bundle;
    }
}