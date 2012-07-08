package com.robomorphine.strictmode.adapter;

import com.google.common.base.Objects;
import com.robomorphine.strictmode.R;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SelectedPackageAdapter extends BaseAdapter implements OnClickListener {
    
    public interface SelectedPackageAdapterListener {
        void onPackageChanged(String packageName);
    }
    
    private final Context mContext;
    private final LayoutInflater mInflater;
    private final SelectedPackageAdapterListener mListener;
    private String mSelectedPackage;
    
    public SelectedPackageAdapter(Context context, SelectedPackageAdapterListener listener) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mListener = listener;
    }
    
    public void setPackage(String selectedPackage) {
        if(!Objects.equal(mSelectedPackage, selectedPackage)) {
            mSelectedPackage = selectedPackage;
            mListener.onPackageChanged(mSelectedPackage);
            notifyDataSetChanged();                
        }
    }
    
    @Override
    public int getCount() {
        return 1;
    }
    
    @Override
    public String getItem(int position) {
        return mSelectedPackage;
    }
    
    @Override
    public long getItemId(int position) {
        return position;
    }
            
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            view = mInflater.inflate(R.layout.filter_info, parent, false);
        }
                    
        ImageView icon = (ImageView)view.findViewById(R.id.icon);
        TextView appLabel = (TextView)view.findViewById(R.id.title);
        TextView appPackage = (TextView)view.findViewById(R.id.subtitle);
        
        ImageView clearButton = (ImageView)view.findViewById(R.id.action);
        clearButton.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
        clearButton.setBackgroundResource(R.drawable.clear_btn_bg);
        clearButton.setClickable(true);
        clearButton.setOnClickListener(this);
        
        if(mSelectedPackage == null) {
            clearButton.setVisibility(View.GONE);
            icon.setImageResource(R.drawable.ic_launcher);
            appLabel.setText(R.string.all_applications);
            appPackage.setText(R.string.click_to_select_application);
        } else {
            clearButton.setVisibility(View.VISIBLE);
            PackageManager pm = mContext.getPackageManager();
            try {
                ApplicationInfo info = pm.getApplicationInfo(mSelectedPackage, 0);
                icon.setImageDrawable(pm.getApplicationIcon(info));
                appLabel.setText(pm.getApplicationLabel(info));
                appPackage.setText(mSelectedPackage);
            } catch(NameNotFoundException ex) {
                /* maybe it was uninstalled, just reset filter */
                setPackage(null);
            }
        }
        
        return view;
    }
    
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.action) {
            setPackage(null);
            notifyDataSetChanged();                
        }
    }
}