package com.robomorphine.strictmode.viewer.adapter;

import com.robomorphine.strictmode.viewer.R;
import com.robomorphine.strictmode.viewer.entity.AndroidPackage;

import android.content.Context;
import android.content.pm.PackageInfo;
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
    private List<AndroidPackage> mPackages = new ArrayList<AndroidPackage>();
    
    public PackageListAdapter(Context context) {
        mInflator = LayoutInflater.from(context);
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
    
    public String getItemPackageName(int position) {
        return getItem(position).getInfo().packageName;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
   
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            view = mInflator.inflate(R.layout.item_package_list, parent, false);
        }
        
        AndroidPackage pkg = getItem(position);
        ImageView icon = (ImageView)view.findViewById(R.id.icon);
        TextView appLabel = (TextView)view.findViewById(R.id.name);
        TextView appPkg = (TextView)view.findViewById(R.id.package_name);
        
        icon.setImageDrawable(pkg.getIcon());
        
        PackageInfo info = pkg.getInfo();
        appLabel.setText(pkg.getApplicationLabel());
        appPkg.setText(info.packageName);
        
        return view;
    }
    
    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }
}