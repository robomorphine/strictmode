package com.robomorphine.log.logger;

import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.ForwardingList;

public class MultiLogger extends ForwardingList<Logger> implements Logger {

    private final List<Logger> mLoggers = new LinkedList<Logger>();
       
    @Override
    protected List<Logger> delegate() {
        return mLoggers;
    }

    public void add(Logger... loggers) {
        for (Logger logger : loggers) {
            add(logger);
        }
    }
    
    @Override
    public void print(int level, String tag, String msg) {
        for(Logger logger : this) {
            logger.print(level, tag, msg);
        }
    }
}
