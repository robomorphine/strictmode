package com.robomorphine.test.filtering;

import com.robomorphine.test.annotation.LongTest;
import com.robomorphine.test.annotation.ManualTest;
import com.robomorphine.test.annotation.NonUiTest;
import com.robomorphine.test.annotation.PerformanceTest;
import com.robomorphine.test.annotation.ShortTest;
import com.robomorphine.test.annotation.StabilityTest;
import com.robomorphine.test.annotation.UiTest;
import com.robomorphine.test.filtering.FilterParser.FilterEntry;
import com.robomorphine.test.predicate.HasAnnotation;
import com.robomorphine.test.predicate.IsUiTest;
import com.robomorphine.test.predicate.Predicate;
import com.robomorphine.test.predicate.Predicates;
import com.robomorphine.test.predicate.TestTypeEqualsTo;

import android.test.suitebuilder.TestMethod;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This predicate has special logic for filtering test cases. 
 * As input it receives a filter of type: "[{+,-}<annotationClass>]*", 
 * where "+" symbol means "include" and "-" means "exclude".
 *  
 *         For example:    "+andoird.test.ann1+andoird.test.ann2-andoird.test.ann3".
 *                         "-andoird.test.ann1"
 *                         "-andoird.test.ann1-andoird.test.ann2"
 * 
 * For any TestMethod to be included it should have ANY of "+" annotations and NONE 
 * of the "-" annotations.
 * ( Exclusion "-" takes precedences over Inclusion "+" ). The order is not relevant.
 * 
 * Note: TestMethid annotations are "inherited" from enclosing TestCase class.
 * 
 *      For example:
 *          @a
 *          class T1 extends TestCase
 *          {
 *              void tm1() {}
 *              @b void tm2() {}
 *              @b @c void tm3() {}
 *                @b @c @d void tm4() {}
 *          }
 *  
 *          "+a" -> tm1, tm2, tm3, tm4
 *          "-a" -> none
 *          "+b" -> tm2, tm3, tm4
 *          "-b" -> tm
 *          "+a-b" -> tm1
 *          "+c-d" -> tm3
 *          "-d+a" -> tm1, tm2, tm3
 * 
 * 
 * As a special case, if first +/- symbol is omitted then predicate assumes "+".
 * 
 *    For example:  "android.test.ann1+android.test.ann2" == "+android.test.ann1+android.test.ann2".
 * 
 * Some specific annotations have built-in aliases. This aliases are case insensitive 
 * (but note that annotation class names _are_ case sensitive). List of aliases: 
 * 
 * s, small   - android.test.suitebuilder.annotation.SmallTest 
 * m, medium  - android.test.suitebuilder.annotation.MediumTest
 * l, large   - android.test.suitebuilder.annotation.LargeTest
 * p, perf, performance - android.test.suitebuilder.annotation.PerformanceTest
 * mn, manual - android.test.suitebuilder.annotation.ManualTest
 * 
 *         For example: 
 * 
 *         "+s-medium" == "+android.test.suitebuilder.annotation.SmallTest-android.test.suitebuilder.annotation.MediumTest"
 *         "s+M+l" == "s+m+l" == "S+M+L"
 *  
 * Five annotation have special meaning and a handled in a different way then all other annotations are.
 * These annotations represent test types:
 *  
 *   small - android.test.suitebuilder.annotation.SmallTest
 *   medium - android.test.suitebuilder.annotation.MediumTest
 *   large - android.test.suitebuilder.annotation.LargeTest
 *   performance - android.test.suitebuilder.annotation.PerformanceTest
 *   manual - android.test.suitebuilder.annotation.ManualTest
 *   
 * At any given time any TestMethod may have only one effective test type associated with it. 
 * Even if several annotations are applied to this TestMethod, only one will take effect during filtering. 
 * Here is how effective test type is calculated:
 * 
 *  1) If TestMethod has only one TestType annotation -> this annotation takes effect.
 *      For example: 
 *          @SmallTest void testM1() {} -> small test
 *          @ManualTest void testM2() {} -> manual test
 *  
 *  2) If TestMethod has several TestType annotations, only one annotations takes effect. 
 *     The winner is determined by annotation precedence order which is next:
 *     manual > performance > large > medium > small.
 *     
 *     For example:
 *             @SmallTest @LargeTest void testM1() -> large test
 *             @LargeTest @SmallTest void testM1() -> large test
 *             @LargeTest @SmallTest @ManualTest void testM1() -> manual test
 *  
 *  3) If TestMethod has no test type annotations, then test type annotations are taken 
 *     from enclosing TestCase class. If TestCase class has only one test type annotation
 *     then it takes effects.
 *     
 *      For example: 
 *          @SmallTest class T1 extends TestCase 
 *          {
 *               void testM1() {} -> small test 
 *             } 
 *     
 *             @LargeTest class T1 extends TestCase 
 *          {
 *               void testM1() {} -> large test
 *             }
 *     
 *  4) If TestMethod has no test type annotations, but enclosing TestCase class has 
 *      several test type annotations, then only one test type annotation takes effect.
 *      This annotation is determined as in step #2 for test methods: according to test type precedence.
 *  
 *      For example:
 *  
 *       @SmallTest @LargeTest class T1 extends TestCase 
 *      {
 *          void testM1() {} -> large test 
 *         } 
 *     
 *      @LargeTest @SmallTest class T1 extends TestCase 
 *      {
 *          void testM1() {} -> large test 
 *         }
 *     
 *      @LargeTest @SmallTest @ManualTest class T1 extends TestCase 
 *      {
 *          void testM1() {} -> manual test 
 *         }
 *     
 *  5) If nor TestMethod, neither enclosing TestCase class has any of the test type annotations,
 *      then test is considered to be SmallTest.
 *  
 *      For example:
 *  
 *      class T1 extends TestCase 
 *      {
 *          void testM1() {} -> small test 
 *         } 
 *
 */
