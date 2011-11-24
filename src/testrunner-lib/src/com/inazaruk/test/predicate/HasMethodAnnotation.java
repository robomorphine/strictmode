package com.inazaruk.test.predicate;

import com.android.internal.util.Predicate;
import android.test.suitebuilder.TestMethod;

import java.lang.annotation.Annotation;

class HasMethodAnnotation implements Predicate<TestMethod>
{
	private final Class<? extends Annotation> annotationClass;

	public HasMethodAnnotation(Class<? extends Annotation> annotationClass)
	{
		this.annotationClass = annotationClass;
	}

	public boolean apply(TestMethod testMethod)
	{
		return testMethod.getAnnotation(annotationClass) != null;
	}
}