package com.robomorphine.log.filter.bool;

import com.robomorphine.log.filter.Filter;
import com.robomorphine.log.filter.MultiFilter;

public class OrFilter extends MultiFilter {
        
    public OrFilter() {
    }

    public OrFilter(Filter... filters) {
        add(filters);
    }

    @Override
    public boolean apply(int level, String tag, String msg) {
        for (Filter cur : this) {
            if (cur.apply(level, tag, msg)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        if(size() == 0) {
            return "false";
        }
        
        StringBuilder builder = new StringBuilder();
        
        for(int i = 0; i < size(); i++) {
            if(i > 0) {
                builder.append(" || ");
            }
            builder.append("[");
            builder.append(get(0).toString());
            builder.append("]");
        }
        
        return builder.toString();
    }
}
