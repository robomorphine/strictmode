package com.robomorphine.log.tag;

import com.robomorphine.log.tag.DefaultTagFactory;
import com.robomorphine.log.tag.TagFactory;
import com.robomorphine.log.tag.TagsBase;

import junit.framework.TestCase;

public class TagsBaseTest extends TestCase {
			
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        TagsBase.setTagFactory(new DefaultTagFactory());
    }
	
    public void testSetTagPrefix_null() {
        try {
            TagsBase.setTagFactory((String) null);
            fail();
        } catch (NullPointerException ex) {
            // ok
        }

        try {
            TagsBase.setTagFactory((TagFactory) null);
            fail();
        } catch (NullPointerException ex) {
            // ok
        }
    }
	
    public void testGetTag_nulls() {
        TagsBase.getTag(null, String.class);
        try {
            TagsBase.getTag("", null);
            fail();
        } catch (NullPointerException ex) {
            // ok
        }
    }
	
    public void testDefaultFactory() {

        String prefix = "prefix";
        String expectedTag = prefix + "." + String.class.getSimpleName();
        assertEquals(String.class.getSimpleName(), TagsBase.getTag(null, String.class));
        assertEquals(expectedTag, TagsBase.getTag(prefix, String.class));
    }
	
    public void testCustomPrefixFactory() {

        String defaultPrefix = "defPrefix";
        String preferredPrefix = "prefPrefix";
        TagsBase.setTagFactory(defaultPrefix);

        String expectedTag = defaultPrefix + "." + String.class.getSimpleName();
        assertEquals(expectedTag, TagsBase.getTag(null, String.class));

        expectedTag = preferredPrefix + "." + String.class.getSimpleName();
        assertEquals(expectedTag, TagsBase.getTag(preferredPrefix, String.class));
    }
	
    public void testCustomFactory() {

        String defaultPrefix = "defPrefix";
        String pkgPrefix = "pkg";

        DefaultTagFactory factory = new DefaultTagFactory(defaultPrefix);
        factory.addPackagePrefix(String.class.getPackage().getName(), pkgPrefix);
        TagsBase.setTagFactory(factory);

        String expectedTag = pkgPrefix + "." + String.class.getSimpleName();
        assertEquals(expectedTag, TagsBase.getTag(null, String.class));
    } 
}
