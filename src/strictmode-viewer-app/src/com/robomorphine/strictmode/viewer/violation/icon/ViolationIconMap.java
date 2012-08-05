package com.robomorphine.strictmode.viewer.violation.icon;

import com.robomorphine.strictmode.viewer.R;
import com.robomorphine.strictmode.viewer.violation.CustomThreadViolation;
import com.robomorphine.strictmode.viewer.violation.DiskReadThreadViolation;
import com.robomorphine.strictmode.viewer.violation.DiskWriteThreadViolation;
import com.robomorphine.strictmode.viewer.violation.ExplicitTerminationVmViolation;
import com.robomorphine.strictmode.viewer.violation.InstanceCountVmViolation;
import com.robomorphine.strictmode.viewer.violation.NetworkThreadViolation;
import com.robomorphine.strictmode.viewer.violation.ThreadViolation;
import com.robomorphine.strictmode.viewer.violation.Violation;
import com.robomorphine.strictmode.viewer.violation.VmViolation;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import java.util.HashMap;
import java.util.Map;

public class ViolationIconMap {
    
    private final Map<Class<? extends Violation>, Integer> mIconMap;
    
    public ViolationIconMap() {
        mIconMap = new HashMap<Class<? extends Violation>, Integer>();
        
        mIconMap.put(Violation.class, R.drawable.violation_type_warning);
        
        mIconMap.put(ThreadViolation.class, R.drawable.violation_type_clock);
        mIconMap.put(DiskReadThreadViolation.class, R.drawable.violation_type_disk_read);
        mIconMap.put(DiskWriteThreadViolation.class, R.drawable.violation_type_disk_write);
        mIconMap.put(NetworkThreadViolation.class, R.drawable.violation_type_network);
        mIconMap.put(CustomThreadViolation.class, R.drawable.violation_type_clock);
        
        mIconMap.put(VmViolation.class, R.drawable.violation_type_memory);
        mIconMap.put(ExplicitTerminationVmViolation.class, R.drawable.violation_type_memory_recycle);
        mIconMap.put(InstanceCountVmViolation.class, R.drawable.violation_type_memory_multiple);
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
