package com.robomorphine.strictmode.entity.violation;

import android.content.res.AssetManager;
import android.test.InstrumentationTestCase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ViolationFactoryTest extends InstrumentationTestCase {
    
    private List<InputStream> mOpenedStreams = new LinkedList<InputStream>();
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        for(InputStream in : mOpenedStreams) {
            try {
                in.close();
            } catch(IOException ex) {
                //ignore;
            }
        }
    }
    
    private InputStream openAsset(String name) throws IOException {
        AssetManager am = getInstrumentation().getContext().getAssets();
        InputStream in = am.open(name);
        mOpenedStreams.add(in);
        return in;
    }
    
    private List<String> openAssetAsLines(String name) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(openAsset(name)));
        LinkedList<String> lines = new LinkedList<String>();
        String line = null;
        while((line = reader.readLine()) != null) {
            lines.add(line);
        }
        return lines;
    }
    
    public void testParseExceptionTitle_empty() {
        ViolationFactory factory = new ViolationFactory();
        ViolationException ex = factory.parseExceptionTitle("", null);
        assertNull(ex.getClassName());
        assertNull(ex.getMessage()); 
    }
    
    public void testParseExceptionTitle_semicolon() {
        ViolationFactory factory = new ViolationFactory();
        ViolationException ex = factory.parseExceptionTitle("", null);
        assertNull(ex.getClassName());
        assertNull(ex.getMessage()); 
    }
    
    public void testParseExceptionTitle_classNameOnly() {
        ViolationFactory factory = new ViolationFactory();
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
        ViolationFactory factory = new ViolationFactory();
        String message = "message";
        
        ViolationException ex = factory.parseExceptionTitle(":" + message, null);
        assertNull(ex.getClassName());
        assertEquals(message, ex.getMessage());
        
        ex = factory.parseExceptionTitle(" :" + message, null);
        assertNull(ex.getClassName());
        assertEquals(message, ex.getMessage());
    }
    
    public void testParseExceptionTitle_classNameAndMessage() {
        ViolationFactory factory = new ViolationFactory();
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
        ViolationFactory factory = new ViolationFactory();
        String className = "className";
        String message = "message:message:message";
        
        ViolationException ex = factory.parseExceptionTitle(className + ":" + message, null);
        assertEquals(className, ex.getClassName());
        assertEquals(message, ex.getMessage());
    }
    
    public void testParseExceptionTitle_cuase() {
        ViolationFactory factory = new ViolationFactory();
        String className = "className";
        String message = "message";
        Throwable cause = new Throwable("test");
        
        ViolationException ex = factory.parseExceptionTitle(className + ":" + message, cause);
        assertEquals(className, ex.getClassName());
        assertEquals(message, ex.getMessage());
        assertSame(cause, ex.getCause());
    }
    
    public void testParseStackTrace_noEntries() {
        ViolationFactory factory = new ViolationFactory();
        StackTraceElement [] elements = factory.parseExceptionStackTrace(new LinkedList<String>());
        assertNotNull(elements);
        assertEquals(0, elements.length);
    }
    
    public void testParseStackTrace_emptyLine() {
        ViolationFactory factory = new ViolationFactory();
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
        ViolationFactory factory = new ViolationFactory();
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
        ViolationFactory factory = new ViolationFactory();
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
        ViolationFactory factory = new ViolationFactory();
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
        ViolationFactory factory = new ViolationFactory();
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
        ViolationFactory factory = new ViolationFactory();
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
        ViolationFactory factory = new ViolationFactory();
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
        ViolationFactory factory = new ViolationFactory();
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
        ViolationFactory factory = new ViolationFactory();
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
        ViolationFactory factory = new ViolationFactory();
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
        ViolationFactory factory = new ViolationFactory();
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
        ViolationFactory factory = new ViolationFactory();
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
        
        ViolationFactory factory = new ViolationFactory();
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
        ViolationFactory factory = new ViolationFactory();
        ViolationException ex = factory.parseException(new LinkedList<String>());
        assertNull(ex.getClassName());
        assertNull(ex.getMessage());
        assertNull(ex.getCause());
        assertEquals(0, ex.getStackTrace().length);
    }
    
    public void testParseException_titleOnly() {
        ViolationFactory factory = new ViolationFactory();
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
        ViolationFactory factory = new ViolationFactory();
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
        ViolationFactory factory = new ViolationFactory();
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
        ViolationFactory factory = new ViolationFactory();
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
        ViolationFactory factory = new ViolationFactory();
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
        ViolationFactory factory = new ViolationFactory();
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

        ViolationFactory factory = new ViolationFactory();
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

        ViolationFactory factory = new ViolationFactory();
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
        ViolationFactory factory = new ViolationFactory();
        Map<String, String> map = factory.parseHeaders(new LinkedList<String>());
        assertEquals(0, map.size());
    }
    
    public void testParseHeaders_emptyLine() {
        ViolationFactory factory = new ViolationFactory();
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
        ViolationFactory factory = new ViolationFactory();
                
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
        ViolationFactory factory = new ViolationFactory();
                
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
        ViolationFactory factory = new ViolationFactory();
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
        ViolationFactory factory = new ViolationFactory();
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
}
