package com.robomorphine.log.tag;

import com.robomorphine.log.tag.DefaultTagFactory;

import junit.framework.TestCase;

public class DefaultTagFactoryTest extends TestCase {
		
	/**
	 * DefaultTagFactory(String)
	 * 
	 * Verify that default null prefix is rejected
	 */ 
    public void testCtor_NullDefaultTagPrefix() {
        try {
            new DefaultTagFactory(null);
            fail();
        } catch (NullPointerException ex) {
            // ok
        }
    }
	
	/**
	 * getDefaultPrefix()/setDefaultPrefix(..)
	 * 
	 * Verifies that getDefaultPrefix()/setDefaultPrefix are complimentary and
	 * do what they are meant to do (i.e. save new or return current value of default prefix). 
	 */
    public void testGetSetDefaultPrefix() {
        String defaultPrefix = "default";
        String defaultPrefix2 = "different";
        DefaultTagFactory factory = new DefaultTagFactory(defaultPrefix);

        assertEquals(defaultPrefix, factory.getDefaultPrefix());
        factory.setDefaultPrefix(defaultPrefix2);
        assertEquals(defaultPrefix2, factory.getDefaultPrefix());
    }
	
	/**
	 * setDefaultPrefix(String)
	 * 
	 * Verify that default null prefix is rejected
	 */ 
    public void testSetDefaultPrefix_nullPrefix() {
        DefaultTagFactory factory = new DefaultTagFactory();
        try {
            factory.setDefaultPrefix(null);
            fail();
        } catch (NullPointerException ex) {
            // ok
        }
    }
	
	/**
	 * addPackagePrefix(..)
	 * 
	 * Verifies that addPackagePrefix actually adds prefixes.
	 */
    public void testAddPackagePrefix() {
        String packageName = "com.example1";
        String tagPrefix = "test";

        DefaultTagFactory factory = new DefaultTagFactory();

        /* no prefixes! */
        assertEquals(factory.getDefaultPrefix(), factory.getPackagePrefix(null, packageName));

        /* test that prefix was added */
        factory.addPackagePrefix(packageName, tagPrefix);
        assertEquals(tagPrefix, factory.getPackagePrefix(null, packageName));
    }
	
	/**
	 * addPackagePrefix(..)
	 * 
	 * Verifies that null package name prefix or tag prefix are rejected.
	 */
    public void testAddPackagePrefix_nullArgs() {
        DefaultTagFactory factory = new DefaultTagFactory();
        try {
            factory.addPackagePrefix(null, "");
            fail();
        } catch (NullPointerException ex) {
            // ok
        }

        try {
            factory.addPackagePrefix("", null);
            fail();
        } catch (NullPointerException ex) {
            // ok
        }
    }
	
	/**
	 * getPackagePrefix(..)
	 * 
	 * Test that if no preferred prefix is specified, default prefix is used.
	 */
    public void testGetPackagePrefix_emptyFactory_noPreferredPrefix() {
        String defaultPrefix = "default";
        DefaultTagFactory factory = new DefaultTagFactory(defaultPrefix);

        String packageName = "com.test";
        assertEquals(factory.getDefaultPrefix(), factory.getPackagePrefix(null, packageName));
    }
	
	/**
	 * getPackagePrefix(..)
	 * 
	 * If preferred prefix is specified, it should be used. 
	 */
    public void testGetPackagePrefix_emptyFactory_withPreferredPrefix() {
        String defaultPrefix = "default";
        DefaultTagFactory factory = new DefaultTagFactory(defaultPrefix);

        String preferredPrefix = "prefprefix";
        String packageName = "com.test";
        assertEquals(preferredPrefix, factory.getPackagePrefix(preferredPrefix, packageName));
    }

	/**
	 * getPackagePrefix(..)
	 * 
	 * Add single package with predefined tag prefix.
	 * 1. Test that when matching package name is used then associated tag prefix is returned. 
	 * 2. Test that when non-matching package name is used then default prefix is used.  
	 */
    public void testGetPackagePrefix_singlePackage() {
        String packageName1 = "com.example1";
        String packageName2 = "com.example2";
        String tagPrefix = "test";

        DefaultTagFactory factory = new DefaultTagFactory();
        factory.addPackagePrefix(packageName1, tagPrefix);

        assertEquals(tagPrefix, factory.getPackagePrefix(null, packageName1));
        assertEquals(tagPrefix, factory.getPackagePrefix(null, packageName1 + ".subpackage"));
        assertEquals(factory.getDefaultPrefix(), factory.getPackagePrefix(null, packageName2));
    }	
	
	/**
	 * getPackagePrefix(..)
	 * 
	 * Add multiple package names and associated tag prefixes. All package names do not 
	 * overlap with each other. Test that correct tag prefixes are returned for matching
	 * package names, and default tag prefix is returned for package name that does not 
	 * match any package name prefix. 
	 */
    public void testGetPackagePrefix_multiplePackages_noOverlapping() {
        String packageName1 = "com.example1";
        String packageName2 = "com.example2";
        String packageName3 = "com.example3";
        String packageName4 = "com.example4";
        String subpackage = ".subpackge";
        String tagPrefix1 = "test1";
        String tagPrefix2 = "test2";
        String tagPrefix3 = "test3";

        DefaultTagFactory factory = new DefaultTagFactory();
        factory.addPackagePrefix(packageName1, tagPrefix1);
        factory.addPackagePrefix(packageName2, tagPrefix2);
        factory.addPackagePrefix(packageName3, tagPrefix3);

        assertEquals(tagPrefix1, factory.getPackagePrefix(null, packageName1));
        assertEquals(tagPrefix2, factory.getPackagePrefix(null, packageName2));
        assertEquals(tagPrefix3, factory.getPackagePrefix(null, packageName3));
        assertEquals(tagPrefix1, factory.getPackagePrefix(null, packageName1 + subpackage));
        assertEquals(tagPrefix2, factory.getPackagePrefix(null, packageName2 + subpackage));
        assertEquals(tagPrefix3, factory.getPackagePrefix(null, packageName3 + subpackage));
        assertEquals(factory.getDefaultPrefix(), factory.getPackagePrefix(null, packageName4));
    }
	
