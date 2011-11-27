package com.robomorphine.test.predicate;

import android.test.suitebuilder.TestMethod;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import junit.framework.TestCase;

public class HasMethodAnnotationTest extends TestCase {
    
    @Retention(RetentionPolicy.RUNTIME)
    @interface SimpleAnnotation {        
    }
    
    static class ExampleClass extends TestCase {
        @SimpleAnnotation public void withAnnotation() {}        
        public void noAnnotation() {}
    }
    
    public void testMethodHasAnnotation() {
        HasMethodAnnotation predicate = new HasMethodAnnotation(SimpleAnnotation.class);
        
        TestMethod withAnnotation = new TestMethod("withAnnotation", ExampleClass.class);
        TestMethod noAnnotation = new TestMethod("noAnnotation", ExampleClass.class);
        assertTrue(predicate.apply(withAnnotation));
        assertFalse(predicate.apply(noAnnotation));
    }
}
