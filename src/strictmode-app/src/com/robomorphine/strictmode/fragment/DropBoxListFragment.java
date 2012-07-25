package com.robomorphine.strictmode.fragment;

import com.robomorphine.strictmode.R;
import com.robomorphine.strictmode.adapter.DropBoxListAdapter;
import com.robomorphine.strictmode.entity.DropBoxItem;
import com.robomorphine.strictmode.loader.DropBoxLoader;

import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.List;

public class DropBoxListFragment extends ListFragment implements LoaderCallbacks<List<DropBoxItem>>{

    private final static int DROPBOX_LOADER_ID = 1;

    private final static long   LOADER_THROTTLE_TIMEOUT_MS = 500;
    private final static String LOADER_ARG_FILTER = "filter";
    
    private final static String STATE_FILTER = "filter";
    
    
    private DropBoxListAdapter mAdapter;
    private String mCurrentFilter;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mAdapter = new DropBoxListAdapter(getActivity());
        
        mCurrentFilter = null;
        if(savedInstanceState != null) {
            mCurrentFilter = savedInstanceState.getString(STATE_FILTER);
        }
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
        Bundle args = new Bundle();
        args.putString(LOADER_ARG_FILTER, mCurrentFilter);
        getLoaderManager().initLoader(DROPBOX_LOADER_ID, args, this);  
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_FILTER, mCurrentFilter);
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.drop_box_fragment, menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_thread) {
            SharedPreferences prefs = getActivity().getSharedPreferences("test", 0);
            for(int i = 0; i < 1000; i++) {
                prefs.edit().commit();
            }
        } else if(id == R.id.menu_vm) {
            ContentResolver cr = getActivity().getContentResolver();
            cr.query(ContactsContract.RawContacts.CONTENT_URI, null, null, null, null);
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    private int getContainerViewId() {
        return ((View)getView().getParent()).getId();    
    }
    
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        DropBoxItem item = mAdapter.getItem(position);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
                
        DropBoxItemFragment fragment = DropBoxItemFragment.newInstance(item);

         
        transaction.replace(getContainerViewId(), fragment);
        transaction.hide(this);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    
    /***************************/
    /**     LoaderCallback    **/
    /***************************/
 
    @Override
    public Loader<List<DropBoxItem>> onCreateLoader(int id, Bundle args) {
        String filter = null;
        if(args != null) {
            filter = args.getString(LOADER_ARG_FILTER);
        }
        
        DropBoxLoader loader = new DropBoxLoader(getActivity(), filter);
        loader.setUpdateThrottle(LOADER_THROTTLE_TIMEOUT_MS);
        return loader;
    }
    
    @Override
    public void onLoaderReset(Loader<List<DropBoxItem>> loader) {
        mAdapter.swap(null);
    }
    
    @Override
    public void onLoadFinished(Loader<List<DropBoxItem>> loader, List<DropBoxItem> data) {        
        setListShown(true);
        mAdapter.swap(data);      
    }
}

