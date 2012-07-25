package com.robomorphine.log.tag;

import static com.google.common.base.Preconditions.*;

import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.google.common.base.Preconditions;

/**
 * Central point of tag generation. This class stores one tag factory and uses 
 * it for tag generation. This tag factory can be changed at any time.
 * 
 * Use getTag function to easily create tag. 
 */
@ThreadSafe
public abstract class TagsBase { //NOPMD
    private static TagFactory sTagFactory = new DefaultTagFactory();
    private static Object sSync = new Object();

    /**
     * Set default tag factory with custom prefix
     * 
     * @param prefix
     */
    public static void setTagFactory(String prefix) {
        Preconditions.checkNotNull(prefix);
        synchronized (sSync) {
            setTagFactory(new DefaultTagFactory(prefix));
        }
    }

    /**
     * Sets custom tag factory.
     * 
     * @param factory
     */
    public static void setTagFactory(TagFactory factory) {
        Preconditions.checkNotNull(factory);
        synchronized (sSync) {
            sTagFactory = factory;
        }
    }

    /**
     * Returns current tag factory.
     * 
     * @return
     */
    public static TagFactory getTagFactory() {
        synchronized (sSync) {
            return sTagFactory;
        }
    }

    /**
     * Creates tag.
     * 
     * @param preferredPrefix - prefix that is used when no prefixes are associated with this class.
     * @param clazz - class that is going to use this tag.
     * @return tag (never returns null)
     */
    public static String getTag(@Nullable String preferredPrefix, Class<?> clazz) {
        Preconditions.checkNotNull(clazz);
        synchronized (sSync) {
            return sTagFactory.createTag(preferredPrefix, clazz);
        }
    }

    /**
     * Verify that clazz is from package or subpackage of "packageName"
     * 
     * @param packageName
     * @param clazz
     */
    protected static void checkPackage(String packageName, Class<?> clazz) {
        Preconditions.checkNotNull(packageName);
        Preconditions.checkNotNull(clazz);
        checkArgument(clazz.getPackage().getName().startsWith(packageName));
    }
}