public class FilterPredicate implements Predicate<TestMethod> {
    private static final String TAG = FilterPredicate.class.getSimpleName();

    private final static Map<String, String> KNOWN_ALIASES;
    static {
        Map<String, String> map = new HashMap<String, String>();
        map.put("s".toLowerCase(), SmallTest.class.getName());
        map.put("small".toLowerCase(), SmallTest.class.getName());
        map.put("short".toLowerCase(), ShortTest.class.getName());
        map.put("m".toLowerCase(), MediumTest.class.getName());
        map.put("medium".toLowerCase(), MediumTest.class.getName());
        map.put("l".toLowerCase(), LargeTest.class.getName());
        map.put("large".toLowerCase(), LargeTest.class.getName());
        map.put("long".toLowerCase(), LongTest.class.getName());
        map.put("p".toLowerCase(), PerformanceTest.class.getName());
        map.put("perf".toLowerCase(), PerformanceTest.class.getName());
        map.put("performance".toLowerCase(), PerformanceTest.class.getName());
        map.put("st".toLowerCase(), StabilityTest.class.getName());
        map.put("stable".toLowerCase(), StabilityTest.class.getName());
        map.put("stability".toLowerCase(), StabilityTest.class.getName());
        map.put("ui".toLowerCase(), UiTest.class.getName());
        map.put("mn".toLowerCase(), ManualTest.class.getName());
        map.put("manual".toLowerCase(), ManualTest.class.getName());
        KNOWN_ALIASES = Collections.unmodifiableMap(map);
    }

    public String getByAlias(String val) {
        val = val.toLowerCase();
        if (KNOWN_ALIASES.containsKey(val)) {
            return KNOWN_ALIASES.get(val);
        }
        return val;
    }

    private static class TruePredicate implements Predicate<TestMethod> {
        @Override
        public boolean apply(TestMethod t) {
            return true;
        }
    }

