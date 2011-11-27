package com.robomorphine.test.predicate;

import com.robomorphine.test.annotation.ManualTest;

import android.test.suitebuilder.TestMethod;

import junit.framework.TestCase;

public class HasClassAnnotationTest extends TestCase {
    
    @SuppressWarnings("all")
    @ManualTest
    private static class TestClassWithAnnotation extends TestCase {
        public void testMethod(){}
    }
    
    @SuppressWarnings("all")
    private static class TestClassNoAnnotation extends TestCase {
        public void testMethod(){}
    }
    
    public void testMethodHasAnnotation() {
        HasClassAnnotation predicate = new HasClassAnnotation(ManualTest.class);
        
        TestMethod withAnnotation = new TestMethod("testMethod", TestClassWithAnnotation.class);
        TestMethod noAnnotation = new TestMethod("testMethod", TestClassNoAnnotation.class);
        assertTrue(predicate.apply(withAnnotation));
        assertFalse(predicate.apply(noAnnotation));
    }
}
