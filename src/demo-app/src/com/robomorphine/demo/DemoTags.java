package com.robomorphine.demo;

import com.robomorphine.log.tag.Tags;

public class DemoTags extends Tags {

    public static final String DEMO_TAG_PREFIX = "rbmd";

    public static String getTag(Class<?> clazz) {
        return Tags.getTag(DEMO_TAG_PREFIX, clazz);
    }
}
