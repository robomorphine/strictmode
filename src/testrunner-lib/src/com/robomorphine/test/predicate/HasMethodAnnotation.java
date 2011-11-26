
package com.robomorphine.test.predicate;

import android.test.suitebuilder.TestMethod;

import java.lang.annotation.Annotation;

class HasMethodAnnotation implements Predicate<TestMethod> {
    private final Class<? extends Annotation> mAnnotationClass;

    public HasMethodAnnotation(Class<? extends Annotation> annotationClass) {
        this.mAnnotationClass = annotationClass;
    }

    public boolean apply(TestMethod testMethod) {
        return testMethod.getAnnotation(mAnnotationClass) != null;
    }
    
    @Override
    public String toString() {
        return "[has-method-annotation " + mAnnotationClass.getSimpleName() + "]";
    }
}
