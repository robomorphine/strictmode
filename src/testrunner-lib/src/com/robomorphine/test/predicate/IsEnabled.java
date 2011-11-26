package com.robomorphine.test.predicate;

import com.robomorphine.test.annotation.DisabledTest;
import com.robomorphine.test.annotation.EnabledTest;

import android.test.suitebuilder.TestMethod;

public class IsEnabled implements Predicate<TestMethod>{
    
    private final HasMethodAnnotation mMethodDisabledAnnotation;
    private final HasMethodAnnotation mMethodEnabledAnnotation;
    private final HasClassAnnotation mClassHasDisabledAnnotation;
    private final HasClassAnnotation mClassHasEnabledAnnotation;
        
    public IsEnabled() {
        mMethodDisabledAnnotation = new HasMethodAnnotation(DisabledTest.class);
        mMethodEnabledAnnotation = new HasMethodAnnotation(EnabledTest.class);        
        mClassHasDisabledAnnotation = new HasClassAnnotation(DisabledTest.class);
        mClassHasEnabledAnnotation = new HasClassAnnotation(EnabledTest.class);
    }
    
    @Override
    public boolean apply(TestMethod t) {
        if(mMethodDisabledAnnotation.apply(t)) {
            return false;
        }
        
        if(mMethodEnabledAnnotation.apply(t)) {
            return true;
        }
        
        if(mClassHasDisabledAnnotation.apply(t)) {
            return false;
        }
        
        if(mClassHasEnabledAnnotation.apply(t)) {
            return true;
        }       
        
        return true;
    }
    
    @Override
    public String toString() {
        return "[is-enabled]";
    }
}
