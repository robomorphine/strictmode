package com.robomorphine.log.logger;

import com.google.common.base.Preconditions;
import com.robomorphine.log.filter.Filter;

public class FilterLogger implements Logger {
	
	private Filter mFilter;
	private Logger mLogger;
	
    public FilterLogger(Logger targetLogger, Filter filter) {
        Preconditions.checkNotNull(targetLogger);
        mLogger = targetLogger;
        mFilter = filter;
    }
	
    public FilterLogger(Logger targetLogger) {
        this(targetLogger, null);
    }
    
    public void setFilter(Filter filter) {
        mFilter = filter;
    }
    
    public Filter getFilter() {
        return mFilter;
    }
    
    public void setLogger(Logger logger) {
        Preconditions.checkNotNull(logger);
        mLogger = logger;
    }
    
    public Logger getLogger() {
        return mLogger;
    }
	
    @Override
    public void print(int level, String tag, String msg) {
        if(mFilter == null || mFilter.apply(level, tag, msg)) {
            mLogger.print(level, tag, msg);
        }        
    }
}
