package com.robomorphine.test.predicate;

import android.test.suitebuilder.TestMethod;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import junit.framework.TestCase;

public class HasClassAnnotationTest extends TestCase {
    
    @Retention(RetentionPolicy.RUNTIME)
    @interface SimpleAnnotation {        
    }
    
    @SimpleAnnotation
    static class TestClassWithAnnotation extends TestCase {
        public void testMethod(){}
    }
        
    static class TestClassNoAnnotation extends TestCase {
        public void testMethod(){}
    }
    
    public void testMethodHasAnnotation() {
        HasClassAnnotation predicate = new HasClassAnnotation(SimpleAnnotation.class);
        
        TestMethod withAnnotation = new TestMethod("testMethod", TestClassWithAnnotation.class);
        TestMethod noAnnotation = new TestMethod("testMethod", TestClassNoAnnotation.class);
        assertTrue(predicate.apply(withAnnotation));
        assertFalse(predicate.apply(noAnnotation));
    }
}