	/**
	 * getPackagePrefix(..)
	 * 
	 * Add multiple package name prefixes that overlap. Verify that the first one 
	 * added takes effect, and others are just ignored.
	 */
	public void testGetPackagePrefix_multiplePackages_overallaping() {
		String pkgCom = "com";
		String pkgComExample = pkgCom + ".example";
		String pkgComExampleSub = pkgComExample + ".sub";
		String pkgAntoher = "org.another";
		String subpackage = ".deeper";
		 		
		String tagComPrefix = "com";
		String tagComExamplePrefix = "example";		
		String tagComExampleSubPrefix = "sub";
		
		/* direct order - "com" always wins */		
		DefaultTagFactory factory = new DefaultTagFactory();
		factory.addPackagePrefix(pkgCom, tagComPrefix);
		factory.addPackagePrefix(pkgComExample, tagComExamplePrefix);
		factory.addPackagePrefix(pkgComExampleSub, tagComExampleSubPrefix);
		
		assertEquals(tagComPrefix, factory.getPackagePrefix(null, pkgCom));
		assertEquals(tagComPrefix, factory.getPackagePrefix(null, pkgComExample));
		assertEquals(tagComPrefix, factory.getPackagePrefix(null, pkgComExampleSub));
		assertEquals(tagComPrefix, factory.getPackagePrefix(null, pkgCom + subpackage));
		assertEquals(tagComPrefix, factory.getPackagePrefix(null, pkgComExample + subpackage));
		assertEquals(tagComPrefix, factory.getPackagePrefix(null, pkgComExampleSub + subpackage));
		assertEquals(factory.getDefaultPrefix(), factory.getPackagePrefix(null, pkgAntoher));
		
		/* reversed order - more specific packages come first */		
		factory = new DefaultTagFactory();
		factory.addPackagePrefix(pkgComExampleSub, tagComExampleSubPrefix);
		factory.addPackagePrefix(pkgComExample, tagComExamplePrefix);
		factory.addPackagePrefix(pkgCom, tagComPrefix);		
		
		assertEquals(tagComPrefix, 				factory.getPackagePrefix(null, pkgCom));
		assertEquals(tagComExamplePrefix, 		factory.getPackagePrefix(null, pkgComExample));
		assertEquals(tagComExampleSubPrefix, 	factory.getPackagePrefix(null, pkgComExampleSub));
		assertEquals(tagComPrefix, 				factory.getPackagePrefix(null, pkgCom + subpackage));
		assertEquals(tagComExamplePrefix, 		factory.getPackagePrefix(null, pkgComExample + subpackage));
		assertEquals(tagComExampleSubPrefix, 	factory.getPackagePrefix(null, pkgComExampleSub + subpackage));
		assertEquals(factory.getDefaultPrefix(),factory.getPackagePrefix(null, pkgAntoher));
	}
	
	/**
	 * getPackagePrefix(..)
	 * 
	 * Verify that preferred tag prefix can be null, 
	 * while package prefix must be non-null value.
	 */
    public void testGetPackagePrefix_nullArgs() {
        DefaultTagFactory factory = new DefaultTagFactory();

        factory.getPackagePrefix(null, "");

        try {
            factory.getPackagePrefix("", null);
            fail();
        } catch (NullPointerException ex) {
            // ok
        }
    }
	
	/**
	 * createTag(..)
	 * 
	 * Verify that tag prefix is correctly added to class name
	 */	
	public void testCreateTag() {
		DefaultTagFactory factory = new DefaultTagFactory();
		
		factory.setDefaultPrefix("");		
		assertEquals(String.class.getSimpleName(), factory.createTag(null, String.class));
		
		String defaultPrefix = "def";
		factory.setDefaultPrefix(defaultPrefix);
		assertEquals(defaultPrefix + "." + String.class.getSimpleName(), 
					 factory.createTag(null, String.class));
		
		String preferredPrefix = "preferred";
		assertEquals(preferredPrefix + "." + String.class.getSimpleName(), 
				 	 factory.createTag(preferredPrefix, String.class));
		
		String packagePrefix = "pkg";
		factory.addPackagePrefix(String.class.getPackage().getName(), packagePrefix);
		assertEquals(packagePrefix + "." + String.class.getSimpleName(),
					 factory.createTag(preferredPrefix, String.class));
	}
	
	/**
	 * createTag(..)
	 * 
	 * Verify that preferred tag prefix can be null, 
	 * while class must be non-null value.
	 */
    public void testCreateTag_nullArgs() {
        DefaultTagFactory factory = new DefaultTagFactory();

        factory.createTag(null, String.class);

        try {
            factory.getPackagePrefix("", null);
            fail();
        } catch (NullPointerException ex) {
            // ok
        }
    }
}
