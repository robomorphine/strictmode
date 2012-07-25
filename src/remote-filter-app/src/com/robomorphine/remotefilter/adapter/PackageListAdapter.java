package com.robomorphine.remotefilter.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.robomorphine.log.filter.remote.RemoteFilterContract;
import com.robomorphine.remotefilter.R;

public class PackageListAdapter extends BaseAdapter {

    private static class PackageNameComparator implements Comparator<PackageInfo> {
        @Override
        public int compare(PackageInfo object1, PackageInfo object2) {
            return object1.packageName.compareTo(object2.packageName);
        }
    }

    private static class ApplicationLabelComparator implements Comparator<PackageInfo> {

        private final Context mContext;
        private final PackageManager mPackageManager;

        public ApplicationLabelComparator(Context context) {
            mContext = context;
            mPackageManager = mContext.getPackageManager();
        }

        @Override
        public int compare(PackageInfo object1, PackageInfo object2) {
            String appName1 = mPackageManager.getApplicationLabel(object1.applicationInfo).toString();
            String appName2 = mPackageManager.getApplicationLabel(object2.applicationInfo).toString();
            return appName1.compareTo(appName2);
        }
    }

    private static class InstallationDateComparator implements Comparator<PackageInfo> {
        @Override
        public int compare(PackageInfo object1, PackageInfo object2) {
            Long date1 = object1.firstInstallTime;
            Long date2 = object2.firstInstallTime;
            return date1.compareTo(date2);
        }
    }

    private static class InvertComparator implements Comparator<PackageInfo> {

        private final Comparator<PackageInfo> mComparator;
        public InvertComparator(Comparator<PackageInfo> comparator) {
            mComparator = comparator;
        }

        @Override
        public int compare(PackageInfo object1, PackageInfo object2) {
            return mComparator.compare(object2, object1);
        }
    }

    private class PackageChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            refresh();
        }

        public void register(Context ctx) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_PACKAGE_ADDED);
            filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
            filter.addAction(Intent.ACTION_PACKAGE_REPLACED);
            ctx.registerReceiver(this, filter);
        }

        public void unregister(Context ctx) {
            ctx.unregisterReceiver(this);
        }
    }

    public enum SortBy { AppName, PackageName, DateInstalled };

    private final Context mContext;
    private final PackageManager mPackageManager;
    private PackageChangeReceiver mPackageChangeReceiver = new PackageChangeReceiver();
    private List<PackageInfo> mPackages = new ArrayList<PackageInfo>();
    private SortBy mSortBy = SortBy.AppName;
    private boolean mInvertSort = false;

    public PackageListAdapter(Context ctx) {
        mContext = ctx;
        mPackageManager = ctx.getPackageManager();
        refresh();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if(view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.package_list_item, parent, false);
        }

        PackageManager pkgManager = mContext.getPackageManager();
        PackageInfo pkgInfo = getItem(position);
        ApplicationInfo appInfo = pkgInfo.applicationInfo;

        ImageView appIcon = (ImageView)view.findViewById(R.id.appIcon);
        TextView packageName = (TextView)view.findViewById(R.id.packageName);
        TextView appName = (TextView)view.findViewById(R.id.appLabel);
        TextView versionName = (TextView)view.findViewById(R.id.versionName);
        TextView versionCode = (TextView)view.findViewById(R.id.versionCode);

        appIcon.setImageDrawable(pkgManager.getApplicationIcon(appInfo));
        appName.setText(pkgManager.getApplicationLabel(appInfo));
        packageName.setText(pkgInfo.packageName);
        versionName.setText(pkgInfo.versionName);
        versionCode.setText(Integer.toString(pkgInfo.versionCode));

        view.setTag(pkgInfo);
        return view;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public PackageInfo getItem(int position) {
        return mPackages.get(position);
    }

    @Override
    public int getCount() {
        return mPackages.size();
    }

    public boolean filter(PackageInfo info) {
        if(info.reqFeatures == null) return false;
        for(FeatureInfo feature : info.reqFeatures) {
            if(RemoteFilterContract.FEATURE.equals(feature.name)) {
                return true;
            }
        }
        return false;
    }

    public void refresh() {

        List<PackageInfo> pkgs = mPackageManager.getInstalledPackages(0);
        mPackages = new ArrayList<PackageInfo>();

        int flags = PackageManager.GET_CONFIGURATIONS;
        for(PackageInfo info : pkgs) {
            try {
                info = mPackageManager.getPackageInfo(info.packageName, flags);
                if(filter(info)) {
                    mPackages.add(info);
                }
            } catch(NameNotFoundException ex) {
                //ignore
            }
        }

        Comparator<PackageInfo> comparator = null;
        switch(mSortBy) {
            case AppName:
                comparator = new ApplicationLabelComparator(mContext);
                break;
            case PackageName:
                comparator = new PackageNameComparator();
                break;
            case DateInstalled:
                comparator = new InstallationDateComparator();
                break;
            default:
                comparator = new ApplicationLabelComparator(mContext);
        }

        if(mInvertSort) {
            comparator = new InvertComparator(comparator);
        }

        Collections.sort(mPackages, comparator);
        notifyDataSetChanged();
    }

    public void startTracking() {
        mPackageChangeReceiver.register(mContext);
    }

    public void stopTracking() {
        mPackageChangeReceiver.unregister(mContext);
    }
}
