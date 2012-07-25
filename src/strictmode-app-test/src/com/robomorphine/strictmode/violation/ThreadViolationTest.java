package com.robomorphine.strictmode.violation;

import com.robomorphine.strictmode.violation.ThreadViolation;
import com.robomorphine.strictmode.violation.ThreadViolation.ThreadViolationFactory;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ThreadViolationTest extends BaseTestCase {
    
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
    
    /**
     * Verify that all real violations can be created using ViolationFactory.
     */
    public void testViolationFactory() throws IOException {
        List<String> goodNames = new LinkedList<String>();        
        goodNames.add("dropbox/thread_disk_read.txt");
        goodNames.add("dropbox/thread_disk_write.txt");
        goodNames.add("dropbox/thread_network.txt");
        goodNames.add("dropbox/thread_custom.txt");
        goodNames.add("dropbox/thread_disk_write_remote.txt");
        
        List<String> badNames = new LinkedList<String>();
        badNames.add("dropbox/vm_close.txt");
        badNames.add("dropbox/vm_end.txt");
        badNames.add("dropbox/vm_instance_count.txt");
        
        ThreadViolationFactory factory = new ThreadViolationFactory();
        for(String name : goodNames) {
            RawViolation rawViolation = openAssetAsRawViolation(name); 
            assertNotNull(factory.create(rawViolation.headers, rawViolation.exception));
        }
        
        for(String name : badNames) {
            RawViolation rawViolation = openAssetAsRawViolation(name); 
            assertNull(factory.create(rawViolation.headers, rawViolation.exception));
        }
    }
    
    public void testParseHeaderDuration() {
        assertEquals(123456, ThreadViolation.parseHeaderDuration("123456"));
        assertEquals(1, ThreadViolation.parseHeaderDuration("1"));
        assertEquals(0, ThreadViolation.parseHeaderDuration("asadfsa"));
        assertEquals(0, ThreadViolation.parseHeaderDuration(""));
        assertEquals(0, ThreadViolation.parseHeaderDuration(null));
    }
}
