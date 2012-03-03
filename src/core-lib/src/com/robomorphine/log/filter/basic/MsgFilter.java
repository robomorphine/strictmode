package com.robomorphine.log.filter.basic;

import com.robomorphine.log.filter.Filter;
import com.robomorphine.log.filter.Wildcard;

public class MsgFilter implements Filter {

    private Wildcard mWildcard;

    public MsgFilter(String wildcard) {
        mWildcard = new Wildcard(wildcard);
    }

    public MsgFilter(Wildcard wildcard) {
        mWildcard = wildcard;
    }
    
    public void setWildcard(String wildcard) {
        mWildcard = new Wildcard(wildcard);
    }
    
    public void setWildcard(Wildcard wildcard) {
        mWildcard = wildcard;
    }
    
    public Wildcard getWildcard() {
        return mWildcard;
    }

    public boolean apply(int level, String tag, String msg) {
        return mWildcard.apply(msg);
    };
    
    @Override
    public String toString() {        
        return "msg ~ " + mWildcard.toString();
    }
}
