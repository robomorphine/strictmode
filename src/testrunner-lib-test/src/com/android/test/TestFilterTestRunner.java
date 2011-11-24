package com.android.test;

import junit.framework.TestCase;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.ManualTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.PerformanceTest;
import android.test.suitebuilder.annotation.SmallTest;

public class TestFilterTestRunner extends TestCase
{
	@SmallTest
	public void testAsSmall()
	{
		
	}
	
	@MediumTest
	public void testAsMedium()
	{
		
	}
	
	@LargeTest
	public void testAsLarge()
	{
		
	}
	
	@ManualTest
	public void testAsManual()
	{
		
	}
	
	@PerformanceTest
	public void testAsPerfomance()
	{
		
	}
}
