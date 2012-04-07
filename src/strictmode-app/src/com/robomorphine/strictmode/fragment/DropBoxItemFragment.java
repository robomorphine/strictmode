package com.robomorphine.strictmode.fragment;

import com.robomorphine.strictmode.R;
import com.robomorphine.strictmode.entity.DropBoxItem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DropBoxItemFragment extends Fragment {
    
    public static String EXTRA_DROPBOX_ITEM = "item";
    
    public static DropBoxItemFragment newInstance(DropBoxItem item) {
        DropBoxItemFragment fragment = new DropBoxItemFragment();
        Bundle args = new Bundle();
        args.putBundle(DropBoxItemFragment.EXTRA_DROPBOX_ITEM, item.toBundle());
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dropbox_item_fragment, container, false);
        TextView tagView = (TextView)view.findViewById(R.id.tag);        
        TextView timestampView = (TextView)view.findViewById(R.id.timestamp);
        TextView dataView = (TextView)view.findViewById(R.id.data);
        
        Bundle args = getArguments();
        Bundle itemBundle = null;
        DropBoxItem item = null;
        
        if(args != null) {
            itemBundle = args.getBundle(EXTRA_DROPBOX_ITEM);
        }
        if(itemBundle != null) {
            item = DropBoxItem.fromBundle(itemBundle);
        }
        
        if(item == null) {
            item = new DropBoxItem(getString(R.string.not_available), -1, 
                                   getString(R.string.dropbox_item_no_entry));
        }
        
        tagView.setText(item.getTag());
        timestampView.setText(Long.toString(item.getTimestamp()));
        dataView.setText(item.getData());
        return view;
    }

}
