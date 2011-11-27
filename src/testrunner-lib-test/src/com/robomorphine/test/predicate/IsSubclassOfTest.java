package com.robomorphine.test.predicate;

import android.test.suitebuilder.TestMethod;

import junit.framework.TestCase;

public class IsSubclassOfTest extends TestCase {
    static class Parent extends TestCase {
        public void parentTestMethod(){}
    }
    
    static class Child1 extends Parent{
        public void child1TestMethod(){}
    }
    
    static class Child2 extends Parent{        
        public void child2TestMethod(){}
    }
    
    static class ChildOfChild1 extends Child1 {
        public void childOfChild1TestMethod(){}
    }
    
    public void testPredicated() {        
        TestMethod parentMethod = new TestMethod("parentTestMethod", Parent.class);
        TestMethod child1Method = new TestMethod("child1TestMethod", Child1.class);
        TestMethod child2Method = new TestMethod("child2TestMethod", Child2.class);
        TestMethod childOfChild1Method = new TestMethod("childOfChild1TestMethod", ChildOfChild1.class);
        
        IsSubclassOf predicate = new IsSubclassOf(Parent.class);
        assertTrue(predicate.apply(parentMethod));
        assertTrue(predicate.apply(child1Method));
        assertTrue(predicate.apply(child2Method));
        assertTrue(predicate.apply(childOfChild1Method));
        
        predicate = new IsSubclassOf(Child1.class);
        assertFalse(predicate.apply(parentMethod));
        assertTrue(predicate.apply(child1Method));
        assertFalse(predicate.apply(child2Method));
        assertTrue(predicate.apply(childOfChild1Method));
    }
}
