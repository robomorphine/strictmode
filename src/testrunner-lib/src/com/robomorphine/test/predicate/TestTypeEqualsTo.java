
package com.robomorphine.test.predicate;

import com.robomorphine.test.annotation.LongTest;
import com.robomorphine.test.annotation.ManualTest;
import com.robomorphine.test.annotation.PerformanceTest;
import com.robomorphine.test.annotation.ShortTest;
import com.robomorphine.test.annotation.StabilityTest;

import android.test.suitebuilder.TestMethod;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestTypeEqualsTo implements Predicate<TestMethod> {
    /*
     * Note: Order is important. If multiple annotation are used, then first
     * annotation from the list is considered the effective one. All other
     * annotations should be ignored.
     */
    public final static List<Class<? extends Annotation>> TEST_TYPE_ANNOTATIONS;
    static {
        ArrayList<Class<? extends Annotation>> list = new ArrayList<Class<? extends Annotation>>();
        list.add(ManualTest.class);
        list.add(StabilityTest.class);
        list.add(PerformanceTest.class);
        list.add(LongTest.class);
        list.add(LargeTest.class);
        list.add(LongTest.class);
        list.add(android.test.suitebuilder.annotation.LargeTest.class);
        list.add(MediumTest.class);
        list.add(android.test.suitebuilder.annotation.MediumTest.class);
        list.add(ShortTest.class);
        list.add(SmallTest.class);
        list.add(android.test.suitebuilder.annotation.SmallTest.class);
        TEST_TYPE_ANNOTATIONS = Collections.unmodifiableList(list);
    }
    
    public final static Map<Class<? extends Annotation>, Class<? extends Annotation>> TEST_TYPE_ALIASES;
    static {
        Map<Class<? extends Annotation>, Class<? extends Annotation>> aliases;
        aliases = new HashMap<Class<? extends Annotation>, Class<? extends Annotation>>();
        
        aliases.put(android.test.suitebuilder.annotation.LargeTest.class, LargeTest.class);
        aliases.put(android.test.suitebuilder.annotation.MediumTest.class, MediumTest.class);
        aliases.put(android.test.suitebuilder.annotation.SmallTest.class, SmallTest.class);
        aliases.put(LongTest.class, LargeTest.class);
        aliases.put(ShortTest.class, SmallTest.class);
        TEST_TYPE_ALIASES = Collections.unmodifiableMap(aliases);
    }
    
    public final static Class<? extends Annotation> dealias(Class<? extends Annotation> annotation) {
        Class<? extends Annotation> dealiasedTo = TEST_TYPE_ALIASES.get(annotation);
        if(dealiasedTo != null) {
            return dealiasedTo;
        }
        
        return annotation;
    }

    public static boolean isTestTypeAnnotation(Class<? extends Annotation> annotation) {
        return TEST_TYPE_ANNOTATIONS.contains(annotation);
    }

    private final Class<? extends Annotation> m_testType;

    public TestTypeEqualsTo(Class<? extends Annotation> testTypeAnnotation) {
        if (!isTestTypeAnnotation(testTypeAnnotation)) {
            String msg = String.format("Annotation \"%s\" does not represent test type.",
                    testTypeAnnotation.getName());

            throw new IllegalArgumentException(msg);
        }
        m_testType = dealias(testTypeAnnotation);
    }

    Class<? extends Annotation> getTestType(TestMethod t) {
        // Determine test type

        // 1. Use test method annotations.
        for (Class<? extends Annotation> annotation : TEST_TYPE_ANNOTATIONS) {
            if (t.getAnnotation(annotation) != null)
                return dealias(annotation);
        }

        // 2. Use enclosing test class annotations.
        for (Class<? extends Annotation> annotation : TEST_TYPE_ANNOTATIONS) {
            if (t.getEnclosingClass().getAnnotation(annotation) != null)
                return dealias(annotation);
        }

        // 3. So no test type annotations on method or class? Default to
        // SmallTest.
        return SmallTest.class;
    }

    @Override
    public boolean apply(TestMethod t) {
        return getTestType(t).equals(m_testType);
    }
    
    @Override
    public String toString() {
        return "[is-test-type " + m_testType.getSimpleName() + "]";
    }
}
