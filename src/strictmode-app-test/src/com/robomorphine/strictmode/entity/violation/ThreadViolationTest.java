package com.robomorphine.strictmode.entity.violation;

import java.util.Map;

import junit.framework.TestCase;

public class ThreadViolationTest extends TestCase {
    
    public void testExceptionMessageParsing_empty() {
        Map<String, String> map = ThreadViolation.parseExceptionMessage("");
        assertEquals(0, map.size());
    }
    
    public void testExceptionMessageParsing_noAssigners() {
        Map<String, String> map = ThreadViolation.parseExceptionMessage("some test message");
        assertEquals(0, map.size());
    }
    
    public void testExceptionMessageParsing_simpleKeyValue() {
        String key = "key";
        String value = "value";
        
        Map<String, String> map = ThreadViolation.parseExceptionMessage(key + "=" + value);
        assertEquals(1, map.size());
        assertEquals(value, map.get(key));
    }
    
    public void testExceptionMessageParsing_simpleKeyAndValueWithSpaces() {
        String key = "key";
        String value = "value with spaces ";
        
        Map<String, String> map = ThreadViolation.parseExceptionMessage(key + "=" + value);
        assertEquals(1, map.size());
        assertEquals(value.trim(), map.get(key));
    }
    
    public void testExceptionMessageParsing_multipleKeyValues() {
        String key1 = "key1";
        String value1 = "value1 with spaces ";
        String key2 = "key2";
        String value2 = "value2 with spaces ";
        
        String msg= key1 + "=" + value1;
        msg += " " + key2 + "=" + value2;
        
        Map<String, String> map = ThreadViolation.parseExceptionMessage(msg);
        assertEquals(2, map.size());
        assertEquals(value1.trim(), map.get(key1));
        assertEquals(value2.trim(), map.get(key2));
    }
    
    public void testExceptionMessageParsing_multipleKeyValuesWithAssigners() {
        String key1 = "key1";
        String value1 = "value1=with=spaces";
        String key2 = "key2";
        String value2 = "value2 with spaces ";
        
        String msg= key1 + "=" + value1;
        msg += " " + key2 + "=" + value2;
        
        Map<String, String> map = ThreadViolation.parseExceptionMessage(msg);
        assertEquals(2, map.size());
        assertEquals(value1.trim(), map.get(key1));
        assertEquals(value2.trim(), map.get(key2));
    }
    
    public void testExceptionMessagePArsing_realExample() {
        String msg = "policy=159 violation=8 msg=my example call";
        Map<String, String> map = ThreadViolation.parseExceptionMessage(msg);
        assertEquals(3, map.size());
        assertEquals("159", map.get("policy"));
        assertEquals("8", map.get("violation"));
        assertEquals("my example call", map.get("msg"));
    }
}
