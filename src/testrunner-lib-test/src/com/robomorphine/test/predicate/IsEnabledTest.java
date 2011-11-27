package com.robomorphine.test.predicate;

import com.robomorphine.test.annotation.DisabledTest;
import com.robomorphine.test.annotation.EnabledTest;

import android.test.suitebuilder.TestMethod;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

public class IsEnabledTest extends TestCase {
    
    @Retention(RetentionPolicy.RUNTIME)
    static @interface ExpectedResult {
        boolean value();
    }
    
    static class NoClassAnnotations extends TestCase {
        @ExpectedResult(true)
        public void testNoMethodAnnotations() {
        }
        
        @ExpectedResult(true)
        @EnabledTest
        public void testEnabled() {            
        }
        
        @ExpectedResult(false)
        @DisabledTest("test")
        public void  testDisabled() {            
        }
        
        @ExpectedResult(false)
        @EnabledTest @DisabledTest("test")
        public void testEnabledAndDisabled(){            
        }
    }
    
    @EnabledTest
    static class EnabledClass extends TestCase {
        @ExpectedResult(true)
        public void noMethodAnnotations() {
        }
        
        @ExpectedResult(true)
        @EnabledTest
        public void enabled() {            
        }
        
        @ExpectedResult(false)
        @DisabledTest("test")
        public void  disabled() {            
        }
        
        @ExpectedResult(false)
        @EnabledTest @DisabledTest("test")
        public void enabledAndDisabled(){            
        }
    }
    
    @DisabledTest("test")
    static class DisabledClass extends TestCase {
        @ExpectedResult(false)
        public void noMethodAnnotations() {
        }
        
        @ExpectedResult(true)
        @EnabledTest
        public void enabled() {            
        }
        
        @ExpectedResult(false)
        @DisabledTest("test")
        public void  disabled() {            
        }
        
        
        @ExpectedResult(false)
        @EnabledTest @DisabledTest("test")
        public void enabledAndDisabled(){            
        }
    }
    
    @EnabledTest @DisabledTest("test")
    static class EnabledDisabledClass extends TestCase {
        @ExpectedResult(false)
        public void noMethodAnnotations() {
        }
        
        @ExpectedResult(true)
        @EnabledTest
        public void enabled() {            
        }
        
        @ExpectedResult(false)
        @DisabledTest("test")
        public void  disabled() {            
        }
        
        @ExpectedResult(false)
        @EnabledTest @DisabledTest("test")
        public void enabledAndDisabled(){            
        }
    }
    
    public void testAnnotations() {
        List<Class<? extends TestCase>> testCases = new LinkedList<Class<? extends TestCase>>();
        IsEnabled predicate = new IsEnabled();
        
        testCases.add(NoClassAnnotations.class);
        testCases.add(EnabledClass.class);
        testCases.add(DisabledClass.class);
        testCases.add(EnabledDisabledClass.class);        
        
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
    
    private final static String IGNORED_REASON = "ignored-reason";
    private final static String CLASS_REASON = "class-reason";
    private final static String METHOD_REASON = "method-reason";
    
    static class ReasonEnabledClass extends TestCase {
        public void testMethod() {            
        }
    }
    
    @DisabledTest(IGNORED_REASON)
    static class ReasonEnabledMethod extends TestCase {
        @EnabledTest
        public void testMethod() {            
        }
    }
    
    @DisabledTest(CLASS_REASON)
    static class ReasonDisabledClass extends TestCase {        
        public void testMethod() {            
        }
    }   
    
    static class ReasonDisabledMethod extends TestCase {
        @DisabledTest(METHOD_REASON)
        public void testMethod() {            
        }
    }
    
    public void testDisabledReason() {
        TestMethod method = new TestMethod("testMethod", ReasonEnabledClass.class);        
        String reason = IsEnabled.getDisabledReason(method);
        assertNull(reason);
        
        method = new TestMethod("testMethod", ReasonEnabledMethod.class);
        reason = IsEnabled.getDisabledReason(method);
        assertNull(reason);
        
        method = new TestMethod("testMethod", ReasonDisabledClass.class);
        reason = IsEnabled.getDisabledReason(method);
        assertEquals(CLASS_REASON, reason);
        
        method = new TestMethod("testMethod", ReasonDisabledMethod.class);
        reason = IsEnabled.getDisabledReason(method);
        assertEquals(METHOD_REASON, reason);
    }
}
