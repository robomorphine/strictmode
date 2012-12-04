package com.robomorphine.strictmode.viewer.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.robomorphine.strictmode.viewer.R;
import com.robomorphine.strictmode.viewer.violation.Violation;
import com.robomorphine.strictmode.viewer.violation.group.ViolationGroup;
import com.robomorphine.strictmode.viewer.violation.icon.ViolationIconMap;

public class ViolationListAdapter extends BaseAdapter implements SectionIndexer {

    private final Context mContext;
    private final LayoutInflater mInflator;
    private List<ViolationGroup> mItems = new ArrayList<ViolationGroup>();
    private final ViolationIconMap mIconMap = new ViolationIconMap();
    
    private final SimpleDateFormat mSectionFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
    private final SimpleDateFormat mItemFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.ENGLISH);
    private final Calendar mCalendar;
    
    private boolean mAllApplicationsMode = false;
    
    public ViolationListAdapter(Context context) {
        mContext = context;
        mInflator = LayoutInflater.from(context);
        mCalendar = Calendar.getInstance();
    }
    
    public void setAllApplicationsMode(boolean allAppsMode) {
        if(mAllApplicationsMode != allAppsMode) {
            mAllApplicationsMode = allAppsMode;
            notifyDataSetChanged();
        }
    }
    
    public void swap(List<ViolationGroup> items) {
        if(items != null) { 
            mItems = items;
        } else {
            mItems = new ArrayList<ViolationGroup>();
        }
        notifyDataSetChanged();
    }
    
    @Override
    public int getCount() {
        return mItems.size();
    }
    
    @Override
    public ViolationGroup getItem(int position) {
        return mItems.get(position);
    }
   
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            view = mInflator.inflate(R.layout.item_violation_list, parent, false);
        }
        
        ViolationGroup group = getItem(position);
        Violation violation = group.getViolation();
        
        ImageView iconView = (ImageView)view.findViewById(R.id.icon);
        ImageView appIconView = (ImageView)view.findViewById(R.id.appicon);
        TextView tagView = (TextView)view.findViewById(R.id.tag);
        TextView timestampView = (TextView)view.findViewById(R.id.timestamp);
        TextView countView = (TextView)view.findViewById(R.id.count);
                 
        String tag = null;
        if(mAllApplicationsMode) {
            Drawable appIcon = null;
            appIconView.setVisibility(View.VISIBLE);
            PackageManager pm = mContext.getPackageManager();
            try {
                ApplicationInfo info = pm.getApplicationInfo(violation.getPackage(), 0);
                appIcon = pm.getApplicationIcon(info);
                tag = pm.getApplicationLabel(info).toString();
            } catch(NameNotFoundException ex) {//NOPMD
                //do nothing
            }
            if(appIcon == null) {
                appIcon = mContext.getResources().getDrawable(R.drawable.ic_launcher);
            }
            appIconView.setImageDrawable(appIcon);
                        
            if(TextUtils.isEmpty(tag)) {
                tag = violation.getPackage();
            } 
        } else {
            appIconView.setVisibility(View.GONE);
            tag = group.getViolations().get(0).getClass().getSimpleName();
        }
            
        iconView.setImageDrawable(mIconMap.getIcon(mContext, violation));
        tagView.setText(tag);
        countView.setText(mContext.getString(R.string.violation_count, group.getSize()));
        
        mCalendar.setTimeInMillis(group.getTimestamp());
        String time = mItemFormat.format(mCalendar.getTime());
        timestampView.setText(mContext.getString(R.string.violation_timestamp,time));
        
        return view;
    }
    
    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }
    
    /*****************************************/
    /**          section indexer            **/ 
    /*****************************************/
    
    public int getPositionForSection(int section) {
        return section;
    };
    
    @Override
    public int getSectionForPosition(int position) {
        return position;
    }
    
    @Override
    public Object[] getSections() {
        Object [] labels = new Object[mItems.size()];
        
        for(int i = 0; i < mItems.size(); i++) {
            mCalendar.setTimeInMillis(mItems.get(i).getTimestamp());
            labels[i] = mSectionFormat.format(mCalendar.getTime());
        }
        return labels;
    }
}