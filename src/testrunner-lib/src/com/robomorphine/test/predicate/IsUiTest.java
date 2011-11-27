package com.robomorphine.test.predicate;

import com.robomorphine.test.annotation.NonUiTest;
import com.robomorphine.test.annotation.UiTest;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.TestMethod;

import java.lang.annotation.Annotation;

public class IsUiTest extends BoolAnnotation {
    private static final IsUiTest sIsUiTest = new IsUiTest();
    
    private final IsSubclassOf mIsActivityInstrumentationTestCase;
    private final IsSubclassOf mIsActivityInstrumentationTestCase2;
    
    @SuppressWarnings("deprecation")
    public IsUiTest() {
        super(UiTest.class, NonUiTest.class);
        mIsActivityInstrumentationTestCase = new IsSubclassOf(android.test.ActivityInstrumentationTestCase.class);
        mIsActivityInstrumentationTestCase2 = new IsSubclassOf(ActivityInstrumentationTestCase2.class);
    }
    
    @Override
    protected boolean negativeHasPriority() {
        return true;
    }
    
    @Override
    protected Class<? extends Annotation> calculateDefaultAnnotation(TestMethod method) {
        if(mIsActivityInstrumentationTestCase.apply(method) || 
           mIsActivityInstrumentationTestCase2.apply(method)) {
           return getPositive();
        }
        return getNegative();
    }
    
    @Override
    public String toString() {
        return "[is-ui-test]";
    }
    
    public static boolean isUiTest(TestMethod method) {
        return sIsUiTest.apply(method);
    }
}
