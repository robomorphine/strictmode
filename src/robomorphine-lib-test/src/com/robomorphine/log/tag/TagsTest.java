package com.robomorphine.log.tag;

import junit.framework.TestCase;

public class TagsTest extends TestCase
{
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Tags.setTagFactory(new DefaultTagFactory());
    }

    public void testTags() {
        assertEquals(String.class.getSimpleName(), Tags.getTag(String.class));
    }
	
    public void testNullClass() {
        try {
            Tags.getTag(null);
            fail();
        } catch (NullPointerException ex) {
            // ok
        }
    }
}
