package com.robomorphine.strictmode.viewer.loader;

import com.robomorphine.loader.AsyncLoader;
import com.robomorphine.strictmode.viewer.entity.AndroidPackage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PackageListLoader extends AsyncLoader<List<AndroidPackage>> {    
    
    public static final String META_DATA_NAME = "com.robomorphine.remote.preferences";
    public static final int DEFAULT_ICON_SIZE_PX = 48;
  
    private class PackageBroadcastReceiver extends BroadcastReceiver {
        
        @Override
        public void onReceive(Context context, Intent intent) {
            onContentChanged();
        }
        
        public void register(Context context) {
            IntentFilter intent = new IntentFilter();
            intent.addAction(Intent.ACTION_PACKAGE_ADDED);
            intent.addAction(Intent.ACTION_PACKAGE_CHANGED);
            intent.addAction(Intent.ACTION_PACKAGE_DATA_CLEARED);
            intent.addAction(Intent.ACTION_PACKAGE_FIRST_LAUNCH);
            intent.addAction(Intent.ACTION_PACKAGE_REMOVED);
            intent.addAction(Intent.ACTION_PACKAGE_REPLACED);
            intent.addAction(Intent.ACTION_PACKAGE_RESTARTED);
            intent.addDataScheme("package");
            context.registerReceiver(this, intent);
        }
        
        public void unregister(Context context) {
            context.unregisterReceiver(this);
        }
    }    
    
    private final PackageManager mPackageManager;
    private final PackageBroadcastReceiver mReceiver = new PackageBroadcastReceiver();
    private final String mFilter;
    
    public PackageListLoader(Context context, String filter) {
        super(context);
        mPackageManager = context.getPackageManager();
        mFilter = filter;
    }
    
    public String getFilter() {
        return mFilter;
    }
    
    @Override
    public List<AndroidPackage> loadInBackground() {
        return getPackages();
    }    
    
    @Override
    protected void onRegisterObservers() {
        super.onRegisterObservers();
        mReceiver.register(getContext());
    }
    
    @Override
    protected void onUnregisterObservers() {
        super.onUnregisterObservers();
        mReceiver.unregister(getContext());
    }
   
    /************************************/
    /**             Core               **/
    /************************************/
    
    public List<AndroidPackage> getPackages() {
        int flags = 0;
        
        List<PackageInfo> packages = mPackageManager.getInstalledPackages(flags);
        List<AndroidPackage> androidPackages = new ArrayList<AndroidPackage>(packages.size());
        
        /* List packages which match filter */ 
        for(PackageInfo pkg : packages) {
            
            if(isAbandoned()) {
                break;
            } 
            
            boolean passed = TextUtils.isEmpty(mFilter);
            if(!passed && pkg.applicationInfo != null) {
                CharSequence appName = mPackageManager.getApplicationLabel(pkg.applicationInfo);
                if(appName == null) {
                    appName = "";
                }
                passed = appName.toString().contains(mFilter);
            }
            
            if(!passed) {
                passed = pkg.packageName.contains(mFilter);
            }
            
            /* done filtering, now handle the result */
            if(passed) {
                Drawable drawable = null;
                if(pkg.applicationInfo != null) {
                    drawable = mPackageManager.getApplicationIcon(pkg.applicationInfo);
                }
                AndroidPackage androidPkg = new AndroidPackage(mPackageManager, drawable, pkg);
                androidPackages.add(androidPkg);
            }
        }
        
        Collections.sort(androidPackages);
        return androidPackages;
    }
}
