package com.robomorphine.strictmode.viewer.adapter;

import com.robomorphine.strictmode.viewer.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ViolationTimestampModeAdapter extends BaseAdapter {
    
    public enum TimestampMode { All, SinceInstall };
    
    public static class TimestampModeInfo {
        public final TimestampMode timestampMode;
        public final String title;
        public final String subtitle;
        
        public TimestampModeInfo(TimestampMode mode, String title, String subtitle) {
            this.timestampMode = mode;
            this.title = title;
            this.subtitle = subtitle;
        }
    }
    
    private final LayoutInflater mInflater;
    private final List<TimestampModeInfo> mFilters;
        
    public ViolationTimestampModeAdapter(Context context, List<TimestampModeInfo> filters) {
        mInflater = LayoutInflater.from(context);
        mFilters = new ArrayList<TimestampModeInfo>();
        if(filters != null) {
            mFilters.addAll(filters);
        }
    }
    
    public ViolationTimestampModeAdapter(Context context) {
        this(context, null);
    }   
    
    public int getTimestampModePosition(TimestampMode mode) {
        for(int i = 0; i < mFilters.size(); i++) {
            if(mFilters.get(i).timestampMode == mode) {
                return i;
            }
        }
        return -1;
    }
    
    public void setFilterList(List<TimestampModeInfo> filters) {
        mFilters.clear();
        if(filters != null) {
            mFilters.addAll(filters);
        }
        notifyDataSetChanged();
    }
    
    @Override
    public int getCount() {
        return mFilters.size();
    }
    
    @Override
    public TimestampModeInfo getItem(int position) {
        return mFilters.get(position);
    }
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    private void bindView(View view, int position) {
        ImageView icon = (ImageView)view.findViewById(R.id.icon);
        TextView title = (TextView)view.findViewById(R.id.title);
        TextView subtitle = (TextView)view.findViewById(R.id.subtitle);
        
        icon.setImageResource(R.drawable.violation_filter_timestamp);
        title.setText(getItem(position).title);
        subtitle.setText(getItem(position).subtitle);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            view = mInflater.inflate(R.layout.item_filter_info, parent, false);
        }
        
        bindView(view, position);
        return view;
    }
    
    
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            view = mInflater.inflate(R.layout.item_filter_info_dropdown, parent, false);
        }
        
        bindView(view, position);
        return view;
    }
}
