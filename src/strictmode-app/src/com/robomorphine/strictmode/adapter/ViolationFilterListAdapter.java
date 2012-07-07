package com.robomorphine.strictmode.adapter;

import com.robomorphine.strictmode.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ViolationFilterListAdapter extends BaseAdapter {
    
    public static class ViolationFilterInfo {
        public final String title;
        public final String subtitle;
        
        public ViolationFilterInfo(String title, String subtitle) {
            this.title = title;
            this.subtitle = subtitle;
        }
    }
    
    private final LayoutInflater mInflater;
    private final List<ViolationFilterInfo> mFilters;
        
    public ViolationFilterListAdapter(Context context, List<ViolationFilterInfo> filters) {
        mInflater = LayoutInflater.from(context);
        mFilters = new ArrayList<ViolationFilterInfo>();
        if(filters != null) {
            mFilters.addAll(filters);
        }
    }
    
    public ViolationFilterListAdapter(Context context) {
        this(context, null);
    }   
    
    public void setFilterList(List<ViolationFilterInfo> filters) {
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
    public ViolationFilterInfo getItem(int position) {
        return mFilters.get(position);
    }
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            view = mInflater.inflate(R.layout.filter_info_layout, parent, false);
        }
        
        TextView title = (TextView)view.findViewById(R.id.title);
        title.setText(getItem(position).title);
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            view = mInflater.inflate(R.layout.filter_info_dropdown_layout, parent, false);
        }
        
        TextView title = (TextView)view.findViewById(R.id.title);
        TextView subtitle = (TextView)view.findViewById(R.id.subtitle);
        
        title.setText(getItem(position).title);
        subtitle.setText(getItem(position).subtitle);
        return view;
    }
}
