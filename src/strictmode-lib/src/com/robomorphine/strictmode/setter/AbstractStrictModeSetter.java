package com.robomorphine.strictmode.setter;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.StrictMode.VmPolicy;
import android.util.Log;

import java.lang.reflect.Field;

public abstract class AbstractStrictModeSetter implements StrictModeSetter {
    
    private static final String TAG = AbstractStrictModeSetter.class.getSimpleName();
    
    private static final int PENALTY_LOG = 0x10;
    private static final String POLICY_MASK_FIELD = "mask";
            
    /**
     * @return minimum api level this setter is applicable from.
     */
    protected abstract int getMinimumApiLevel();
    
    @Override
    @TargetApi(9)
    public void set() {
        if (Build.VERSION.SDK_INT >= getMinimumApiLevel() && Build.VERSION.SDK_INT >= 9) {
            updateThreadPolicy();
            updateVmPolicy();
        }
    }
    
    @TargetApi(9)
    private void updateThreadPolicy() {
        ThreadPolicy policy = StrictMode.getThreadPolicy();
        boolean isPenaltyLogSet = isPenaltyLogSet(policy);
        
        ThreadPolicy.Builder builder = new ThreadPolicy.Builder(policy);
        policy = onUpdateThreadPolicy(builder);
        
        if (!isPenaltyLogSet && shouldRestoreThreadPenaltyLog()) {
            clearPenaltyLogBit(policy);
        }
        
        StrictMode.setThreadPolicy(policy);
    }
    
    @TargetApi(9)
    protected ThreadPolicy onUpdateThreadPolicy(ThreadPolicy.Builder builder) {
        return builder.build();
    }
        
    @TargetApi(11)
    private VmPolicy.Builder createVmBuilder_v11(VmPolicy policy) {
        return new VmPolicy.Builder(policy);
    }
    
    @TargetApi(9)
    private VmPolicy.Builder createVmBuilder_v9(VmPolicy policy) {
        /**
         * Since VmPolicy.Builder(policy) ctor is only available since 11th version,
         * we need to somehow initialize builder with already existing policy 
         * (so we don't loose all changes already done to vm policy).
         * 
         * So we do this via some reflection magic.
         */
        VmPolicy.Builder builder = new VmPolicy.Builder();
                
        try {            
            Class<?> policyClazz = policy.getClass();
            Class<?> builderClazz = builder.getClass();
            
            //value taken from source code of 9th/10th api level.
            Field policyMaskField = policyClazz.getDeclaredField("mask");
            Field builderMaskField = builderClazz.getDeclaredField("mMask");
            
            policyMaskField.setAccessible(true);
            builderMaskField.setAccessible(true);
            
            int policyMask = policyMaskField.getInt(policy);
            builderMaskField.setInt(builder, policyMask);        
        } catch (Throwable ex) {//NOPMD
            Log.e(TAG, "Failed to inherit existing VmPolicy mask to VmPolicy.Builder.", ex);
        }
        return builder;
    }
    
    @TargetApi(9)
    protected VmPolicy.Builder createVmBuilder(VmPolicy policy) {
        if (Build.VERSION.SDK_INT >= 11) {
            return createVmBuilder_v11(policy);
        } else if (Build.VERSION.SDK_INT >= 9){
            return createVmBuilder_v9(policy);
        } else {
            //not supported
            throw new IllegalStateException();
        }
    }
    
    @TargetApi(9)
    private void updateVmPolicy() {
        VmPolicy policy = StrictMode.getVmPolicy();
        boolean isPenaltyLogBitSet = isPenaltyLogSet(policy);
        
        VmPolicy.Builder builder = createVmBuilder(policy);
        policy = onUpdateVmPolicy(builder);
                
        //restoring penalty log bit
        if (!isPenaltyLogBitSet && shouldRestoreVmPenaltyLog()) {
            clearPenaltyLogBit(policy);
        }
        
        StrictMode.setVmPolicy(policy);
    }
        
    @TargetApi(9)
    protected VmPolicy onUpdateVmPolicy(VmPolicy.Builder builder) {
        return builder.build();
    }
    
    
    ///////////////////////////////////////////////////////////////////////////////////////////
    //  PENALTY_LOG workaround.                                                              //
    //  If none of these PENALTY_DEATH, PENALTY_LOG, PENALTY_DROPBOX, PENALTY_DIALOG is set, //
    //  both vm and thread policy builders set PENALTY_LOG as default penalty. We don't      //
    //  want that in most cases. These are helper functions to workaround this behavior.     //
    ///////////////////////////////////////////////////////////////////////////////////////////
    
    protected boolean shouldRestoreThreadPenaltyLog() {
        return true;
    }
    
    protected boolean shouldRestoreVmPenaltyLog() {
        return true;
    }
    
    private Integer getPolicyMask(Object policy, String maskFieldName) {
        Class<?> clazz = policy.getClass();
        try {
            Field field = clazz.getDeclaredField(maskFieldName);
            field.setAccessible(true);
            return field.getInt(policy);
        } catch(Exception ex) {
            Log.e(TAG, "Failed to get penalty mask in " + clazz.getSimpleName(), ex);
            return null;
        }
    }
    
    private void setPolicyMask(Object policy, String maskFieldName, int mask) {
        Class<?> clazz = policy.getClass();
        try {
            Field field = clazz.getDeclaredField(maskFieldName);
            field.setAccessible(true);
            field.setInt(policy, mask);
        } catch(Exception ex) {
            Log.e(TAG, "Failed to set penalty mask in " + clazz.getSimpleName(), ex);
        }
    }
    
    private boolean isPenaltyLogSet(Object policy, String maskFieldName, int penaltyLogMask) {
        Integer mask = getPolicyMask(policy, maskFieldName);
        if (mask == null) {
            return false;
        }        
        return (mask & penaltyLogMask) != 0;        
    }
    
    @TargetApi(9)
    protected boolean isPenaltyLogSet(ThreadPolicy policy) {
        return isPenaltyLogSet(policy, POLICY_MASK_FIELD, PENALTY_LOG);
    }
    
    @TargetApi(9)
    protected boolean isPenaltyLogSet(VmPolicy policy) {
        return isPenaltyLogSet(policy, POLICY_MASK_FIELD, PENALTY_LOG);
    }
    
    private int clearPenaltyLogBit(int mask, int penaltyLogMask) {
        return mask & ~penaltyLogMask;
    }
    
    @TargetApi(9)
    private void clearPenaltyLogBit(ThreadPolicy threadPolicy) {
        int mask = getPolicyMask(threadPolicy, POLICY_MASK_FIELD);
        mask = clearPenaltyLogBit(mask, PENALTY_LOG);
        setPolicyMask(threadPolicy, POLICY_MASK_FIELD, mask);
    }
    
    @TargetApi(9)
    private void clearPenaltyLogBit(VmPolicy vmPolicy) {
        int mask = getPolicyMask(vmPolicy, POLICY_MASK_FIELD);
        mask = clearPenaltyLogBit(mask, PENALTY_LOG);        
        setPolicyMask(vmPolicy, POLICY_MASK_FIELD, mask);
    }
}
