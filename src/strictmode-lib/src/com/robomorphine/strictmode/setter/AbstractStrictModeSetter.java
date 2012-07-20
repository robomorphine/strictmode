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
        ThreadPolicy.Builder builder = new ThreadPolicy.Builder(policy);
        policy = onUpdateThreadPolicy(builder);
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
            
            int policyMask = policyMaskField.getInt(policy);
            builderMaskField.setInt(builder, policyMask);        
        } catch (Throwable ex) {
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
        VmPolicy.Builder builder = createVmBuilder(policy);
        StrictMode.setVmPolicy(onUpdateVmPolicy(builder));
    }   
        
    @TargetApi(9)
    protected VmPolicy onUpdateVmPolicy(VmPolicy.Builder builder) {
        return builder.build();
    }
    
}
