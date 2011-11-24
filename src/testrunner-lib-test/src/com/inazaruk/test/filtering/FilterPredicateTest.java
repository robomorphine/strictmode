package com.inazaruk.test.filtering;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.inazaruk.test.filtering.FilterPredicate;

import junit.framework.TestCase;
import android.test.suitebuilder.TestMethod;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.ManualTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.PerformanceTest;
import android.test.suitebuilder.annotation.SmallTest;

public class FilterPredicateTest extends TestCase
{
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.METHOD, ElementType.TYPE})
	public static @interface a {}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.METHOD, ElementType.TYPE})
	public static @interface b {}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.METHOD, ElementType.TYPE})
	public static @interface c {}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.METHOD, ElementType.TYPE})
	public static @interface d {}
	
	private static class CustomFilterPredicate extends FilterPredicate
	{
		private final static Map<String,String> ALIAS_MAP;
		static
		{
			Map<String,String> map = new HashMap<String, String>();
			map.put("a", a.class.getName());
			map.put("b", b.class.getName());
			map.put("c", c.class.getName());
			map.put("d", d.class.getName());
			ALIAS_MAP = Collections.unmodifiableMap(map);
		}
		
		public CustomFilterPredicate(String filter)
		{
			super(filter);
		}
		
		@Override
		public String getByAlias(String val)
		{
			if(ALIAS_MAP.containsKey(val))
			{
				return ALIAS_MAP.get(val);
			}			
			return super.getByAlias(val);
		}
	}
	
	@a
	static class ExampleTestCase extends TestCase
	{	
		public void testA() {}
		public @b void testAB() {}
		public @b @c void testABC() {}
		public @b @c @d void testABCD() {}
	}
	
	private final static TestMethod TM_A = new TestMethod("testA", ExampleTestCase.class);
	private final static TestMethod TM_AB = new TestMethod("testAB", ExampleTestCase.class);
	private final static TestMethod TM_ABC = new TestMethod("testABC", ExampleTestCase.class);
	private final static TestMethod TM_ABCD = new TestMethod("testABCD", ExampleTestCase.class);
	
	
	public void testFilter_ordinaryAnnotations()
	{
		FilterPredicate predicate = new CustomFilterPredicate(null);
		assertTrue(predicate.apply(TM_A));
		assertTrue(predicate.apply(TM_AB));
		assertTrue(predicate.apply(TM_ABC));
		assertTrue(predicate.apply(TM_ABCD));
				
		predicate = new CustomFilterPredicate("");
		assertTrue(predicate.apply(TM_A));
		assertTrue(predicate.apply(TM_AB));
		assertTrue(predicate.apply(TM_ABC));
		assertTrue(predicate.apply(TM_ABCD));
		
		predicate = new CustomFilterPredicate("a");
		assertTrue(predicate.apply(TM_A));
		assertTrue(predicate.apply(TM_AB));
		assertTrue(predicate.apply(TM_ABC));
		assertTrue(predicate.apply(TM_ABCD));
		
		predicate = new CustomFilterPredicate("+a");
		assertTrue(predicate.apply(TM_A));
		assertTrue(predicate.apply(TM_AB));
		assertTrue(predicate.apply(TM_ABC));
		assertTrue(predicate.apply(TM_ABCD));
		
		predicate = new CustomFilterPredicate("-a");
		assertFalse(predicate.apply(TM_A));
		assertFalse(predicate.apply(TM_AB));
		assertFalse(predicate.apply(TM_ABC));
		assertFalse(predicate.apply(TM_ABCD));
		
		predicate = new CustomFilterPredicate("+b");
		assertFalse(predicate.apply(TM_A));
		assertTrue(predicate.apply(TM_AB));
		assertTrue(predicate.apply(TM_ABC));
		assertTrue(predicate.apply(TM_ABCD));
		
		predicate = new CustomFilterPredicate("-b");
		assertTrue(predicate.apply(TM_A));
		assertFalse(predicate.apply(TM_AB));
		assertFalse(predicate.apply(TM_ABC));
		assertFalse(predicate.apply(TM_ABCD));
		
		predicate = new CustomFilterPredicate("+c");
		assertFalse(predicate.apply(TM_A));
		assertFalse(predicate.apply(TM_AB));
		assertTrue(predicate.apply(TM_ABC));
		assertTrue(predicate.apply(TM_ABCD));
		
		predicate = new CustomFilterPredicate("+d");
		assertFalse(predicate.apply(TM_A));
		assertFalse(predicate.apply(TM_AB));
		assertFalse(predicate.apply(TM_ABC));
		assertTrue(predicate.apply(TM_ABCD));
		
		predicate = new CustomFilterPredicate("+b-d");
		assertFalse(predicate.apply(TM_A));
		assertTrue(predicate.apply(TM_AB));
		assertTrue(predicate.apply(TM_ABC));
		assertFalse(predicate.apply(TM_ABCD));
		
		predicate = new CustomFilterPredicate("-d+b");
		assertFalse(predicate.apply(TM_A));
		assertTrue(predicate.apply(TM_AB));
		assertTrue(predicate.apply(TM_ABC));
		assertFalse(predicate.apply(TM_ABCD));		
		
		predicate = new CustomFilterPredicate("+d-a");
		assertFalse(predicate.apply(TM_A));
		assertFalse(predicate.apply(TM_AB));
		assertFalse(predicate.apply(TM_ABC));
		assertFalse(predicate.apply(TM_ABCD));
		
		predicate = new CustomFilterPredicate("-d+a");
		assertTrue(predicate.apply(TM_A));
		assertTrue(predicate.apply(TM_AB));
		assertTrue(predicate.apply(TM_ABC));
		assertFalse(predicate.apply(TM_ABCD));
	}
		
	static class AliasExampleTestCase extends TestCase
	{
		public void testNoAnnotationsTestMethod(){}
		
		@SmallTest 	public void testSmallTestMethod(){}		
		@MediumTest public void testMediumTestMethod(){}		
		@LargeTest 	public void testLargeTestMethod(){}
		@PerformanceTest public void testPerformanceTestMethod(){}		
		@ManualTest	public void testManualTestMethod(){}		
	}
	
	private final static TestMethod TM_small = new TestMethod("testSmallTestMethod", 		AliasExampleTestCase.class);
	private final static TestMethod TM_medium = new TestMethod("testMediumTestMethod", 		AliasExampleTestCase.class);
	private final static TestMethod TM_large = new TestMethod("testLargeTestMethod", 		AliasExampleTestCase.class);
	private final static TestMethod TM_perf = new TestMethod("testPerformanceTestMethod", 	AliasExampleTestCase.class);
	private final static TestMethod TM_manual = new TestMethod("testManualTestMethod", 		AliasExampleTestCase.class);
	
	public void testFilter_aliases()
	{
		FilterPredicate predicate = new FilterPredicate("+s");
		assertTrue(predicate.apply(TM_small));
		assertFalse(predicate.apply(TM_medium));
		assertFalse(predicate.apply(TM_large));
		assertFalse(predicate.apply(TM_perf));
		assertFalse(predicate.apply(TM_manual));
		
		predicate = new FilterPredicate("+S");
		assertTrue(predicate.apply(TM_small));
		assertFalse(predicate.apply(TM_medium));
		assertFalse(predicate.apply(TM_large));
		assertFalse(predicate.apply(TM_perf));
		assertFalse(predicate.apply(TM_manual));
		
		predicate = new FilterPredicate("+small");
		assertTrue(predicate.apply(TM_small));
		assertFalse(predicate.apply(TM_medium));
		assertFalse(predicate.apply(TM_large));
		assertFalse(predicate.apply(TM_perf));
		assertFalse(predicate.apply(TM_manual));
		
		predicate = new FilterPredicate("+m");
		assertFalse(predicate.apply(TM_small));
		assertTrue(predicate.apply(TM_medium));
		assertFalse(predicate.apply(TM_large));
		assertFalse(predicate.apply(TM_perf));
		assertFalse(predicate.apply(TM_manual));
		
		predicate = new FilterPredicate("+l");
		assertFalse(predicate.apply(TM_small));
		assertFalse(predicate.apply(TM_medium));
		assertTrue(predicate.apply(TM_large));
		assertFalse(predicate.apply(TM_perf));
		assertFalse(predicate.apply(TM_manual));
		
		predicate = new FilterPredicate("+p");
		assertFalse(predicate.apply(TM_small));
		assertFalse(predicate.apply(TM_medium));
		assertFalse(predicate.apply(TM_large));
		assertTrue(predicate.apply(TM_perf));
		assertFalse(predicate.apply(TM_manual));
		
		predicate = new FilterPredicate("+mn");
		assertFalse(predicate.apply(TM_small));
		assertFalse(predicate.apply(TM_medium));
		assertFalse(predicate.apply(TM_large));
		assertFalse(predicate.apply(TM_perf));
		assertTrue(predicate.apply(TM_manual));
		
		predicate = new FilterPredicate("+s-m+l-p+mn");
		assertTrue(predicate.apply(TM_small));
		assertFalse(predicate.apply(TM_medium));
		assertTrue(predicate.apply(TM_large));
		assertFalse(predicate.apply(TM_perf));
		assertTrue(predicate.apply(TM_manual));
	}
}
