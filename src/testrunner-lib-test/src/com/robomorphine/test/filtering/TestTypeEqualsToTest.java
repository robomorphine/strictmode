package com.robomorphine.test.filtering;

import com.robomorphine.test.annotation.LongTest;
import com.robomorphine.test.annotation.ManualTest;
import com.robomorphine.test.annotation.PerformanceTest;
import com.robomorphine.test.annotation.ShortTest;
import com.robomorphine.test.predicate.TestTypeEqualsTo;

import android.test.suitebuilder.TestMethod;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;

import java.lang.annotation.Annotation;

import junit.framework.TestCase;

public class TestTypeEqualsToTest extends TestCase
{
	static class NoAnnotationsTestCase extends TestCase
	{
		public void testNoAnnotationsTestMethod(){}
		
		@ShortTest  public void testShortTestMethod(){}
		@SmallTest 	public void testSmallTestMethod(){}
		@android.test.suitebuilder.annotation.SmallTest public void testAndroidSmallTestMethod(){}
		
		@MediumTest public void testMediumTestMethod(){}		
		@android.test.suitebuilder.annotation.MediumTest public void testAndroidMediumTestMethod(){}
				
		@LongTest   public void testLongTestMethod(){}
		@LargeTest 	public void testLargeTestMethod(){}
		@android.test.suitebuilder.annotation.LargeTest    public void testAndroidLargeTestMethod(){}
		
		@PerformanceTest public void testPerformanceTestMethod(){}		
		@ManualTest	public void testManualTestMethod(){}
		
		@SmallTest @MediumTest @LargeTest @PerformanceTest @ManualTest
		public void test5AnnotationsTestMethod(){}
		
		@SmallTest @MediumTest @LargeTest @PerformanceTest		
		public void test4AnnotationsTestMethod(){}
		
		@SmallTest @MediumTest @LargeTest public void test3AnnotationsTestMethod(){}
		
		@SmallTest @MediumTest public void test2AnnotationsTestMethod() {}
	}
	
	@LargeTest
	static class LargeTestCase extends TestCase
	{
		public void testNoAnnotationsTestMethod(){}
		
		@SmallTest 	public void testSmallTestMethod(){}		
		@MediumTest public void testMediumTestMethod(){}		
		@LargeTest 	public void testLargeTestMethod(){}
		@PerformanceTest public void testPerformanceTestMethod(){}		
		@ManualTest	public void testManualTestMethod(){}
	}
	
	@SmallTest @MediumTest @LargeTest @PerformanceTest @ManualTest
	static class Annotations5TestCase extends TestCase
	{
		public void testNoAnnotationsTestMethod(){}	
	}
	
	@SmallTest @MediumTest @LargeTest @PerformanceTest 
	static class Annotations4TestCase extends TestCase
	{
		public void testNoAnnotationsTestMethod(){}	
	}
	
	@SmallTest @MediumTest @LargeTest 
	static class Annotations3TestCase extends TestCase
	{
		public void testNoAnnotationsTestMethod(){}	
	}
	
	@SmallTest @MediumTest 
	static class Annotations2TestCase extends TestCase
	{
		public void testNoAnnotationsTestMethod(){}	
	}
	
	@SmallTest 
	static class Annotations1TestCase extends TestCase
	{
		public void testNoAnnotationsTestMethod(){}	
	}
	
	@SmallTest 
	static class Annotations0TestCase extends TestCase
	{
		public void testNoAnnotationsTestMethod(){}	
	}
	
	
	static boolean typeEquals(TestMethod method, Class<? extends Annotation> testType)
	{
		TestTypeEqualsTo predicate = new TestTypeEqualsTo(testType);
		return predicate.apply(method);
	}
	
	public void testTestTypeEqualsTo_noAnnotationsClass()
	{
		TestMethod method;
		
		method = new TestMethod("testNoAnnotationsTestMethod", NoAnnotationsTestCase.class);
		assertTrue(typeEquals(method, SmallTest.class));
				
		method = new TestMethod("testSmallTestMethod", NoAnnotationsTestCase.class);
		assertTrue(typeEquals(method, SmallTest.class));
		
		method = new TestMethod("testMediumTestMethod", NoAnnotationsTestCase.class);
		assertTrue(typeEquals(method, MediumTest.class));
		
		method = new TestMethod("testLargeTestMethod", NoAnnotationsTestCase.class);
		assertTrue(typeEquals(method, LargeTest.class));
		
		method = new TestMethod("testPerformanceTestMethod", NoAnnotationsTestCase.class);
		assertTrue(typeEquals(method, PerformanceTest.class));
		
		method = new TestMethod("testManualTestMethod", NoAnnotationsTestCase.class);
		assertTrue(typeEquals(method, ManualTest.class));
		
		method = new TestMethod("test5AnnotationsTestMethod", NoAnnotationsTestCase.class);
		assertTrue(typeEquals(method, ManualTest.class));
		
		method = new TestMethod("test4AnnotationsTestMethod", NoAnnotationsTestCase.class);
		assertTrue(typeEquals(method, PerformanceTest.class));
		
		method = new TestMethod("test3AnnotationsTestMethod", NoAnnotationsTestCase.class);
		assertTrue(typeEquals(method, LargeTest.class));
		
		method = new TestMethod("test2AnnotationsTestMethod", NoAnnotationsTestCase.class);
		assertTrue(typeEquals(method, MediumTest.class));
	}
	
	public void testTestTypeEqualsTo_oneAnnotationsClass()
	{
		TestMethod method;
		
		method = new TestMethod("testNoAnnotationsTestMethod", LargeTestCase.class);
		assertTrue(typeEquals(method, LargeTest.class));
				
		method = new TestMethod("testSmallTestMethod", LargeTestCase.class);
		assertTrue(typeEquals(method, SmallTest.class));
		
		method = new TestMethod("testMediumTestMethod", LargeTestCase.class);
		assertTrue(typeEquals(method, MediumTest.class));
		
		method = new TestMethod("testLargeTestMethod", LargeTestCase.class);
		assertTrue(typeEquals(method, LargeTest.class));
		
		method = new TestMethod("testPerformanceTestMethod", LargeTestCase.class);
		assertTrue(typeEquals(method, PerformanceTest.class));
		
		method = new TestMethod("testManualTestMethod", LargeTestCase.class);
		assertTrue(typeEquals(method, ManualTest.class));		
	}
	
	public void testTestTypeEqualsTo_multiAnnotationsClasses()
	{
		TestMethod method;
		
		method = new TestMethod("testNoAnnotationsTestMethod", Annotations5TestCase.class);
		assertTrue(typeEquals(method, ManualTest.class));
		
		method = new TestMethod("testNoAnnotationsTestMethod", Annotations4TestCase.class);
		assertTrue(typeEquals(method, PerformanceTest.class));
		
		method = new TestMethod("testNoAnnotationsTestMethod", Annotations3TestCase.class);
		assertTrue(typeEquals(method, LargeTest.class));
		
		method = new TestMethod("testNoAnnotationsTestMethod", Annotations2TestCase.class);
		assertTrue(typeEquals(method, MediumTest.class));
		
		method = new TestMethod("testNoAnnotationsTestMethod", Annotations1TestCase.class);
		assertTrue(typeEquals(method, SmallTest.class));				
		
		method = new TestMethod("testNoAnnotationsTestMethod", Annotations0TestCase.class);
		assertTrue(typeEquals(method, SmallTest.class));				
		
	}
}
