package com.inazaruk.test.predicate;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.test.suitebuilder.TestMethod;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.ManualTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.PerformanceTest;
import android.test.suitebuilder.annotation.SmallTest;

import com.android.internal.util.Predicate;

public class TestTypeEqualsTo implements Predicate<TestMethod>
{		
	/* Note: Order is important. If multiple annotation are used, 
	 * then first annotation from the list is considered the effective one. 
	 * All other annotations should be ignored.   
	 */
	public final static List<Class<? extends Annotation>> TEST_TYPE_ANNOTATIONS;
	static
	{
		ArrayList<Class<? extends Annotation>> list = new ArrayList<Class<? extends Annotation>>();
		list.add(ManualTest.class);
		list.add(PerformanceTest.class);
		list.add(LargeTest.class);
		list.add(MediumTest.class);
		list.add(SmallTest.class);				
		TEST_TYPE_ANNOTATIONS = Collections.unmodifiableList(list);
	}
	
	public static boolean isTestTypeAnnotation(Class<? extends Annotation> annotation)
	{
		return TEST_TYPE_ANNOTATIONS.contains(annotation);
	}
	
	
	private final Class<? extends Annotation> m_testType;
	public TestTypeEqualsTo(Class<? extends Annotation> testTypeAnnotation)
	{
		if(!isTestTypeAnnotation(testTypeAnnotation))
		{
			String msg = String.format("Annotation \"%s\" does not represent test type.",
										testTypeAnnotation.getName());
			
			throw new IllegalArgumentException(msg);
		}
		m_testType = testTypeAnnotation;	
	}
	
	
	Class<? extends Annotation> getTestType(TestMethod t)
	{
		//Determine test type
		
		//1. Use test method annotations. 
		for(Class<? extends Annotation> annotation : TEST_TYPE_ANNOTATIONS)
		{
			if(t.getAnnotation(annotation) != null) return annotation;
		}
		
		//2. Use enclosing test class annotations.
		for(Class<? extends Annotation> annotation : TEST_TYPE_ANNOTATIONS)
		{
			if(t.getEnclosingClass().getAnnotation(annotation) != null) return annotation;
		}		
		
		//3. So no test type annotations on method or class? Default to SmallTest. 
		return SmallTest.class;
	}
	
	@Override
	public boolean apply(TestMethod t)
	{				
		return getTestType(t).equals(m_testType);
	}
}
