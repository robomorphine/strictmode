package com.robomorphine.strictmode.setter.predefined;

import com.robomorphine.strictmode.setter.StrictModeMultiSetter;

/**
 * Resets modes for all known policies relative to current context. 
 * ("context" means current thread and current vm)
 * 
 * Same as: VmReset + ThreadReset.  
 * 
 * @author inazaruk
 */
public class ResetAll extends StrictModeMultiSetter {
    public ResetAll() {
        super(new ThreadReset(), new VmReset());
    }    
}
