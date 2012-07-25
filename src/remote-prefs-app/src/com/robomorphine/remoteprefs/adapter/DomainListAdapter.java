package com.robomorphine.remoteprefs.adapter;

import com.robomorphine.remoteprefs.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class DomainListAdapter extends BaseAdapter {
    
    private final LayoutInflater mInflator;
    private List<String> mDomains = new ArrayList<String>();
    
    public DomainListAdapter(Context context) {
        mInflator = LayoutInflater.from(context);
    }
    
    public void swap(Set<String> domains) {
        if(domains != null) { 
            mDomains = new ArrayList<String>(domains);
            Collections.sort(mDomains);
        } else {
            mDomains = new ArrayList<String>();
        }
        notifyDataSetChanged();
    }
    
    @Override
    public int getCount() {
        return mDomains.size();
    }
    
    @Override
    public String getItem(int position) {
        return mDomains.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            view = mInflator.inflate(R.layout.domain_list_item, parent, false);
        }
        
        TextView name = (TextView)view.findViewById(R.id.name);
        name.setText(getItem(position));
        return view;
    }
    
    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }
     
}
