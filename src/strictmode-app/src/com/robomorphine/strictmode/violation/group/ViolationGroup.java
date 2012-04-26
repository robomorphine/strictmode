package com.robomorphine.strictmode.violation.group;

import com.google.common.base.Preconditions;
import com.robomorphine.strictmode.violation.Violation;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * Violation group is a collections of violations that have identical
 * stack traces and are of the same type, but that occurred at different moments of time (at least).
 * 
 * The group has same hash code and equals to any violation it contains. So its easy to find
 * what group violations should be added to just by looking for something equal to violation. 
 */
public class ViolationGroup implements Serializable {
    
    private static final long serialVersionUID = 1L;

    public static class TimestampComparator implements Comparator<ViolationGroup> {
        @Override
        public int compare(ViolationGroup lhs, ViolationGroup rhs) {
            if(lhs.getTimestamp() == rhs.getTimestamp()) {
                return 0;
            } 
            return lhs.getTimestamp() > rhs.getTimestamp() ? -1 : 1;
        }
    }
    
    private final List<Violation> mViolations = new LinkedList<Violation>();
    private final List<Violation> mReadOnlyViolation = Collections.unmodifiableList(mViolations);
    
    private long mLatestTimestamp = 0;
    
    /**
     * ViolationGroup must have at least one violation.
     */
    public ViolationGroup(Violation violation) {
        Preconditions.checkNotNull(violation);
        mViolations.add(violation);
        mLatestTimestamp = violation.getTimestamp();
    }
    
    /**
     * The group must have at least one violation. 
     * This function returns first violation from the list,
     * but it might be different form violation that 
     * was passed to constructor (due to potential 
     * order changes when adding violations to group).
     */
    @Nonnull
    public Violation getViolation() {
        /* invariant: list of size >= 1 */
        return mViolations.get(0);
    }
    
    public boolean canAdd(Violation violation) {
        return (violation != null && violation.equals(getViolation()));
    }
    
    public void add(Violation violation) {
        Preconditions.checkNotNull(violation);
        Preconditions.checkArgument(canAdd(violation));
        
        /* TODO: add by timestamp order */
        mViolations.add(violation);
        if(mLatestTimestamp < violation.getTimestamp()) {
            mLatestTimestamp = violation.getTimestamp();
        }
    }
    
    public List<Violation> getViolations() {
        return mReadOnlyViolation;
    }
    
    /**
     * Return latest timestamp of all currently in the group.
     */
    public long getTimestamp() {
        return mLatestTimestamp;
    }    
    
    public int getSize() {
        return mViolations.size();
    }
    
    @Override
    public String toString() {
        return String.format("[%d] %s x %d", 
                             mLatestTimestamp, 
                             getViolation().getClass().getSimpleName(),
                             getSize());
    }
    
}