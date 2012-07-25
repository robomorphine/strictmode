package com.robomorphine.prefs.remote.provider;

import java.util.Set;

public class RemotePrefsContract {
    
    private RemotePrefsContract() {
    }
    
    public static class Domain {
        public static final String CONTENT_TYPE = "";
        public static final String NAME = "name";
        
        public static final String [] COLUMN_NAMES = new String [] {
            NAME
        };
    }
    
    public static class Variable {//NOPMD
        
        public enum VariableType {
            Boolean,
            Integer,
            Long,
            Float,
            String,
            StringSet
        };
        
        public static final String CONTENT_TYPE = "";        
        public static final String NAME = "name";
        public static final String TYPE = "type";        
        public static final String VALUE_COUNT = "count";
        public static final String VALUE = "value";
        
        public static final String [] COLUMN_NAMES = new String[] {
            NAME,
            TYPE,
            VALUE_COUNT,
            VALUE
        };
        
        protected static String [] newRow(String name, Object value) {
            VariableType type = null;
            int valueCount = 1;
            String convertedValue = null;
            
            if(value instanceof Boolean) {
                type = VariableType.Boolean;
                convertedValue = Boolean.toString((Boolean)value);
            } else if(value instanceof Integer) {
                type = VariableType.Integer;
                convertedValue = Integer.toString((Integer)value);
            } else if(value instanceof Long) {
                type = VariableType.Long;
                convertedValue = Long.toString((Long)value);
            } else if(value instanceof Float) {
                type = VariableType.Float;
                convertedValue = Float.toString((Float)value);
            } else if(value instanceof String) {
                type = VariableType.String;
                convertedValue = (String)value;
            } else if(value instanceof Set<?>) {
                /* we currently support only set of strings */
                type = VariableType.StringSet;
                Set<?> set = (Set<?>)value;
                valueCount = set.size();
            } else {
                throw new IllegalArgumentException("Unkown argument type");
            }
            
            String [] row = new String[COLUMN_NAMES.length];
            row[0] = name;
            row[1] = type.name();
            row[2] = Integer.toString(valueCount);
            row[3] = convertedValue;
            return row;
        }
    }
    
    public static class Value { 
        public static final String CONTENT_TYPE = "";        
        public static final String VALUE = "value"; //NOPMD
    }
    
    

}
