package com.robomorphine.test.predicate;

import com.robomorphine.test.predicate.Predicate;
import com.robomorphine.test.predicate.Predicates;

import junit.framework.TestCase;

public class PredicatesTest extends TestCase {
    
    public void testTruePredicated() {
        Predicate<Object> predicate = Predicates.alwaysTrue((Object)null);
        assertTrue(predicate.apply(null));
        assertTrue(predicate.apply("any"));
        assertTrue(predicate.apply(1));
        assertTrue(predicate.apply(123));
        
        predicate = Predicates.<Object>alwaysTrue();
        assertTrue(predicate.apply(null));
        assertTrue(predicate.apply("any"));
        assertTrue(predicate.apply(1));
        assertTrue(predicate.apply(123));
    }
    
    public void testFalsePredicated() {
        Predicate<Object> predicate = Predicates.alwaysFalse((Object)null);
        assertFalse(predicate.apply(null));
        assertFalse(predicate.apply("any"));
        assertFalse(predicate.apply(1));
        assertFalse(predicate.apply(123));
        
        predicate = Predicates.<Object>alwaysFalse();
        assertFalse(predicate.apply(null));
        assertFalse(predicate.apply("any"));
        assertFalse(predicate.apply(1));
        assertFalse(predicate.apply(123));
    }
    
    
    @SuppressWarnings("unchecked")
    public void testAndPredicate_empty() {         
        Predicate<Object> predicate = Predicates.and();
        assertTrue(predicate.apply(null));
    }
    
    @SuppressWarnings("unchecked")
    public void testAndPredicate_truthTable() {
        Predicate<Object> predicate;
        Predicate<Object> ptrue = Predicates.alwaysTrue();
        Predicate<Object> pfalse = Predicates.alwaysFalse();
        
        predicate = Predicates.and(ptrue, ptrue);
        assertTrue(predicate.apply(null));
        
        predicate = Predicates.and(ptrue, pfalse);
        assertFalse(predicate.apply(null));
        
        predicate = Predicates.and(pfalse, ptrue);
        assertFalse(predicate.apply(null));
        
        predicate = Predicates.and(pfalse, pfalse);
        assertFalse(predicate.apply(null));
    }
    
    @SuppressWarnings("unchecked")
    public void testEmptyOrPredicate() {         
        Predicate<Object> predicate = Predicates.or();
        assertFalse(predicate.apply(null));
    }
    
    @SuppressWarnings("unchecked")
    public void testOrPredicate_truthTable() {
        Predicate<Object> predicate;
        Predicate<Object> ptrue = Predicates.alwaysTrue();
        Predicate<Object> pfalse = Predicates.alwaysFalse();
        
        predicate = Predicates.or(ptrue, ptrue);
        assertTrue(predicate.apply(null));
        
        predicate = Predicates.or(ptrue, pfalse);
        assertTrue(predicate.apply(null));
        
        predicate = Predicates.or(pfalse, ptrue);
        assertTrue(predicate.apply(null));
        
        predicate = Predicates.or(pfalse, pfalse);
        assertFalse(predicate.apply(null));
    }
    
    public void testNotPredicate_truthTable() {
        Predicate<Object> predicate;
        Predicate<Object> ptrue = Predicates.alwaysTrue();
        Predicate<Object> pfalse = Predicates.alwaysFalse();
        
        predicate = Predicates.not(ptrue);
        assertFalse(predicate.apply(null));
        
        predicate = Predicates.not(pfalse);
        assertTrue(predicate.apply(null));        
    }

}
