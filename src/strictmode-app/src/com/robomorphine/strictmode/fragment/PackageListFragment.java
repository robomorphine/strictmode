package com.robomorphine.strictmode.fragment;


import com.google.common.base.Objects;
import com.robomorphine.strictmode.R;
import com.robomorphine.strictmode.adapter.PackageListAdapter;
import com.robomorphine.strictmode.entity.AndroidPackage;
import com.robomorphine.strictmode.loader.PackageListLoader;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import java.util.List;

public class PackageListFragment extends ListFragment 
                                 implements OnQueryTextListener,
                                            LoaderCallbacks<List<AndroidPackage>> {
    
    public interface PackageSelectionListener {
        public void onPackageSelected(String packageName);
    }
    
    private final static int PACKAGE_LOADER_ID = 1;

    private final static long   LOADER_THROTTLE_TIMEOUT_MS = 500;
    private final static String LOADER_ARG_FILTER = "filter";
    
    private final static String STATE_FILTER = "filter";
    private final static String STATE_PACKAGE = "package";
    
    public final static String RESULT_EXTRA_PACKAGE = "package";
    
    private PackageSelectionListener mListener;
    
    private PackageListAdapter mAdapter;
    private String mCurrentFilter;
    private String mCurrentPackage;
        
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof PackageSelectionListener) {
            mListener = (PackageSelectionListener)activity;
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
        mAdapter = new PackageListAdapter(getActivity());
        
        mCurrentFilter = null;
        if(savedInstanceState != null) {
            mCurrentFilter = savedInstanceState.getString(STATE_FILTER);
            mCurrentPackage = savedInstanceState.getString(STATE_PACKAGE);
        }
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        /* show menu items */
        setHasOptionsMenu(true);
        
        /* setup list */
        setListAdapter(mAdapter);
        setEmptyText(getString(R.string.package_list_empty));
        setListShown(false);
        
        ListView list = getListView();
        list.setFastScrollEnabled(true);
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        list.setItemChecked(list.getCheckedItemPosition(), false);
        
                
        /* fetch data */
        Bundle args = new Bundle();
        args.putString(LOADER_ARG_FILTER, mCurrentFilter);
        getLoaderManager().initLoader(PACKAGE_LOADER_ID, args, this);       
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_FILTER, mCurrentFilter);
        outState.putString(STATE_PACKAGE, mCurrentPackage);
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.add(R.string.menu_search);
        item.setIcon(android.R.drawable.ic_menu_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        SearchView sv = new SearchView(getActivity());
        sv.setQuery(mCurrentFilter, false);
        sv.setOnQueryTextListener(this);
        item.setActionView(sv);
    }
    
    @Override
    public boolean onQueryTextChange(String newText) {        
        mCurrentFilter = newText;
        if(TextUtils.isEmpty(newText)) {
           mCurrentFilter = null; 
        }

        Bundle args = new Bundle();
        args.putString(LOADER_ARG_FILTER, mCurrentFilter);
        getLoaderManager().restartLoader(PACKAGE_LOADER_ID, args, this);
        return true;
    }    
    
    @Override
    public boolean onQueryTextSubmit(String query) {
        /* don't care about this, already handled it */
        return true;
    }
    
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String newPackage = null;
        if(position >= 0) {
            newPackage = mAdapter.getItemPackageName(position);
        }
        
        if(!Objects.equal(newPackage, mCurrentFilter)) {
            mCurrentPackage = newPackage;
            if(mListener != null) {
                mListener.onPackageSelected(mCurrentPackage);
            }
        }
    }    
    
    /***************************/
    /**     LoaderCallback    **/
    /***************************/
 
    @Override
    public Loader<List<AndroidPackage>> onCreateLoader(int id, Bundle args) {
        String filter = null;
        if(args != null) {
            filter = args.getString(LOADER_ARG_FILTER);
        }
        
        PackageListLoader loader = new PackageListLoader(getActivity(), filter);
        loader.setUpdateThrottle(LOADER_THROTTLE_TIMEOUT_MS);
        return loader;
    }
    
    @Override
    public void onLoaderReset(Loader<List<AndroidPackage>> loader) {
        mAdapter.swap(null);
    }
    
    @Override
    public void onLoadFinished(Loader<List<AndroidPackage>> loader, List<AndroidPackage> data) {        
        setListShown(true);
        mAdapter.swap(data);
        
        /* restore checked item */
        if(mCurrentPackage != null) {
            for(int i = 0; i < mAdapter.getCount(); i++) {
                if(Objects.equal(mCurrentPackage, mAdapter.getItemPackageName(i))) {
                    getListView().setItemChecked(i, true);
                    break;
                }
            }
        }
    }
}

