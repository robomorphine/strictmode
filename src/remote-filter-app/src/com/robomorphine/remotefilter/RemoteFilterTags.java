package com.robomorphine.remotefilter;

import com.robomorphine.log.tag.Tags;

public class RemoteFilterTags extends Tags {
    public static final String REMOTE_FILTER_TAG_PREFIX = "rbmrf";

    public static String getTag(Class<?> clazz) {
        return Tags.getTag(REMOTE_FILTER_TAG_PREFIX, clazz);
    }
}
