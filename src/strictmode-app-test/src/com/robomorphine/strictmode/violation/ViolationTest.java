package com.robomorphine.strictmode.violation;

import com.robomorphine.strictmode.violation.Violation;
import com.robomorphine.strictmode.violation.ViolationException;
import com.robomorphine.strictmode.violation.Violation.ViolationFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class ViolationTest extends BaseTestCase {
    
    /**
     * Verify that all real violations can be created using ViolationFactory.
     */
    public void testViolationFactory() throws IOException {
        List<String> fileNames = new LinkedList<String>();
        fileNames.add("dropbox/thread_disk_read.txt");
        fileNames.add("dropbox/thread_disk_write.txt");
        fileNames.add("dropbox/thread_network.txt");
        fileNames.add("dropbox/thread_custom.txt");
        fileNames.add("dropbox/thread_disk_write_remote.txt");
        fileNames.add("dropbox/vm_close.txt");
        fileNames.add("dropbox/vm_end.txt");
        fileNames.add("dropbox/vm_instance_count.txt");
        
        ViolationFactory factory = new ViolationFactory();
        for(String name : fileNames) {
            RawViolation rawViolation = openAssetAsRawViolation(name); 
            assertNotNull(factory.create(rawViolation.headers, rawViolation.exception));
        }
    }
    
    public void testParseHeaderFlags() {
        assertEquals(1, Violation.parseHeaderFlags("1"));
        assertEquals(0x123, Violation.parseHeaderFlags("123"));
        assertEquals(0x123, Violation.parseHeaderFlags("0x123"));
        assertEquals(0xff, Violation.parseHeaderFlags("0xff"));
        assertEquals(0xffff, Violation.parseHeaderFlags("0xffff"));
        assertEquals(0x8765, Violation.parseHeaderFlags("0x8765"));
        assertEquals(0, Violation.parseHeaderFlags("afasd"));
        assertEquals(0, Violation.parseHeaderFlags(""));
        assertEquals(0, Violation.parseHeaderFlags(null));
    }
    
    public void testParseHeaderPackage() {
        assertEquals("com.test", Violation.parseHeaderPackage("com.test v1 (12)"));
        assertEquals("com.test", Violation.parseHeaderPackage("com.test"));
        assertEquals("com.test", Violation.parseHeaderPackage("com.test "));
        assertEquals(null, Violation.parseHeaderPackage(""));
        assertEquals(null, Violation.parseHeaderPackage(null));
    }
    
    public void testParseHeaderPackageVersionCode() {
        assertEquals(1, Violation.parseHeaderPackageVersionCode("com.test v1 (12)"));
        assertEquals(123, Violation.parseHeaderPackageVersionCode("com.test v123 (12)"));
        assertEquals(456, Violation.parseHeaderPackageVersionCode("com.test 456 (12)"));
        assertEquals(789, Violation.parseHeaderPackageVersionCode("com.test 789"));
        assertEquals(0, Violation.parseHeaderPackageVersionCode("com.test "));
        assertEquals(0, Violation.parseHeaderPackageVersionCode("com.test asd (12)"));
        assertEquals(0, Violation.parseHeaderPackageVersionCode("com.test asd"));
        assertEquals(0, Violation.parseHeaderPackageVersionCode("com.test"));
        assertEquals(0, Violation.parseHeaderPackageVersionCode(""));
        assertEquals(0, Violation.parseHeaderPackageVersionCode(null));
    }
    
    public void testParseHeaderPackageVersionName() {
        assertEquals("12", Violation.parseHeaderPackageVersionName("com.test v1 (12)"));
        assertEquals("1.2.3", Violation.parseHeaderPackageVersionName("com.test v1 (1.2.3)"));
        assertEquals("1.2.3", Violation.parseHeaderPackageVersionName("com.test v1 1.2.3"));
        assertEquals("abc", Violation.parseHeaderPackageVersionName("com.test v1 abc"));
        assertEquals(null, Violation.parseHeaderPackageVersionName("com.test v1 "));
        assertEquals(null, Violation.parseHeaderPackageVersionName("com.test v1"));
        assertEquals(null, Violation.parseHeaderPackageVersionName(""));
        assertEquals(null, Violation.parseHeaderPackageVersionName(null));
    }
    
    public void testParseHeaderTimestamp() {
        assertEquals(123456, Violation.parseHeaderTimestamp("123456"));
        assertEquals(1, Violation.parseHeaderTimestamp("1"));
        assertEquals(0, Violation.parseHeaderTimestamp("asadfsa"));
        assertEquals(0, Violation.parseHeaderTimestamp(""));
        assertEquals(0, Violation.parseHeaderTimestamp(null));
    }
    
    public void testHashCode() {
        String className1 = "class1";
        String className2 = "class2";
        String msg1 = "msg1";
        String msg2 = "msg2";
        String pkg1 = "pkg1";
        String pkg2 = "pkg2";
        
        Map<String, String> headers1 = new HashMap<String, String>();
        headers1.put(Violation.HEADER_KEY_PACKAGE, pkg1);
        
        Map<String, String> headers2 = new HashMap<String, String>();
        headers2.put(Violation.HEADER_KEY_PACKAGE, pkg1);
        
        ViolationException ex1 = new ViolationException(className1, msg1);
        ViolationException ex2 = new ViolationException(className1, msg1);
        
        /* exceptions and headers are the same, so must be violations */
        Violation v1 = new Violation(headers1, ex1);
        Violation v2 = new Violation(headers2, ex2);
        v1.setTimestamp(0);
        v2.setTimestamp(1);
        assertTrue(v1.hashCode() == v2.hashCode());
        
        /* exceptions are different */
        ex1 = new ViolationException(className1, msg1); 
        ex2 = new ViolationException(className2, msg2);
        
        v1 = new Violation(headers1, ex1);
        v2 = new Violation(headers2, ex2);
        v1.setTimestamp(0);
        v2.setTimestamp(1);
        assertTrue(v1.hashCode() != v2.hashCode());
        
        /* headers are different */
        headers1.put(Violation.HEADER_KEY_PACKAGE, pkg1);
        headers2.put(Violation.HEADER_KEY_PACKAGE, pkg2);
        ex1 = new ViolationException(className1, msg1); 
        ex2 = new ViolationException(className1, msg1);
        
        v1 = new Violation(headers1, ex1);
        v2 = new Violation(headers2, ex2);
        v1.setTimestamp(0);
        v2.setTimestamp(1);
        assertTrue(v1.hashCode() != v2.hashCode());
    }
    
    public void testEquals() {
        String className1 = "class1";
        String className2 = "class2";
        String msg1 = "msg1";
        String msg2 = "msg2";
        String pkg1 = "pkg1";
        String pkg2 = "pkg2";
        
        Map<String, String> headers1 = new HashMap<String, String>();
        headers1.put(Violation.HEADER_KEY_PACKAGE, pkg1);
        
        Map<String, String> headers2 = new HashMap<String, String>();
        headers2.put(Violation.HEADER_KEY_PACKAGE, pkg1);
        
        ViolationException ex1 = new ViolationException(className1, msg1);
        ViolationException ex2 = new ViolationException(className1, msg1);
        
        /* exceptions and headers are the same, so must be violations */
        Violation v1 = new Violation(headers1, ex1);
        Violation v2 = new Violation(headers2, ex2);
        v1.setTimestamp(0);
        v2.setTimestamp(1);
        assertTrue(v1.equals(v2));
        
        /* exceptions are different */
        ex1 = new ViolationException(className1, msg1); 
        ex2 = new ViolationException(className2, msg2);
        
        v1 = new Violation(headers1, ex1);
        v2 = new Violation(headers2, ex2);
        v1.setTimestamp(0);
        v2.setTimestamp(1);
        assertFalse(v1.equals(v2));
        
        /* headers are different */
        headers1.put(Violation.HEADER_KEY_PACKAGE, pkg1);
        headers2.put(Violation.HEADER_KEY_PACKAGE, pkg2);
        ex1 = new ViolationException(className1, msg1); 
        ex2 = new ViolationException(className1, msg1);
        
        v1 = new Violation(headers1, ex1);
        v2 = new Violation(headers2, ex2);
        v1.setTimestamp(0);
        v2.setTimestamp(1);
        assertFalse(v1.equals(v2));
    }
}
