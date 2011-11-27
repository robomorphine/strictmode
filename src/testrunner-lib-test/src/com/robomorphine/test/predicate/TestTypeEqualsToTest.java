package com.robomorphine.test.predicate;

import com.robomorphine.test.annotation.LongTest;
import com.robomorphine.test.annotation.ManualTest;
import com.robomorphine.test.annotation.PerformanceTest;
import com.robomorphine.test.annotation.ShortTest;
import com.robomorphine.test.annotation.StabilityTest;
import com.robomorphine.test.predicate.TestTypeEqualsTo;
import com.robomorphine.test.predicate.TestTypeEqualsTo.TestType;

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
		@android.test.suitebuilder.annotation.LargeTest public void testAndroidLargeTestMethod(){}
		
		@PerformanceTest public void testPerformanceTestMethod(){}		
		@StabilityTest public void testStabilityTestMethod(){}
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
		@StabilityTest public void testStabilityTestMethod(){}
		@ManualTest	public void testManualTestMethod(){}
	}
	
	@SmallTest @MediumTest @LargeTest @PerformanceTest @StabilityTest @ManualTest
    static class Annotations6TestCase extends TestCase
    {
        public void testNoAnnotationsTestMethod(){} 
    }
	
	@SmallTest @MediumTest @LargeTest @PerformanceTest @StabilityTest
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
	
	public void testTestTypeEqualsTo_alias_small()
    {   
        String [] smallTestMethods = new String [] {
                "testNoAnnotationsTestMethod",
                "testSmallTestMethod",
                "testAndroidSmallTestMethod",
                "testShortTestMethod"
        };
        
        for(String testMethod : smallTestMethods) {
            TestMethod method = new TestMethod(testMethod, NoAnnotationsTestCase.class);
            assertTrue(typeEquals(method, SmallTest.class));
            assertTrue(typeEquals(method, ShortTest.class));
            assertTrue(typeEquals(method, android.test.suitebuilder.annotation.SmallTest.class));
            assertFalse(typeEquals(method, MediumTest.class));
            assertFalse(typeEquals(method, android.test.suitebuilder.annotation.MediumTest.class));
            assertFalse(typeEquals(method, LargeTest.class));
            assertFalse(typeEquals(method, android.test.suitebuilder.annotation.LargeTest.class));
            assertFalse(typeEquals(method, LongTest.class));
            assertFalse(typeEquals(method, PerformanceTest.class));
            assertFalse(typeEquals(method, StabilityTest.class));
            assertFalse(typeEquals(method, ManualTest.class));
        }
    }
	
	public void testTestTypeEqualsTo_alias_medium()
    {   
        String [] smallTestMethods = new String [] {
                "testMediumTestMethod",
                "testAndroidMediumTestMethod"
        };
        
        for(String testMethod : smallTestMethods) {
            TestMethod method = new TestMethod(testMethod, NoAnnotationsTestCase.class);
            assertFalse(typeEquals(method, SmallTest.class));
            assertFalse(typeEquals(method, ShortTest.class));
            assertFalse(typeEquals(method, android.test.suitebuilder.annotation.SmallTest.class));
            assertTrue(typeEquals(method, MediumTest.class));
            assertTrue(typeEquals(method, android.test.suitebuilder.annotation.MediumTest.class));
            assertFalse(typeEquals(method, LargeTest.class));
            assertFalse(typeEquals(method, android.test.suitebuilder.annotation.LargeTest.class));
            assertFalse(typeEquals(method, LongTest.class));
            assertFalse(typeEquals(method, PerformanceTest.class));
            assertFalse(typeEquals(method, StabilityTest.class));
            assertFalse(typeEquals(method, ManualTest.class));
        }
    }
	
	public void testTestTypeEqualsTo_alias_large()
    {   
        String [] smallTestMethods = new String [] {
                "testLargeTestMethod",
                "testAndroidLargeTestMethod",
                "testLongTestMethod"
        };
        
        for(String testMethod : smallTestMethods) {
            TestMethod method = new TestMethod(testMethod, NoAnnotationsTestCase.class);
            assertFalse(typeEquals(method, SmallTest.class));
            assertFalse(typeEquals(method, ShortTest.class));
            assertFalse(typeEquals(method, android.test.suitebuilder.annotation.SmallTest.class));
            assertFalse(typeEquals(method, MediumTest.class));
            assertFalse(typeEquals(method, android.test.suitebuilder.annotation.MediumTest.class));
            assertTrue(typeEquals(method, LargeTest.class));
            assertTrue(typeEquals(method, android.test.suitebuilder.annotation.LargeTest.class));
            assertTrue(typeEquals(method, LongTest.class));
            assertFalse(typeEquals(method, PerformanceTest.class));
            assertFalse(typeEquals(method, StabilityTest.class));
            assertFalse(typeEquals(method, ManualTest.class));
        }
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
        
        method = new TestMethod("testStabilityTestMethod", NoAnnotationsTestCase.class);
        assertTrue(typeEquals(method, StabilityTest.class));
        
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
		
		method = new TestMethod("testStabilityTestMethod", LargeTestCase.class);
        assertTrue(typeEquals(method, StabilityTest.class));
		
		method = new TestMethod("testManualTestMethod", LargeTestCase.class);
		assertTrue(typeEquals(method, ManualTest.class));		
	}
	
	public void testTestTypeEqualsTo_multiAnnotationsClasses()
	{
		TestMethod method;
		
		method = new TestMethod("testNoAnnotationsTestMethod", Annotations6TestCase.class);
        assertTrue(typeEquals(method, ManualTest.class));
		
		method = new TestMethod("testNoAnnotationsTestMethod", Annotations5TestCase.class);
		assertTrue(typeEquals(method, StabilityTest.class));
		
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
	
	public void testTestType() {

        TestMethod method;
        
        method = new TestMethod("testShortTestMethod", NoAnnotationsTestCase.class);
        assertEquals(TestType.Small, TestTypeEqualsTo.getTestType(method));
        
        method = new TestMethod("testSmallTestMethod", NoAnnotationsTestCase.class);
        assertEquals(TestType.Small, TestTypeEqualsTo.getTestType(method));
        
        method = new TestMethod("testAndroidSmallTestMethod", NoAnnotationsTestCase.class);
        assertEquals(TestType.Small, TestTypeEqualsTo.getTestType(method));
        
        method = new TestMethod("testMediumTestMethod", NoAnnotationsTestCase.class);
        assertEquals(TestType.Medium, TestTypeEqualsTo.getTestType(method));

        method = new TestMethod("testAndroidMediumTestMethod", NoAnnotationsTestCase.class);
        assertEquals(TestType.Medium, TestTypeEqualsTo.getTestType(method));
        
        method = new TestMethod("testAndroidMediumTestMethod", NoAnnotationsTestCase.class);
        assertEquals(TestType.Medium, TestTypeEqualsTo.getTestType(method));
        
        method = new TestMethod("testLongTestMethod", NoAnnotationsTestCase.class);
        assertEquals(TestType.Large, TestTypeEqualsTo.getTestType(method));
        
        method = new TestMethod("testLargeTestMethod", NoAnnotationsTestCase.class);
        assertEquals(TestType.Large, TestTypeEqualsTo.getTestType(method));
        
        method = new TestMethod("testAndroidLargeTestMethod", NoAnnotationsTestCase.class);
        assertEquals(TestType.Large, TestTypeEqualsTo.getTestType(method));
        
        method = new TestMethod("testPerformanceTestMethod", NoAnnotationsTestCase.class);
        assertEquals(TestType.Performance, TestTypeEqualsTo.getTestType(method));
        
        method = new TestMethod("testStabilityTestMethod", NoAnnotationsTestCase.class);
        assertEquals(TestType.Stability, TestTypeEqualsTo.getTestType(method));
        
        method = new TestMethod("testManualTestMethod", NoAnnotationsTestCase.class);
        assertEquals(TestType.Manual, TestTypeEqualsTo.getTestType(method));
	}
}
