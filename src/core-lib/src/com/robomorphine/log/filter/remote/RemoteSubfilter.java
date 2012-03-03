package com.robomorphine.log.filter.remote;

import android.util.Log;

import com.robomorphine.log.filter.Filter;
import com.robomorphine.log.filter.Wildcard;
import com.robomorphine.log.filter.basic.LevelFilter;
import com.robomorphine.log.filter.basic.MsgFilter;
import com.robomorphine.log.filter.basic.TagFilter;
import com.robomorphine.log.filter.bool.AndFilter;

public class RemoteSubfilter implements Filter {

    private AndFilter mAndFilter;
    private LevelFilter mLevelFilter;
    private TagFilter mTagFilter;
    private MsgFilter mMsgFilter;

    public RemoteSubfilter(LevelFilter levelFilter, TagFilter tagFilter, MsgFilter msgFilter) {
        mLevelFilter = levelFilter;
        mTagFilter = tagFilter;
        mMsgFilter = msgFilter;

        mAndFilter = new AndFilter();
        mAndFilter.add(mLevelFilter, mTagFilter, mMsgFilter);
    }

    public RemoteSubfilter(int level, String tag, String msg) {
        this(new LevelFilter(level), new TagFilter(tag), new MsgFilter(msg));
    }

    public RemoteSubfilter() {
        this(Log.VERBOSE, "*", "*");
    }

    public void setLevel(int level) {
        mLevelFilter.setLevel(level);
    }

    public int getLevel() {
        return mLevelFilter.getLevel();
    }

    public void setTagWildcard(String wildcard) {
        mTagFilter.setWildcard(wildcard);
    }

    public void setTagWildcard(Wildcard wildcard) {
        mTagFilter.setWildcard(wildcard);
    }

    public String getTagWildcard() {
        return mTagFilter.getWildcard().toString();
    }

    public void setMsgWildcard(String wildcard) {
        mMsgFilter.setWildcard(wildcard);
    }

    public void setMsgWildcard(Wildcard wildcard) {
        mMsgFilter.setWildcard(wildcard);
    }

    public String getMsgWildcard() {
        return mMsgFilter.getWildcard().toString();
    }

    @Override
    public boolean apply(int level, String tag, String msg) {
        return mAndFilter.apply(level, tag, msg);
    }
}
