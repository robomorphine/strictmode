package com.robomorphine.strictmode.setter.predefined;

import com.robomorphine.strictmode.setter.StrictModeMultiSetter;

/**
 *  Enable detected violations log a stacktrace and timing data to the DropBox on policy violation. 
 *  Intended mostly for platform integrators doing beta user field data collection.
 *
 * Same as: VmPenaltyDropBox + ThreadPenaltyDropBox.
 * 
 * @author inazaruk
 */
public class PenaltyDropBoxAll extends StrictModeMultiSetter {
    public PenaltyDropBoxAll() {
        super(new ThreadPenaltyDropBox(), new VmPenaltyDropBox());
    }
}
