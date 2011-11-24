package com.inazaruk.test.filtering;

import java.util.List;

import junit.framework.TestCase;

import com.inazaruk.test.filtering.FilterParser;
import com.inazaruk.test.filtering.FilterParser.FilterEntry;

public class FilterParserTest extends TestCase
{
	public void testParser()
	{
		/* empty filter */
		List<FilterEntry> list = FilterParser.parse("");
		assertNotNull(list);
		assertTrue(list.isEmpty());
		
		/* only one action symbol */
		list = FilterParser.parse("+");
		assertEquals(1, list.size());
		assertEquals(FilterParser.INCLUDE_ACTION, list.get(0).action);
		assertEquals("", list.get(0).value);
		
		/* several action symbols with empty values */
		list = FilterParser.parse("+-++--");
		assertEquals(6, list.size());
		assertEquals(FilterParser.INCLUDE_ACTION, list.get(0).action);
		assertEquals(FilterParser.EXCLUDE_ACTION, list.get(1).action);
		assertEquals(FilterParser.INCLUDE_ACTION, list.get(2).action);
		assertEquals(FilterParser.INCLUDE_ACTION, list.get(3).action);
		assertEquals(FilterParser.EXCLUDE_ACTION, list.get(4).action);	
		assertEquals(FilterParser.EXCLUDE_ACTION, list.get(5).action);		
		for(FilterEntry entry : list)
		{
			assertEquals("", entry.value);
		}
		
		/* single action symbol and value */		 
		list = FilterParser.parse("+customValue");
		assertEquals(1, list.size());
		assertEquals(FilterParser.INCLUDE_ACTION, list.get(0).action);
		assertEquals("customValue", list.get(0).value);
		
		list = FilterParser.parse("-anotherCustomValue");
		assertEquals(1, list.size());
		assertEquals(FilterParser.EXCLUDE_ACTION, list.get(0).action);
		assertEquals("anotherCustomValue", list.get(0).value);
		
		/* multiple actions and values */
		list = FilterParser.parse("+val1-val2+val3+val4-val5-val6");
		assertEquals(6, list.size());
		assertEquals(FilterParser.INCLUDE_ACTION, list.get(0).action);
		assertEquals(FilterParser.EXCLUDE_ACTION, list.get(1).action);
		assertEquals(FilterParser.INCLUDE_ACTION, list.get(2).action);
		assertEquals(FilterParser.INCLUDE_ACTION, list.get(3).action);
		assertEquals(FilterParser.EXCLUDE_ACTION, list.get(4).action);	
		assertEquals(FilterParser.EXCLUDE_ACTION, list.get(5).action);
		
		for(int i = 0; i < list.size(); i++)
		{
			assertEquals("val"+Integer.toString(i+1), list.get(i).value);
		}
		
		/* value without action */
		list = FilterParser.parse("value");
		assertEquals(1, list.size());
		assertEquals(FilterParser.INCLUDE_ACTION, list.get(0).action);
		assertEquals("value", list.get(0).value);
	}
}
