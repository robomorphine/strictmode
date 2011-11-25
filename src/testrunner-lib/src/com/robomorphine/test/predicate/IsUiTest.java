package com.robomorphine.test.predicate;

import com.robomorphine.test.annotation.NonUiTest;
import com.robomorphine.test.annotation.UiTest;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.TestMethod;

public class IsUiTest implements Predicate<TestMethod>{
    
    private final HasMethodAnnotation mMethodHasUiTestAnnotation;
    private final HasMethodAnnotation mMethodHasNonUiTestAnnotation;
    private final HasClassAnnotation mClassHasUiTestAnnotation;
    private final HasClassAnnotation mClassHasNonUiTestAnnotation;
    private final IsSubclassOf mIsActivityInstrumentationTestCase;
    private final IsSubclassOf mIsActivityInstrumentationTestCase2;
    
    @SuppressWarnings("deprecation")
    public IsUiTest() {
        mMethodHasUiTestAnnotation = new HasMethodAnnotation(NonUiTest.class);
        mMethodHasNonUiTestAnnotation = new HasMethodAnnotation(UiTest.class);
        mClassHasUiTestAnnotation = new HasClassAnnotation(NonUiTest.class);
        mClassHasNonUiTestAnnotation = new HasClassAnnotation(UiTest.class);
        mIsActivityInstrumentationTestCase = new IsSubclassOf(android.test.ActivityInstrumentationTestCase.class);
        mIsActivityInstrumentationTestCase2 = new IsSubclassOf(ActivityInstrumentationTestCase2.class);
    }
    
    @Override
    public boolean apply(TestMethod t) {
        if(mMethodHasNonUiTestAnnotation.apply(t)) {
            return false;
        }
        
        if(mMethodHasUiTestAnnotation.apply(t)) {
            return true;
        }
        
        if(mClassHasNonUiTestAnnotation.apply(t)) {
            return false;
        }
        
        if(mClassHasUiTestAnnotation.apply(t)) {
            return true;
        }
        
        if(mIsActivityInstrumentationTestCase.apply(t) || 
           mIsActivityInstrumentationTestCase2.apply(t)) {
            return true;
        }
        
        return false;
    }
}
