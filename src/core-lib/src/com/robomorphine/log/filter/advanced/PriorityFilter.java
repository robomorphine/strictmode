package com.robomorphine.log.filter.advanced;

import java.util.LinkedList;
import java.util.List;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.robomorphine.log.filter.Filter;

public class PriorityFilter<F extends Filter> implements Filter {

    public enum FilterAction {
        Include, /* if filter matches, include the log entry */
        Exclude, /* if filter matches, exclude the log entry */
        Ignore   /* filter is disabled and will be ignored */
    };

    private class Entry {
        public Entry(F filter, FilterAction action) {
            this.filter = filter;
            this.action = action;
        }

        protected F filter;
        protected FilterAction action;
    }

    private FilterAction mDefaultAction = FilterAction.Include;
    private final List<Entry> mFilters = new LinkedList<Entry>();

    public PriorityFilter(FilterAction defaultAction) {
        Preconditions.checkNotNull(defaultAction);
        setDefaultAction(defaultAction);
    }

    public PriorityFilter() {
        this(FilterAction.Include);
    }

    /***************************
     **     Default action    **
     ***************************/

    /**
     * Default action is used when there is no matching filters.
     * Valid options are: FilterAction.Include and Filter.Action.Exclude.
     * Invalid option: FilterAction.Ignore
     * @param action
     */
    public final void setDefaultAction(FilterAction action) {
        Preconditions.checkNotNull(action);
        Preconditions.checkArgument(action == FilterAction.Include ||
                                    action == FilterAction.Exclude);
        mDefaultAction = action;
    }

    /**
     * Default action is FilterAction.Include;
     */
    public FilterAction getDefaultAction() {
        return mDefaultAction;
    }

    /***************************
     **    Managing filters   **
     ***************************/

    public int indexOf(F filter) {
        Preconditions.checkNotNull(filter);
        for (int i = 0; i < mFilters.size(); i++) {
            if(mFilters.get(i).filter == filter) {
                return i;
            }
        }
        return -1;
    }

    public boolean contains(F filter) {
        Preconditions.checkNotNull(filter);
        return indexOf(filter) >= 0;
    }

    public boolean add(F filter, FilterAction action) {
        Preconditions.checkNotNull(filter);
        Preconditions.checkNotNull(action);
        if (!contains(filter)) {
            mFilters.add(new Entry(filter, action));
            return true;
        }
        return false;
    }

    public boolean add(int location, F filter, FilterAction action) {
        Preconditions.checkNotNull(filter);
        Preconditions.checkNotNull(action);
        if (!contains(filter)) {
            mFilters.add(location, new Entry(filter, action));
            return true;
        }
        return false;
    }

    public void remove(int location) {
        mFilters.remove(location);
    }

    public void clear() {
        mFilters.clear();
    }

    public F getFilter(int location) {
        return mFilters.get(location).filter;
    }

    public FilterAction getAction(int location) {
        return mFilters.get(location).action;
    }

    public void setAction(int location, FilterAction action) {
        mFilters.get(location).action = action;
    }

    public int size() {
        return mFilters.size();
    }

    /**************************/
    /**  Reordering filters  **/
    /**************************/

    @VisibleForTesting 
    protected void swap(int x, int y) {
        if(x == y) return; //NOPMD
        Entry xEntry = mFilters.get(x);
        mFilters.set(x, mFilters.get(y));
        mFilters.set(y, xEntry);
    }

    public void moveUp(int location) {
        if (location != 0) {
            swap(location, location - 1);
        }
    }

    public void moveDown(int location) {
        if (location != mFilters.size() - 1) {
            swap(location, location + 1);
        }
    }

    public void moveFirst(int location) {
        Entry entry = mFilters.get(location);
        mFilters.remove(location);
        mFilters.add(0, entry);
    }

    public void moveLast(int location) {
        Entry entry = mFilters.get(location);
        mFilters.remove(location);
        mFilters.add(mFilters.size(), entry);
    }

    public void reverse() {
        for(int i = 0; i < mFilters.size() / 2; i++) {
            swap(i, mFilters.size() - 1 - i);
        }
    }

    /**************************/
    /**      Filtering       **/
    /**************************/

    @Override
    public boolean apply(int level, String tag, String msg) {
        for(Entry entry : mFilters) {
            if(entry.action == FilterAction.Ignore) {
                continue;
            }

            if(entry.filter.apply(level, tag, msg)) {
                switch(entry.action) {//NOPMD
                    case Exclude: return false;
                    case Include: return true;
                }
            }
        }

        switch(mDefaultAction) {//NOPMD: no default on purpose
            case Exclude: return false;
            case Include: return true;
            case Ignore: throw new IllegalStateException();
        }

        /* should never reach this point */
        return true;
    }

    /**************************/
    /**         Misc         **/
    /**************************/

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();
        builder.append("[filters: \n");
        for(int i = 0; i < mFilters.size(); i++) {
            builder.append(i);
            builder.append(": ");
            builder.append(mFilters.get(i).filter.toString());
            builder.append(" -> ");
            builder.append(mFilters.get(i).action.name());
            builder.append(";\n");
        }
        builder.append("]");

        return super.toString();
    }
}
