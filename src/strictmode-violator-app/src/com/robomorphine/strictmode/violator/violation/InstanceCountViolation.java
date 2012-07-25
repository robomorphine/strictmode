package com.robomorphine.strictmode.violator.violation;

import com.robomorphine.strictmode.violator.MainActivity;
import com.robomorphine.strictmode.violator.R;

import android.content.Context;
import android.os.Build;

import java.util.LinkedList;
import java.util.Queue;


public class InstanceCountViolation extends VmViolation {
      
    private Queue<Object> mLeakQueue = new LinkedList<Object>();
    
    public InstanceCountViolation(Context context) {
        super(context, R.drawable.memory_multiple, 
                        R.string.instance_count_name,
                        R.string.instance_count_descr);
    }
    
    @Override
    public void violate() {
        int count = 10;
        for(int i = 0; i < count; i++) {
            mLeakQueue.offer(new MainActivity());
        }
    }
    
    @Override
    public int getMinimunPlatformVersion() {
        return Build.VERSION_CODES.HONEYCOMB;
    }
}
