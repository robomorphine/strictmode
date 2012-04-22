package com.robomorphine.strictmode.violation;

import com.google.common.annotations.VisibleForTesting;
import com.robomorphine.strictmode.violation.Violation.ViolationFactory;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ViolationParser {
    
    private static final String STACK_TRACE_ENTRY_PREFIX = "at";
    private static final String STACK_TRACE_COMMENT_PREFIX = "#";
    private static final String STACK_TRACE_NATIVE_METHOD = "Native Method";
    private static final String STACK_TRACE_UNKNOWN_SOURCE = "Unknown Source";
    
    private static final List<Violation.ViolationFactory> sFactoryRegistry;
    static {
        List<Violation.ViolationFactory> factoryRegistry = new LinkedList<Violation.ViolationFactory>();
        factoryRegistry.add(new DiskReadThreadViolation.DiskReadThreadViolationFactory());
        factoryRegistry.add(new DiskWriteThreadViolation.DiskWriteThreadViolationFactory());
        factoryRegistry.add(new NetworkThreadViolation.NetworkThreadViolationFactory());
        factoryRegistry.add(new CustomThreadViolation.CustomThreadViolationFactory());
        factoryRegistry.add(new ThreadViolation.ThreadViolationFactory());
        
        factoryRegistry.add(new ExplicitTerminationVmViolation.ExplicitTerminationVmViolationFactory());
        factoryRegistry.add(new InstanceCountVmViolation.InstanceCountVmViolationFactory());
        
        factoryRegistry.add(new Violation.ViolationFactory());
        
        sFactoryRegistry = Collections.unmodifiableList(factoryRegistry);
    }
    
    public Violation createViolation(String data) {
        List<String> headers = new LinkedList<String>();
        List<String> stackTrace = new LinkedList<String>();
        extractHeadersAndException(data, headers, stackTrace);
        return createViolation(headers, stackTrace);
    }
    
    @VisibleForTesting    
    Violation createViolation(List<String> rawHeaders, List<String> stackTrace) {
        Violation violation = null;
        Map<String, String> headers = parseHeaders(rawHeaders);
        ViolationException exception = parseException(stackTrace);
        for(ViolationFactory factory : sFactoryRegistry) {
            violation = factory.create(headers, exception);
            if(violation != null) {
                return violation;
            }
        }
        return null;
    }
    
    @VisibleForTesting
    void extractHeadersAndException(String data, List<String> headers, List<String> stackTrace) {
        List<String> lines = headers;
        BufferedReader reader = new BufferedReader(new StringReader(data));
        
        try {
            String line = null;
            while((line = reader.readLine()) != null) {
                line = line.trim();
                if(line.length() == 0) {
                    lines = stackTrace;
                } else {
                    lines.add(line);
                }
            }
        } catch(IOException ex) {
            throw new IllegalArgumentException("Failed to parse violation data", ex);
        }
    }
    
    /**
     * Format:
     *  header-name: header-values
     */
    @VisibleForTesting
    Map<String, String> parseHeaders(List<String> headers) {
        final Character separator = ':';
        
        HashMap<String, String> map = new HashMap<String, String>();
        for(String line : headers) {
            line = line.trim();
            if(TextUtils.isEmpty(line) || line.equals(Character.toString(separator))) {
                //ignore empty line or line that consists only of separator (":")
                continue;
            }
            
            int separatorIndex = line.indexOf(separator);
            if(separatorIndex < 0) {
                map.put(line, null);
            } else {
                String key = line.substring(0, separatorIndex).trim();
                String value = null;
                if(separatorIndex < line.length() - 1) {
                    value = line.substring(separatorIndex + 1).trim();
                }
                if(TextUtils.isEmpty(value)) {
                    value = null;
                }
                map.put(key, value);
            }
        }
        return map;
    }
    
    /**
     * The exception is represented as sequence of lines, with first line describing 
     * details about exception and all following lines being stack trace.
     * 
     * For example:
     * android.os.StrictMode$StrictModeDiskWriteViolation: policy=415 violation=1
     *      at android.os.StrictMode$AndroidBlockGuardPolicy.onWriteToDisk(StrictMode.java:1063)
     *      at com.android.strictmodetest.ServiceBase$1.doDiskWrite(ServiceBase.java:72)
     *      at com.android.strictmodetest.ServiceBase$1.doDiskWrite(ServiceBase.java:67)
     *      
     * Sometimes violation can be detected in call to binder.
     * 
     * For example:
     * android.os.StrictMode$StrictModeDiskWriteViolation: policy=415 violation=1
     *       at android.os.StrictMode$AndroidBlockGuardPolicy.onWriteToDisk(StrictMode.java:1063)
     *       at com.android.strictmodetest.ServiceBase$1.doDiskWrite(ServiceBase.java:72)
     *       at com.android.strictmodetest.ServiceBase$1.doDiskWrite(ServiceBase.java:67)
     * #via Binder call with stack:
     * android.os.StrictMode$LogStackTrace
     *       at android.os.StrictMode.readAndHandleBinderCallViolations(StrictMode.java:1617)
     *       at android.os.Parcel.readExceptionCode(Parcel.java:1309)
     *       at android.os.Parcel.readException(Parcel.java:1278) 
     *
     * This function will detect both cases. When second case is detected, the second exception
     * will be stored as "cause" exception. It can be retrieved via "getCause()" function.
     */
    @VisibleForTesting
    ViolationException parseException(List<String> stackTrace) {        
        ViolationException exception = null;
        
        LinkedList<String> exceptionStackTrace = new LinkedList<String>();
        for(int i = stackTrace.size() - 1; i >= 0; i--) {               
            String line = stackTrace.get(i).trim();
            if(line.startsWith(STACK_TRACE_COMMENT_PREFIX)) {
                //comment, ignore
            } else if(line.startsWith(STACK_TRACE_ENTRY_PREFIX)) {
                exceptionStackTrace.push(line);
            } else {
                exception = parseExceptionTitle(line, exception);
                exception.setStackTrace(parseExceptionStackTrace(exceptionStackTrace));
                exceptionStackTrace.clear();
            }
        }
        
        if(exception == null) {
            exception = new ViolationException(null, null);
            exception.setStackTrace(new StackTraceElement[0]);
        }
        return exception;
    }
    
    /**
     * Exception title: "classname: message". 
     * For example: "android.os.StrictMode$StrictModeCustomViolation: policy=159 violation=8" 
     * 
     * This function extracts class name and message. 
     * From example above:
     *      Class Name: "android.os.StrictMode$StrictModeCustomViolation"
     *      Message: "policy=159 violation=8"
     */
    @VisibleForTesting
    ViolationException parseExceptionTitle(String title, ViolationException cause) {
        String className = null;
        String message = null;
        
        title = title.trim();
        
        int separatorIndex = title.indexOf(':');
        if(separatorIndex < 0) {
            className = title;
        } else {
            if(separatorIndex > 0) {
                className = title.substring(0, separatorIndex).trim();
            }
            
            if(separatorIndex < title.length() - 1) {
                message = title.substring(separatorIndex + 1).trim();
            }
        }
        
        if(TextUtils.isEmpty(className)) {
            className = null;
        }
        
        if(TextUtils.isEmpty(message)) {
            message = null;
        }
        
        return new ViolationException(className, message, cause);
    }
    
    /**
     * Stack trace has next format:
     *      at android.os.Handler.handleCallback(Handler.java:605)
     *      at android.os.Handler.dispatchMessage(Handler.java:92)
     *      at android.os.Looper.loop(Looper.java:137)
     *      at dalvik.system.NativeStart.main(Native Method)
     *      
     * File name or line number might not be available. 
     */
    protected StackTraceElement[] parseExceptionStackTrace(List<String> stackTrace) {
        StackTraceElement [] elements = new StackTraceElement[stackTrace.size()]; 
        for(int i = 0; i < stackTrace.size(); i++) {
            String line = stackTrace.get(i).trim();
            if(line.startsWith(STACK_TRACE_ENTRY_PREFIX)) {
                line = line.substring(STACK_TRACE_ENTRY_PREFIX.length());
                line = line.trim();
            }
            /* format: package.name.className.func(file:line) */
            String [] parts = line.split("[\\(\\)]");
            String className = "";//default is an empty class name
            String method = "";//default is an empty function name
            String locationFileName = null;
            int locationLine = -1;
            
            if(parts.length > 0) {
                String fullName = parts[0].trim();
                int separator = fullName.lastIndexOf('.');
                if(separator < 0) {
                    /* no method name */
                    className = fullName;
                } else {
                    /* class name and method are available */
                    className = fullName.substring(0, separator).trim();
                    if(separator < fullName.length() - 1) { 
                        method = fullName.substring(separator + 1).trim();
                    }
                }
            }
            
            if(parts.length > 1) {
                String fullLocation = parts[1].trim();
                if(fullLocation.equalsIgnoreCase(STACK_TRACE_NATIVE_METHOD)) {
                    locationLine = -2;//as mentioned in documentation
                } else if(fullLocation.equalsIgnoreCase(STACK_TRACE_UNKNOWN_SOURCE)) {
                    locationFileName = null;
                    locationLine = -1;
                } else {
                    /* extract file and line number */
                    String [] locationParts = fullLocation.split("\\:");
                    if(locationParts.length > 0) {
                        locationFileName = locationParts[0];
                    }
                    if(locationParts.length > 1) {
                        try {
                            locationLine = Integer.parseInt(locationParts[1]);
                        } catch(NumberFormatException ex) {
                            //ignore
                        }
                    }
                }
            }            
            elements[i] = new StackTraceElement(className, method, locationFileName, locationLine);                        
        }
        return elements;
    }
}

