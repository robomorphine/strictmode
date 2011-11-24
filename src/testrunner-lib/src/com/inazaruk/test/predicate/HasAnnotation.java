package com.inazaruk.test.predicate;

import static com.android.internal.util.Predicates.or;

import java.lang.annotation.Annotation;

import android.test.suitebuilder.TestMethod;

import com.android.internal.util.Predicate;

public class HasAnnotation implements Predicate<TestMethod>
{

	private Predicate<TestMethod> hasMethodOrClassAnnotation;

	@SuppressWarnings("all")
	public HasAnnotation(Class<? extends Annotation> annotationClass)
	{
		this.hasMethodOrClassAnnotation = 
			or(new HasMethodAnnotation(annotationClass),
			   new HasClassAnnotation(annotationClass));
	}

	public boolean apply(TestMethod testMethod)
	{
		return hasMethodOrClassAnnotation.apply(testMethod);
	}
}