package com.robomorphine.remotefilter.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.robomorphine.remotefilter.R;
import com.robomorphine.remotefilter.adapter.RemoteFilterAdapter;

public class RemoteFilter extends Fragment {

    private ListView mListView;
    private RemoteFilterAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.remote_filter_fragment, container, false);
        mListView = (ListView)view.findViewById(R.id.subfilterList);

        TextView action = (TextView)view.findViewById(R.id.action);
        TextView level = (TextView)view.findViewById(R.id.level);
        TextView tag = (TextView)view.findViewById(R.id.tag);
        TextView msg = (TextView)view.findViewById(R.id.msg);

        action.setText(R.string.remote_filter_action);
        level.setText(R.string.remote_filter_level);
        tag.setText(R.string.remote_filter_tag);
        msg.setText(R.string.remote_filter_msg);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new RemoteFilterAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }
}
