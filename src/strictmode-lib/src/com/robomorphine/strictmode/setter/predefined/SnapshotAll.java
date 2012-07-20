package com.robomorphine.strictmode.setter.predefined;

import com.robomorphine.strictmode.setter.StrictModeMultiSetter;

/**
 * Saves current state of policies relative to current context. 
 * This state can then be restored to any other context including current. 
 * ("context" means current thread and current vm)
 * 
 * Same as: VmSnapshot + ThreadSnapshot.
 * 
 * @author inazaruk
 */
public class SnapshotAll extends StrictModeMultiSetter {
    public SnapshotAll() {
        super(new ThreadSnapshot(), new VmSnapshot());
    }
}
