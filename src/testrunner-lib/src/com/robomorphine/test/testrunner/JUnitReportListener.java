/*
 * Copyright (C) 2010-2011 Zutubi Pty Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.robomorphine.test.testrunner;

import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestListener;

/**
 * Custom test listener that outputs test results to XML files. The files
 * use a similar format to the Ant JUnit task XML formatter, with a few of
 * caveats:
 * <ul>
 *   <li>
 *     By default, multiple suites are all placed in a single file under a root
 *     &lt;testsuites&gt; element.  In multiFile mode a separate file is
 *     created for each suite, which may be more compatible with existing
 *     tools.
 *   </li>
 *   <li>
 *     Redundant information about the number of nested cases within a suite is
 *     omitted.
 *   </li>
 *   <li>
 *     Durations are omitted from suites.
 *   </li>
 *   <li>
 *     Neither standard output nor system properties are included.
 *   </li>
 * </ul>
 * The differences mainly revolve around making this reporting as lightweight as
 * possible. The report is streamed as the tests run, making it impossible to,
 * e.g. include the case count in a &lt;testsuite&gt; element.
 */
public class JUnitReportListener implements TestListener {
    private static final String LOG_TAG = "JUnitReportListener";

    private static final String ENCODING_UTF_8 = "utf-8";

    private static final String TAG_SUITES = "testsuites";
    private static final String TAG_SUITE = "testsuite";
    private static final String TAG_CASE = "testcase";
    private static final String TAG_ERROR = "error";
    private static final String TAG_FAILURE = "failure";

    private static final String ATTRIBUTE_NAME = "name";
    private static final String ATTRIBUTE_CLASS = "classname";
    private static final String ATTRIBUTE_TYPE = "type";
    private static final String ATTRIBUTE_MESSAGE = "message";
    private static final String ATTRIBUTE_TIME = "time";
   
    private Context mContext;
    private Context mTargetContext;
    private String mReportFile;
    private String mReportDir;
    private boolean mMultiFile;
    private FileOutputStream mOutputStream;
    private XmlSerializer mSerializer;
    private String mCurrentSuite;

    // simple time tracking
    private boolean mTimeAlreadyWritten = false;
    private long mTestStartTime;

    /**
     * Creates a new listener.
     *
     * @param context context of the test application
     * @param targetContext context of the application under test
     * @param reportFile name of the report file(s) to create
     * @param reportDir  path of the directory under which to write files
     *                  (may be null in which case files are written under
     *                  the context using {@link Context#openFileOutput(String, int)}).
     * @param filterTraces if true, stack traces will have common noise (e.g.
     *            framework methods) omitted for clarity
     * @param multiFile if true, use a separate file for each test suite
     */
    public JUnitReportListener(Context context, Context targetContext, String reportFile,
            String reportDir, boolean multiFile) {
        this.mContext = context;
        this.mTargetContext = targetContext;
        this.mReportFile = reportFile;
        this.mReportDir = reportDir;
        this.mMultiFile = multiFile;
    }

    @Override
    public void startTest(Test test) {
        try {
            if (test instanceof TestCase) {
                TestCase testCase = (TestCase) test;
                checkForNewSuite(testCase);
                mSerializer.startTag("", TAG_CASE);
                mSerializer.attribute("", ATTRIBUTE_CLASS, mCurrentSuite);
                mSerializer.attribute("", ATTRIBUTE_NAME, testCase.getName());

                mTimeAlreadyWritten = false;
                mTestStartTime = System.currentTimeMillis();
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, safeMessage(e));
        }
    }

    private void checkForNewSuite(TestCase testCase) throws IOException {
        String suiteName = testCase.getClass().getName();
        if (mCurrentSuite == null || !mCurrentSuite.equals(suiteName)) {
            if (mCurrentSuite != null) {
                if (mMultiFile) {
                    close();
                } else {
                    mSerializer.endTag("", TAG_SUITE);
                }
            }

            openIfRequired(suiteName);

            mSerializer.startTag("", TAG_SUITE);
            mSerializer.attribute("", ATTRIBUTE_NAME, suiteName);
            mCurrentSuite = suiteName;
        }
    }

    private void openIfRequired(String suiteName) throws IOException {
        if (mSerializer == null) {
            String fileName = mReportFile;
            if (mMultiFile) {
                fileName = fileName.replace("[suite]", suiteName);
            }

            if (mReportDir == null) {
                if (mContext.getFilesDir() != null) {
                    mOutputStream = mContext.openFileOutput(fileName, 0);
                } else {
                    mOutputStream = mTargetContext.openFileOutput(fileName, 0);
                }
            } else {
                mOutputStream = new FileOutputStream(new File(mReportDir, fileName));
            }

            mSerializer = Xml.newSerializer();
            mSerializer.setOutput(mOutputStream, ENCODING_UTF_8);
            mSerializer.startDocument(ENCODING_UTF_8, true);
            if (!mMultiFile) {
                mSerializer.startTag("", TAG_SUITES);
            }
        }
    }

    @Override
    public void addError(Test test, Throwable error) {
        addProblem(TAG_ERROR, error);
    }

    @Override
    public void addFailure(Test test, AssertionFailedError error) {
        addProblem(TAG_FAILURE, error);
    }

    private void addProblem(String tag, Throwable error) {
        try {
            recordTestTime();

            mSerializer.startTag("", tag);
            mSerializer.attribute("", ATTRIBUTE_MESSAGE, safeMessage(error));
            mSerializer.attribute("", ATTRIBUTE_TYPE, error.getClass().getName());
            StringWriter w = new StringWriter();
            error.printStackTrace(new PrintWriter(w));
            mSerializer.text(w.toString());
            mSerializer.endTag("", tag);
        } catch (IOException e) {
            Log.e(LOG_TAG, safeMessage(e));
        }
    }

    private void recordTestTime() throws IOException {
        if (!mTimeAlreadyWritten) {
            mTimeAlreadyWritten = true;
            mSerializer.attribute("", ATTRIBUTE_TIME,
                    String.format("%.3f", (System.currentTimeMillis() - mTestStartTime) / 1000.));
        }
    }

    @Override
    public void endTest(Test test) {
        try {
            if (test instanceof TestCase) {
                recordTestTime();
                mSerializer.endTag("", TAG_CASE);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, safeMessage(e));
        }
    }

	/**
	 * Releases all resources associated with this listener. Must be called when
	 * the listener is finished with.
	 */
    public void close() {
        if (mSerializer != null) {
            try {

                if (mCurrentSuite != null) {
                    mSerializer.endTag("", TAG_SUITE);
                }

                if (!mMultiFile) {
                    mSerializer.endTag("", TAG_SUITES);
                }
                mSerializer.endDocument();
                mSerializer = null;
            } catch (IOException e) {
                Log.e(LOG_TAG, safeMessage(e));
            }
        }

        if (mOutputStream != null) {
            try {
                mOutputStream.close();
                mOutputStream = null;
            } catch (IOException e) {
                Log.e(LOG_TAG, safeMessage(e));
            }
        }
    }

    private String safeMessage(Throwable error) {
        String message = error.getMessage();
        return error.getClass().getName() + ": " + (message == null ? "<null>" : message);
    }	
}