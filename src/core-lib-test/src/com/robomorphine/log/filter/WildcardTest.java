package com.robomorphine.log.filter;

import junit.framework.TestCase;

public class WildcardTest extends TestCase {

    public void testInvalidWildcard() {
        try {
            new Wildcard(null);
            fail();
        } catch (NullPointerException ex) {
            // ok
        }

        String[] wildcards = new String[] {
                "", "**", "**aaaa", "**aa", "*aa*aa", "aa*aa*", "aa*aa*aa"
        };

        for (String str : wildcards) {
            try {
                new Wildcard(str);
                fail("\"" + str + "\" was treated as valid wildcard, while its not.");
            } catch (IllegalArgumentException ex) {
                // ok
            }
        }
    }

    public void testParse() {
        Wildcard wildcard;

        wildcard = new Wildcard("*");
        assertNull(wildcard.getExact());
        assertNull(wildcard.getPrefix());
        assertNull(wildcard.getSuffix());
        assertNull(wildcard.getContains());

        wildcard = new Wildcard("*abc");
        assertNull(wildcard.getExact());
        assertNull(wildcard.getPrefix());
        assertEquals("abc", wildcard.getSuffix());
        assertNull(wildcard.getContains());

        wildcard = new Wildcard("abc*");
        assertNull(wildcard.getExact());
        assertEquals("abc", wildcard.getPrefix());
        assertNull(wildcard.getSuffix());
        assertNull(wildcard.getContains());

        wildcard = new Wildcard("abc*xyz");
        assertNull(wildcard.getExact());
        assertEquals("abc", wildcard.getPrefix());
        assertEquals("xyz", wildcard.getSuffix());
        assertNull(wildcard.getContains());
        
        wildcard = new Wildcard("*abc*");
        assertNull(wildcard.getExact());
        assertNull(wildcard.getPrefix());
        assertNull(wildcard.getSuffix());
        assertEquals("abc", wildcard.getContains());

        wildcard = new Wildcard("abc");
        assertEquals("abc", wildcard.getExact());
        assertNull(wildcard.getPrefix());
        assertNull(wildcard.getSuffix());
        assertNull(wildcard.getContains());
    }

    public void testMatches() {
        Wildcard wildcard = new Wildcard("*");
        assertTrue(wildcard.apply(""));
        assertTrue(wildcard.apply("abc"));

        wildcard = new Wildcard("*abc");
        assertTrue(wildcard.apply("abc"));
        assertTrue(wildcard.apply("xyzabc"));
        assertFalse(wildcard.apply("abcxyz"));

        wildcard = new Wildcard("abc*");
        assertTrue(wildcard.apply("abc"));
        assertFalse(wildcard.apply("xyzabc"));
        assertTrue(wildcard.apply("abcxyz"));

        wildcard = new Wildcard("abc*xyz");
        assertFalse(wildcard.apply("abc"));
        assertFalse(wildcard.apply("xyz"));
        assertFalse(wildcard.apply("xyzabc"));
        assertTrue(wildcard.apply("abcxyz"));
        assertTrue(wildcard.apply("abcQWERxyz"));

        wildcard = new Wildcard("*abc*");
        assertTrue(wildcard.apply("abc"));
        assertFalse(wildcard.apply("xyz"));
        assertTrue(wildcard.apply("xyzabc"));
        assertTrue(wildcard.apply("abcxyz"));
        assertTrue(wildcard.apply("xyzabcxyz"));
        
        wildcard = new Wildcard("abc");
        assertTrue(wildcard.apply("abc"));
        assertFalse(wildcard.apply("xyz"));
        assertFalse(wildcard.apply("xyzabc"));
        assertFalse(wildcard.apply("abcxyz"));
        assertFalse(wildcard.apply("xyzabcxyz"));
    }
}
