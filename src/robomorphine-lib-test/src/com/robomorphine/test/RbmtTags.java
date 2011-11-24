package com.robomorphine.test;

import static com.google.common.base.Preconditions.checkNotNull;

import com.robomorphine.log.tag.Tags;

public class RbmtTags extends Tags  {
    
    public static final String ROBOMORPHINE_TEST_TAG_PREFIX = "rbmt";
    public static final String ROBOMORPHINE_TEST_PACKAGE = "com.robomorphine.test";

    /**
     * Generate tag for robomorphine class
     */
    public static String getTag(Class<?> clazz) {
        checkNotNull(clazz);
        checkPackage(ROBOMORPHINE_TEST_PACKAGE, clazz);
        return Tags.getTag(ROBOMORPHINE_TEST_TAG_PREFIX, clazz);
    }
}
