package com.robomorphine.strictmode.setter.predefined;

import com.robomorphine.strictmode.setter.StrictModeMultiSetter;

/**
 *  Crashes the whole process on violation. This penalty runs at the end of all enabled 
 *  penalties so yo you'll still get your logging or other violations before the process dies.
 * 
 * Same as: VmPenaltyDeath + ThreadPenaltyDeath.
 * 
 * @author inazaruk
 */
public class PenaltyDeathAll extends StrictModeMultiSetter {
    public PenaltyDeathAll() {
        super(new ThreadPenaltyDeath(), new VmPenaltyDeath());
    }
}
