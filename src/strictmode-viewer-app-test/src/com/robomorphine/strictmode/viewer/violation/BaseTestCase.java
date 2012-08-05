package com.robomorphine.strictmode.viewer.violation;

import com.robomorphine.strictmode.viewer.violation.ViolationException;
import com.robomorphine.strictmode.viewer.violation.ViolationParser;

import android.content.res.AssetManager;
import android.test.InstrumentationTestCase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class BaseTestCase extends InstrumentationTestCase { 
    
    protected static class RawViolation {
        public RawViolation(Map<String, String> headers, ViolationException exception) {
            this.headers = headers;
            this.exception = exception;
        }
        protected final Map<String, String> headers;
        protected final ViolationException exception;
    }
    
    private final List<InputStream> mOpenedStreams = new LinkedList<InputStream>();
    
    @Override
    protected void tearDown() throws Exception { //NOPMD
        super.tearDown();
        for(InputStream in : mOpenedStreams) {
            try {
                in.close();
            } catch(IOException ex) {//NOPMD
                //ignore;
            }
        }
    }
    
    protected InputStream openAsset(String name) throws IOException {
        AssetManager am = getInstrumentation().getContext().getAssets();
        InputStream in = am.open(name);
        mOpenedStreams.add(in);
        return in;
    }
    
    protected String openAssetAsString(String name) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(openAsset(name)));
        StringBuilder builder = new StringBuilder();
        String line = null;
        while((line = reader.readLine()) != null) {
            builder.append(line);
            builder.append("\n");
        }
        return builder.toString();
    }
    
    protected List<String> openAssetAsLines(String name) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(openAsset(name)));
        LinkedList<String> lines = new LinkedList<String>();
        String line = null;
        while((line = reader.readLine()) != null) {
            lines.add(line);
        }
        return lines;
    }
    
    protected RawViolation openAssetAsRawViolation(String name) throws IOException {
        ViolationParser parser = new ViolationParser();
        
        String data = openAssetAsString(name);
        List<String> headerLines = new LinkedList<String>();
        List<String> stackTraceLines = new LinkedList<String>();
        parser.extractHeadersAndException(data, headerLines, stackTraceLines);
        Map<String, String> headers = Collections.unmodifiableMap(parser.parseHeaders(headerLines));
        ViolationException exception = parser.parseException(stackTraceLines);
        
        return new RawViolation(headers, exception);
    }   
    
}
