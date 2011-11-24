package com.inazaruk.test.predicate;

import java.lang.annotation.Annotation;

import android.test.suitebuilder.TestMethod;
import com.android.internal.util.Predicate;

class HasClassAnnotation implements Predicate<TestMethod>
{

	private Class<? extends Annotation> annotationClass;

	public HasClassAnnotation(Class<? extends Annotation> annotationClass)
	{
		this.annotationClass = annotationClass;
	}

	public boolean apply(TestMethod testMethod)
	{
		return testMethod.getEnclosingClass().getAnnotation(annotationClass) != null;
	}
}