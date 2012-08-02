package com.robomorphine.strictmode.adapter;

import com.robomorphine.strictmode.R;
import com.robomorphine.strictmode.entity.DropBoxItem;

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
import java.util.Locale;

@Deprecated
public class DropBoxListAdapter extends BaseAdapter implements SectionIndexer {
    
    private final LayoutInflater mInflator;
    private List<DropBoxItem> mItems = new ArrayList<DropBoxItem>();
    
    public DropBoxListAdapter(Context context) {
        mInflator = LayoutInflater.from(context);
    }
    
    public void swap(List<DropBoxItem> items) {
        if(items != null) { 
            mItems = items;
        } else {
            mItems = new ArrayList<DropBoxItem>();
        }
        notifyDataSetChanged();
    }
    
    @Override
    public int getCount() {
        return mItems.size();
    }
    
    @Override
    public DropBoxItem getItem(int position) {
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
            view = mInflator.inflate(R.layout.item_dropbox_list, parent, false);
        }
        
        DropBoxItem item = getItem(position);
        TextView tagView = (TextView)view.findViewById(R.id.tag);
        TextView timestampView = (TextView)view.findViewById(R.id.timestamp);
        
        tagView.setText(item.getTag());
        timestampView.setText(Long.toString(item.getTimestamp()));
        
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
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        for(int i = 0; i < mItems.size(); i++) {
            cal.setTimeInMillis(mItems.get(i).getTimestamp());
            labels[i] = format.format(cal.getTime());
        }
        return labels;
    }
    
    
}