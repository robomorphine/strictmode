package com.robomorphine.log.filter.remote;

import android.net.Uri;
import android.provider.BaseColumns;


public class RemoteFilterContract {

    private RemoteFilterContract() {
    }

    public static final String FEATURE = "com.robomorphine.filter.remote";
    public static final String AUTHORITY = "com.robomorphine.filter.remote";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    public abstract static class Filter implements BaseColumns
    {
        private Filter() {}

        static final String ENTITIY_NAME = "Filter"; /* used for logging */

        static final String TABLE_NAME = "Filter";

        static final String URI_SUFFIX = "filters";

        static final String URI_PACKAGE_SUFFIX = "filterspkg";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, URI_SUFFIX);
        public static final Uri CONTENT_PKG_URI = Uri.withAppendedPath(AUTHORITY_URI, URI_PACKAGE_SUFFIX);


        /********************************
         *          MIME types
         ********************************/

        /** MIME type of filter list */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.robomorphine.remotefilters";

        /** MIME type of single filter */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.robomorphine.remotefilter";


        /********************************
         *          Columns
         ********************************/

        /**
         * Target application package name
         * Type: String
         */
        public static final String PACKAGE = "package";

        /**
         * Priority of filter. Filter with lower value is used first.
         * Type: unsigned int
         */
        public static final String PRIORITY = "priority";

        /**
         * What should the action be if filter matches.
         * Type: enum int {0 - Ignore, 1 - Include, 2 - Exclude }
         */
        public static final String ACTION = "action";

        public static final int ACTION_IGNORE = 0;
        public static final int ACTION_INCLUDE = 1;
        public static final int ACTION_EXCLUDE = 2;

        /**
         * If log entry has greater or equal entry is considered
         * as matching by this parameter. See LevelFilter for more details.
         * Type: int (should be a valid level)
         */
        public static final String LEVEL = "level";

        /**
         * Tag wildcard. If log has tag that is matching the wildcard,
         * then entry is considered as matching by this parameter.
         * For more details see TagFilter and Wildcard.
         * Type: String (should be a valid wildcard)
         */
        public static final String TAG = "tag";

        /**
         * Message wildcard. If log has message that is matching the wildcard,
         * then entry is considered as matching by this parameter.
         * For more details see MsgFilter and Wildcard.
         * Type: String (should be a valid wildcard)
         */
        public static final String MSG = "msg";

        /**
         * List of all columns
         */
        public static final String [] FULL_PROJECTION = new String[]
        {
            _ID,
            PACKAGE,
            PRIORITY,
            ACTION,
            LEVEL,
            TAG,
            MSG
        };

        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = PACKAGE + " ASC, " + PRIORITY + " ASC";
    }
}
