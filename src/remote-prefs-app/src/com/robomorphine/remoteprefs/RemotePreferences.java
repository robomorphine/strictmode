package com.robomorphine.remoteprefs;

import com.robomorphine.log.Log;
import com.robomorphine.log.tag.Tags;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class RemotePreferences {
    private static final String TAG = Tags.getTag(RemotePreferences.class);
    
    public static final String META_DATA_NAME = "com.robomorphine.remote.preferences";
    public static final int DEFAULT_ICON_SIZE_PX = 48;
    
    private final Context mContext;
    private final PackageManager mPackageManager;
    
    public RemotePreferences(Context context) {
        mContext = context.getApplicationContext();
        mPackageManager = mContext.getPackageManager();
    }
    
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
        drawable.draw(canvas);
        
        /* apply grayscale transformation */
        for(int w = 0; w < width; w++) {
            for(int h = 0; h < height; h++) {
                int pixel = bitmap.getPixel(w, h);
                int avarage = Color.red(pixel) + Color.green(pixel) + Color.blue(pixel);
                avarage /= 3;
                pixel = Color.argb(255, avarage, avarage, avarage);
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
                         
            ProviderInfo providerInfo = null;
            if(pkg.providers != null) {
                for(ProviderInfo provider : pkg.providers) {
                    if(provider.metaData != null &&
                       provider.metaData.containsKey(RemotePreferences.META_DATA_NAME)) {
                        Log.d(TAG, "Package %s: provider %s matches.", pkg.packageName, provider.name);
                        providerInfo = provider;
                        break;
                    }
                }
            }
            
            if(providerInfo == null) {
                Log.d(TAG, "Package %s: no matching providers.");
            } else {
                Drawable icon = mPackageManager.getApplicationIcon(pkg.applicationInfo);
                if(!AndroidPackage.isEnabled(providerInfo)) {
                    icon = makeGrayscale(mContext, icon);
                }
                
                AndroidPackage androidPkg = new AndroidPackage(icon, pkg, providerInfo);
                androidPackages.add(androidPkg);            
            }
        }
        return androidPackages;
    }
    
    public Set<String> getDomains(String packageName) {
        return null;
    }
    
    public Map<String, Object> getVariables(String pacakgeName, String domain) {
        return null;
    }
}
