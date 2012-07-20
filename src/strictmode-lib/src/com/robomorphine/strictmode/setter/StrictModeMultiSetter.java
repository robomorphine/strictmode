package com.robomorphine.strictmode.setter;

import java.util.ArrayList;
import java.util.List;

public class StrictModeMultiSetter implements StrictModeSetter {
    
    private static List<StrictModeSetter> toList(StrictModeSetter [] setters) {
        ArrayList<StrictModeSetter> list = new ArrayList<StrictModeSetter>(setters.length);
        for (StrictModeSetter setter : setters) {
            list.add(setter);
        }
        return list;
    }
    
    private final List<StrictModeSetter> mSetters;
    public StrictModeMultiSetter(StrictModeSetter...setters) { 
        this(toList(setters));
    }
    
    public StrictModeMultiSetter(List<StrictModeSetter> setters) {
        mSetters = new ArrayList<StrictModeSetter>(setters);
    }
    
    @Override
    public void set() {
        for (StrictModeSetter setter : mSetters) {
            setter.set();
        }
    }
}
