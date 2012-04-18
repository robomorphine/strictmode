package com.robomorphine.strictmode.entity.violation;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ViolationParserTest extends BaseTestCase {
    
    public void testParseExceptionTitle_empty() {
        ViolationParser factory = new ViolationParser();
        ViolationException ex = factory.parseExceptionTitle("", null);
        assertNull(ex.getClassName());
        assertNull(ex.getMessage()); 
    }
    
    public void testParseExceptionTitle_semicolon() {
        ViolationParser factory = new ViolationParser();
        ViolationException ex = factory.parseExceptionTitle("", null);
        assertNull(ex.getClassName());
        assertNull(ex.getMessage()); 
    }
    
    public void testParseExceptionTitle_classNameOnly() {
        ViolationParser factory = new ViolationParser();
        String className = "testName";
        
        ViolationException ex = factory.parseExceptionTitle(className, null);
        assertEquals(className, ex.getClassName());
        assertNull(ex.getMessage()); 
        
        ex = factory.parseExceptionTitle(className + ":", null);
        assertEquals(className, ex.getClassName());
        assertNull(ex.getMessage());
        
        ex = factory.parseExceptionTitle(className + ": ", null);//with space
        assertEquals(className, ex.getClassName());
        assertNull(ex.getMessage());
    }
    
    public void testParseExceptionTitle_messageOnly() {
        ViolationParser factory = new ViolationParser();
        String message = "message";
        
        ViolationException ex = factory.parseExceptionTitle(":" + message, null);
        assertNull(ex.getClassName());
        assertEquals(message, ex.getMessage());
        
        ex = factory.parseExceptionTitle(" :" + message, null);
        assertNull(ex.getClassName());
        assertEquals(message, ex.getMessage());
    }
    
    public void testParseExceptionTitle_classNameAndMessage() {
        ViolationParser factory = new ViolationParser();
        String className = "className";
        String message = "message";
        
        ViolationException ex = factory.parseExceptionTitle(className + ":" + message, null);
        assertEquals(className, ex.getClassName());
        assertEquals(message, ex.getMessage());
        
        ex = factory.parseExceptionTitle(className + " : " + message, null);
        assertEquals(className, ex.getClassName());
        assertEquals(message, ex.getMessage());
    }
    
    public void testParseExceptionTitle_messageWithSeveralSemicolons() {
        ViolationParser factory = new ViolationParser();
        String className = "className";
        String message = "message:message:message";
        
        ViolationException ex = factory.parseExceptionTitle(className + ":" + message, null);
        assertEquals(className, ex.getClassName());
        assertEquals(message, ex.getMessage());
    }
    
    public void testParseExceptionTitle_cuase() {
        ViolationParser factory = new ViolationParser();
        String className = "className";
        String message = "message";
        ViolationException cause = new ViolationException("class", "message");
        
        ViolationException ex = factory.parseExceptionTitle(className + ":" + message, cause);
        assertEquals(className, ex.getClassName());
        assertEquals(message, ex.getMessage());
        assertSame(cause, ex.getCause());
    }
    
    public void testParseStackTrace_noEntries() {
        ViolationParser factory = new ViolationParser();
        StackTraceElement [] elements = factory.parseExceptionStackTrace(new LinkedList<String>());
        assertNotNull(elements);
        assertEquals(0, elements.length);
    }
    
    public void testParseStackTrace_emptyLine() {
        ViolationParser factory = new ViolationParser();
        List<String> stackTrace = new LinkedList<String>();
        stackTrace.add("");
        
        StackTraceElement [] elements = factory.parseExceptionStackTrace(stackTrace);
        assertNotNull(elements);
        assertEquals(1, elements.length);
        assertEquals("", elements[0].getClassName());
        assertEquals("", elements[0].getMethodName());
        assertEquals(null, elements[0].getFileName());
        assertEquals(-1, elements[0].getLineNumber());
    }
    
    public void testParseStackTrace_validEntry() {
        ViolationParser factory = new ViolationParser();
        List<String> stackTrace = new LinkedList<String>();
        stackTrace.add("at com.android.strictmodetest.ServiceBase$1.doDiskWrite(ServiceBase.java:72)");
        
        StackTraceElement [] elements = factory.parseExceptionStackTrace(stackTrace);
        assertNotNull(elements);
        assertEquals(1, elements.length);
        assertEquals("com.android.strictmodetest.ServiceBase$1", elements[0].getClassName());
        assertEquals("doDiskWrite", elements[0].getMethodName());
        assertEquals("ServiceBase.java", elements[0].getFileName());
        assertEquals(72, elements[0].getLineNumber());
    }
    
    public void testParseStackTrace_validEntry_noAt() {
        ViolationParser factory = new ViolationParser();
        List<String> stackTrace = new LinkedList<String>();
        stackTrace.add("com.android.strictmodetest.ServiceBase$1.doDiskWrite(ServiceBase.java:72)");
        
        StackTraceElement [] elements = factory.parseExceptionStackTrace(stackTrace);
        assertNotNull(elements);
        assertEquals(1, elements.length);
        assertEquals("com.android.strictmodetest.ServiceBase$1", elements[0].getClassName());
        assertEquals("doDiskWrite", elements[0].getMethodName());
        assertEquals("ServiceBase.java", elements[0].getFileName());
        assertEquals(72, elements[0].getLineNumber());
    }
    
    public void testParseStackTrace_noFunction() {
        ViolationParser factory = new ViolationParser();
        List<String> stackTrace = new LinkedList<String>();
        //classname(file:line)
        stackTrace.add("at comandroidstrictmodetestServiceBase$1(ServiceBase.java:72)");
        
        StackTraceElement [] elements = factory.parseExceptionStackTrace(stackTrace);
        assertNotNull(elements);
        assertEquals(1, elements.length);
        assertEquals("comandroidstrictmodetestServiceBase$1", elements[0].getClassName());
        assertEquals("", elements[0].getMethodName());
        assertEquals("ServiceBase.java", elements[0].getFileName());
        assertEquals(72, elements[0].getLineNumber());
    }
    
    public void testParseStackTrace_emptyFunction() {
        ViolationParser factory = new ViolationParser();
        List<String> stackTrace = new LinkedList<String>();
        //classname.(file:line)
        stackTrace.add("at com.android.strictmodetest.ServiceBase$1.(ServiceBase.java:72)");
        
        StackTraceElement [] elements = factory.parseExceptionStackTrace(stackTrace);
        assertNotNull(elements);
        assertEquals(1, elements.length);
        assertEquals("com.android.strictmodetest.ServiceBase$1", elements[0].getClassName());
        assertEquals("", elements[0].getMethodName());
        assertEquals("ServiceBase.java", elements[0].getFileName());
        assertEquals(72, elements[0].getLineNumber());
    }
    
    public void testParseStackTrace_noLocation() {
        ViolationParser factory = new ViolationParser();
        List<String> stackTrace = new LinkedList<String>();
        //classname.function
        stackTrace.add("at com.android.strictmodetest.ServiceBase$1.doDiskWrite");
        
        StackTraceElement [] elements = factory.parseExceptionStackTrace(stackTrace);
        assertNotNull(elements);
        assertEquals(1, elements.length);
        assertEquals("com.android.strictmodetest.ServiceBase$1", elements[0].getClassName());
        assertEquals("doDiskWrite", elements[0].getMethodName());
        assertEquals(null, elements[0].getFileName());
        assertEquals(-1, elements[0].getLineNumber());
    }
    
    public void testParseStackTrace_emptyLocation() {
        ViolationParser factory = new ViolationParser();
        List<String> stackTrace = new LinkedList<String>();
        //classname.function()
        stackTrace.add("at com.android.strictmodetest.ServiceBase$1.doDiskWrite()");
        
        StackTraceElement [] elements = factory.parseExceptionStackTrace(stackTrace);
        assertNotNull(elements);
        assertEquals(1, elements.length);
        assertEquals("com.android.strictmodetest.ServiceBase$1", elements[0].getClassName());
        assertEquals("doDiskWrite", elements[0].getMethodName());
        assertEquals(null, elements[0].getFileName());
        assertEquals(-1, elements[0].getLineNumber());
    }
    
    public void testParseStackTrace_nativeMethod() {
        ViolationParser factory = new ViolationParser();
        List<String> stackTrace = new LinkedList<String>();
        stackTrace.add("at com.android.strictmodetest.ServiceBase$1.doDiskWrite(Native method)");
        
        StackTraceElement [] elements = factory.parseExceptionStackTrace(stackTrace);
        assertNotNull(elements);
        assertEquals(1, elements.length);
        assertEquals("com.android.strictmodetest.ServiceBase$1", elements[0].getClassName());
        assertEquals("doDiskWrite", elements[0].getMethodName());
        assertEquals(null, elements[0].getFileName());
        assertEquals(-2, elements[0].getLineNumber());
    }
    
    public void testParseStackTrace_unknownSource() {
        ViolationParser factory = new ViolationParser();
        List<String> stackTrace = new LinkedList<String>();
        stackTrace.add("at com.android.strictmodetest.ServiceBase$1.doDiskWrite(Unknown Source)");
        
        StackTraceElement [] elements = factory.parseExceptionStackTrace(stackTrace);
        assertNotNull(elements);
        assertEquals(1, elements.length);
        assertEquals("com.android.strictmodetest.ServiceBase$1", elements[0].getClassName());
        assertEquals("doDiskWrite", elements[0].getMethodName());
        assertEquals(null, elements[0].getFileName());
        assertEquals(-1, elements[0].getLineNumber());
    }
    
    public void testParseStackTrace_noLineNumber() {
        ViolationParser factory = new ViolationParser();
        List<String> stackTrace = new LinkedList<String>();
        stackTrace.add("at com.android.strictmodetest.ServiceBase$1.doDiskWrite(ServiceBase.java)");
        
        StackTraceElement [] elements = factory.parseExceptionStackTrace(stackTrace);
        assertNotNull(elements);
        assertEquals(1, elements.length);
        assertEquals("com.android.strictmodetest.ServiceBase$1", elements[0].getClassName());
        assertEquals("doDiskWrite", elements[0].getMethodName());
        assertEquals("ServiceBase.java", elements[0].getFileName());
        assertEquals(-1, elements[0].getLineNumber());
    }
    
    public void testParseStackTrace_noLineNumberWithSemicolon() {
        ViolationParser factory = new ViolationParser();
        List<String> stackTrace = new LinkedList<String>();
        stackTrace.add("at com.android.strictmodetest.ServiceBase$1.doDiskWrite(ServiceBase.java:)");
        
        StackTraceElement [] elements = factory.parseExceptionStackTrace(stackTrace);
        assertNotNull(elements);
        assertEquals(1, elements.length);
        assertEquals("com.android.strictmodetest.ServiceBase$1", elements[0].getClassName());
        assertEquals("doDiskWrite", elements[0].getMethodName());
        assertEquals("ServiceBase.java", elements[0].getFileName());
        assertEquals(-1, elements[0].getLineNumber());
    }
    
    public void testParseStackTrace_invalidLineNumber() {
        ViolationParser factory = new ViolationParser();
        List<String> stackTrace = new LinkedList<String>();
        stackTrace.add("at com.android.strictmodetest.ServiceBase$1.doDiskWrite(ServiceBase.java:ABC)");
        
        StackTraceElement [] elements = factory.parseExceptionStackTrace(stackTrace);
        assertNotNull(elements);
        assertEquals(1, elements.length);
        assertEquals("com.android.strictmodetest.ServiceBase$1", elements[0].getClassName());
        assertEquals("doDiskWrite", elements[0].getMethodName());
        assertEquals("ServiceBase.java", elements[0].getFileName());
        assertEquals(-1, elements[0].getLineNumber());
    }
    
    public void testParseStackTrace_stackTraceElementRoundTrip() {
        StackTraceElement [] elements = new StackTraceElement[] {
                new StackTraceElement("",        "",     null,   -1), 
                new StackTraceElement("",        "",     null,   -2),
                new StackTraceElement("",        "",     null,  133), 
                new StackTraceElement("class",   "",     null,   -1),
                new StackTraceElement("class$1", "",     null,   -1),
                new StackTraceElement("",        "func", null,   -1),
                new StackTraceElement("class",   "func", null,   -1),
                new StackTraceElement("class$1", "func", null,   -1),
                new StackTraceElement("class$1", "func", null,   123),
                new StackTraceElement("class$1", "func", "",     123),
                new StackTraceElement("class$1", "func", "file", 123),
                new StackTraceElement("class$1", "func", "file",  -1),
                new StackTraceElement("class$1", "func", "file",  -2),
        };
        
        ViolationParser factory = new ViolationParser();
        for(StackTraceElement in : elements) {
            List<String> stackTrace = new LinkedList<String>();
            stackTrace.add(in.toString());
            
            StackTraceElement [] parsedElements = factory.parseExceptionStackTrace(stackTrace);
            assertNotNull(parsedElements);
            assertEquals(1, parsedElements.length);
            StackTraceElement out = parsedElements[0];
            
            assertEquals(in.getClassName(),  out.getClassName());
            assertEquals(in.getMethodName(), out.getMethodName());
            
            if(in.getLineNumber() == -2) {
                //file name will not be restored
                assertEquals(null, out.getFileName());
                //but line number should be restored
                assertEquals(in.getLineNumber(), out.getLineNumber()); 
            } else {
                assertEquals(in.getFileName(),   out.getFileName());
                if(in.getFileName() != null) {
                    //line number must be restored if file is specified
                    assertEquals(in.getLineNumber(), out.getLineNumber());
                } else {
                    //no file name in original, line number cannot be restored
                    assertEquals(-1, out.getLineNumber());
                }
            }
        }
    }
    
    public void testParseException_empty() {
        ViolationParser factory = new ViolationParser();
        ViolationException ex = factory.parseException(new LinkedList<String>());
        assertNull(ex.getClassName());
        assertNull(ex.getMessage());
        assertNull(ex.getCause());
        assertEquals(0, ex.getStackTrace().length);
    }
    
    public void testParseException_titleOnly() {
        ViolationParser factory = new ViolationParser();
        LinkedList<String> lines = new LinkedList<String>();
        String className = "class";
        String message = "message";
        lines.add(className + ":" + message); 
        
        ViolationException ex = factory.parseException(lines);
        assertEquals(className, ex.getClassName());
        assertEquals(message, ex.getMessage());
        assertNull(ex.getCause());
        assertEquals(0, ex.getStackTrace().length);
    }
    
    public void testParseException_twoTitles() {
        ViolationParser factory = new ViolationParser();
        LinkedList<String> lines = new LinkedList<String>();
        String className = "class";
        String message = "message";
        lines.add(className + ":" + message); 
        lines.add(className + ":" + message);
        
        ViolationException ex = factory.parseException(lines);
        assertEquals(className, ex.getClassName());
        assertEquals(message, ex.getMessage());
        assertNotNull(ex.getCause());
        assertEquals(0, ex.getStackTrace().length);
        
        ex = (ViolationException)ex.getCause();
        assertEquals(className, ex.getClassName());
        assertEquals(message, ex.getMessage());
        assertNull(ex.getCause());
        assertEquals(0, ex.getStackTrace().length);
    }
    
    public void testParseException_twoTitlesAndComments() {
        ViolationParser factory = new ViolationParser();
        LinkedList<String> lines = new LinkedList<String>();
        String className = "class";
        String message = "message";
        lines.add("#" + className + ":" + message);
        lines.add(className + ":" + message); 
        lines.add("#" + className + ":" + message);
        lines.add(className + ":" + message);
        lines.add("#" + className + ":" + message);
        
        ViolationException ex = factory.parseException(lines);
        assertEquals(className, ex.getClassName());
        assertEquals(message, ex.getMessage());
        assertNotNull(ex.getCause());
        assertEquals(0, ex.getStackTrace().length);
        
        ex = (ViolationException)ex.getCause();
        assertEquals(className, ex.getClassName());
        assertEquals(message, ex.getMessage());
        assertNull(ex.getCause());
        assertEquals(0, ex.getStackTrace().length);
    }
    
    public void testParseException_manyTitles() {
        ViolationParser factory = new ViolationParser();
        LinkedList<String> lines = new LinkedList<String>();
        String className = "class";
        String message = "message";
        int count = 10;
        for(int i = 0; i < count; i++) {
            lines.add(className + ":" + message);
        }
        
        ViolationException ex = factory.parseException(lines);
        
        for(int i = 0; i < count; i++) {
            assertNotNull(ex);
            assertEquals(className, ex.getClassName());
            assertEquals(message, ex.getMessage());
            assertEquals(0, ex.getStackTrace().length);
            ex = (ViolationException)ex.getCause();
        }
        assertNull(ex);
    }
    
    public void testParseException_titleAndStackTrace() {
        ViolationParser factory = new ViolationParser();
        LinkedList<String> lines = new LinkedList<String>();
        
        String className = "class";
        String message = "message";
        lines.add(className + ":" + message);
        
        StackTraceElement [] elements = new StackTraceElement[5];
        for(int i = 0; i < elements.length; i++) {
            elements[i] = new StackTraceElement("class" + i, "method" + i, "file" + i, i);
            lines.add("    at " + elements[i].toString());
        }
        
        ViolationException ex = factory.parseException(lines);
        assertEquals(className, ex.getClassName());
        assertEquals(message, ex.getMessage());
        assertNull(ex.getCause());
        assertEquals(elements.length, ex.getStackTrace().length);
        
        for(int i = 0; i < elements.length; i++) {
            assertEquals(elements[i], ex.getStackTrace()[i]);
        }
    }
    
    public void testParseException_manyStackTracesAndComments() {
        ViolationParser factory = new ViolationParser();
        LinkedList<String> lines = new LinkedList<String>();
        String className = "class";
        String message = "message";
        
        int count = 10;
        StackTraceElement[][] traces = new StackTraceElement[count][];
        
        for(int i = 0; i < count; i++) {
            lines.add("# some comment here");
            lines.add(className + i + ":" + message + i);
            lines.add("# some comment here");
            StackTraceElement [] elements = new StackTraceElement[5 + i];
            for(int j= 0; j < elements.length; j++) {
                int id = i * 10 + j;
                elements[j] = new StackTraceElement("class" + id, 
                                                   "method" + id, 
                                                   "file" + id,
                                                   id);
                lines.add("# some comment here");
                lines.add("    at " + elements[j].toString());
                lines.add("# some comment here");
            }
            lines.add("# some comment here");
            traces[i] = elements;
        }
        
        ViolationException ex = factory.parseException(lines);
        
        for(int i = 0; i < count; i++) {
            assertNotNull(ex);
            assertEquals(className + i, ex.getClassName());
            assertEquals(message + i, ex.getMessage());
            assertEquals(traces[i].length, ex.getStackTrace().length);
            for(int j = 0; j < traces[i].length; j++) {
                assertEquals(traces[i][j], ex.getStackTrace()[j]);
            }
            
            ex = (ViolationException)ex.getCause();
        }
        assertNull(ex);
    }
    
    public void testParseExceptionFromAsset_single() throws IOException {
        List<String> lines = openAssetAsLines("stacktrace/single.txt");
        
        /* taken from file */
        StackTraceElement [] elements = new StackTraceElement[] {
            new StackTraceElement("android.os.StrictMode$AndroidBlockGuardPolicy", "onCustomSlowCall", "StrictMode.java", 1076),
            new StackTraceElement("android.os.StrictMode", "noteSlowCall", "StrictMode.java", 1820),
            new StackTraceElement("com.android.strictmodetest.StrictModeActivity$16", "onClick", "StrictModeActivity.java", 340),
            new StackTraceElement("android.view.View", "performClick", "View.java", 3511),
            new StackTraceElement("android.view.View$PerformClick", "run", "View.java", 14105),
            new StackTraceElement("android.os.Handler", "handleCallback", "Handler.java", 605),
            new StackTraceElement("android.os.Handler", "dispatchMessage", "Handler.java", 92),
            new StackTraceElement("android.os.Looper", "loop", "Looper.java", 137),
            new StackTraceElement("android.app.ActivityThread", "main", "ActivityThread.java", 4424),
            new StackTraceElement("java.lang.reflect.Method", "invokeNative", null, -2),
            new StackTraceElement("java.lang.reflect.Method", "invoke", "Method.java", 511),
            new StackTraceElement("com.android.internal.os.ZygoteInit$MethodAndArgsCaller", "run", "ZygoteInit.java", 784),
            new StackTraceElement("com.android.internal.os.ZygoteInit", "main", "ZygoteInit.java", 551),
            new StackTraceElement("dalvik.system.NativeStart", "main", null, -2),
        };

        ViolationParser factory = new ViolationParser();
        ViolationException ex = factory.parseException(lines);
        assertEquals("android.os.StrictMode$StrictModeCustomViolation", ex.getClassName());
        assertEquals("policy=159 violation=8 msg=my example call", ex.getMessage());
        assertNull(ex.getCause());
        assertEquals(elements.length, ex.getStackTrace().length);
        for(int i = 0; i < elements.length; i++) {
            assertEquals(elements[i], ex.getStackTrace()[i]);
        }
    }
    
    public void testParseExceptionFromAsset_nested() throws IOException {
        List<String> lines = openAssetAsLines("stacktrace/nested.txt");
        
        /* taken from file */
        StackTraceElement [] mainElements = new StackTraceElement[] {
                new StackTraceElement("android.os.StrictMode$AndroidBlockGuardPolicy", "onWriteToDisk", "StrictMode.java", 1063),
                new StackTraceElement("com.android.strictmodetest.ServiceBase$1", "doDiskWrite", "ServiceBase.java", 72),
                new StackTraceElement("com.android.strictmodetest.ServiceBase$1", "doDiskWrite", "ServiceBase.java", 67),
                new StackTraceElement("com.android.strictmodetest.ServiceBase$1", "doDiskWrite", "ServiceBase.java", 67),
                new StackTraceElement("com.android.strictmodetest.IService$Stub", "onTransact", "IService.java", 58),
                new StackTraceElement("android.os.Binder", "execTransact", "Binder.java", 338),
                new StackTraceElement("dalvik.system.NativeStart", "run", null, -2),
        };
        
        StackTraceElement [] nestedElements = new StackTraceElement[] {
            new StackTraceElement("android.os.StrictMode", "readAndHandleBinderCallViolations", "StrictMode.java", 1617),
            new StackTraceElement("android.os.Parcel", "readExceptionCode", "Parcel.java", 1309),
            new StackTraceElement("android.os.Parcel", "readException", "Parcel.java", 1278),
            new StackTraceElement("com.android.strictmodetest.IService$Stub$Proxy", "doDiskWrite", "IService.java", 113),
            new StackTraceElement("com.android.strictmodetest.StrictModeActivity$9", "onClick", "StrictModeActivity.java", 244),
            new StackTraceElement("android.view.View", "performClick", "View.java", 3511),
            new StackTraceElement("android.view.View$PerformClick", "run", "View.java", 14105),
            new StackTraceElement("android.os.Handler", "handleCallback", "Handler.java", 605),
            new StackTraceElement("android.os.Handler", "dispatchMessage", "Handler.java", 92),
            new StackTraceElement("android.os.Looper", "loop", "Looper.java", 137),
            new StackTraceElement("android.app.ActivityThread", "main", "ActivityThread.java", 4424),
            new StackTraceElement("java.lang.reflect.Method", "invokeNative", null, -2),
            new StackTraceElement("java.lang.reflect.Method", "invoke", "Method.java", 511),
            new StackTraceElement("com.android.internal.os.ZygoteInit$MethodAndArgsCaller", "run", "ZygoteInit.java", 784),
            new StackTraceElement("com.android.internal.os.ZygoteInit", "main", "ZygoteInit.java", 551),
            new StackTraceElement("dalvik.system.NativeStart", "main", null, -2),
        };

        ViolationParser factory = new ViolationParser();
        ViolationException ex = factory.parseException(lines);
        assertEquals("android.os.StrictMode$StrictModeDiskWriteViolation", ex.getClassName());
        assertEquals("policy=415 violation=1", ex.getMessage());
        assertNotNull(ex.getCause());
        assertEquals(mainElements.length, ex.getStackTrace().length);
        for(int i = 0; i < mainElements.length; i++) {
            assertEquals(mainElements[i], ex.getStackTrace()[i]);
        }
        
        ex = (ViolationException) ex.getCause();
        assertEquals("android.os.StrictMode$LogStackTrace", ex.getClassName());
        assertEquals(null, ex.getMessage());
        assertNull(ex.getCause());
        assertEquals(nestedElements.length, ex.getStackTrace().length);
        for(int i = 0; i < nestedElements.length; i++) {
            assertEquals(nestedElements[i], ex.getStackTrace()[i]);
        }
    }
    
    public void testParseHeaders_empty() {
        ViolationParser factory = new ViolationParser();
        Map<String, String> map = factory.parseHeaders(new LinkedList<String>());
        assertEquals(0, map.size());
    }
    
    public void testParseHeaders_emptyLine() {
        ViolationParser factory = new ViolationParser();
        LinkedList<String> lines = new LinkedList<String>();        
        lines.add("");
        lines.add(" ");
        lines.add(":");
        lines.add(": ");
        lines.add(" :");
        lines.add(" : ");
        
        Map<String, String> map = factory.parseHeaders(lines);
        assertEquals(0, map.size());
    }
    
    public void testParseHeaders_noValue() {
        ViolationParser factory = new ViolationParser();
                
        LinkedList<String> samples = new LinkedList<String>();
        String key = "key";
        samples.add(key);
        samples.add(" " + key);
        samples.add(key + " ");
        samples.add(" " + key + " ");
        samples.add(key + ":");
        samples.add(key + " :");
        samples.add(key + ": ");
        samples.add(key + " : ");
        
        for(String sample : samples) {
            LinkedList<String> lines = new LinkedList<String>();
            lines.add(sample);
            
            Map<String, String> map = factory.parseHeaders(samples);
            assertEquals(1, map.size());
            assertTrue(map.containsKey(key));
            assertNull(map.get(key));
        }
    }
    
    public void testParseHeaders_keyValue() {
        ViolationParser factory = new ViolationParser();
                
        LinkedList<String> samples = new LinkedList<String>();
        String key = "key";
        String value = "value";
        samples.add(key + ":" + value);
        samples.add(key + " :" + value);
        samples.add(key + ": " + value);
        samples.add(key + " : " + value);
        samples.add(" " + key + " : " + value + " ");
        
        for(String sample : samples) {
            LinkedList<String> lines = new LinkedList<String>();
            lines.add(sample);
            
            Map<String, String> map = factory.parseHeaders(samples);
            assertEquals(1, map.size());
            assertTrue(map.containsKey(key));
            assertEquals(value, map.get(key));
        }
    }

    public void testParseHeaders_fromAsset_thread() throws IOException {
        ViolationParser factory = new ViolationParser();
        List<String> lines = openAssetAsLines("header/thread_headers.txt");
        Map<String, String> map = factory.parseHeaders(lines);
        
        assertEquals("com.android.strictmodetest", map.get("Process"));
        assertEquals("0xbe46", map.get("Flags"));
        assertEquals("com.android.strictmodetest v1 (1.0)", map.get("Package"));
        assertEquals("google/yakju/maguro:4.0.4/IMM76D/299849:user/release-keys", map.get("Build"));
        assertEquals("false", map.get("System-App"));
        assertEquals("192722555", map.get("Uptime-Millis"));
        assertEquals("1", map.get("Loop-Violation-Number"));
        assertEquals("21", map.get("Duration-Millis"));
    }
    
    public void testParseHeaders_fromAsset_vm() throws IOException {
        ViolationParser factory = new ViolationParser();
        List<String> lines = openAssetAsLines("header/vm_headers.txt");
        Map<String, String> map = factory.parseHeaders(lines);
        
        assertEquals("com.vivox.bobsled", map.get("Process"));
        assertEquals("0x8be46", map.get("Flags"));
        assertEquals("com.vivox.bobsled v17 (2.0.4)", map.get("Package"));
        assertEquals("google/yakju/maguro:4.0.4/IMM76D/299849:user/release-keys", map.get("Build"));
        assertEquals("false", map.get("System-App"));
        assertEquals("60936758", map.get("Uptime-Millis"));
        assertEquals("2", map.get("Instance-Count"));
    }
    
    
    /**
     * Verify that all real violations can be created using ViolationFactory.
     */
    public void testViolationFactory() throws IOException {
        Map<String, Class<? extends Violation>> tests = new HashMap<String, Class<? extends Violation>>();
        tests.put("dropbox/thread_disk_read.txt", DiskReadThreadViolation.class);
        tests.put("dropbox/thread_disk_write.txt", DiskWriteThreadViolation.class);
        tests.put("dropbox/thread_disk_write_remote.txt", DiskWriteThreadViolation.class);
        tests.put("dropbox/thread_network.txt", NetworkThreadViolation.class);
        tests.put("dropbox/thread_custom.txt", CustomThreadViolation.class);
        tests.put("dropbox/vm_close.txt", ExplicitTerminationVmViolation.class);
        tests.put("dropbox/vm_end.txt", ExplicitTerminationVmViolation.class);
        tests.put("dropbox/vm_instance_count.txt", InstanceCountVmViolation.class);
        
        ViolationParser parser = new ViolationParser();
        for(String file : tests.keySet()) {
            Violation violation = parser.createViolation(openAssetAsString(file));
            assertTrue(violation.getClass().isAssignableFrom(tests.get(file)));
        }
    }
}
