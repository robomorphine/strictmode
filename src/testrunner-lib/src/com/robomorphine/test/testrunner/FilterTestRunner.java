package com.robomorphine.test.testrunner;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.test.InstrumentationTestRunner;
import android.test.suitebuilder.TestMethod;

import com.robomorphine.test.filtering.FilterPredicate;
import com.android.internal.util.Predicate;

/**
 * This class will deprecate "size" argument and add "filter" argument instead.
 */
public class FilterTestRunner extends InstrumentationTestRunner
{
	private static final String ARGUMENT_TEST_SIZE_PREDICATE = "size";
	private static final String ARGUMENT_TEST_FILTER_PREDICATE = "filter";
	
	private FilterPredicate m_filter = null;
		
	@Override
	public void onCreate(Bundle args) 
	{
		if(args.containsKey(ARGUMENT_TEST_SIZE_PREDICATE))
		{
			String msg =
				String.format("Please do not use \"%s\" argument, it's not valid with this runner. " +
							  "Use \"%s\" argument instead.",
							  ARGUMENT_TEST_SIZE_PREDICATE, 
							  ARGUMENT_TEST_FILTER_PREDICATE);
			
			throw new IllegalArgumentException(msg);
		}				
		m_filter = new FilterPredicate(args.getString(ARGUMENT_TEST_FILTER_PREDICATE));
		
		super.onCreate(args);		
	};
	
	List<Predicate<TestMethod>> getBuilderRequirements() 
	{			
		List<Predicate<TestMethod>> list = new ArrayList<Predicate<TestMethod>>();
		list.add(new Predicate<TestMethod>() {            
            @Override
            public boolean apply(TestMethod t) {         
                return m_filter.apply(t);
            }
        });
		return list;
	}
}
