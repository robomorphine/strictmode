package com.robomorphine.test.predicate;

import com.robomorphine.test.annotation.DisabledTest;
import com.robomorphine.test.annotation.EnabledTest;

import android.test.suitebuilder.TestMethod;

import java.lang.annotation.Annotation;

public class IsEnabled extends BoolAnnotation {
    
    static private IsEnabled sEnabled = new IsEnabled();
    
    public IsEnabled() {
        super(EnabledTest.class, DisabledTest.class);        
    }
    
    @Override
    protected boolean negativeHasPriority() {
        return true;
    }
    
    @Override
    protected Class<? extends Annotation> calculateDefaultAnnotation(TestMethod method) {
        return getPositive();
    }
    
    @Override
    public String toString() {
        return "[is-enabled]";
    }
    
    /**
     * Returns null if test is enabled, returns reasons if test is disabled.
     */
    public static String getDisabledReason(TestMethod method) {
        IsEnabled enabled = new IsEnabled();
        if(enabled.apply(method)) {
            return null;
        }
        
        DisabledTest annotation = method.getAnnotation(DisabledTest.class);
        if(annotation == null) {
            annotation = method.getEnclosingClass().getAnnotation(DisabledTest.class);
        }
        
        if(annotation != null) {
            return annotation.value();
        }        
        return null;
    }
    
    public static boolean isEnabled(TestMethod method) {
        return sEnabled.apply(method);
    }
}
