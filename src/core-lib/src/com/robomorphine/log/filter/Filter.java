package com.robomorphine.log.filter;

public interface Filter {
    boolean apply(int level, String tag, String msg);
}
