package com.robomorphine.test.predicate;

import com.robomorphine.test.annotation.NonUiTest;
import com.robomorphine.test.annotation.UiTest;


import android.app.Activity;
import android.test.suitebuilder.TestMethod;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

public class IsUiTestPredicateTest extends TestCase {

    @Retention(RetentionPolicy.RUNTIME)
    static @interface ExpectedResult {
        boolean value();
    }
    
    static class NoClassAnnotations extends TestCase {
        @ExpectedResult(false)
        public void testNoMethodAnnotations() {
        }
        
        @ExpectedResult(true)
        @UiTest
        public void testUi() {
        }
        
        @ExpectedResult(false)
        @NonUiTest
        public void  testNonUi() {            
        }
        
        @ExpectedResult(false)
        @UiTest @NonUiTest
        public void testUiAndNonUi(){            
        }
    }
    
    @UiTest
    static class UiClass extends TestCase {
        
        @ExpectedResult(true)
        public void testNoMethodAnnotations() {
        }
        
        @ExpectedResult(true)
        @UiTest
        public void testUi() {
        }
        
        @ExpectedResult(false)
        @NonUiTest
        public void  testNonUi() {            
        }
        
        @ExpectedResult(false)
        @UiTest @NonUiTest
        public void testUiAndNonUi(){            
        }
    }
    
    @NonUiTest
    static class NonUiClass extends TestCase {
        
        @ExpectedResult(false)
        public void testNoMethodAnnotations() {
        }
        
        @ExpectedResult(true)
        @UiTest
        public void testUi() {
        }
        
        @ExpectedResult(false)
        @NonUiTest
        public void  testNonUi() {            
        }
        
        @ExpectedResult(false)
        @UiTest @NonUiTest
        public void testUiAndNonUi(){            
        }
    }
    
    @UiTest @NonUiTest
    static class UiAndNonUiClass extends TestCase {
        
        @ExpectedResult(false)
        public void testNoMethodAnnotations() {
        }
        
        @ExpectedResult(true)
        @UiTest
        public void testUi() {
        }
        
        @ExpectedResult(false)
        @NonUiTest
        public void  testNonUi() {            
        }
        
        @ExpectedResult(false)
        @UiTest @NonUiTest
        public void testUiAndNonUi(){            
        }
    }
    
    static class TestActivity extends Activity {        
    }
    
    @SuppressWarnings("deprecation")
    static class ActivityInstrumentationTestCase 
           extends android.test.ActivityInstrumentationTestCase<TestActivity> {
        
        public ActivityInstrumentationTestCase() {
            super(null, TestActivity.class);
        }
        
        @ExpectedResult(true)
        public void testNoMethodAnnotations() {
        }
        
        @ExpectedResult(true)
        @UiTest
        public void testUi() {
        }
        
        @ExpectedResult(false)
        @NonUiTest
        public void  testNonUi() {            
        }
        
        @ExpectedResult(false)
        @UiTest @NonUiTest
        public void testUiAndNonUi(){            
        }
    }
    
    static class ActivityInstrumentationTestCase2 
           extends android.test.ActivityInstrumentationTestCase2<TestActivity> {
        
        public ActivityInstrumentationTestCase2() {
            super(null, TestActivity.class);
        }
        
        @ExpectedResult(true)
        public void testNoMethodAnnotations() {
        }
        
        @ExpectedResult(true)
        @UiTest
        public void testUi() {
        }
        
        @ExpectedResult(false)
        @NonUiTest
        public void  testNonUi() {            
        }
        
        @ExpectedResult(false)
        @UiTest @NonUiTest
        public void testUiAndNonUi(){            
        }
    }
    
    @NonUiTest
    @SuppressWarnings("deprecation")
    static class NonUiActivityInstrumentationTestCase 
           extends android.test.ActivityInstrumentationTestCase<TestActivity> {
        
        public NonUiActivityInstrumentationTestCase() {
            super(null, TestActivity.class);
        }
        
        @ExpectedResult(false)
        public void testNoMethodAnnotations() {
        }
        
        @ExpectedResult(true)
        @UiTest
        public void testUi() {
        }
        
        @ExpectedResult(false)
        @NonUiTest
        public void  testNonUi() {            
        }
        
        @ExpectedResult(false)
        @UiTest @NonUiTest
        public void testUiAndNonUi(){            
        }
    }
    
    @NonUiTest
    static class NonUiActivityInstrumentationTestCase2 
           extends android.test.ActivityInstrumentationTestCase2<TestActivity> {
        
        public NonUiActivityInstrumentationTestCase2() {
            super(null, TestActivity.class);
        }
        
        @ExpectedResult(false)
        public void testNoMethodAnnotations() {
        }
        
        @ExpectedResult(true)
        @UiTest
        public void testUi() {
        }
        
        @ExpectedResult(false)
        @NonUiTest
        public void  testNonUi() {            
        }
        
        @ExpectedResult(false)
        @UiTest @NonUiTest
        public void testUiAndNonUi(){            
        }
    }
    
    
    public void testAnnotations() {
        List<Class<? extends TestCase>> testCases = new LinkedList<Class<? extends TestCase>>();
        IsUiTest predicate = new IsUiTest();
        
        testCases.add(NoClassAnnotations.class);
        testCases.add(UiClass.class);
        testCases.add(NonUiClass.class);
        testCases.add(UiAndNonUiClass.class);        
        testCases.add(ActivityInstrumentationTestCase.class);
        testCases.add(ActivityInstrumentationTestCase2.class);
        testCases.add(NonUiActivityInstrumentationTestCase.class);
        testCases.add(NonUiActivityInstrumentationTestCase2.class);
        
        for(Class<? extends TestCase> clazz : testCases) {            
            for(Method method : clazz.getDeclaredMethods()) {                
                TestMethod testMethod = new TestMethod(method.getName(), clazz);
                
                ExpectedResult expectedAnnotaiton = method.getAnnotation(ExpectedResult.class);
                assertNotNull("Has no expected result: " + method, expectedAnnotaiton);
                
                boolean expected = expectedAnnotaiton.value();
                boolean actual = predicate.apply(testMethod);   
                
                String name = testMethod.getEnclosingClass().getSimpleName() + "." + testMethod.getName();
                assertEquals(name, expected, actual);
            }
        }
    }
}
