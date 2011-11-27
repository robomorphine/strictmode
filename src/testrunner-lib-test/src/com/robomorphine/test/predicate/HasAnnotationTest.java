package com.robomorphine.test.predicate;

import com.robomorphine.test.annotation.ManualTest;

import android.test.suitebuilder.TestMethod;

import junit.framework.TestCase;

public class HasAnnotationTest extends TestCase {
    
    @ManualTest
    static class ClassAnnotations extends TestCase {
        public void testMethod(){}
    }
    
    static class MethodAnnotations extends TestCase {
        @ManualTest
        public void testMethod(){}
    }
    
    @ManualTest
    static class ClassAndMethodAnnotations extends TestCase {
        @ManualTest
        public void testMethod(){}
    }
    
    static class NoAnnotations extends TestCase {
        public void testMethod(){}
    }    
    
    public void testAnnotations() {
        TestMethod classAnnotations = new TestMethod("testMethod", ClassAnnotations.class);
        TestMethod methodAnnotations = new TestMethod("testMethod", MethodAnnotations.class);
        TestMethod classAndMethodAnnotations = new TestMethod("testMethod", ClassAndMethodAnnotations.class);
        TestMethod noAnnotations = new TestMethod("testMethod", NoAnnotations.class);
        
        HasAnnotation predicate = new HasAnnotation(ManualTest.class);
        assertTrue(predicate.apply(classAnnotations));
        assertTrue(predicate.apply(methodAnnotations));
        assertTrue(predicate.apply(classAndMethodAnnotations));
        assertFalse(predicate.apply(noAnnotations));
    }
}
