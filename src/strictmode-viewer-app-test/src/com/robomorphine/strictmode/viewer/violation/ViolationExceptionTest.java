package com.robomorphine.strictmode.viewer.violation;

import com.robomorphine.strictmode.viewer.violation.ViolationException;

import java.io.IOException;

public class ViolationExceptionTest extends BaseTestCase {
    
    public void testHashCode_nameAndMessage() {
        String className1 = "className1";
        String className2 = "className2";
        String message1 = "msg1";
        String message2 = "msg2";
        
        ViolationException ex1 = new ViolationException(className1, message1);
        ViolationException ex2 = new ViolationException(className1, message1);
        assertEquals(ex1.hashCode(), ex2.hashCode());
        
        ex1 = new ViolationException(className1, message1);
        ex2 = new ViolationException(className1, message2);
        assertTrue(ex1.hashCode() != ex2.hashCode());
        
        ex1 = new ViolationException(className1, message1);
        ex2 = new ViolationException(className2, message1);
        assertTrue(ex1.hashCode() != ex2.hashCode());
        
        ex1 = new ViolationException(className1, message1);
        ex2 = new ViolationException(className2, message2);
        assertTrue(ex1.hashCode() != ex2.hashCode());
    }
    
    private StackTraceElement [] newStackTrace() {
        Throwable th = new Throwable();//NOPMD
        th.fillInStackTrace();
        return th.getStackTrace();
    }
    
    public void testHashCode_stackTrace() {
        String className1 = "className1";
        String className2 = "className2";
        String message1 = "msg1";
        String message2 = "msg2";
        
        ViolationException ex1 = new ViolationException(className1, message1);
        ViolationException ex2 = new ViolationException(className1, message1);
        
        /* same stacktraces (+same class name and message) */
        StackTraceElement [] st1 = newStackTrace(), st2 = newStackTrace();
        ex1.setStackTrace(st1);
        ex2.setStackTrace(st2);
        assertEquals(ex1.hashCode(), ex2.hashCode());
                
        /* same stacktraces, but different messages */        
        ex1 = new ViolationException(className1, message1);
        ex2 = new ViolationException(className1, message2);
        ex1.setStackTrace(st1);
        ex2.setStackTrace(st2);
        assertTrue(ex1.hashCode() != ex2.hashCode());
        
        /* same stacktraces, but different class names*/
        ex1 = new ViolationException(className1, message1);
        ex2 = new ViolationException(className2, message1);
        ex1.setStackTrace(st1);
        ex2.setStackTrace(st2);
        assertTrue(ex1.hashCode() != ex2.hashCode());
        
        /* different stacktraces (+same class name and message) */
        st1 = newStackTrace();
        st2 = newStackTrace();
        ex1.setStackTrace(st1);
        ex2.setStackTrace(st2);
        assertTrue(ex1.hashCode() != ex2.hashCode());
    }
    
