package com.robomorphine.strictmode.viewer.adapter;

import com.robomorphine.strictmode.viewer.R;
import com.robomorphine.strictmode.viewer.violation.Violation;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;


public class ViolationHeadersAdapter extends BaseAdapter {
    
    private static class HeadersComparator implements Comparator<Pair<String, String>> {
        @Override
        public int compare(Pair<String, String> lhs, Pair<String, String> rhs) {
            return lhs.first.compareTo(rhs.first);
        }
    }
    
    
    private final LayoutInflater mInflater;
    private final List<Pair<String, String>> mHeaders; 
    
    public ViolationHeadersAdapter(Context context, Violation violation) {
        mInflater = LayoutInflater.from(context);
        
        List<Pair<String, String>> headers;
        headers = new ArrayList<Pair<String,String>>(violation.getHeaders().size());
        for(Entry<String, String> header : violation.getHeaders().entrySet()) {
            headers.add(Pair.create(header.getKey(), header.getValue()));
        }
        Collections.sort(headers, new HeadersComparator());
        mHeaders = Collections.unmodifiableList(headers);
    }
    
    @Override
    public int getCount() {
        return mHeaders.size();
    }
    
    @Override
    public Pair<String, String> getItem(int position) {
        return mHeaders.get(position);
    }
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            view = mInflater.inflate(R.layout.item_violation_header, parent, false);
        }
        TextView key = (TextView)view.findViewById(R.id.key);
        TextView value = (TextView)view.findViewById(R.id.value);
        
        Pair<String, String> item = getItem(position);
        key.setText(item.first);
        value.setText(item.second);
        return view;
    }

}
