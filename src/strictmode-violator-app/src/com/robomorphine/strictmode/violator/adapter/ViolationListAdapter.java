package com.robomorphine.strictmode.violator.adapter;

import com.robomorphine.strictmode.violator.R;
import com.robomorphine.strictmode.violator.violation.Violation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViolationListAdapter extends BaseAdapter {
    
    private final LayoutInflater mInflator;
    private final List<Violation> mViolations;
    
    public ViolationListAdapter(Context context, List<Violation> violations) {
        mInflator = LayoutInflater.from(context);
        mViolations = Collections.unmodifiableList(new ArrayList<Violation>(violations));
    }
    
    @Override
    public int getCount() {
        return mViolations.size();
    }
    
    @Override
    public Violation getItem(int position) {
        return mViolations.get(position);
    }
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            view = mInflator.inflate(R.layout.violation_item, parent, false);
        }
        
        Violation violation = mViolations.get(position);
        
        ImageView iconView = (ImageView)view.findViewById(R.id.icon);
        TextView nameView = (TextView)view.findViewById(R.id.name);
        TextView descrView = (TextView)view.findViewById(R.id.description);
        iconView.setImageDrawable(violation.getIcon());
        nameView.setText(violation.getName()); 
        descrView.setText(violation.getDescription());
                
        return view;
    }
    
}
