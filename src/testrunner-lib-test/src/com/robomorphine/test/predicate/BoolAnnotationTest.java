package com.robomorphine.test.predicate;

import android.test.suitebuilder.TestMethod;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

public class BoolAnnotationTest extends TestCase {
    
    @Retention(RetentionPolicy.RUNTIME)
    static @interface Positive {
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    static @interface Negative {
    }
    
    static class NoClassAnnotations extends TestCase {
        public void testNoMethodAnnotation() {            
        }
        
        @Positive
        public void testPositive() {            
        }
        
        @Negative
        public void testNegative() {            
        }
        
        @Positive @Negative
        public void testPositiveNegative() {            
        }
    }
    
    @Positive
    static class PositiveClass extends TestCase {
        public void testNoMethodAnnotation() {            
        }
        
        @Positive
        public void testPositive() {            
        }
        
        @Negative
        public void testNegative() {            
        }
        
        @Positive @Negative
        public void testPositiveNegative() {            
        }
    }
    
    @Negative
    static class NegativeClass extends TestCase {
        public void testNoMethodAnnotation() {            
        }
        
        @Positive
        public void testPositive() {            
        }
        
        @Negative
        public void testNegative() {            
        }
        
        @Positive @Negative
        public void testPositiveNegative() {            
        }
    }
    
    @Positive @Negative
    static class PositiveNegativeClass extends TestCase {
        public void testNoMethodAnnotation() {            
        }
        
        @Positive
        public void testPositive() {            
        }
        
        @Negative
        public void testNegative() {            
        }
        
        @Positive @Negative
        public void testPositiveNegative() {            
        }
    }
    
    static class PostiveNegative extends BoolAnnotation {
        public PostiveNegative() {
            super(Positive.class, Negative.class);
        }
    }
    
    static class PositiveNegative_priorityNegative extends PostiveNegative {
        @Override
        protected boolean negativeHasPriority() {            
            return true;
        }
    }
    
    static class PositiveNegative_priorityPositive extends PostiveNegative {
        @Override
        protected boolean negativeHasPriority() {            
            return false;
        }        
    }    
    
    public void testPositivePriority() {
        HashMap<TestMethod, Boolean> expected = new HashMap<TestMethod, Boolean>();
        expected.put(new TestMethod("testNoMethodAnnotation", NoClassAnnotations.class), true);        
        expected.put(new TestMethod("testPositive", NoClassAnnotations.class), true);
        expected.put(new TestMethod("testNegative", NoClassAnnotations.class), false);
        expected.put(new TestMethod("testPositiveNegative", NoClassAnnotations.class), true);
        
        expected.put(new TestMethod("testNoMethodAnnotation", PositiveClass.class), true);        
        expected.put(new TestMethod("testPositive", PositiveClass.class), true);
        expected.put(new TestMethod("testNegative", PositiveClass.class), false);
        expected.put(new TestMethod("testPositiveNegative", PositiveClass.class), true);
        
        expected.put(new TestMethod("testNoMethodAnnotation", NegativeClass.class), false);        
        expected.put(new TestMethod("testPositive", NegativeClass.class), true);
        expected.put(new TestMethod("testNegative", NegativeClass.class), false);
        expected.put(new TestMethod("testPositiveNegative", NegativeClass.class), true);
        
        expected.put(new TestMethod("testNoMethodAnnotation", PositiveNegativeClass.class), true);        
        expected.put(new TestMethod("testPositive", PositiveNegativeClass.class), true);
        expected.put(new TestMethod("testNegative", PositiveNegativeClass.class), false);
        expected.put(new TestMethod("testPositiveNegative", PositiveNegativeClass.class), true);
                
        PositiveNegative_priorityPositive predicate = new PositiveNegative_priorityPositive();
        for(Map.Entry<TestMethod, Boolean> entry : expected.entrySet()) {
            
            boolean expectedResult = entry.getValue();
            TestMethod method = entry.getKey();
            boolean actualResult = predicate.apply(method);
            
            String name = method.getEnclosingClass().getSimpleName() + "." + method.getName();
            assertEquals(name, expectedResult, actualResult);
        }        
    }
    
    public void testNegativePriority() {
        HashMap<TestMethod, Boolean> expected = new HashMap<TestMethod, Boolean>();
        expected.put(new TestMethod("testNoMethodAnnotation", NoClassAnnotations.class), false);        
        expected.put(new TestMethod("testPositive", NoClassAnnotations.class), true);
        expected.put(new TestMethod("testNegative", NoClassAnnotations.class), false);
        expected.put(new TestMethod("testPositiveNegative", NoClassAnnotations.class), false);
        
        expected.put(new TestMethod("testNoMethodAnnotation", PositiveClass.class), true);        
        expected.put(new TestMethod("testPositive", PositiveClass.class), true);
        expected.put(new TestMethod("testNegative", PositiveClass.class), false);
        expected.put(new TestMethod("testPositiveNegative", PositiveClass.class), false);
        
        expected.put(new TestMethod("testNoMethodAnnotation", NegativeClass.class), false);        
        expected.put(new TestMethod("testPositive", NegativeClass.class), true);
        expected.put(new TestMethod("testNegative", NegativeClass.class), false);
        expected.put(new TestMethod("testPositiveNegative", NegativeClass.class), false);
        
        expected.put(new TestMethod("testNoMethodAnnotation", PositiveNegativeClass.class), false);        
        expected.put(new TestMethod("testPositive", PositiveNegativeClass.class), true);
        expected.put(new TestMethod("testNegative", PositiveNegativeClass.class), false);
        expected.put(new TestMethod("testPositiveNegative", PositiveNegativeClass.class), false);
                
        PositiveNegative_priorityNegative predicate = new PositiveNegative_priorityNegative();
        for(Map.Entry<TestMethod, Boolean> entry : expected.entrySet()) {
            
            boolean expectedResult = entry.getValue();
            TestMethod method = entry.getKey();
            boolean actualResult = predicate.apply(method);
            
            String name = method.getEnclosingClass().getSimpleName() + "." + method.getName();
            assertEquals(name, expectedResult, actualResult);
        }        
    }
}
