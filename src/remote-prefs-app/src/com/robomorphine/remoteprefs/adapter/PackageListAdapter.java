package com.robomorphine.remoteprefs.adapter;

import com.robomorphine.remoteprefs.AndroidPackage;
import com.robomorphine.remoteprefs.R;

import android.content.Context;
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

    @Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            view = mInflator.inflate(R.layout.package_list_item, parent, false);
        }
        
        AndroidPackage pkg = getItem(position);
        ImageView icon = (ImageView)view.findViewById(R.id.icon);
        TextView text = (TextView)view.findViewById(R.id.name);
        
        icon.setImageDrawable(pkg.getIcon());
        text.setText(pkg.getInfo().packageName);
        
        return view;
    }
    
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }
    
    @Override
    public boolean isEnabled(int position) {
        return getItem(position).isEnabled();
    } 
}