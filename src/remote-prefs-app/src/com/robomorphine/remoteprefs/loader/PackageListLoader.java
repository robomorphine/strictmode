package com.robomorphine.remoteprefs.loader;

import com.robomorphine.log.Log;
import com.robomorphine.log.tag.Tags;
import com.robomorphine.remoteprefs.AndroidPackage;
import com.robomorphine.remoteprefs.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

public class PackageListLoader extends AsyncTaskLoader<List<AndroidPackage>> {
    
    private static final String TAG = Tags.getTag(PackageListLoader.class);
    
    private final static boolean LOAD_ALL_PACKAGES = false;
    
    public static final String META_DATA_NAME = "com.robomorphine.remote.preferences";
    public static final int DEFAULT_ICON_SIZE_PX = 48;
   
    
    private class PackageBroadcastReceiver extends BroadcastReceiver {
        private boolean mRegistered = false;
        
        @Override
        public void onReceive(Context context, Intent intent) {
            onContentChanged();
        }
        
        public void register(Context context) {
            if(!mRegistered) {
                IntentFilter intent = new IntentFilter();
                intent.addAction(Intent.ACTION_PACKAGE_ADDED);
                intent.addAction(Intent.ACTION_PACKAGE_CHANGED);
                intent.addAction(Intent.ACTION_PACKAGE_DATA_CLEARED);
                intent.addAction(Intent.ACTION_PACKAGE_FIRST_LAUNCH);
                intent.addAction(Intent.ACTION_PACKAGE_INSTALL);
                intent.addAction(Intent.ACTION_PACKAGE_REMOVED);
                intent.addAction(Intent.ACTION_PACKAGE_REPLACED);
                intent.addAction(Intent.ACTION_PACKAGE_RESTARTED);
                context.registerReceiver(this, intent);
                mRegistered = true;
            }
        }
        
        public void unregister(Context context) {
            if(mRegistered) {
                context.unregisterReceiver(this);
                mRegistered = false;
            }
        }
    }    
    
    private final PackageManager mPackageManager;
    private final PackageBroadcastReceiver mReceiver = new PackageBroadcastReceiver();
    private List<AndroidPackage> mPackages;
    
    public PackageListLoader(Context context) {
        super(context);
        mPackageManager = context.getPackageManager();
    }
    
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if(takeContentChanged() || mPackages == null) {
            forceLoad();
        } else {
            deliverResult(mPackages);
        }
    }
    
    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        cancelLoad();
    }
    
    @Override
    public List<AndroidPackage> loadInBackground() {
        mPackages = getPackages();
        mReceiver.register(getContext());
        return mPackages;
    }
    
    @Override
    protected void onAbandon() {
        super.onAbandon();
        mReceiver.unregister(getContext());
    }
    
    @Override
    protected void onReset() {
        super.onReset();
        stopLoading();
        mPackages = null;
    } 
    
    /************************************/
    /**             Core               **/
    /************************************/
    
    public static Drawable makeGrayscale(Context context, Drawable drawable) {
        int width = drawable.getBounds().width();        
        int height = drawable.getBounds().height();    
                
        Resources res = context.getResources();
        int size = (int)res.getDimension(R.dimen.app_icon_size);
        if(width <= 0) {
            width = size;
        }
        
        if(height <= 0) {
            height = size;
        }
        
        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        
        /* apply grayscale transformation */
        for(int w = 0; w < width; w++) {
            for(int h = 0; h < height; h++) {
                int pixel = bitmap.getPixel(w, h);
                int avarage = Color.red(pixel) + Color.green(pixel) + Color.blue(pixel);
                int alpha = Color.alpha(pixel);
                avarage /= 3;
                pixel = Color.argb(alpha, avarage, avarage, avarage);
                bitmap.setPixel(w, h, pixel);
            }
        }
        BitmapDrawable result = new BitmapDrawable(res, bitmap);
        return result;
    }
    
    public List<AndroidPackage> getPackages() {
        
        int flags = PackageManager.GET_DISABLED_COMPONENTS |
                    PackageManager.GET_META_DATA | 
                    PackageManager.GET_PROVIDERS;
        
        List<PackageInfo> packages = mPackageManager.getInstalledPackages(flags);
        List<AndroidPackage> androidPackages = new ArrayList<AndroidPackage>(packages.size());
        
        /* List packages with matching providers */ 
        for(PackageInfo pkg : packages) {
            
            if(isAbandoned()) {
                break;
            }
                         
            ProviderInfo providerInfo = null;
            if(pkg.providers != null) {
                for(ProviderInfo provider : pkg.providers) {
                    if(provider.metaData != null &&
                       provider.metaData.containsKey(META_DATA_NAME)) {
                        Log.d(TAG, "Package %s: provider %s matches.",
                                    pkg.packageName, provider.name);
                        providerInfo = provider;
                        break;
                    }
                }
            }
            
            if(providerInfo != null || LOAD_ALL_PACKAGES) {
                Drawable icon = mPackageManager.getApplicationIcon(pkg.applicationInfo);
                if(!AndroidPackage.isEnabled(providerInfo)) {
                    icon = makeGrayscale(getContext(), icon);
                }       
                AndroidPackage androidPkg = new AndroidPackage(icon, pkg, providerInfo);
                androidPackages.add(androidPkg);
            }
            
        }
        return androidPackages;
    }
}
