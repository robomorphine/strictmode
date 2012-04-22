package com.robomorphine.strictmode.violation.icon;

import com.robomorphine.strictmode.R;
import com.robomorphine.strictmode.violation.CustomThreadViolation;
import com.robomorphine.strictmode.violation.DiskReadThreadViolation;
import com.robomorphine.strictmode.violation.DiskWriteThreadViolation;
import com.robomorphine.strictmode.violation.ExplicitTerminationVmViolation;
import com.robomorphine.strictmode.violation.InstanceCountVmViolation;
import com.robomorphine.strictmode.violation.NetworkThreadViolation;
import com.robomorphine.strictmode.violation.ThreadViolation;
import com.robomorphine.strictmode.violation.Violation;
import com.robomorphine.strictmode.violation.VmViolation;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import java.util.HashMap;
import java.util.Map;

public class ViolationIconMap {
    
    private Map<Class<? extends Violation>, Integer> mIconMap;
    
    public ViolationIconMap() {
        mIconMap = new HashMap<Class<? extends Violation>, Integer>();
        
        mIconMap.put(Violation.class, R.drawable.warning);
        
        mIconMap.put(ThreadViolation.class, R.drawable.clock);
        mIconMap.put(DiskReadThreadViolation.class, R.drawable.disk_read);
        mIconMap.put(DiskWriteThreadViolation.class, R.drawable.disk_write);
        mIconMap.put(NetworkThreadViolation.class, R.drawable.network);
        mIconMap.put(CustomThreadViolation.class, R.drawable.clock);
        
        mIconMap.put(VmViolation.class, R.drawable.memory);
        mIconMap.put(ExplicitTerminationVmViolation.class, R.drawable.memory_recycle);
        mIconMap.put(InstanceCountVmViolation.class, R.drawable.memory_multiple);
    }
    
    public Drawable getIcon(Context context, Violation violation) {
        Class<?> clazz = violation.getClass();
        while(!mIconMap.containsKey(clazz) && clazz != null) {
            clazz = clazz.getSuperclass();
        }
                
        if(clazz != null) {
            Resources res = context.getResources();
            int resId = mIconMap.get(clazz);
            return res.getDrawable(resId);
        } else {
            //should never happen
            throw new IllegalStateException("No icon was found for " + violation.getClass());
        }
    }    
}
