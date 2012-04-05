package com.robomorphine.remoteprefs.fragment;

import com.robomorphine.remoteprefs.R;
import com.robomorphine.remoteprefs.adapter.DomainListAdapter;
import com.robomorphine.remoteprefs.loader.DomainListLoader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;

import java.util.Set;

public class DomainListFragment extends ListFragment implements LoaderCallbacks<Set<String>>{
    
    private final static int DOMAIN_LOADER_ID = 1;
    
    public final static String EXTRA_PACKAGE = "package";
    
    private DomainListAdapter mAdapter;
    private String mPackage;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new DomainListAdapter(getActivity());
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        if(mPackage == null) {
            setListShown(true);
            setEmptyText(getString(R.string.package_not_specified));
        } else {
            setListAdapter(mAdapter);
            setEmptyText(getString(R.string.domain_list_empty));
            setListShown(false);
            
            getLoaderManager().initLoader(DOMAIN_LOADER_ID, null, this);    
        }        
    }
    
    public void setPackage(String packageName) {
        mPackage = packageName;
    }

    /***************************/
    /**     LoaderCallback    **/
    /***************************/
    
    @Override
    public Loader<Set<String>> onCreateLoader(int id, Bundle args) {
        if(mPackage != null) {
            Context context = getActivity();
            return new DomainListLoader(context, mPackage);
        }
        return null;
    }
    
    @Override
    public void onLoaderReset(Loader<Set<String>> loader) {
        mAdapter.swap(null);
    }
    
    @Override
    public void onLoadFinished(Loader<Set<String>> loader, Set<String> data) {
        setListShown(true);
        mAdapter.swap(data);
    }

}
