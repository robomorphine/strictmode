package com.robomorphine.strictmode.fragment;

import com.robomorphine.strictmode.R;
import com.robomorphine.strictmode.activity.PackageListActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import javax.annotation.Nullable;

public class ViolationListFilterFragment extends Fragment implements OnClickListener {
    
    public interface FilterListener {
        void onFilterChanged(@Nullable String selectedPackage);
    }
    
    private final static String SELECTED_PACKAGE_PREF_KEY = "ViolationPackageFilter";
    private final static int SELECT_PACKAGE_REQUEST_KEY = 1;
    
    private FilterListener mListener;
    
    private View mRootView;
    private ImageView mIconView;
    private TextView mAppLabel;
    private TextView mAppPackage;    
    private ImageView mClearButton;
    
    private String mSelectedPackage;
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof FilterListener) {
            mListener = (FilterListener)activity;
        }
    }
    
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.violation_list_filter_fragment, container, false);
        mIconView = (ImageView)mRootView.findViewById(R.id.icon);
        mAppLabel = (TextView)mRootView.findViewById(R.id.name);
        mAppPackage = (TextView)mRootView.findViewById(R.id.package_name);
        
        mClearButton = (ImageView)mRootView.findViewById(R.id.checkbox);
        mClearButton.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
        mClearButton.setBackgroundResource(R.drawable.clear_btn_bg);
        mClearButton.setClickable(true);
        mClearButton.setOnClickListener(this);
        
        mRootView.setBackgroundResource(R.drawable.filter_btn_bg);
        mRootView.setClickable(true);
        mRootView.setOnClickListener(this);        
        return mRootView;
    } 
    
    @Override
    public void onResume() {
        super.onResume();
        setSelectedPackage(getSelectedPackage());
        updateUI();
    }
    
    private String getSelectedPackage() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return prefs.getString(SELECTED_PACKAGE_PREF_KEY, null);
    }
    
    private void setSelectedPackage(String packge) {
        mSelectedPackage = packge;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.edit().putString(SELECTED_PACKAGE_PREF_KEY, packge).apply();
        updateUI();
        
        if(mListener != null) {
            mListener.onFilterChanged(mSelectedPackage);
        }
    }
    
    private void updateUI() {
        if(mSelectedPackage == null) {
            mClearButton.setVisibility(View.GONE);
            mIconView.setImageResource(R.drawable.ic_launcher);
            mAppLabel.setText("All applications");
            mAppPackage.setText("Click to select application.");
        } else {
            mClearButton.setVisibility(View.VISIBLE);
            PackageManager pm = getActivity().getPackageManager();
            try {
                ApplicationInfo info = pm.getApplicationInfo(mSelectedPackage, 0);
                mIconView.setImageDrawable(pm.getApplicationIcon(info));
                mAppLabel.setText(pm.getApplicationLabel(info));
                mAppPackage.setText(mSelectedPackage);
            } catch(NameNotFoundException ex) {
                /* maybe it was uninstalled, just reset filter */
                setSelectedPackage(null);
            }
        }
    }
    
    @Override
    public void onClick(View v) {
        if(v == mRootView) {
            Intent intent = new Intent(getActivity(), PackageListActivity.class);
            startActivityForResult(intent, SELECT_PACKAGE_REQUEST_KEY);
        } else if(v == mClearButton) {
            setSelectedPackage(null);
        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == SELECT_PACKAGE_REQUEST_KEY) {
            if(resultCode == Activity.RESULT_OK) {
                String pkg = data.getStringExtra(PackageListActivity.EXTRA_SELECTED_PACKAGE);
                setSelectedPackage(pkg);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
