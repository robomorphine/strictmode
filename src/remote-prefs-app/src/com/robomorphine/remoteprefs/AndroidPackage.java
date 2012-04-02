package com.robomorphine.remoteprefs;

import android.content.pm.PackageInfo;
import android.content.pm.ProviderInfo;
import android.graphics.drawable.Drawable;

public class AndroidPackage {
    
    private final Drawable mPackageIcon;
    private final PackageInfo mPackageInfo;
    private final ProviderInfo mProviderInfo;
        
    public static boolean isEnabled(ProviderInfo providerInfo) {
        return providerInfo != null && providerInfo.enabled && providerInfo.exported;
    }
        
    public AndroidPackage(Drawable icon, PackageInfo packageInfo, ProviderInfo providerInfo) {
        mPackageInfo = packageInfo;
        mProviderInfo = providerInfo;
        mPackageIcon = icon;
    }
    
    public Drawable getIcon() {
        return mPackageIcon;
    }
    
    public PackageInfo getInfo() {
        return mPackageInfo;
    }
    
    public ProviderInfo getProvider() {
        return mProviderInfo;
    }
    
    public boolean isEnabled() {
        return isEnabled(mProviderInfo);
    }

}