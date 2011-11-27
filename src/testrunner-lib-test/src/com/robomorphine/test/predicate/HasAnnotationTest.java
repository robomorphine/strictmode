package com.robomorphine.test.predicate;

import android.test.suitebuilder.TestMethod;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import junit.framework.TestCase;

public class HasAnnotationTest extends TestCase {
    
    @Retention(RetentionPolicy.RUNTIME)
    @interface SimpleAnnotation {        
    }
    
    @SimpleAnnotation
    static class ClassAnnotations extends TestCase {
        public void testMethod(){}
    }
    
    static class MethodAnnotations extends TestCase {
        @SimpleAnnotation
        public void testMethod(){}
    }
    
    @SimpleAnnotation
    static class ClassAndMethodAnnotations extends TestCase {
        @SimpleAnnotation
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
        
        HasAnnotation predicate = new HasAnnotation(SimpleAnnotation.class);
        assertTrue(predicate.apply(classAnnotations));
        assertTrue(predicate.apply(methodAnnotations));
        assertTrue(predicate.apply(classAndMethodAnnotations));
        assertFalse(predicate.apply(noAnnotations));
    }
}
