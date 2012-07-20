package com.robomorphine.strictmode.setter.predefined;

import com.robomorphine.strictmode.setter.StrictModeMultiSetter;

/**
 * Log detected violations to the system log.
 * 
 * Same as: VmLogPenalty + ThreadLogPenalty.
 * 
 * @author inazaruk
 */
public class PenaltyLogAll extends StrictModeMultiSetter {
    public PenaltyLogAll() {
        super(new ThreadPenaltyLog(), new VmPenaltyLog());
    }
}
