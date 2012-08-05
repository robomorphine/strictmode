package com.robomorphine.strictmode.viewer.entity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

public class AndroidPackage implements Comparable<AndroidPackage> {
    
    private final Drawable mPackageIcon;        
    private final PackageInfo mPackageInfo;
    private final String mApplicationLabel;
    
    private static String getApplicationLabel(PackageManager pm, PackageInfo info) {
        if(info.applicationInfo != null) {
            CharSequence label = pm.getApplicationLabel(info.applicationInfo);
            if(label != null) {
                return label.toString();
            }
        }
        return info.packageName;
    }
        
    public AndroidPackage(PackageManager packageManager, Drawable icon, PackageInfo packageInfo) {
        mPackageInfo = packageInfo;
        mPackageIcon = icon;
        mApplicationLabel = getApplicationLabel(packageManager, packageInfo);
    }
    
    public Drawable getIcon() {
        return mPackageIcon;
    }
    
    public String getApplicationLabel() {
        return mApplicationLabel;
    }
    
    public PackageInfo getInfo() {
        return mPackageInfo;
    }
    
    @Override
    public int hashCode() {
        return mPackageInfo.hashCode();
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof AndroidPackage) {
            AndroidPackage ap = (AndroidPackage)o;
            return mPackageInfo.equals(ap.mPackageInfo);
        }
        return false;
    }
    
    @Override
    public int compareTo(AndroidPackage another) {        
        return mApplicationLabel.compareTo(another.mApplicationLabel);
    }
}