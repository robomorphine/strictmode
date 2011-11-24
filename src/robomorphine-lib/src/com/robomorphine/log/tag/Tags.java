package com.robomorphine.log.tag;

import javax.annotation.concurrent.ThreadSafe;

/**
 * Default tag generator. 
 * Basically a shortcut to TagsBase with no preferred prefix. 
 */
@ThreadSafe
public class Tags extends TagsBase
{		
    public static String getTag(Class<?> clazz) {
        return getTag(null, clazz);
    }
}
