package com.robomorphine.log.tag;

import com.robomorphine.log.tag.RbmTags;

import junit.framework.TestCase;

public class RbmTagsTest extends TestCase
{
    public void testValidPackage() {
        String tag = RbmTags.getTag(RbmTagsTest.class);
        assertTrue(tag.startsWith(RbmTags.ROBOMORPHINE_TAG_PREFIX));
    }
	
    public void testInvalidPackage() {
        try {
            RbmTags.getTag(String.class);
            fail();
        } catch (IllegalArgumentException ex) {
            // ok
        }
    }
	
    public void testNullClass() {
        try {
            RbmTags.getTag(null);
            fail();
        } catch (NullPointerException ex) {
            // ok
        }
    }
}
