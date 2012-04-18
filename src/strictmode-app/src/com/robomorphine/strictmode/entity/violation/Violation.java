package com.robomorphine.strictmode.entity.violation;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;

import android.text.TextUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Violation {    
    
    public static final String HEADER_KEY_PROCESS = "Process";
    public static final String HEADER_KEY_FLAGS = "Flags";
    public static final String HEADER_KEY_PACKAGE = "Package";
    public static final String HEADER_KEY_BUILD = "Build";
    public static final String HEADER_KEY_SYSTEM_APP = "System-App";
    public static final String HEADER_KEY_TIMESTAMP = "Uptime-Millis";
    
    public static class ViolationFactory {
        /**
         * @return new instance if factory knows how to create violation from provided information,
         *         or returns null if violation can not be created using provided information.
         */
        Violation create(Map<String, String> headers, ViolationException exception) {
            return new Violation(headers, exception);
        }
    }
    
    /**
     * Example: 0x8325
     */
    @VisibleForTesting
    static int parseHeaderFlags(String rawFlags) {
        int flags = 0;
        if(rawFlags == null) {
            return flags;
        }
        
        String prefix = "0x";
        if(rawFlags.startsWith(prefix)) {
            rawFlags = rawFlags.substring(prefix.length());
        }
        try {
            flags = Integer.parseInt(rawFlags, 16);
        } catch(NumberFormatException ex) {
            //ignore
        }
        return flags;
    }
    
    /**
     * Example: com.android.strictmodetest v1 (1.0)
     */
    @VisibleForTesting
    static String parseHeaderPackage(String rawPackage) {       
        String pkg = null;
        
        if(rawPackage == null) {
            return pkg;
        }
        
        String [] parts = rawPackage.split(" ");
        if(parts.length >= 1) {
            pkg = parts[0];
        }
        if(TextUtils.isEmpty(pkg)) {
            pkg = null;
        }
        return pkg;
    }
    
    /**
     * Example: com.android.strictmodetest v1 (1.0)
     */
    @VisibleForTesting
    static int parseHeaderPackageVersionCode(String rawPackage) {
        int versionCode = 0;
        if(rawPackage == null) {
            return versionCode;
        }
        
        String [] parts = rawPackage.split(" ");
        if(parts.length >= 2) {
            String rawVersion = parts[1];
            String prefix = "v";
            if(rawVersion.startsWith(prefix)) {
                rawVersion = rawVersion.substring(prefix.length());
            }
            try {
                versionCode = Integer.parseInt(rawVersion);
            } catch(NumberFormatException ex) {
                //ignore
            }
        }
        return versionCode;
    }
    
    /**
     * Example: com.android.strictmodetest v1 (1.0)
     */
    @VisibleForTesting
    static String parseHeaderPackageVersionName(String rawPackage) {
        String versionName = null;
        
        if(rawPackage == null) {
            return versionName;
        }
        
        String [] parts = rawPackage.split(" ");
        if(parts.length >= 3) {
            String rawVersion = parts[2];
            String prefix = "(";
            String postfix = ")";
            if(rawVersion.startsWith(prefix)) {
                rawVersion = rawVersion.substring(prefix.length());
            }
            
            if(rawVersion.endsWith(postfix)) {
                rawVersion = rawVersion.substring(0, rawVersion.length() - postfix.length());
            }
            versionName = rawVersion;
        }
        return versionName;
    }
    
    @VisibleForTesting
    static long parseHeaderTimestamp(String rawTimestamp) {
        long timestamp = 0;
        try {
            timestamp = Long.parseLong(rawTimestamp);
        } catch(NumberFormatException ex) {
            //ignore
        }
        return timestamp;
    }
    
    
    private final Map<String, String> mHeaders;
    private final ViolationException mException;
        
    private final String mProcessId;    
    private final int mFlags;
    private final String mPackage;    
    private final int mVersionCode;
    private final String mVersionName;    
    private final long mTimestamp;
    
    public Violation(Map<String, String> headers, ViolationException exception) {
        mHeaders = Collections.unmodifiableMap(new HashMap<String, String>(headers));
        mException = exception;
        mProcessId = headers.get(HEADER_KEY_PROCESS);
        mFlags = parseHeaderFlags(headers.get(HEADER_KEY_FLAGS));
        mPackage = parseHeaderPackage(headers.get(HEADER_KEY_PACKAGE));       
        mVersionCode = parseHeaderPackageVersionCode(headers.get(HEADER_KEY_PACKAGE));
        mVersionName = parseHeaderPackageVersionName(headers.get(HEADER_KEY_PACKAGE));
        mTimestamp = parseHeaderTimestamp(headers.get(HEADER_KEY_TIMESTAMP));
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof Violation) {
            Violation other = (Violation)o;
            return Objects.equal(mPackage, other.mPackage) &&                  
                   Objects.equal(mException, other.mException);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(mPackage, mException);
    }
    
    public Map<String, String> getHeaders() {
        return mHeaders;
    }
    
    public ViolationException getException() {
        return mException;
    }
    
    public String getProcessId() {
        return mProcessId;
    }
    
    public int getFlags() {
        return mFlags;
    }
    
    public String getPackage() {
        return mPackage;
    }
    
    public int getVersionCode() {
        return mVersionCode;
    }
    
    public String getVersionName() {
        return mVersionName;
    }
    
    public long getTimestamp() {
        return mTimestamp;
    }
}
