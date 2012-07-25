package com.robomorphine.remotefilter.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.robomorphine.log.Log;
import com.robomorphine.log.filter.advanced.PriorityFilter.FilterAction;
import com.robomorphine.log.filter.remote.RemoteFilter;
import com.robomorphine.log.filter.remote.RemoteSubfilter;
import com.robomorphine.remotefilter.R;

public class RemoteFilterAdapter extends BaseAdapter {

    private final Context mContext;
    private final LayoutInflater mInflater;
    private RemoteFilter mFilter;

    public RemoteFilterAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mFilter = new RemoteFilter(mContext);

        mFilter.add(new RemoteSubfilter(Log.ERROR, "*", "*msg*"), FilterAction.Ignore);
        mFilter.add(new RemoteSubfilter(Log.INFO, "*err", "*23asdfasdfas*"), FilterAction.Exclude);
        mFilter.add(new RemoteSubfilter(Log.ERROR, "pp*", "*er*"), FilterAction.Include);
        mFilter.add(new RemoteSubfilter(Log.ERROR, "*", "*msg*"), FilterAction.Ignore);
        mFilter.add(new RemoteSubfilter(Log.ERROR, "*", "*msg*"), FilterAction.Ignore);
        mFilter.add(new RemoteSubfilter(Log.ERROR, "*", "*msg*"), FilterAction.Ignore);
        mFilter.add(new RemoteSubfilter(Log.INFO, "*err", "*23*"), FilterAction.Exclude);
        mFilter.add(new RemoteSubfilter(Log.INFO, "*err", "*23*"), FilterAction.Exclude);
        mFilter.add(new RemoteSubfilter(Log.INFO, "*err", "*23*"), FilterAction.Exclude);
        mFilter.add(new RemoteSubfilter(Log.ERROR, "pp*", "*er*"), FilterAction.Include);
        mFilter.add(new RemoteSubfilter(Log.ERROR, "pp*", "*er*"), FilterAction.Include);
        mFilter.add(new RemoteSubfilter(Log.ERROR, "pp*", "*er*"), FilterAction.Include);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteSubfilter getItem(int position) {
        return mFilter.getFilter(position);
    }

    @Override
    public int getCount() {
        return mFilter.size();
    }

    private Drawable getDrawable(int resId) {
        Resources res = mContext.getResources();
        return res.getDrawable(resId);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if(view == null) {
            view = mInflater.inflate(R.layout.remote_filter_fragment_item, parent, false);
        }

        TextView action = (TextView)view.findViewById(R.id.action);
        TextView level = (TextView)view.findViewById(R.id.level);
        TextView tag = (TextView)view.findViewById(R.id.tag);
        TextView msg = (TextView)view.findViewById(R.id.msg);

        RemoteSubfilter subfilter = mFilter.getFilter(position);
        action.setText(mFilter.getAction(position).name());
        level.setText(Log.LEVEL_NAMES[subfilter.getLevel()]);
        tag.setText(subfilter.getTagWildcard().toString());
        msg.setText(subfilter.getMsgWildcard().toString());

        switch(mFilter.getAction(position)) {
            case Ignore:
                view.setBackgroundDrawable(getDrawable(R.drawable.background_filter_ignore));
                break;
            case Exclude:
                view.setBackgroundDrawable(getDrawable(R.drawable.background_filter_exclude));
                break;
            case Include:
                view.setBackgroundDrawable(getDrawable(R.drawable.background_filter_include));
                break;
        }
        view.setSelected(true);

        return view;
    }
}
