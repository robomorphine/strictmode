package com.robomorphine.strictmode.fragment;

import com.google.common.base.Objects;
import com.robomorphine.strictmode.R;
import com.robomorphine.strictmode.activity.PackageListActivity;
import com.robomorphine.strictmode.adapter.SelectedPackageAdapter;
import com.robomorphine.strictmode.adapter.SelectedPackageAdapter.SelectedPackageAdapterListener;
import com.robomorphine.strictmode.adapter.ViolationFilterListAdapter;
import com.robomorphine.strictmode.adapter.ViolationFilterListAdapter.ViolationFilterInfo;
import com.robomorphine.strictmode.view.ClickableSpinner;
import com.robomorphine.strictmode.violation.filter.PackageViolationFilter;
import com.robomorphine.strictmode.violation.filter.ViolationFilter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Spinner;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nullable;

public class ViolationListFilterFragment extends Fragment 
                                         implements OnClickListener, SelectedPackageAdapterListener {
    
    public interface OnViolationFilterChangedListener {
        void onViolationFilterChanged(@Nullable ViolationFilter filter);
    }
     
    private enum TimestampMode { All, SinceInstall }; 
    
    private final static String SELECTED_TIMESTAMP_MODE_PREF_KEY = "ViolationTimestampFil ter";
    private final static String SELECTED_PACKAGE_PREF_KEY = "ViolationPackageFilter";
    
    private final static int SELECT_PACKAGE_REQUEST_KEY = 1;
    
    private OnViolationFilterChangedListener mListener;
        
    private SelectedPackageAdapter mSelectedPackageAdapter;
    private ClickableSpinner mPackageSpinner;
    
    private ViolationFilterListAdapter mTimestampSpinnerAdapter;
    private Spinner mTimestampSpinner;
    
    private View mFilterDivider;
        
    private String mSelectedPackage;
    private TimestampMode mSelectedTimestampMode;
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof OnViolationFilterChangedListener) {
            mListener = (OnViolationFilterChangedListener)activity;
        }
    }
    
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mSelectedPackageAdapter = new SelectedPackageAdapter(getActivity(), this);
        
        List<ViolationFilterInfo> generalFilters = new LinkedList<ViolationFilterInfo>();
        generalFilters.add(new ViolationFilterInfo("Show Since Last Install", "Only violations that were reported after last application installation are visible."));
        generalFilters.add(new ViolationFilterInfo("Ignore Timestamp", "All violations are visible."));
        
        mTimestampSpinnerAdapter = new ViolationFilterListAdapter(getActivity(), generalFilters);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.violation_list_filter_fragment, container, false);
                
        mPackageSpinner = (ClickableSpinner)view.findViewById(R.id.package_filter);
        mPackageSpinner.setAdapter(mSelectedPackageAdapter);
        mPackageSpinner.setOnClickListener(this);
        
        mTimestampSpinner = (Spinner)view.findViewById(R.id.timestamp_filter_mode);
        mTimestampSpinner.setAdapter(mTimestampSpinnerAdapter);
        
        mFilterDivider = view.findViewById(R.id.divider);
        
        return view;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        setSelectedPackage(getSelectedPackage());
        updateUi();
    }
    
    private String getSelectedPackage() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return prefs.getString(SELECTED_PACKAGE_PREF_KEY, null);
    }
    
    private void setSelectedPackage(String selectedPackage) {
        if(!Objects.equal(mSelectedPackage, selectedPackage)) {
            mSelectedPackage = selectedPackage;
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            prefs.edit().putString(SELECTED_PACKAGE_PREF_KEY, selectedPackage).apply();
                        
            mSelectedPackageAdapter.setPackage(mSelectedPackage);
            if(mListener != null) {
                mListener.onViolationFilterChanged(new PackageViolationFilter(mSelectedPackage));
            }
            
            updateUi();
        }
    }
    
    private void updateUi() {
        int visibility = View.VISIBLE;
        if(mSelectedPackage == null) {
            visibility = View.GONE;
        }
        mTimestampSpinner.setVisibility(visibility);
        mFilterDivider.setVisibility(visibility);
    }
    
    @Override
    public void onClick(View v) {
        if(v == mPackageSpinner) {
            Intent intent = new Intent(getActivity(), PackageListActivity.class);
            startActivityForResult(intent, SELECT_PACKAGE_REQUEST_KEY);
        }
    }
    
    @Override
    public void onPackageChanged(String packageName) {
        setSelectedPackage(packageName);
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
