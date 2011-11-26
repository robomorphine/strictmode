
package com.robomorphine.test.predicate;

import java.lang.annotation.Annotation;

import android.test.suitebuilder.TestMethod;

class HasClassAnnotation implements Predicate<TestMethod> {

    private final Class<? extends Annotation> mAnnotationClass;

    public HasClassAnnotation(Class<? extends Annotation> annotationClass) {
        this.mAnnotationClass = annotationClass;
    }

    public boolean apply(TestMethod testMethod) {
        return testMethod.getEnclosingClass().getAnnotation(mAnnotationClass) != null;
    }
    
    @Override
    public String toString() {
        return "[has-class-annotation " + mAnnotationClass.getSimpleName() + "]";
    }
}
