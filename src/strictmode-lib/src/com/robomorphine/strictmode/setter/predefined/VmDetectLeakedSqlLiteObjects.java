package com.robomorphine.strictmode.setter.predefined;

import com.robomorphine.strictmode.setter.AbstractStrictModeSetter;

import android.annotation.TargetApi;
import android.os.StrictMode.VmPolicy;
import android.os.StrictMode.VmPolicy.Builder;

/**
 * Detect when an SQLiteCursor or other SQLite object is finalized without having been closed.
 * 
 *  See: StrictMode.VmPolicy.Builder.detectLeakedSqlLiteObjects()
 * 
 * @author inazaruk
 */
public class VmDetectLeakedSqlLiteObjects extends AbstractStrictModeSetter {
    private static final int TARGET_VERSION = 9; //Build.VERSION_CODES.GINGERBREAD
    
    @Override
    public int getMinimumApiLevel() {
        return TARGET_VERSION;
    }
    
    @Override
    @TargetApi(TARGET_VERSION)
    protected VmPolicy onUpdateVmPolicy(Builder builder) {
        builder.detectLeakedSqlLiteObjects();
        return builder.build();
    }
}
