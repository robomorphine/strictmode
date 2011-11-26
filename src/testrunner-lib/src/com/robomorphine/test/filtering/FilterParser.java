
package com.robomorphine.test.filtering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterParser {
    
    public static class FilterEntry {
        public FilterEntry(int action, String value) {
            this.action = action;
            this.value = value;
        }

        final int action;
        final String value;
        
        @Override
        public String toString() {
            return "[" + ACTION_NAMES[action] + " " + value + "]";
        }
    }

    static public final int INCLUDE_ACTION = 1;
    static public final int EXCLUDE_ACTION = 2;
    
    static private final String INCLUDE_ACTION_NAME = "include";
    static private final String EXCLUDE_ACTION_NAME = "exclude";
    static private final String [] ACTION_NAMES = new String[] {
        "",
        INCLUDE_ACTION_NAME,
        EXCLUDE_ACTION_NAME
    };

    static private final char INCLUDE_ACTION_SYMBOL = '+';
    static private final char EXCLUDE_ACTION_SYMBOL = '-';

    static public final Map<Character, Integer> SYMBOL2ACTION_MAP;
    static {
        Map<Character, Integer> map = new HashMap<Character, Integer>();
        map.put(INCLUDE_ACTION_SYMBOL, INCLUDE_ACTION);
        map.put(EXCLUDE_ACTION_SYMBOL, EXCLUDE_ACTION);
        SYMBOL2ACTION_MAP = Collections.unmodifiableMap(map);
    }

    static public final char DEFAULT_ACTION_SYMBOL = INCLUDE_ACTION_SYMBOL;

    static public boolean isActionSymbol(char symbol) {
        return SYMBOL2ACTION_MAP.containsKey(symbol);
    }

    public static List<FilterEntry> parse(String filter) {
        ArrayList<FilterEntry> list = new ArrayList<FilterEntry>();
        if (filter == null || filter.length() == 0) {
            return list;
        }

        char action = DEFAULT_ACTION_SYMBOL;
        int valueStartPos = 0;

        if (isActionSymbol(filter.charAt(0))) {
            action = filter.charAt(0);
            valueStartPos = 1;
        }

        while (true) {
            // find next action symbol position
            int nextActionSymbolPos = -1;
            for (int i = valueStartPos; i < filter.length(); i++) {
                if (isActionSymbol(filter.charAt(i))) {
                    nextActionSymbolPos = i;
                    break;
                }
            }

            // extract last value and exit
            if (nextActionSymbolPos < 0) {
                String value = filter.substring(valueStartPos);
                list.add(new FilterEntry(SYMBOL2ACTION_MAP.get(action), value));
                break;
            }

            // extract value and save to list
            String value = filter.substring(valueStartPos, nextActionSymbolPos);
            list.add(new FilterEntry(SYMBOL2ACTION_MAP.get(action), value));

            action = filter.charAt(nextActionSymbolPos);
            valueStartPos = nextActionSymbolPos + 1;
        }

        return list;
    }
}
