package com.robomorphine.test.predicate;

import com.robomorphine.test.annotation.ManualTest;

import android.test.suitebuilder.TestMethod;

import junit.framework.TestCase;

public class HasMethodAnnotationTest extends TestCase {
    
    static class ExampleClass extends TestCase {
        @ManualTest public void withAnnotation() {}        
        public void noAnnotation() {}
    }
   
    
    public void testMethodHasAnnotation() {
        HasMethodAnnotation predicate = new HasMethodAnnotation(ManualTest.class);
        
        TestMethod withAnnotation = new TestMethod("withAnnotation", ExampleClass.class);
        TestMethod noAnnotation = new TestMethod("noAnnotation", ExampleClass.class);
        assertTrue(predicate.apply(withAnnotation));
        assertFalse(predicate.apply(noAnnotation));
    }
}
