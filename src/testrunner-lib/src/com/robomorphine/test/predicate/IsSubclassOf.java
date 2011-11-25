package com.robomorphine.test.predicate;

import android.test.suitebuilder.TestMethod;

public class IsSubclassOf implements Predicate<TestMethod>{
    private final Class<?> mSuper;
    
    public IsSubclassOf(Class<?> superClass) {
        mSuper=superClass;
    }
    
    @Override
    public boolean apply(TestMethod t) {
        return mSuper.isAssignableFrom(mSuper);
    }
}
