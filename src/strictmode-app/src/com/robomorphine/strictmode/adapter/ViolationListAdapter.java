package com.robomorphine.strictmode.adapter;

import com.robomorphine.strictmode.R;
import com.robomorphine.strictmode.violation.group.ViolationGroup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ViolationListAdapter extends BaseAdapter implements SectionIndexer {
    
    private final LayoutInflater mInflator;
    private List<ViolationGroup> mItems = new ArrayList<ViolationGroup>();
    
    private SimpleDateFormat mSectionFormat = new SimpleDateFormat("HH:mm");
    private SimpleDateFormat mItemFormat = new SimpleDateFormat("HH:mm:ss.SSS");
    private final Calendar mCalendar;
    
    public ViolationListAdapter(Context context) {
        mInflator = LayoutInflater.from(context);
        mCalendar = Calendar.getInstance();
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
            view = mInflator.inflate(R.layout.dropbox_list_item, parent, false);
        }
        
        ViolationGroup item = getItem(position);
        TextView tagView = (TextView)view.findViewById(R.id.tag);
        TextView timestampView = (TextView)view.findViewById(R.id.timestamp);
        
        String violationName = item.getViolations().get(0).getClass().getSimpleName();
        String title = String.format("%s [x%d]", violationName, item.getSize());                
        tagView.setText(title);
        
        mCalendar.setTimeInMillis(item.getTimestamp());
        String time = mItemFormat.format(mCalendar.getTime());
        timestampView.setText(time);
        
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