package com.robomorphine.strictmode.viewer.violation.group;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Environment;

import com.google.common.base.Preconditions;
import com.robomorphine.strictmode.viewer.violation.ThreadViolation;
import com.robomorphine.strictmode.viewer.violation.Violation;
import com.robomorphine.strictmode.viewer.violation.filter.ViolationFilter;
import com.robomorphine.strictmode.viewer.violation.group.ViolationGroup.TimestampComparator;

public class ViolationGroups {

    private final Map<Violation, ViolationGroup> mGroups = new HashMap<Violation, ViolationGroup>();
        
    private final List<ViolationGroup> mSortedGroups = new ArrayList<ViolationGroup>();
        
    private long mLatestTimestamp = 0;
        
    public static ViolationGroups clone(ViolationGroups from, ViolationFilter filter) {
        ViolationGroups groups = new ViolationGroups();
        for(ViolationGroup group : from.mSortedGroups) {
            for(Violation violation : group.getViolations()) {
                if(filter == null || filter.matches(violation)) {
                    groups.add(violation);
                }
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
    }
    
    public void sort(Comparator<ViolationGroup> comparator) {
    	if (comparator == null) {
    		comparator = new TimestampComparator();
    	}
        Collections.sort(mSortedGroups, comparator);
    }
    
    public List<ViolationGroup> getSortedGroups() {
    	return mSortedGroups;
    }
    
    public Collection<ViolationGroup> getGroups() {
        return mGroups.values();
    }
    
    /**
     * TODO: implement in a better way
     * Ugly way of exporting
     */
    public void export(Context context) throws IOException {
    	File file = new File(Environment.getExternalStorageDirectory(), "strictmode.csv");
    	OutputStreamWriter ow = new OutputStreamWriter(new FileOutputStream(file));
    	BufferedWriter writer = new BufferedWriter(ow);
    	
    	try {
    		writer.append("type, count, timestamp, time used, loop-counter, exception message, stack\n");
	    	for (ViolationGroup group : mSortedGroups) {
	    		export(writer, group);
	    		writer.append("\n");
	    	}
    	} finally {
    		writer.close();
    	}
    }
    
    public void export(BufferedWriter writer, ViolationGroup group) throws IOException {
    	StringBuilder builder = new StringBuilder();
    	
    	//type, count, time used, exception name, exception message
    	builder.append(group.getViolation().getClass().getSimpleName());
    	builder.append(", ");
    	builder.append(group.getSize());
    	builder.append(", ");
    	builder.append(group.getTimestamp());
    	builder.append(", ");
    	
    	long elapsed = 0;
    	for (Violation violation : group.getViolations()) {
    		if (violation instanceof ThreadViolation) {
    			ThreadViolation threadViolation = (ThreadViolation)violation;
    			elapsed += threadViolation.getDuration();
    		}
    	}
    	builder.append(elapsed);
    	builder.append(", ");

    	int loopCounter = 0;
    	String key = "Loop-Violation-Number";
    	if (group.getViolation().getHeaders().containsKey(key)) {
    		String rawLoopCounter = group.getViolation().getHeaders().get(key);
    		try {
    			loopCounter = Integer.parseInt(rawLoopCounter);
    		} catch (NumberFormatException ex) {
    			// ignore for now
    		}
    	}
    	builder.append(loopCounter);
    	builder.append(",");
    	
    	builder.append("\"" + group.getViolation().getException().getMessage() + "\"");
    	builder.append(", ");    	
    	
    	//hacky way to print exception
    	for (StackTraceElement element : group.getViolation().getException().getStackTrace()) {
    		builder.append(element.toString() + " ## ");
    	}
    	writer.append(builder.toString());
    }
}