    private final static TruePredicate TRUE_PREDICATE = new TruePredicate();

    private final Predicate<TestMethod> m_predicate;

    public FilterPredicate(String filter) {
        Log.w(TAG, "Filtering tests with: \"" + filter + "\"");
        m_predicate = createPredicate(FilterParser.parse(filter));
    }

    @SuppressWarnings("unchecked")
    private Class<? extends Annotation> getAnnotationClass(String annotationClassName) {
        if (annotationClassName == null) {
            return null;
        }
        annotationClassName = getByAlias(annotationClassName);

        try {
            Class<?> annotationClass = Class.forName(annotationClassName);
            if (annotationClass.isAnnotation()) {
                return (Class<? extends Annotation>) annotationClass;
            } else {
                String msg = String.format("Provided annotation value \"%s\" is not an Annotation",
                        annotationClassName);
                Log.e(TAG, msg);
            }
        } catch (ClassNotFoundException e) {
            String msg = String.format("Could not find class for specified annotation \"%s\"",
                    annotationClassName);
            Log.e(TAG, msg);
        }
        return null;
    }

    @SuppressWarnings("all")
    private Predicate<TestMethod> createPredicate(List<FilterEntry> filterEntries) {
        if (filterEntries == null || filterEntries.size() == 0) {
            return TRUE_PREDICATE;
        }

        List<Predicate<TestMethod>> orPredicates = new ArrayList<Predicate<TestMethod>>(
                filterEntries.size());

        List<Predicate<TestMethod>> andPredicates = new ArrayList<Predicate<TestMethod>>(
                filterEntries.size());

        for (FilterEntry filterEntry : filterEntries) {
            Class<? extends Annotation> annotation = getAnnotationClass(filterEntry.value);
            if (annotation != null) {
                addPredicate(filterEntry.action, annotation, orPredicates, andPredicates);
            }
        }

        if (orPredicates.isEmpty())
            orPredicates.add(TRUE_PREDICATE);
        if (andPredicates.isEmpty())
            andPredicates.add(TRUE_PREDICATE);
        return Predicates.and(Predicates.or(orPredicates), Predicates.and(andPredicates));
    }

    private void addPredicate(int action, Class<? extends Annotation> annotation,
            List<Predicate<TestMethod>> orPredicates, List<Predicate<TestMethod>> andPredicates) {
        /* Special handling for annotations that indicate test type */
        if (TestTypeEqualsTo.isTestTypeAnnotation(annotation)) {
            switch (action) {
                case FilterParser.INCLUDE_ACTION:
                    orPredicates.add(new TestTypeEqualsTo(annotation));
                    break;
                case FilterParser.EXCLUDE_ACTION:
                    andPredicates
                            .add(Predicates.<TestMethod> not(new TestTypeEqualsTo(annotation)));
                    break;
            }
        } else if(annotation == UiTest.class) {
            switch(action) {
                case FilterParser.INCLUDE_ACTION:
                    andPredicates.add(new IsUiTest());
                    break;
                case FilterParser.EXCLUDE_ACTION:
                    andPredicates.add(Predicates.not(new IsUiTest()));
                    break;
            }
        } else if(annotation == NonUiTest.class) {        
            switch(action) {
                case FilterParser.INCLUDE_ACTION:
                    andPredicates.add(Predicates.not(new IsUiTest()));
                    break;
                case FilterParser.EXCLUDE_ACTION:
                    andPredicates.add(new IsUiTest());
                    break;
            }
        } else {
            /* All other annotation are handled using standard rules */
            switch (action) {
                case FilterParser.INCLUDE_ACTION:
                    orPredicates.add(new HasAnnotation(annotation));
                    break;
                case FilterParser.EXCLUDE_ACTION:
                    andPredicates.add(Predicates.<TestMethod> not(new HasAnnotation(annotation)));
                    break;
            }
        }
    }

    @Override
    public boolean apply(TestMethod t) {
        return m_predicate.apply(t);
    }
}
