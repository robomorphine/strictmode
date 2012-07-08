package com.robomorphine.strictmode.fragment;

import com.google.common.base.Objects;
import com.robomorphine.strictmode.R;
import com.robomorphine.strictmode.adapter.ViolationListAdapter;
import com.robomorphine.strictmode.loader.ViolationLoader;
import com.robomorphine.strictmode.violation.filter.ViolationFilter;
import com.robomorphine.strictmode.violation.group.ViolationGroup;
import com.robomorphine.strictmode.violation.group.ViolationGroups;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import javax.annotation.Nullable;

public class ViolationListFragment extends ListFragment 
                                   implements LoaderCallbacks<ViolationGroups>, 
                                              OnItemLongClickListener {

    public interface OnViolationClickListener {
        public void onViolationClicked(ViolationGroup group);
        public void onViolationLongClicked(ViolationGroup group);
    }
    
    private final static int LOADER_ID = 1;

    private final static long LOADER_THROTTLE_TIMEOUT_MS = 500;
    
    private final static String STATE_VIOLATION_FILTER = "violationFilter";
    
    private OnViolationClickListener mListener;
    private ViolationListAdapter mAdapter;
    private ViolationFilter mViolationFilter;
    private TextView mViolationCount;
    private TextView mViolationGroupCount;
    
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
            mViolationFilter = (ViolationFilter)savedInstanceState.getSerializable(STATE_VIOLATION_FILTER);
        }
                
        mAdapter = new ViolationListAdapter(getActivity());         
        mAdapter.setAllApplicationsMode(isAllApplicationsMode(mViolationFilter));
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.violation_list_fragment, container, false);
        LinearLayout listContainer = (LinearLayout)view.findViewById(R.id.container);
        mViolationCount = (TextView)view.findViewById(R.id.violation_count);
        mViolationGroupCount = (TextView)view.findViewById(R.id.violation_group_count);
        
        View listLayout = super.onCreateView(inflater, listContainer, savedInstanceState);
        listContainer.addView(listLayout);
        
        return view;
    }
    
    private String getEmptyText() {
        Context context = getActivity();        
        int result = context.checkCallingOrSelfPermission(Manifest.permission.READ_LOGS);
        if(result == PackageManager.PERMISSION_GRANTED) {
            return getString(R.string.dropbox_list_empty);
        } else {
            return getString(R.string.permission_not_granted, Manifest.permission.READ_LOGS);
        }
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        /* show menu items */
        setHasOptionsMenu(true);
        
        /* setup list */
        setListAdapter(mAdapter);
        setEmptyText(getEmptyText());
        setListShown(false);
        
        ListView list = getListView();
        list.setFastScrollEnabled(true);
        list.setOnItemLongClickListener(this);
                
        /* fetch data */
        getLoaderManager().initLoader(LOADER_ID, null, this);  
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
        return loader;
    }
    
    @Override
    public void onLoaderReset(Loader<ViolationGroups> loader) {
        mAdapter.swap(null);
    }
    
    @Override
    public void onLoadFinished(Loader<ViolationGroups> loader, ViolationGroups data) {        
        setListShown(true);
        mAdapter.swap(data.getSortedGroups());
        
        int violationCount = 0;
        for (ViolationGroup group : data.getSortedGroups()) {
            violationCount += group.getSize();
        }
        
        mViolationCount.setText(Integer.toString(violationCount));
        mViolationGroupCount.setText(Integer.toString(data.getSortedGroups().size()));
    }
}

