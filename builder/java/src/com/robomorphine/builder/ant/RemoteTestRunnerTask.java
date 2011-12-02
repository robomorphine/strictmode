package com.robomorphine.builder.ant;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.testrunner.ITestRunListener;
import com.android.ddmlib.testrunner.RemoteAndroidTestRunner;
import com.android.ddmlib.testrunner.TestIdentifier;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

import java.io.File;
import java.util.Map;

public class RemoteTestRunnerTask extends Task {

    private File mAndroidSdkPath;
    private String mPkgName;
    private ITestRunListener mListner = new ITestRunListener() {
        
        @Override
        public void testStarted(TestIdentifier test) {
            log("testStarted():" + test.getTestName());            
        }
        
        @Override
        public void testRunStopped(long elapsedTime) {
            log("testRunStopped():" + elapsedTime);
        }
        
        @Override
        public void testRunStarted(String runName, int testCount) {
            log("testRunStarted():" + runName + "/" + testCount);            
        }
        
        @Override
        public void testRunFailed(String errorMessage) {
            log("testRunFailed():" + errorMessage); 
        }
        
        @Override
        public void testRunEnded(long elapsedTime, Map<String, String> runMetrics) {
            log("testRunEnded():" + elapsedTime + "/" + runMetrics.toString());
        }
        
        @Override
        public void testFailed(TestFailure status, TestIdentifier test, String trace) {
            log("testFailed():" + status + "/" + test+ "/" + trace);
        }
        
        @Override
        public void testEnded(TestIdentifier test, Map<String, String> testMetrics) {
            log("testEnded():" + test + "/" + testMetrics.toString());
        }
    };
    
    public void setAndroidSdk(File path) {
        mAndroidSdkPath = path;
    }
    
    public void setPackage(String pkgName) {
        mPkgName = pkgName; 
    }
    
    @Override
    public void execute() throws BuildException {
        if(mAndroidSdkPath == null) {
            throw new BuildException("Path to Android SDK is not specified.");
        }
        
        File adbPath = new File(mAndroidSdkPath, "platform-tools/adb");
        if(adbPath.exists()) {
            throw new BuildException("Adb is not found at: " + adbPath);
        }
        
        try {
            AndroidDebugBridge.init(false);
        } catch(IllegalStateException ex) {
            log("ADB was already initialized", Project.MSG_WARN);
        }
        
        AndroidDebugBridge debugBridge;
        debugBridge = AndroidDebugBridge.createBridge(adbPath.getAbsolutePath(), false);
        
        while(!debugBridge.hasInitialDeviceList()) {
            log("Waiting for device list...");
            try {
                Thread.sleep(100);
            } catch(InterruptedException ex) {
                throw new BuildException(ex);
            }
        }
        
        log("Connected devices: " + debugBridge.getDevices().length);
        for(IDevice device : debugBridge.getDevices()) {
            try {
                RemoteAndroidTestRunner testRunner = new RemoteAndroidTestRunner(mPkgName, device);
                testRunner.run(mListner);
            } catch(Throwable ex) {
                throw new BuildException(ex);
            }
        }
    }
}
