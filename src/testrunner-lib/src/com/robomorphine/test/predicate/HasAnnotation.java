package com.robomorphine.test.predicate;

import static com.robomorphine.test.predicate.Predicates.or;

import java.lang.annotation.Annotation;

import android.test.suitebuilder.TestMethod;

public class HasAnnotation implements Predicate<TestMethod> {

    private final Class<? extends Annotation> mAnnotationClass;
    private final Predicate<TestMethod> mHasMethodOrClassAnnotation;

    @SuppressWarnings("all")
    public HasAnnotation(Class<? extends Annotation> annotationClass) {
        mAnnotationClass = annotationClass;
        this.mHasMethodOrClassAnnotation = or(new HasMethodAnnotation(annotationClass),
                                             new HasClassAnnotation(annotationClass));
    }

    public boolean apply(TestMethod testMethod) {
        return mHasMethodOrClassAnnotation.apply(testMethod);
    }
    
    @Override
    public String toString() {
        return "[has-annotation " + mAnnotationClass.getSimpleName() + "]";
    }
}