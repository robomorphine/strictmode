package com.robomorphine.prefs.remote;

public class RemotePrefsContract {
    
    private RemotePrefsContract() {
    }
    
    public static class SharedPrefs {
        public static final String CONTENT_TYPE = "";
        public static final String NAME = "name";
        
        public static final String [] COLUMN_NAMES = new String [] {
            NAME
        };
    }
    
    public static class Variables {
        public static final String CONTENT_TYPE = "";        
        public static final String NAME = "name";
        public static final String TYPE = "type";        
        public static final String COUNT = "count";
        public static final String IS_SET = "isSet";
    }
    
    public static class Values {
        public static final String CONTENT_TYPE = "";        
        public static final String NAME = "name";
    }

}
