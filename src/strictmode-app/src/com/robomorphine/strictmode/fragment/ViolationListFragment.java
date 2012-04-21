package com.robomorphine.strictmode.fragment;

import com.robomorphine.strictmode.R;
import com.robomorphine.strictmode.adapter.ViolationListAdapter;
import com.robomorphine.strictmode.loader.ViolationLoader;
import com.robomorphine.strictmode.violation.group.ViolationGroups;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.widget.ListView;

public class ViolationListFragment extends ListFragment implements LoaderCallbacks<ViolationGroups>{

    private final static int LOADER_ID = 1;

    private final static long LOADER_THROTTLE_TIMEOUT_MS = 500;
    
    private ViolationListAdapter mAdapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mAdapter = new ViolationListAdapter(getActivity());
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        /* show menu items */
        setHasOptionsMenu(true);
        
        /* setup list */
        setListAdapter(mAdapter);
        setEmptyText(getString(R.string.dropbox_list_empty));
        setListShown(false);
        
        ListView list = getListView();
        list.setFastScrollEnabled(true);
                
        /* fetch data */
        getLoaderManager().initLoader(LOADER_ID, null, this);  
    }
        
    /***************************/
    /**     LoaderCallback    **/
    /***************************/
 
    @Override
    public Loader<ViolationGroups> onCreateLoader(int id, Bundle args) {
        ViolationLoader loader = new ViolationLoader(getActivity());
        loader.setUpdateThrottle(LOADER_THROTTLE_TIMEOUT_MS);
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
    }
}

