package com.robomorphine.remotefilter.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.robomorphine.remotefilter.R;

public class RemoteFilterEditPanel extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.remote_filter_edit_panel_fragment, container, false);
        return view;
    }
}
