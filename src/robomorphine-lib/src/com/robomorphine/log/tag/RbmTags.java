package com.robomorphine.log.tag;

import javax.annotation.concurrent.ThreadSafe;

import static com.google.common.base.Preconditions.*;

/**
 * Shortcut class for generating tags for classes from robomorphine library.
 * 
 * Note: should not be used by classes outside of robomorphine library. 
 */
@ThreadSafe
public class RbmTags extends TagsBase {

    public static final String ROBOMORPHINE_TAG_PREFIX = "rbm";
    public static final String ROBOMORPHINE_PACKAGE = "com.robomorphine";

    /**
     * Generate tag for robomorphine class
     */
    public static String getTag(Class<?> clazz) {
        checkNotNull(clazz);
        checkPackage(ROBOMORPHINE_PACKAGE, clazz);
        return Tags.getTag(ROBOMORPHINE_TAG_PREFIX, clazz);
    }
}