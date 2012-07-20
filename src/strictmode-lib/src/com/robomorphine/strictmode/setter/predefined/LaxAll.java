package com.robomorphine.strictmode.setter.predefined;

import com.robomorphine.strictmode.setter.StrictModeMultiSetter;

/**
 * Sets LAX mode for all known policies relative to current context. This means nothing is going 
 * to be reported.
 * 
 * ("context" means current thread and current vm)
 * 
 * Same as: VmLax + ThreadLax.
 * 
 * @author inazaruk
 */
public class LaxAll extends StrictModeMultiSetter {
    public LaxAll() {
        super(new ThreadLax(), new VmLax());
    }
}
