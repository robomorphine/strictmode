package com.robomorphine.log.filter;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;

public class Wildcard
{
    public static final String STAR = "*";

    private final String mWildcard;
    private final String mExact;
    private final String mPrefix;
    private final String mContains;
    private final String mSuffix;
    
    public static boolean isValid(String wildcard) {
        try {
            new Wildcard(wildcard);
            return true;
        } catch(IllegalArgumentException ex) {
            return false;
        }
    }

    public Wildcard(String wildcard) {
        Preconditions.checkNotNull(wildcard);
        Preconditions.checkArgument(wildcard.length() > 0);

        String exact = null;
        String prefix = null;
        String suffix = null;
        String contains = null;

        int pos = wildcard.indexOf(STAR);        
        if(pos < 0) {
            /* no stars in the string - exact match */
            exact = wildcard;            
        } else if(pos == 0 && wildcard.endsWith(STAR)) {
            /* start and ends with a star - "contains" match */
            
            if(wildcard.length() == 1) {
                /* wildcard: "*" - means any */
                contains = null;
            } else if(wildcard.length() == 2) {
                /* wildcard: "**". Not a meaningful wildcard. */
                throw new IllegalArgumentException("Empty contains wildcard are invalid: "+wildcard);
            } else {
                /* wildcard: "*abc*", extract "abc" substring. */
                contains = wildcard.replaceAll("\\"+STAR, "");    
            }
            
        } else if(wildcard.indexOf(STAR, pos + 1) >= 0) {
            /* has two stars - invalid */
            throw new IllegalArgumentException("Multiple * symbols are not supported!");
        } else {
            /* has one star, extract prefix and suffix */
            prefix = wildcard.substring(0, pos);
            suffix = wildcard.substring(pos + 1);
            if (prefix.length() == 0) {
                prefix = null;
            }

            if (suffix.length() == 0) {
                suffix = null;
            }
        }    

        mWildcard = wildcard;
        mExact = exact;
        mPrefix = prefix;
        mSuffix = suffix;
        mContains = contains;
    }
    
    @VisibleForTesting
    String getExact() {
        return mExact;
    }
		
    @VisibleForTesting
    String getPrefix() {
        return mPrefix;
    }

    @VisibleForTesting
    String getSuffix() {
        return mSuffix;
    }

    @VisibleForTesting
    String getContains() {
        return mContains;
    }	
	
    public boolean apply(String value) {
        
        if(mExact != null) {
            return value.equals(mExact);
        }
        
        if (mPrefix != null) {
            if (!value.startsWith(mPrefix)) {
                return false;
            }
        }

        if (mSuffix != null) {
            if (!value.endsWith(mSuffix)) {
                return false;
            }
        }

        if (mContains != null) {
            if (!value.contains(mContains)) {
                return false;
            }
        }

        return true;
    }
    
    @Override
    public String toString() {
        return mWildcard;
    }
}