    public void testHashCode_nestedExceptions() {
        String className1 = "className1";
        String className2 = "className2";
        String message1 = "msg1";
        String message2 = "msg2";
        
        String nestedClassName1 = "nestedClassName1";
        String nestedClassName2 = "nestedClassName2";
        String nestedMessage1 = "nestedMsg1";
        String nestedMessage2 = "nestedMsg2";
        
        /* identical nested exceptions */
        ViolationException nestedEx1 = new ViolationException(nestedClassName1, nestedMessage1);
        ViolationException nestedEx2 = new ViolationException(nestedClassName1, nestedMessage1);
        
        StackTraceElement [] nestedSt1 = newStackTrace(), nestedSt2 = newStackTrace();
        nestedEx1.setStackTrace(nestedSt1);
        nestedEx2.setStackTrace(nestedSt2);
        
        /* identical exceptions with identical nested exceptions */
        ViolationException ex1 = new ViolationException(className1, message1, nestedEx1);
        ViolationException ex2 = new ViolationException(className1, message1, nestedEx2);
                
        StackTraceElement [] st1 = newStackTrace(), st2 = newStackTrace();
        ex1.setStackTrace(st1);
        ex2.setStackTrace(st2);
        assertEquals(ex1.hashCode(), ex2.hashCode());
                
        /* different exceptions by message with identical nested exceptions */        
        ex1 = new ViolationException(className1, message1, nestedEx1);
        ex2 = new ViolationException(className1, message2, nestedEx2);
        ex1.setStackTrace(st1);
        ex2.setStackTrace(st2);
        assertTrue(ex1.hashCode() != ex2.hashCode());
        
        /* different exceptions by classname with identical nested exceptions */
        ex1 = new ViolationException(className1, message1, nestedEx1);
        ex2 = new ViolationException(className2, message1, nestedEx2);
        ex1.setStackTrace(st1);
        ex2.setStackTrace(st2);
        assertTrue(ex1.hashCode() != ex2.hashCode());
        
        /* different exceptions by classname and message with identical nested exceptions */
        ex1 = new ViolationException(className1, message1, nestedEx1);
        ex2 = new ViolationException(className2, message2, nestedEx2);
        ex1.setStackTrace(st1);
        ex2.setStackTrace(st2);
        assertTrue(ex1.hashCode() != ex2.hashCode());
        
        /* same exceptions with different nested exceptions */
        nestedEx1 = new ViolationException(nestedClassName1, nestedMessage1);
        nestedEx2 = new ViolationException(nestedClassName2, nestedMessage2);
        
        nestedSt1 = newStackTrace();
        nestedSt2 = newStackTrace();
        nestedEx1.setStackTrace(nestedSt1);
        nestedEx2.setStackTrace(nestedSt2);
                
        ex1 = new ViolationException(className1, message1, nestedEx1);
        ex2 = new ViolationException(className1, message1, nestedEx2);
        
        assertTrue(ex1.hashCode() != ex2.hashCode());
    }
    
    public void testHashCode_assets() throws IOException {
        RawViolation v1 = openAssetAsRawViolation("dropbox/thread_disk_read.txt");
        RawViolation v2 = openAssetAsRawViolation("dropbox/thread_disk_read.txt");
        RawViolation v3 = openAssetAsRawViolation("dropbox/thread_disk_write.txt");
        
        assertEquals(v1.exception.hashCode(), v2.exception.hashCode());
        assertTrue(v1.exception.hashCode() != v3.exception.hashCode());
        assertTrue(v2.exception.hashCode() != v3.exception.hashCode());
    }
    
    
    public void testEquals_nameAndMessage() {
        String className1 = "className1";
        String className2 = "className2";
        String message1 = "msg1";
        String message2 = "msg2";
        
        ViolationException ex1 = new ViolationException(className1, message1);
        ViolationException ex2 = new ViolationException(className1, message1);
        assertTrue(ex1.equals(ex2));
        
        ex1 = new ViolationException(className1, message1);
        ex2 = new ViolationException(className1, message2);
        assertFalse(ex1.equals(ex2));
        
        ex1 = new ViolationException(className1, message1);
        ex2 = new ViolationException(className2, message1);
        assertFalse(ex1.equals(ex2));
        
        ex1 = new ViolationException(className1, message1);
        ex2 = new ViolationException(className2, message2);
        assertFalse(ex1.equals(ex2));
    }
    
    public void testEquals_stackTrace() {
        String className1 = "className1";
        String className2 = "className2";
        String message1 = "msg1";
        String message2 = "msg2";
        
        ViolationException ex1 = new ViolationException(className1, message1);
        ViolationException ex2 = new ViolationException(className1, message1);
        
        /* same stacktraces (+same class name and message) */
        StackTraceElement [] st1 = newStackTrace(), st2 = newStackTrace();
        ex1.setStackTrace(st1);
        ex2.setStackTrace(st2);
        assertTrue(ex1.equals(ex2));
                
        /* same stacktraces, but different messages */        
        ex1 = new ViolationException(className1, message1);
        ex2 = new ViolationException(className1, message2);
        ex1.setStackTrace(st1);
        ex2.setStackTrace(st2);
        assertFalse(ex1.equals(ex2));
        
        /* same stacktraces, but different class names*/
        ex1 = new ViolationException(className1, message1);
        ex2 = new ViolationException(className2, message1);
        ex1.setStackTrace(st1);
        ex2.setStackTrace(st2);
        assertFalse(ex1.equals(ex2));
        
        /* different stacktraces (+same class name and message) */
        st1 = newStackTrace();
        st2 = newStackTrace();
        ex1.setStackTrace(st1);
        ex2.setStackTrace(st2);
        assertFalse(ex1.equals(ex2));
    }
    
