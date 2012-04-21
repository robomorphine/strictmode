package com.robomorphine.strictmode.violation.group;

import com.google.common.base.Preconditions;
import com.robomorphine.strictmode.violation.Violation;
import com.robomorphine.strictmode.violation.group.ViolationGroup.TimestampComparator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViolationGroups implements Cloneable {

    private Map<Violation, ViolationGroup> mGroups = new HashMap<Violation, ViolationGroup>();
    
    private Comparator<ViolationGroup> mGroupComparator = new TimestampComparator();
    private List<ViolationGroup> mSortedGroups = new ArrayList<ViolationGroup>();
    private boolean mNeedsSorting = false;
    
    private long mLatestTimestamp = 0;
    
    public ViolationGroups() {
    }
    
    @Override
    public ViolationGroups clone() {
        ViolationGroups groups = new ViolationGroups();
        for(ViolationGroup group : mSortedGroups) {
            for(Violation violation : group.getViolations()) {
                groups.add(violation);
            }
        }
        return groups;
    }
    
    public long getTimestamp() {
        return mLatestTimestamp;
    }
        
    public void add(Violation violation) {
        Preconditions.checkNotNull(violation);
        
        if(mLatestTimestamp < violation.getTimestamp()) {
            mLatestTimestamp = violation.getTimestamp();
        }

        ViolationGroup group = mGroups.get(violation);
        if(group != null) {
            group.add(violation);
        } else {
            group = new ViolationGroup(violation);
            mGroups.put(violation, group);
            mSortedGroups.add(group);
        }
        mNeedsSorting = true;
    }
    
    public void sort() {
        if(mNeedsSorting) {
            Collections.sort(mSortedGroups, mGroupComparator);
            mNeedsSorting = false;
        }
    }
    
    public List<ViolationGroup> getSortedGroups() {
        sort();
        return mSortedGroups;
    }
    
    public Collection<ViolationGroup> getGroups() {
        return mGroups.values();
    }
}
