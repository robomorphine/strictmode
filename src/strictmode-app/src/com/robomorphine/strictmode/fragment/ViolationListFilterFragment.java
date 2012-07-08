package com.robomorphine.strictmode.fragment;

import com.google.common.base.Objects;
import com.robomorphine.strictmode.R;
import com.robomorphine.strictmode.activity.PackageListActivity;
import com.robomorphine.strictmode.adapter.SelectedPackageAdapter;
import com.robomorphine.strictmode.adapter.SelectedPackageAdapter.SelectedPackageAdapterListener;
import com.robomorphine.strictmode.adapter.ViolationTimestampModeAdapter;
import com.robomorphine.strictmode.adapter.ViolationTimestampModeAdapter.TimestampMode;
import com.robomorphine.strictmode.adapter.ViolationTimestampModeAdapter.TimestampModeInfo;
import com.robomorphine.strictmode.view.ClickableSpinner;
import com.robomorphine.strictmode.violation.filter.AndViolationFilter;
import com.robomorphine.strictmode.violation.filter.PackageViolationFilter;
import com.robomorphine.strictmode.violation.filter.TimestampViolationFilter;
import com.robomorphine.strictmode.violation.filter.ViolationFilter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nullable;

public class ViolationListFilterFragment extends Fragment 
                                         implements OnClickListener,
                                                    OnItemSelectedListener,
                                                    SelectedPackageAdapterListener {
    
    public interface OnViolationFilterChangedListener {
        void onViolationFilterChanged(@Nullable ViolationFilter filter);
    }
    
    public class PackageTimestampTracker extends BroadcastReceiver {
        
        private final Context mContext;
        private final PackageManager mPackageManager;
        private final IntentFilter mFilter;
        private String mPackage;
        private long mTimestamp;
        private boolean mTracking;
        
        public PackageTimestampTracker(Context context) {
            mContext = context;
            mPackageManager = mContext.getPackageManager();
            mFilter = new IntentFilter();
            mFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
            mFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
            mFilter.addDataScheme("package");
            
            updateTimestamp();
        }
        
        public long getTimestamp() {
            return mTimestamp;
        }
        
        public void startTracking() {
            if(!mTracking) {
                mContext.registerReceiver(this, mFilter);
                mTracking = true;
                updateTimestamp();
            }
        }
        
        public void stopTracking() {
            if(mTracking) {
                mContext.unregisterReceiver(this);
                mTracking = false;
            }
        }
        
        public void setPackage(String packageName) {
            if(!Objects.equal(mPackage, packageName)) {
                mPackage = packageName;
                if(mPackage == null) {
                    stopTracking();
                } else {
                    startTracking();
                }
                updateTimestamp();
            }
        }
        
        @Override
        public void onReceive(Context context, Intent intent) {
            if(mFilter.hasAction(intent.getAction())) {
                updateTimestamp();
            }
        }
        
        private void updateTimestamp() {
            long timestamp = 0;
            if(mPackage != null) {
                try {
                    PackageInfo pi = mPackageManager.getPackageInfo(mPackage, 0);
                    timestamp = pi.lastUpdateTime;
                } catch(NameNotFoundException ex) {
                    Log.e(getClass().getSimpleName(), "Failed to get package info.", ex);
                }
            } 
            
            if(mTimestamp != timestamp) {
                mTimestamp = timestamp;
                onPackageTimestampChanged(mPackage, mTimestamp);
            }
        }
        
    }     
    
    public static final String TAG = ViolationListFilterFragment.class.getSimpleName();
    
    private final static String SELECTED_TIMESTAMP_MODE_PREF_KEY = "ViolationTimestampFil ter";
    private final static String SELECTED_PACKAGE_PREF_KEY = "ViolationPackageFilter";
    
    private final static int SELECT_PACKAGE_REQUEST_KEY = 1;
    
    private OnViolationFilterChangedListener mListener;
    private PackageTimestampTracker mPackageTimestampTracker;
        
    private SelectedPackageAdapter mPackageAdapter;
    private ClickableSpinner mPackageSpinner;
    
    private ViolationTimestampModeAdapter mTimestampModeSpinnerAdapter;
    private Spinner mTimestampModeSpinner;
    
    private String mSelectedPackage;
    private TimestampMode mSelectedTimestampMode = TimestampMode.SinceInstall;

    private View mFilterDivider;
    
    private ViolationFilter mPendingFilter = null;
        
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
        
        mPackageTimestampTracker = new PackageTimestampTracker(getActivity());
        mPackageAdapter = new SelectedPackageAdapter(getActivity(), this);
        
        List<TimestampModeInfo> generalFilters = new LinkedList<TimestampModeInfo>();
        generalFilters.add(new TimestampModeInfo(TimestampMode.SinceInstall,
                                                 getString(R.string.show_since_last_install), 
                                                 getString(R.string.show_since_last_install_descr)));        
        generalFilters.add(new TimestampModeInfo(TimestampMode.All,
                                                 getString(R.string.ignore_timestamp), 
                                                 getString(R.string.ignore_timestamp_descr)));        
        mTimestampModeSpinnerAdapter = new ViolationTimestampModeAdapter(getActivity(), generalFilters);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.violation_list_filter_fragment, container, false);
                
        mPackageSpinner = (ClickableSpinner)view.findViewById(R.id.package_filter);
        mPackageSpinner.setAdapter(mPackageAdapter);
        mPackageSpinner.setOnClickListener(this);
        
        mTimestampModeSpinner = (Spinner)view.findViewById(R.id.timestamp_filter_mode);
        mTimestampModeSpinner.setAdapter(mTimestampModeSpinnerAdapter);
        mTimestampModeSpinner.setOnItemSelectedListener(this);
        
        mFilterDivider = view.findViewById(R.id.divider);
        return view;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setSelectedPackage(getSelectedPackage());
        setSelectedTimestampMode(getSelectedTimestampMode());
    }
    
    @Override
    public void onResume() {
        super.onResume();        
                
        mPackageTimestampTracker.startTracking();        
        updateUi();
        updateFilter();
        
        if(mPendingFilter != null) {
            if(mListener != null) {
                mListener.onViolationFilterChanged(mPendingFilter);
                mPendingFilter = null;
            }
        }
    }
    
    @Override
    public void onPause() {
        super.onPause();        
        mPackageTimestampTracker.stopTracking();
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
                        
            mPackageTimestampTracker.setPackage(mSelectedPackage);
            mPackageAdapter.setPackage(mSelectedPackage);
            
            updateUi();
            updateFilter();
        }
    }
    
    private TimestampMode getSelectedTimestampMode() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String modeName = prefs.getString(SELECTED_TIMESTAMP_MODE_PREF_KEY, null);
        if(modeName == null) {
            return TimestampMode.SinceInstall;
        } else {
            try {
                return TimestampMode.valueOf(modeName);
            } catch(IllegalArgumentException ex) {
                String msg = String.format("Failed to parse %s enum value: \"%s\"",
                        TimestampMode.class.getSimpleName(), modeName);
                Log.e(TAG, msg, ex);
                return TimestampMode.SinceInstall;
            }
        }
    }
    
    private void setSelectedTimestampMode(TimestampMode mode) {
        if(!Objects.equal(mSelectedTimestampMode, mode)) {
            mSelectedTimestampMode = mode;
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            prefs.edit()
                 .putString(SELECTED_TIMESTAMP_MODE_PREF_KEY, mSelectedTimestampMode.name())
                 .apply();
            
            int pos = mTimestampModeSpinnerAdapter.getTimestampModePosition(mSelectedTimestampMode);
            mTimestampModeSpinner.setSelection(pos);
            
            updateUi();
            updateFilter();
        }
    }
    
    void updateFilter() {
        ViolationFilter filter = null;
        if(mSelectedPackage == null || mSelectedTimestampMode == TimestampMode.All) {
            filter = new PackageViolationFilter(mSelectedPackage);
        } else {
            long timestamp = mPackageTimestampTracker.getTimestamp();
            PackageViolationFilter pkgFilter = new PackageViolationFilter(mSelectedPackage);
            TimestampViolationFilter timestampFilter = new TimestampViolationFilter(timestamp);
            filter = new AndViolationFilter(pkgFilter, timestampFilter);
        }
        
        if(isResumed()) {
            if(mListener != null) {
                mListener.onViolationFilterChanged(filter);
            }    
        } else {
            mPendingFilter = filter;
        }
        
    }
    
        
    private void updateUi() {
        int visibility = View.VISIBLE;
        if(mSelectedPackage == null) {
            visibility = View.GONE;
        }
        mTimestampModeSpinner.setVisibility(visibility);
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
    
    public void onPackageTimestampChanged(String packageName, long timestmap) {
        updateFilter();
    }
    
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent == mTimestampModeSpinner) {
            setSelectedTimestampMode(mTimestampModeSpinnerAdapter.getItem(position).timestampMode);
        }
    }
    
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if(parent == mTimestampModeSpinner) {
            setSelectedTimestampMode(TimestampMode.SinceInstall);
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
