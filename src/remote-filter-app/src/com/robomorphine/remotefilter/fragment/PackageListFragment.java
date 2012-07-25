package com.robomorphine.remotefilter.fragment;

import com.robomorphine.remotefilter.R;
import com.robomorphine.remotefilter.activity.RemoteFilterActivity;
import com.robomorphine.remotefilter.adapter.PackageListAdapter;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class PackageListFragment extends Fragment implements OnItemClickListener {

    private ListView mListView;
    private PackageListAdapter mAdapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new PackageListAdapter(getActivity());
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.package_list_fragment, container, false);
        
        mListView = (ListView)view.findViewById(R.id.packageList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.startTracking();
    }

    @Override
    public void onPause() {
        super.onPause();
        mAdapter.stopTracking();
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
        PackageInfo info = mAdapter.getItem(position);
        String pkg = info.packageName;
        Intent intent = new Intent(getActivity(), RemoteFilterActivity.class);
        intent.putExtra(RemoteFilterActivity.EXTRA_PACKAGE, pkg);
        startActivity(intent);
    }
}
