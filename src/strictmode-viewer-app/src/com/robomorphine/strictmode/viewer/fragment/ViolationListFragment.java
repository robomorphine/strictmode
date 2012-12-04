package com.robomorphine.strictmode.viewer.fragment;

import java.io.IOException;
import java.util.Comparator;

import javax.annotation.Nullable;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.common.base.Objects;
import com.robomorphine.strictmode.viewer.R;
import com.robomorphine.strictmode.viewer.adapter.ViolationListAdapter;
import com.robomorphine.strictmode.viewer.loader.ViolationLoader;
import com.robomorphine.strictmode.viewer.violation.filter.ViolationFilter;
import com.robomorphine.strictmode.viewer.violation.group.ViolationGroup;
import com.robomorphine.strictmode.viewer.violation.group.ViolationGroup.CountComparator;
import com.robomorphine.strictmode.viewer.violation.group.ViolationGroup.TimestampComparator;
import com.robomorphine.strictmode.viewer.violation.group.ViolationGroups;

public class ViolationListFragment extends ConfigurableListFragment 
                                   implements LoaderCallbacks<ViolationGroups>, 
                                              OnItemLongClickListener {

    public interface OnViolationClickListener {
        void onViolationClicked(ViolationGroup group);
        void onViolationLongClicked(ViolationGroup group);
    }
    
    private final static int LOADER_ID = 1;

    private final static long LOADER_THROTTLE_TIMEOUT_MS = 500;
    
    private final static String STATE_VIOLATION_FILTER = "violationFilter";
    
    private OnViolationClickListener mListener;
    private ViolationListAdapter mAdapter;
    private ViolationFilter mViolationFilter;
    private ViewGroup mListContainer;
    private TextView mViolationCount;
    private TextView mViolationGroupCount;
    private Comparator<ViolationGroup> mViolationGroupComparator = new CountComparator();
    @Nullable private ViolationGroups mLastData;
            
    private static boolean isAllApplicationsMode(@Nullable ViolationFilter filter) {
        return filter == null || !filter.usesProperty(ViolationFilter.PROPERTY_PACKAGE);
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof OnViolationClickListener) {
            mListener = (OnViolationClickListener)activity;
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
                
        /* restore filter */
        if(savedInstanceState != null) {
            mViolationFilter = 
                    (ViolationFilter)savedInstanceState.getSerializable(STATE_VIOLATION_FILTER);
        }
                
        mAdapter = new ViolationListAdapter(getActivity());         
        mAdapter.setAllApplicationsMode(isAllApplicationsMode(mViolationFilter));
        
        setHasOptionsMenu(true);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_violation_list, container, false);
        mListContainer = (LinearLayout)view.findViewById(R.id.container);
        mViolationCount = (TextView)view.findViewById(R.id.violation_count);
        mViolationGroupCount = (TextView)view.findViewById(R.id.violation_group_count);
                
        View listLayout = super.onCreateView(inflater, mListContainer, savedInstanceState);
        mListContainer.addView(listLayout);
                
        return view;
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	inflater.inflate(R.menu.fragment_violation_list,  menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if (item.getItemId() == R.id.sort_by_count) {
    		mViolationGroupComparator = new CountComparator();
    		getLoaderManager().restartLoader(LOADER_ID, null, this);
    		return true;
    	} else if(item.getItemId() == R.id.sort_by_timetstamp) {
    		mViolationGroupComparator = new TimestampComparator();
    		getLoaderManager().restartLoader(LOADER_ID, null, this);
    		return true;    		
    	} else if(item.getItemId() == R.id.export) {
    		ViolationGroups groups = mLastData;
    		if (groups == null) {
    			Toast.makeText(getActivity(), "No data available.", Toast.LENGTH_LONG).show();
    		} else {
    			//TODO: IO on ui thread
    			try {
    				groups.export(getActivity());
    				Toast.makeText(getActivity(), "Exported", Toast.LENGTH_LONG).show();
				} catch(IOException ex) {
					Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_LONG).show();
				}
     		}
    		return true;
    	} else {
    		return super.onOptionsItemSelected(item);
    	}
    }
    
    @Override
    protected void onConfigureEmptyView(TextView emptyView) {
        super.onConfigureEmptyView(emptyView);
        emptyView.setMovementMethod(LinkMovementMethod.getInstance());         
                
        /* apply 20 dp padding for empty view */
        float dp = 20;
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, dm);
        emptyView.setPadding(px, px, px, px);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
                
        /* show menu items */
        setHasOptionsMenu(true);
        
        /* setup list */
        setListAdapter(mAdapter);        
        setListShown(false);
        
        ListView list = getListView();
        list.setFastScrollEnabled(true);
        list.setOnItemLongClickListener(this);
                
        /* fetch data */
        getLoaderManager().initLoader(LOADER_ID, null, this);
        
        /* update empty view */
        updateEmptyView();
    }    
    
    private CharSequence getEmptyText() {
        Context context = getActivity();        
        int result = context.checkCallingOrSelfPermission(Manifest.permission.READ_LOGS);
        boolean readLogsGranted = (result == PackageManager.PERMISSION_GRANTED); 
        boolean packageFilterEnabled = false;
        
        if (mViolationFilter != null &&
            mViolationFilter.usesProperty(ViolationFilter.PROPERTY_PACKAGE)) { 
            packageFilterEnabled = true;
        }
            
        if(readLogsGranted) {
            if (packageFilterEnabled) {
                /* filtered by package */
                return getText(R.string.violation_list_empty);
            } else {
                /* not filtered by package */
                SpannableStringBuilder builder = new SpannableStringBuilder();
                builder.append(getText(R.string.violation_list_empty));
                builder.append("\n\n");
                builder.append(getText(R.string.strictmode_violator_reference));
                return builder;
            }
        } else {
            /* no READ_LOGS permissions */            
            String jellyBeanNotSupported = getString(R.string.jelly_bean_not_supported);
            String permissionNotGranted = getString(R.string.permission_not_granted, 
                                                    Manifest.permission.READ_LOGS);            
            return permissionNotGranted + " " + jellyBeanNotSupported;
        }
    }
    
    private void updateEmptyView() {        
        setEmptyText(getEmptyText());
    }
    
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if(mListener != null)  {
            mListener.onViolationClicked(mAdapter.getItem(position));
        }
    }
    
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if(mListener != null)  {
            mListener.onViolationLongClicked(mAdapter.getItem(position));
        }
        return true;
    }
        
    public void setFilter(ViolationFilter filter) {
        if(!Objects.equal(mViolationFilter, filter)) {
            mViolationFilter = filter;
            Loader<?> loader = getLoaderManager().getLoader(LOADER_ID);
            if(loader != null) {
                ViolationLoader violationLoader = (ViolationLoader)loader;
                violationLoader.setFilter(mViolationFilter);
            }
            if(mAdapter != null) {
                mAdapter.setAllApplicationsMode(isAllApplicationsMode(mViolationFilter));
            }
        }
        updateEmptyView();
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_VIOLATION_FILTER, mViolationFilter);
    }
        
    /***************************/
    /**     LoaderCallback    **/
    /***************************/
 
    @Override
    public Loader<ViolationGroups> onCreateLoader(int id, Bundle args) {
        ViolationLoader loader = new ViolationLoader(getActivity());
        loader.setUpdateThrottle(LOADER_THROTTLE_TIMEOUT_MS);
        loader.setFilter(mViolationFilter);
        loader.setSorter(mViolationGroupComparator);
        return loader;
    }
    
    @Override
    public void onLoaderReset(Loader<ViolationGroups> loader) {
        mAdapter.swap(null);
    }
    
    @Override
    public void onLoadFinished(Loader<ViolationGroups> loader, ViolationGroups data) {        
        setListShown(true);
        mLastData = data;
        mAdapter.swap(data.getSortedGroups());
        
        int violationCount = 0;
        for (ViolationGroup group : data.getSortedGroups()) {
            violationCount += group.getSize();
        }
        
        mViolationCount.setText(Integer.toString(violationCount));
        mViolationGroupCount.setText(Integer.toString(data.getSortedGroups().size()));
        updateEmptyView();
    }
}

