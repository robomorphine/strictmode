package com.robomorphine.remoteprefs.fragment;


import com.robomorphine.remoteprefs.AndroidPackage;
import com.robomorphine.remoteprefs.R;
import com.robomorphine.remoteprefs.activity.DomainListActivity;
import com.robomorphine.remoteprefs.adapter.PackageListAdapter;
import com.robomorphine.remoteprefs.loader.PackageListLoader;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import java.util.List;

public class PackageListFragment extends ListFragment 
                                 implements LoaderCallbacks<List<AndroidPackage>>{
    
    private final static int PACKAGE_LOADER_ID = 1;
    
    private PackageListAdapter mAdapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new PackageListAdapter(getActivity());
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        setListAdapter(mAdapter);
        setEmptyText(getString(R.string.package_list_empty));
        setListShown(false);
                
        getLoaderManager().initLoader(PACKAGE_LOADER_ID, null, this);        
    }
    
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(getActivity(), DomainListActivity.class);
        PackageInfo info = mAdapter.getItem(position).getInfo();
        intent.putExtra(DomainListActivity.EXTRA_PACKAGE, info.packageName);
        startActivity(intent);
    }
    
    /***************************/
    /**     LoaderCallback    **/
    /***************************/
    
    
    @Override
    public Loader<List<AndroidPackage>> onCreateLoader(int id, Bundle args) {
        return new PackageListLoader(getActivity());
    }
    
    @Override
    public void onLoaderReset(Loader<List<AndroidPackage>> loader) {
        mAdapter.swap(null);
    }
    
    @Override
    public void onLoadFinished(Loader<List<AndroidPackage>> loader, List<AndroidPackage> data) {
        setListShown(true);
        mAdapter.swap(data);
    }
}