    public void testEquals_nestedExceptions() {
        String className1 = "className1";
        String className2 = "className2";
        String message1 = "msg1";
        String message2 = "msg2";
        
        String nestedClassName1 = "nestedClassName1";
        String nestedClassName2 = "nestedClassName2";
        String nestedMessage1 = "nestedMsg1";
        String nestedMessage2 = "nestedMsg2";
        
        /* identical nested exceptions */
        ViolationException nestedEx1 = new ViolationException(nestedClassName1, nestedMessage1);
        ViolationException nestedEx2 = new ViolationException(nestedClassName1, nestedMessage1);
        
        StackTraceElement [] nestedSt1 = newStackTrace(), nestedSt2 = newStackTrace();
        nestedEx1.setStackTrace(nestedSt1);
        nestedEx2.setStackTrace(nestedSt2);
        
        /* identical exceptions with identical nested exceptions */
        ViolationException ex1 = new ViolationException(className1, message1, nestedEx1);
        ViolationException ex2 = new ViolationException(className1, message1, nestedEx2);
                
        StackTraceElement [] st1 = newStackTrace(), st2 = newStackTrace();
        ex1.setStackTrace(st1);
        ex2.setStackTrace(st2);
        assertTrue(ex1.equals(ex2));
                
        /* different exceptions by message with identical nested exceptions */        
        ex1 = new ViolationException(className1, message1, nestedEx1);
        ex2 = new ViolationException(className1, message2, nestedEx2);
        ex1.setStackTrace(st1);
        ex2.setStackTrace(st2);
        assertFalse(ex1.equals(ex2));
        
        /* different exceptions by classname with identical nested exceptions */
        ex1 = new ViolationException(className1, message1, nestedEx1);
        ex2 = new ViolationException(className2, message1, nestedEx2);
        ex1.setStackTrace(st1);
        ex2.setStackTrace(st2);
        assertFalse(ex1.equals(ex2));
        
        /* different exceptions by classname and message with identical nested exceptions */
        ex1 = new ViolationException(className1, message1, nestedEx1);
        ex2 = new ViolationException(className2, message2, nestedEx2);
        ex1.setStackTrace(st1);
        ex2.setStackTrace(st2);
        assertFalse(ex1.equals(ex2));
        
        /* same exceptions with different nested exceptions */
        nestedEx1 = new ViolationException(nestedClassName1, nestedMessage1);
        nestedEx2 = new ViolationException(nestedClassName2, nestedMessage2);
        
        nestedSt1 = newStackTrace();
        nestedSt2 = newStackTrace();
        nestedEx1.setStackTrace(nestedSt1);
        nestedEx2.setStackTrace(nestedSt2);
                
        ex1 = new ViolationException(className1, message1, nestedEx1);
        ex2 = new ViolationException(className1, message1, nestedEx2);
        
        assertFalse(ex1.equals(ex2));
    }
    
    public void testEquals_assets() throws IOException {
        RawViolation v1 = openAssetAsRawViolation("dropbox/thread_disk_read.txt");
        RawViolation v2 = openAssetAsRawViolation("dropbox/thread_disk_read.txt");
        RawViolation v3 = openAssetAsRawViolation("dropbox/thread_disk_write.txt");
        
        assertTrue(v1.exception.equals(v2.exception));
        assertFalse(v1.exception.equals(v3.exception));
        assertFalse(v2.exception.equals(v3.exception));
    }
}
