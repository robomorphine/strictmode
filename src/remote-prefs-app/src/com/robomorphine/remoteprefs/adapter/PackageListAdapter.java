package com.robomorphine.remoteprefs.adapter;

import com.robomorphine.remoteprefs.AndroidPackage;
import com.robomorphine.remoteprefs.R;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PackageListAdapter extends BaseAdapter {
    
    private final LayoutInflater mInflator;
    private final PackageManager mPackageManager;
    private List<AndroidPackage> mPackages = new ArrayList<AndroidPackage>();
    
    public PackageListAdapter(Context context) {
        mInflator = LayoutInflater.from(context);
        mPackageManager = context.getPackageManager();
    }
    
    public void swap(List<AndroidPackage> packages) {
        if(packages != null) { 
            mPackages = packages;
        } else {
            mPackages = new ArrayList<AndroidPackage>();
        }
        notifyDataSetChanged();
    }
    
    @Override
    public int getCount() {
        return mPackages.size();
    }
    
    @Override
    public AndroidPackage getItem(int position) {
        return mPackages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    
    private CharSequence getApplicationLabel(PackageInfo info) {
        CharSequence applicationName = info.packageName;
        if(info.applicationInfo != null) {
            
            CharSequence label = mPackageManager.getApplicationLabel(info.applicationInfo);
            if(label != null) {
                applicationName = label;
            }
            
        }
        return applicationName;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            view = mInflator.inflate(R.layout.package_list_item, parent, false);
        }
        
        AndroidPackage pkg = getItem(position);
        ImageView icon = (ImageView)view.findViewById(R.id.icon);
        TextView appLabel = (TextView)view.findViewById(R.id.name);
        TextView appPkg = (TextView)view.findViewById(R.id.package_name);
        View disabledInfo = (View)view.findViewById(R.id.disabled);
        View disabledIcon = (View)view.findViewById(R.id.disabled_icon);
               
        icon.setImageDrawable(pkg.getIcon());
        
        PackageInfo info = pkg.getInfo();
        appLabel.setText(getApplicationLabel(info));
        appPkg.setText(info.packageName);
        
        int visibility = View.GONE;
        if(!pkg.isEnabled()) {
            visibility = View.VISIBLE;
        }
        disabledInfo.setVisibility(visibility);
        disabledIcon.setVisibility(visibility);
        
        return view;
    }
    
    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }
    
    @Override
    public boolean isEnabled(int position) {
        return getItem(position).isEnabled();
    } 
}