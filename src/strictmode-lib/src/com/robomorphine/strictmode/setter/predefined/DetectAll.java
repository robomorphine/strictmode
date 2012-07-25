package com.robomorphine.strictmode.setter.predefined;

import com.robomorphine.strictmode.setter.StrictModeMultiSetter;

/**
 * Detects all violations for all known policies in current context.
 * ("context" means current thread and current vm)
 * 
 * Same as: VmDetectAll + ThreadDetectAll.
 * 
 * @author inazaruk
 */
public class DetectAll extends StrictModeMultiSetter {
    public DetectAll() {
        super(new ThreadDetectAll(), new VmDetectAll());
    }
}
