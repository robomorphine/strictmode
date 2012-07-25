package com.robomorphine.log.filter.remote;

import static com.robomorphine.log.filter.remote.RemoteFilterContract.Filter.ACTION_EXCLUDE;
import static com.robomorphine.log.filter.remote.RemoteFilterContract.Filter.ACTION_IGNORE;
import static com.robomorphine.log.filter.remote.RemoteFilterContract.Filter.ACTION_INCLUDE;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.robomorphine.log.Log;
import com.robomorphine.test.ContentObserverCounter;
import com.robomorphine.test.ProviderTestCase;

public class RemoteFilterProviderTest extends ProviderTestCase<RemoteFilterProvider> {
        
    public RemoteFilterProviderTest() {
        super(RemoteFilterProvider.class, RemoteFilterContract.AUTHORITY);
    }   
        
    @Override
    protected void setUp() throws Exception {     
        super.setUp();        
        ContentResolver resolver = getContext().getContentResolver(); 
        resolver.delete(RemoteFilterContract.Filter.CONTENT_URI, null, null);        
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        ContentResolver resolver = getContext().getContentResolver();
        resolver.delete(RemoteFilterContract.Filter.CONTENT_URI, null, null);                
    }
        
    /************************************/
    /**             helpers            **/
    /************************************/
    
    /**
     * Returns all known keys for ContentValues 
     */
    private static String [] getContentValueKeys() {
        return new String [] {
            RemoteFilterContract.Filter.PACKAGE,
            RemoteFilterContract.Filter.PRIORITY,
            RemoteFilterContract.Filter.ACTION,
            RemoteFilterContract.Filter.LEVEL,
            RemoteFilterContract.Filter.TAG,
            RemoteFilterContract.Filter.MSG,
         };
    }
    
    /**
     * Returns ContentValues instance that represent one valid filter. 
     */
    private static ContentValues getValidContentValues() {        
        return getContentValues("com.pkg", 10, ACTION_IGNORE, Log.DEBUG, "*", "*");
    }
    
    /**
     * Construct ContentValues with values that represent custom filter 
     */
    private static ContentValues getContentValues(String pkg, int priority, 
                                                  int action, int level,
                                                  String tag, String msg) {
        
        ContentValues values = new ContentValues();
        values.put(RemoteFilterContract.Filter.PACKAGE, pkg);
        values.put(RemoteFilterContract.Filter.PRIORITY, priority);
        values.put(RemoteFilterContract.Filter.ACTION, action);
        values.put(RemoteFilterContract.Filter.LEVEL, level);
        values.put(RemoteFilterContract.Filter.TAG, tag);
        values.put(RemoteFilterContract.Filter.MSG, msg);
        return values;
    }
       
    /**
     * Verifies current cursor row and ContentValues describe same filter
     */
    public void checkCursor(ContentValues expected, Cursor cursor) {
        for(String key : getContentValueKeys()) {
            if(expected.containsKey(key)) {
                int index = cursor.getColumnIndexOrThrow(key);
                Object obj = expected.get(key);
                if(obj instanceof Integer) {
                    assertEquals(expected.getAsInteger(key), (Integer)cursor.getInt(index));
                } else if(obj instanceof String) {
                    assertEquals(expected.getAsString(key), cursor.getString(index));
                } else {
                    fail("Unexpected content valu type.");
                }
            } 
        }
    }
    
    public void checkCursorUnordered(ContentValues [] expected, Cursor cursor) {
        assertEquals(expected.length, cursor.getCount());
        
        String pkgKey = RemoteFilterContract.Filter.PACKAGE;
        String priorityKey = RemoteFilterContract.Filter.PRIORITY;
        
        for(int i = 0; i < expected.length; i++) {
            cursor.moveToPosition(i);
            String pkg = cursor.getString(cursor.getColumnIndex(pkgKey));
            Integer priority = cursor.getInt(cursor.getColumnIndex(priorityKey));
            
            boolean found = false;
            for(int j = 0; j < expected.length; j++) {
                ContentValues curValues = expected[j];
                String curPkg = curValues.getAsString(pkgKey);
                Integer curPriority = curValues.getAsInteger(priorityKey);
                
                if(curPkg.equals(pkg)) {
                    if(curPriority.equals(priority)) {                
                        found = true;
                        checkCursor(expected[j], cursor);
                        break;
                    }
                }
            }
            
            assertTrue(found);
        }
    }
    
    public void checkCursorUnordered(List<ContentValues> expected, Cursor cursor) {
        checkCursorUnordered(expected.toArray(new ContentValues[]{}), cursor);
    }
    
    public void checkCursorOrdered(ContentValues [] expected, Cursor cursor) {        
        assertEquals(expected.length, cursor.getCount());        
        for(int i = 0; i < expected.length; i++) {
            cursor.moveToPosition(i);
            checkCursor(expected[i], cursor);
        }
    }
    
    public void checkCursorOrdered(List<ContentValues> expected, Cursor cursor) {
        checkCursorOrdered(expected.toArray(new ContentValues[]{}), cursor);
    }
    
    
    /************************************/
    /**          URI matcher           **/
    /************************************/
    
    /**
     * Verifies UriMatcher is configured correctly and returns 
     * correct IDs for URIs
     */
    public void testUriMatcher() {        
        
        Uri uri = RemoteFilterContract.Filter.CONTENT_URI;
        int matchId = RemoteFilterProvider.sUriMatcher.match(uri);
        assertEquals(RemoteFilterProvider.MATCH_FILTERS, matchId);
        
        uri = Uri.withAppendedPath(RemoteFilterContract.Filter.CONTENT_URI, "123");
        matchId = RemoteFilterProvider.sUriMatcher.match(uri);
        assertEquals(RemoteFilterProvider.MATCH_FILTERS_ITEM, matchId);
        
        uri = Uri.withAppendedPath(RemoteFilterContract.Filter.CONTENT_PKG_URI, "com.pkg");
        matchId = RemoteFilterProvider.sUriMatcher.match(uri);
        assertEquals(RemoteFilterProvider.MATCH_FILTERS_PACKAGE, matchId);
        
        uri = Uri.withAppendedPath(RemoteFilterContract.Filter.CONTENT_PKG_URI, "com.pkg");
        uri = Uri.withAppendedPath(uri, "subfragment");
        matchId = RemoteFilterProvider.sUriMatcher.match(uri);
        assertEquals(-1, matchId);
    }
    
    /**
     * Verifies that getType() returns correct mime types for URIs
     */
    public void testContentType() {
        
        Uri uri = RemoteFilterContract.Filter.CONTENT_URI;
        String type = getMockContentResolver().getType(uri);        
        assertEquals(RemoteFilterContract.Filter.CONTENT_TYPE, type);
        
        uri = Uri.withAppendedPath(RemoteFilterContract.Filter.CONTENT_URI, "123");
        type = getMockContentResolver().getType(uri);        
        assertEquals(RemoteFilterContract.Filter.CONTENT_ITEM_TYPE, type);
        
        uri = Uri.withAppendedPath(RemoteFilterContract.Filter.CONTENT_PKG_URI, "com.pkg");
        type = getMockContentResolver().getType(uri); 
        assertEquals(RemoteFilterContract.Filter.CONTENT_TYPE, type);
    }
    
    /*******************************************/
    /**   ContentValues verification tests    **/
    /*******************************************/
    
    /**
     * Verifies badValue() throws exception.
     */
    public void testBadValue() {
        try {
            RemoteFilterProvider.badValue(new ContentValues(), "value");
            fail();
        } catch(IllegalArgumentException ex) {
            //ok
        }
    }
    
    /**
     * Valid ContentValues should pass the check
     */
    public void testCheckFilterValues_valid() {
        ContentValues values = getValidContentValues();                
        RemoteFilterProvider.checkFilterValues(values);
    }
    
    /**
     * ContentValues with at least one missing key should fail the check.
     */
    public void testCheckFilterValues_missing() {
        ContentValues values = getValidContentValues();
        
        /* Create ContentValues with one missing key 
         * and verify exception is thrown during the validity check */
        for(String key : getContentValueKeys()) {
            ContentValues copy = new ContentValues(values);
            copy.remove(key);
            
            try { 
                RemoteFilterProvider.checkFilterValues(copy);
                fail(key);
            } catch (IllegalArgumentException ex) {
                //ok
            }
        }        
    }
    
    /**
     * ContentValues with all valid values and empty package name should fail the check.
     */
    public void testCheckFilterValues_emptyPackage() {
        ContentValues values = getValidContentValues();
        values.put(RemoteFilterContract.Filter.PACKAGE, "");
        try { 
            RemoteFilterProvider.checkFilterValues(values);
            fail();
        } catch (IllegalArgumentException ex) {
            //ok
        }
    }
    
    /**
     * ContentValues with all valid values and invalid priority name should fail the check.
     */
    public void testCheckFilterValues_invalidPriority() {
        ContentValues values = getValidContentValues();
        values.put(RemoteFilterContract.Filter.PRIORITY, -1);
        try { 
            RemoteFilterProvider.checkFilterValues(values);
            fail();
        } catch (IllegalArgumentException ex) {
            //ok
        }
    }    
    
    /**
     * ContentValues with all valid values and invalid action name should fail the check.
     */
    public void testCheckFilterValues_invalidAction() {
        ContentValues values = getValidContentValues();
                
        values.put(RemoteFilterContract.Filter.ACTION, ACTION_IGNORE - 1);
        try { 
            RemoteFilterProvider.checkFilterValues(values);
            fail();
        } catch (IllegalArgumentException ex) {
            //ok
        }
        
        values.put(RemoteFilterContract.Filter.ACTION, ACTION_EXCLUDE + 1);
        try { 
            RemoteFilterProvider.checkFilterValues(values);
            fail();
        } catch (IllegalArgumentException ex) {
            //ok
        }
    }
    
    /**
     * ContentValues with all valid values and invalid level name should fail the check.
     */
    public void testCheckFilterValues_invalidLevel() {
        ContentValues values = getValidContentValues();
        
        values.put(RemoteFilterContract.Filter.LEVEL, Log.VERBOSE - 1);
        try { 
            RemoteFilterProvider.checkFilterValues(values);
            fail();
        } catch (IllegalArgumentException ex) {
            //ok
        }
        
        values.put(RemoteFilterContract.Filter.LEVEL, Log.ASSERT + 1);
        try { 
            RemoteFilterProvider.checkFilterValues(values);
            fail();
        } catch (IllegalArgumentException ex) {
            //ok
        }
    }
    
    /****************************************/
    /**    Notification URIs calculation   **/
    /****************************************/
    
    private static final String ADD_NOTIFICATION_PKG1 = "com.test.package1";
    private static final String ADD_NOTIFICATION_PKG2 = "com.test.package2";
    private static final String ADD_NOTIFICATION_PKG3 = "com.test.package3";

    private static final Uri ADD_NOTIFICATION_PKG1_URI;
    private static final Uri ADD_NOTIFICATION_PKG2_URI;
    private static final Uri ADD_NOTIFICATION_PKG3_URI;
    
    static {
        Uri pkgUri = RemoteFilterContract.Filter.CONTENT_PKG_URI;
        ADD_NOTIFICATION_PKG1_URI = Uri.withAppendedPath(pkgUri, ADD_NOTIFICATION_PKG1);
        ADD_NOTIFICATION_PKG2_URI = Uri.withAppendedPath(pkgUri, ADD_NOTIFICATION_PKG2);
        ADD_NOTIFICATION_PKG3_URI = Uri.withAppendedPath(pkgUri, ADD_NOTIFICATION_PKG3);    
    }
    
    private void prepareAddNotificationData() {                       
        ContentValues [] values = new ContentValues[] {
            getContentValues(ADD_NOTIFICATION_PKG1, 0, ACTION_IGNORE,  Log.DEBUG,   "a", "z"),    
            getContentValues(ADD_NOTIFICATION_PKG2, 1, ACTION_EXCLUDE, Log.VERBOSE, "b", "y"),
            getContentValues(ADD_NOTIFICATION_PKG2, 2, ACTION_INCLUDE, Log.ERROR,   "c", "x"),
            getContentValues(ADD_NOTIFICATION_PKG3, 3, ACTION_IGNORE,  Log.FATAL,   "d", "w"),
            getContentValues(ADD_NOTIFICATION_PKG3, 4, ACTION_INCLUDE, Log.ERROR,   "e", "q"),
            getContentValues(ADD_NOTIFICATION_PKG3, 5, ACTION_IGNORE,  Log.WARN,    "g", "r"),
        };        
        getMockContentResolver().bulkInsert(RemoteFilterContract.Filter.CONTENT_URI, values);
    }
    
    public void testAddNotificationUris_selectAll() {
        prepareAddNotificationData();
        Uri rootUri = RemoteFilterContract.Filter.CONTENT_URI;
       
        LinkedHashSet<Uri> notificationUris = new LinkedHashSet<Uri>();
        getProvider().addNotificationUris(notificationUris, null, null);
        
        assertEquals(4, notificationUris.size());
        assertTrue(notificationUris.contains(rootUri));
        assertTrue(notificationUris.contains(ADD_NOTIFICATION_PKG1_URI));
        assertTrue(notificationUris.contains(ADD_NOTIFICATION_PKG2_URI));
        assertTrue(notificationUris.contains(ADD_NOTIFICATION_PKG3_URI));
    }  
    
    public void testAddNotificationUris_selectPackage() {
        prepareAddNotificationData();
        Uri rootUri = RemoteFilterContract.Filter.CONTENT_URI;
        
        String [] pkgs = new String [] { 
                ADD_NOTIFICATION_PKG1, 
                ADD_NOTIFICATION_PKG2, 
                ADD_NOTIFICATION_PKG3
        };
        
        
        Uri [] uris = new Uri [] { 
                ADD_NOTIFICATION_PKG1_URI, 
                ADD_NOTIFICATION_PKG2_URI, 
                ADD_NOTIFICATION_PKG3_URI
        };
        
        for(int i = 0; i < pkgs.length; i++) {
            String selection = RemoteFilterContract.Filter.PACKAGE + " = ?";
            String [] selectionArgs = new String[] { pkgs[i] };
            
            LinkedHashSet<Uri> notificationUris = new LinkedHashSet<Uri>();
            getProvider().addNotificationUris(notificationUris, selection, selectionArgs);
            
            assertEquals(2, notificationUris.size());
            assertTrue(notificationUris.contains(rootUri));
            assertTrue(notificationUris.contains(uris[i]));
        }
    }   
        
    public void testAddNotificationUris_emptyDb() {
        LinkedHashSet<Uri> notificationUris = new LinkedHashSet<Uri>();
        getProvider().addNotificationUris(notificationUris, null, null);        
        assertEquals(0, notificationUris.size());        
    }
    
    /************************************/
    /**        Simple CRUD test        **/
    /************************************/
        
    /**
     * Verifies a very simple scenario:
     * 1) Insert 1 filter
     * 2) Query
     * 3) Delete all items
     * 4) Query 
     * 
     * This test just verifies that simplest operations work nice together.
     */
    public void testSimpleInsertDeleteQuery() {
                
        Uri uri = RemoteFilterContract.Filter.CONTENT_URI;
        
        /* insert */
        ContentValues values = getValidContentValues();
        Uri itemUri = getMockContentResolver().insert(uri, values);
        assertNotNull(itemUri);
        
        /* query */
        Cursor cursor = getMockContentResolver().query(uri, null, null, null, null);
        assertNotNull(cursor);
        assertEquals(1, cursor.getCount());
        
        cursor.moveToFirst();
        checkCursor(values, cursor);
        cursor.close();
        
        /* delete */
        int count = getMockContentResolver().delete(uri, null, null);
        assertEquals(1, count);
        
        /* query */
        cursor = getMockContentResolver().query(uri, null, null, null, null);
        assertNotNull(cursor);
        assertEquals(0, cursor.getCount());
        cursor.close();        
    }
    
    /************************************/
    /**            Insert              **/
    /************************************/
    
    /**
     *  Verify insertion of single entry at root content URI works fine 
     */
    public void testInsert_root_single() {
        Uri uri = RemoteFilterContract.Filter.CONTENT_URI;
        
        /* insert */
        ContentValues values = getValidContentValues();
        Uri itemUri = getMockContentResolver().insert(uri, values);
        assertNotNull(itemUri);
        
        /* query */
        Cursor cursor = getMockContentResolver().query(itemUri, null, null, null, null);
        assertNotNull(cursor);
        assertEquals(1, cursor.getCount());
        
        /* verify */
        cursor.moveToFirst();
        checkCursor(values, cursor);
        cursor.close();
    }
        
    /**
     *  Verify insertion of multiple values at root content URI works fine 
     */
    public void testInsert_root_multiple() {
        Uri uri = RemoteFilterContract.Filter.CONTENT_URI;
                
        ContentValues [] values = new ContentValues[] {
            getContentValues("com.test.package1", 0, ACTION_IGNORE, Log.DEBUG,   "a", "z"),    
            getContentValues("com.test.package2", 1, ACTION_EXCLUDE, Log.VERBOSE, "b", "y"),
            getContentValues("com.test.package3", 2, ACTION_INCLUDE, Log.ERROR,   "c", "x"),
            getContentValues("com.test.package4", 3, ACTION_IGNORE, Log.FATAL,   "d", "w"),
            getContentValues("com.test.package5", 4, ACTION_EXCLUDE, Log.INFO,    "e", "u"),
        };
        
        Uri uris [] = new Uri[values.length]; 
        
        /* insert */
        for(int i = 0; i < values.length; i++) {
            uris[i] = getMockContentResolver().insert(uri, values[i]);
            assertNotNull(uris[i]);
        }
        
        /* query each item by URI */
        for(int i = 0; i < values.length; i++) {
            Cursor cursor = getMockContentResolver().query(uris[i], null, null, null, null);
            assertNotNull(cursor);
            assertEquals(1, cursor.getCount());
            
            cursor.moveToFirst();
            checkCursor(values[i], cursor);
            cursor.close();        
        }
        
        /* query all items at once */
        Cursor cursor = getMockContentResolver().query(uri, null, null, null, null);
        assertNotNull(cursor);
        assertEquals(values.length, cursor.getCount());
        cursor.moveToFirst();
        do {
            int priorityIndex = cursor.getColumnIndex(RemoteFilterContract.Filter.PRIORITY);
            int priority = cursor.getInt(priorityIndex);
            checkCursor(values[priority], cursor);
            
        } while (cursor.moveToNext());
    }
        
    /**
     * Verify that invalid entries can not be inserted at root URI.    
     */    
    public void testInsert_root_invalid() {
        Uri uri = RemoteFilterContract.Filter.CONTENT_URI;
        ContentValues values = getValidContentValues();
        
        /* check exception is thrown for ContentValues with missing data */
        for(String key : getContentValueKeys()) {
            ContentValues copy = new ContentValues(values);
            copy.remove(key);
            
            try { 
                getMockContentResolver().insert(uri, copy);
                fail(key);
            } catch (IllegalArgumentException ex) {
                //ok
            }
        }
    }
    
    /**
     * Verify that insertion at root URI generates notifications.   
     */
    public void testInsert_root_notify() {
        Uri uri = RemoteFilterContract.Filter.CONTENT_URI;
        
        ContentObserverCounter counter = newContentObserverCounter(uri, true);        
        
        Context context = getContext();                
        counter.register(context);
                
        /* insert */
        ContentValues values = getValidContentValues();
        values.put(RemoteFilterContract.Filter.PACKAGE, "com.package.root.notify");
        
        context.getContentResolver().insert(uri, values);
        
        /* wait for all notifications to be processed on main thread */
        syncContentResolver();
        
        /* verify observers' hit counters */        
        counter.checkFired(1);            
    }
    
    /**
     * Verify that failed insertion at root URI does not generate notifications.   
     */    
    public void testInsert_root_invalid_notify() {
        Uri uri = RemoteFilterContract.Filter.CONTENT_URI;
                                
        ContentObserverCounter exactCounter = newContentObserverCounter(uri, false);
        ContentObserverCounter descedantCounter = newContentObserverCounter(uri, true);
        
        Context context = getContext();
        exactCounter.register(context);
        descedantCounter.register(context);
        
        /* insert */
        ContentValues values = getValidContentValues();
        values.put(RemoteFilterContract.Filter.PACKAGE, "com.package.root.notify.invalid");
        
        values.remove(RemoteFilterContract.Filter.PRIORITY);
        try {
            context.getContentResolver().insert(uri, values);
            fail();
        } catch(IllegalArgumentException ex) {
            //expected
        }
        
        /* wait for all notifications to be processed */
        syncContentResolver();
        
        /* verify observers' hit count */
        exactCounter.checkNotFired();
        descedantCounter.checkNotFired();       
    }
    
    /**
     * Verify that insertion at pkg URI works fine.   
     */
    public void testInsert_pkg_single() {
        /* insert */
        String pkgKey = RemoteFilterContract.Filter.PACKAGE; 
        
        ContentValues values = getValidContentValues();
        ContentValues valuesNoPkg = new ContentValues(values);
        valuesNoPkg.remove(pkgKey);
        
        Uri uri = RemoteFilterContract.Filter.CONTENT_PKG_URI;
        uri = Uri.withAppendedPath(uri, values.getAsString(pkgKey));
        
        Uri itemUri = getMockContentResolver().insert(uri, values);
        assertNotNull(itemUri);
        
        /* query */
        Cursor cursor = getMockContentResolver().query(itemUri, null, null, null, null);
        assertNotNull(cursor);
        assertEquals(1, cursor.getCount());
        
        /* verify */        
        cursor.moveToFirst();
        checkCursor(values, cursor);
        cursor.close();
    }
    
    /**
     * Verify that insertion of multiple valid entries at pkg URI works fine.   
     */
    public void testInsert_pkg_multiple() {
        Uri uri = RemoteFilterContract.Filter.CONTENT_URI;                
        ContentValues [] values = new ContentValues[] {
            getContentValues("com.test.package1", 0, ACTION_IGNORE, Log.DEBUG,   "a", "z"),    
            getContentValues("com.test.package1", 1, ACTION_EXCLUDE, Log.VERBOSE, "b", "y"),
            getContentValues("com.test.package1", 2, ACTION_INCLUDE, Log.ERROR,   "c", "x"),
            getContentValues("com.test.package2", 3, ACTION_IGNORE, Log.FATAL,   "d", "w"),
            getContentValues("com.test.package3", 4, ACTION_EXCLUDE, Log.INFO,    "e", "u"),
        };
        
        ContentValues [] valuesNoPkg = new ContentValues[values.length];
        Uri [] insertUris = new Uri[values.length];
        Uri [] uris = new Uri[values.length];
        
        String pkgKey = RemoteFilterContract.Filter.PACKAGE;
        for(int i = 0; i < values.length; i++) {
            
            ContentValues v = new ContentValues(values[i]);
            v.remove(pkgKey);
            valuesNoPkg[i] = v;
            
            String pkg = values[i].getAsString(pkgKey); 
            insertUris[i] = Uri.withAppendedPath(RemoteFilterContract.Filter.CONTENT_PKG_URI, pkg);
        }
        
        /* insert */
        for(int i = 0; i < values.length; i++) {
            uris[i] = getMockContentResolver().insert(insertUris[i], values[i]);
            assertNotNull(uris[i]);
        }
        
        /* query each item by URI */
        for(int i = 0; i < values.length; i++) {
            Cursor cursor = getMockContentResolver().query(uris[i], null, null, null, null);
            assertNotNull(cursor);
            assertEquals(1, cursor.getCount());
            
            cursor.moveToFirst();
            checkCursor(values[i], cursor);
            cursor.close();        
        }
        
        /* query all items at once */
        Cursor cursor = getMockContentResolver().query(uri, null, null, null, null);
        assertNotNull(cursor);
        assertEquals(values.length, cursor.getCount());
        cursor.moveToFirst();
        do {
            int priorityIndex = cursor.getColumnIndex(RemoteFilterContract.Filter.PRIORITY);
            int priority = cursor.getInt(priorityIndex);
            checkCursor(values[priority], cursor);
            
        } while (cursor.moveToNext());
    }
    
    /**
     * Verify that insertion of invalid entry results in error.   
     */
    public void testInsert_pkg_invalid() {
        Uri uri = RemoteFilterContract.Filter.CONTENT_URI;
        ContentValues values = getValidContentValues();
        values.remove(RemoteFilterContract.Filter.PACKAGE);
        
        /* check exception is thrown if ContentValues with missing key is inserted */
        for(String key : getContentValueKeys()) {
            ContentValues copy = new ContentValues(values);
            copy.remove(key);
            
            try { 
                getMockContentResolver().insert(uri, copy);
                fail(key);
            } catch (IllegalArgumentException ex) {
                //ok
            }
        }
    }
    
    /**
     * Verify that insertion of valid entry at Package URI generates notification.   
     */    
    public void testInsert_pkg_notify() {
        
        /* prepare data */        
        String pkgKey = RemoteFilterContract.Filter.PACKAGE; 
        
        ContentValues values = getValidContentValues();
        values.put(RemoteFilterContract.Filter.PACKAGE, "com.package.pkg.notify.invalid");
        
        ContentValues valuesNoPkg = new ContentValues(values);
        valuesNoPkg.remove(pkgKey);
        
        Uri uri = RemoteFilterContract.Filter.CONTENT_PKG_URI;
        Uri pkgUri = Uri.withAppendedPath(uri, values.getAsString(pkgKey));
                        
        ContentObserverCounter rootCounter = newContentObserverCounter(uri, true);        
        ContentObserverCounter pkgCounter = newContentObserverCounter(pkgUri, false);
        
        /* register for notifications */
        Context context = getContext();
        rootCounter.register(context);        
        pkgCounter.register(context);        
        
        /* insert */ 
        context.getContentResolver().insert(pkgUri, values);        
        
        /* wait for all notifications to be processed */
        syncContentResolver();
        
        /* verify observers' hit counts */
        rootCounter.checkFired(1);        
        pkgCounter.checkFired(1);
    }
    
    /**
     * Verify that insertion of invalid entry at Package URI does not generate notification.   
     */
    public void testInsert_pkg_notify_invalid() {
        
        /* prepare data */        
        String pkgKey = RemoteFilterContract.Filter.PACKAGE; 
        
        ContentValues values = getValidContentValues();
        values.put(RemoteFilterContract.Filter.PACKAGE, "com.package.pkg.notify.invalid");
        values.remove(RemoteFilterContract.Filter.PRIORITY);
        
        ContentValues valuesNoPkg = new ContentValues(values);
        valuesNoPkg.remove(pkgKey);
        
        Uri uri = RemoteFilterContract.Filter.CONTENT_PKG_URI;
        Uri pkgUri = Uri.withAppendedPath(uri, values.getAsString(pkgKey));
                        
        ContentObserverCounter rootCounter = newContentObserverCounter(uri, true);        
        ContentObserverCounter pkgCounter = newContentObserverCounter(pkgUri, false);
        
        /* register for notifications */
        Context context = getContext();
        rootCounter.register(context);        
        pkgCounter.register(context);        
        
        /* insert */ 
        try {
            context.getContentResolver().insert(pkgUri, values);
            fail();
        } catch(IllegalArgumentException ex) {
            //expected
        }      
        
        /* wait for all notifications to be processed */
        syncContentResolver();
                
        /* verify observers */
        rootCounter.checkNotFired();        
        pkgCounter.checkNotFired();        
    }
    
    /************************************/
    /**         Bulk Insert            **/
    /************************************/
    
    /**
     * Verify that bulk insert works fine   
     */
    public void testBulkInsert_root() {
        Uri uri = RemoteFilterContract.Filter.CONTENT_URI;                
        ContentValues [] values = new ContentValues[] {
            getContentValues("com.test.package1", 0, ACTION_IGNORE,  Log.DEBUG,   "a", "z"),    
            getContentValues("com.test.package2", 1, ACTION_EXCLUDE, Log.VERBOSE, "b", "y"),
            getContentValues("com.test.package3", 2, ACTION_INCLUDE, Log.ERROR,   "c", "x"),
            getContentValues("com.test.package4", 3, ACTION_IGNORE,  Log.FATAL,   "d", "w"),
            getContentValues("com.test.package5", 4, ACTION_EXCLUDE, Log.INFO,    "e", "u"),
        };        
        
        int count = getMockContentResolver().bulkInsert(uri, values);
        assertEquals(values.length, count);
      
        /* query all items at once */
        Cursor cursor = getMockContentResolver().query(uri, null, null, null, null);
        assertNotNull(cursor);
        assertEquals(values.length, cursor.getCount());
        cursor.moveToFirst();
        do {
            int priorityIndex = cursor.getColumnIndex(RemoteFilterContract.Filter.PRIORITY);
            int priority = cursor.getInt(priorityIndex);
            checkCursor(values[priority], cursor);
            
        } while (cursor.moveToNext());
    }
    
    /**
     * Verify that if error happens in the middle of insert,
     * none of the entries is added.
     */
    public void testBulkInsert_root_invalid() {
        Uri uri = RemoteFilterContract.Filter.CONTENT_URI;                
        ContentValues [] values = new ContentValues[] {
            getContentValues("com.test.package1", 0, ACTION_IGNORE,  Log.DEBUG,   "a", "z"),    
            getContentValues("com.test.package1", 1, ACTION_EXCLUDE, Log.VERBOSE, "b", "y"),
            getContentValues("ERROR",            -1, ACTION_INCLUDE, Log.ERROR,   "c", "x"),
            getContentValues("com.test.package2", 3, ACTION_IGNORE,  Log.FATAL,   "d", "w"),
            getContentValues("com.test.package3", 4, ACTION_EXCLUDE, Log.INFO,    "e", "u"),
        };        
        
        try {
            getMockContentResolver().bulkInsert(uri, values);
            fail();
        } catch (IllegalArgumentException ex) {
            //ok
        }        
      
        /* query all items at once */
        Cursor cursor = getMockContentResolver().query(uri, null, null, null, null);
        assertNotNull(cursor);
        assertEquals(0, cursor.getCount());        
    }
    
    /**
     * Verify that insertion of valid entries generates notification:
     * 1) once for root
     * 2) once for each unique package   
     */
    public void testBulkInsert_root_notify() {        
        Uri uri = RemoteFilterContract.Filter.CONTENT_URI;
        
        String sfx = ".bulk.insert.root.notify";
        ContentValues [] values = new ContentValues[] {
            getContentValues("com.test.package3" + sfx, 0, ACTION_IGNORE,  Log.DEBUG,   "a", "z"),    
            getContentValues("com.test.package2" + sfx, 1, ACTION_EXCLUDE, Log.VERBOSE, "b", "y"),
            getContentValues("com.test.package1" + sfx, 2, ACTION_INCLUDE, Log.ERROR,   "c", "x"),
            getContentValues("com.test.package2" + sfx, 3, ACTION_IGNORE,  Log.FATAL,   "d", "w"),
            getContentValues("com.test.package1" + sfx, 4, ACTION_EXCLUDE, Log.INFO,    "e", "u"),
        };    
        
        ContentObserverCounter counter = newContentObserverCounter(uri, true);        
                
        /* register for notifications */
        Context context = getContext();
        counter.register(context);        
        
        /* insert */    
        context.getContentResolver().bulkInsert(uri, values);
                
        /* wait for all notifications to be processed */
        syncContentResolver();
        
        /* verify observers' hit count */
        counter.checkFired(1);                
    }
    
    /**
     * Verify that if there is at least one invalid entry in the bulk,
     * then insertion fails and no notification are fired.
     */
    public void testBulkInsert_root_notify_invalid() {        
        Uri uri = RemoteFilterContract.Filter.CONTENT_URI;
        
        String sfx = ".bulk.insert.root.notify.invalid";
        ContentValues [] values = new ContentValues[] {
            getContentValues("com.test.package1" + sfx, 0, ACTION_IGNORE,  Log.DEBUG,   "a", "z"),    
            getContentValues("com.test.package1" + sfx, 1, ACTION_EXCLUDE, Log.VERBOSE, "b", "y"),
            getContentValues("ERROR",            -1, ACTION_INCLUDE, Log.ERROR,   "c", "x"),
            getContentValues("com.test.package2" + sfx, 3, ACTION_IGNORE,  Log.FATAL,   "d", "w"),
            getContentValues("com.test.package3" + sfx, 4, ACTION_EXCLUDE, Log.INFO,    "e", "u"),
        };            
        
        ContentObserverCounter counter = newContentObserverCounter(uri, true);
        
        
        /* register for notifications */
        Context context = getContext();
        counter.register(context);
        
        /* insert */    
        try {
            context.getContentResolver().bulkInsert(uri, values);
            fail();
        } catch(IllegalArgumentException ex) {
            //ok
        }         
                
        /* wait for all notifications to be processed */
        syncContentResolver();
        
        /* verify observers' hit count */
        counter.checkNotFired();
    }
    
    /**
     * Verify successful bulk insert is possible using Package URI    
     */
    public void testBulkInsert_pkg() {
        String pkg = "com.package";
        Uri insertUri = Uri.withAppendedPath(RemoteFilterContract.Filter.CONTENT_PKG_URI, pkg);
        Uri queryUri = RemoteFilterContract.Filter.CONTENT_URI;
        
        ContentValues [] values = new ContentValues[] {
            getContentValues(null, 0, ACTION_IGNORE,  Log.DEBUG,   "a", "z"),    
            getContentValues(null, 1, ACTION_EXCLUDE, Log.VERBOSE, "b", "y"),
            getContentValues(null, 2, ACTION_INCLUDE, Log.ERROR,   "c", "x"),
            getContentValues(null, 3, ACTION_IGNORE,  Log.FATAL,   "d", "w"),
            getContentValues(null, 4, ACTION_EXCLUDE, Log.INFO,    "e", "u"),
        };        
        
        int count = getMockContentResolver().bulkInsert(insertUri, values);
        assertEquals(values.length, count);
      
        /* query all items at once */         
        Cursor cursor = getMockContentResolver().query(queryUri, null, null, null, null);
        assertNotNull(cursor);
        assertEquals(values.length, cursor.getCount());
        
        for(int i = 0; i < values.length; i++) {
            values[i].put(RemoteFilterContract.Filter.PACKAGE, pkg);
        }
        
        cursor.moveToFirst();
        do {
            int priorityIndex = cursor.getColumnIndex(RemoteFilterContract.Filter.PRIORITY);
            int priority = cursor.getInt(priorityIndex);
            checkCursor(values[priority], cursor);
            
        } while (cursor.moveToNext());
    }
    
    /**
     * Verify that when bulk insert at Package URI fails no items are added.    
     */
    public void testBulkInsert_pkg_invalid() {
        String pkg = "com.package";
        Uri insertUri = Uri.withAppendedPath(RemoteFilterContract.Filter.CONTENT_PKG_URI, pkg);
        Uri queryUri = RemoteFilterContract.Filter.CONTENT_URI;
                
        ContentValues [] values = new ContentValues[] {
            getContentValues(null, 0, ACTION_IGNORE,  Log.DEBUG,   "a", "z"),    
            getContentValues(null, 1, ACTION_EXCLUDE, Log.VERBOSE, "b", "y"),
            getContentValues(null,-1, ACTION_INCLUDE, Log.ERROR,   "c", "x"),
            getContentValues(null, 3, ACTION_IGNORE,  Log.FATAL,   "d", "w"),
            getContentValues(null, 4, ACTION_EXCLUDE, Log.INFO,    "e", "u"),
        };       
      
        try {
            getMockContentResolver().bulkInsert(insertUri, values);
            fail();
        } catch (IllegalArgumentException ex) {
            //ok
        }        
      
        /* query all items at once */
        Cursor cursor = getMockContentResolver().query(queryUri, null, null, null, null);
        assertNotNull(cursor);
        assertEquals(0, cursor.getCount());
    }    

    /**
     * Verify that insertion of valid entries generates notification:
     * 1) once for root
     * 2) once for package   
     */
    public void testBulkInsert_pkg_notify() {        
        String pkg = "com.package.bulk.insert.pkg.notify";
        Uri rootUri = RemoteFilterContract.Filter.CONTENT_URI;
        Uri pkgUri = Uri.withAppendedPath(RemoteFilterContract.Filter.CONTENT_PKG_URI, pkg);
                
        ContentValues [] values = new ContentValues[] {
            getContentValues(null, 0, ACTION_IGNORE,  Log.DEBUG,   "a", "z"),    
            getContentValues(null, 1, ACTION_EXCLUDE, Log.VERBOSE, "b", "y"),
            getContentValues(null, 2, ACTION_INCLUDE, Log.ERROR,   "c", "x"),
            getContentValues(null, 3, ACTION_IGNORE,  Log.FATAL,   "d", "w"),
            getContentValues(null, 4, ACTION_EXCLUDE, Log.INFO,    "e", "u"),
        };    
        
        ContentObserverCounter rootCounter = newContentObserverCounter(rootUri, true);        
        ContentObserverCounter pkgCounter = newContentObserverCounter(pkgUri, true);
        
        /* register for notifications */
        Context context = getContext();
        rootCounter.register(context);
        pkgCounter.register(context);        
        
        /* insert */    
        context.getContentResolver().bulkInsert(pkgUri, values);
                
        /* wait for all notifications to be processed */
        syncContentResolver();
        
        /* verify observers */
        rootCounter.checkFired(1); 
        pkgCounter.checkFired(1);
    }
    
    /**
     * Verify that if bulk insertion failed at Package URI, no notifications are fired.   
     */
    public void testBulkInsert_pkg_notify_ivalid() {        
        String pkg = "com.package.bulk.insert.pkg.notify.invalid";
        Uri rootUri = RemoteFilterContract.Filter.CONTENT_URI;
        Uri uri = Uri.withAppendedPath(RemoteFilterContract.Filter.CONTENT_PKG_URI, pkg);
                
        ContentValues [] values = new ContentValues[] {
            getContentValues(null, 0, ACTION_IGNORE,  Log.DEBUG,   "a", "z"),    
            getContentValues(null, 1, ACTION_EXCLUDE, Log.VERBOSE, "b", "y"),
            getContentValues(null,-1, ACTION_INCLUDE, Log.ERROR,   "c", "x"),
            getContentValues(null, 3, ACTION_IGNORE,  Log.FATAL,   "d", "w"),
            getContentValues(null, 4, ACTION_EXCLUDE, Log.INFO,    "e", "u"),
        };    
        
        ContentObserverCounter rootCounter = newContentObserverCounter(rootUri, true);        
        ContentObserverCounter pkgCounter = newContentObserverCounter(uri, true);
        
        /* register for notifications */ 
        Context context = getContext();
        rootCounter.register(context);
        pkgCounter.register(context);        
        
        /* insert */    
        try {
            context.getContentResolver().bulkInsert(uri, values);
            fail();
        } catch (IllegalArgumentException ex) {
            //ok
        }
                                
        /* wait for all notifications to be processed */
        syncContentResolver();
                
        /* verify observers */
        rootCounter.checkNotFired();
        pkgCounter.checkNotFired();
    }
    
    /************************************/
    /**             Query              **/
    /************************************/
    
    private static final String QUERY_PKG1 = "com.test.package1";
    private static final String QUERY_PKG2 = "com.test.package2";
    private static final String QUERY_PKG3 = "com.test.package3";    
    
    private static final String DEFAULT_SORT_ORDER = RemoteFilterContract.Filter.DEFAULT_SORT_ORDER;
    private static final String REVERESED_SORT_ORDER;
    
    private static final Comparator<ContentValues> DEFAULT_SORT_ORDER_COMPARATOR;
    private static final Comparator<ContentValues> REVERSED_SORT_ORDER_COMPARATOR;
    
    static {
        
        REVERESED_SORT_ORDER = RemoteFilterContract.Filter.PACKAGE + " DESC, " + 
                               RemoteFilterContract.Filter.PRIORITY + " DESC";    
        
        DEFAULT_SORT_ORDER_COMPARATOR = new Comparator<ContentValues>() {
            @Override
            public int compare(ContentValues object1, ContentValues object2) {
                String pkg1 = object1.getAsString(RemoteFilterContract.Filter.PACKAGE);
                String pkg2 = object2.getAsString(RemoteFilterContract.Filter.PACKAGE);
                if(pkg1.equals(pkg2)) {
                    Integer priority1 = object1.getAsInteger(RemoteFilterContract.Filter.PRIORITY);
                    Integer priority2 = object2.getAsInteger(RemoteFilterContract.Filter.PRIORITY);
                    return priority1.compareTo(priority2);
                } else {
                    return pkg1.compareTo(pkg2);
                }                
            }
        };
        
        REVERSED_SORT_ORDER_COMPARATOR = new Comparator<ContentValues>() {
            @Override
            public int compare(ContentValues object1, ContentValues object2) {                
                return DEFAULT_SORT_ORDER_COMPARATOR.compare(object2, object1);
            }
        };
    }
    
    private void prepareQueryData(List<ContentValues> outValues, List<Uri> outUri) {
        prepareQueryData(getMockContentResolver(), outValues, outUri);
    }
    
    private void prepareQueryData(ContentResolver resolver, 
                                  List<ContentValues> outValues,
                                  List<Uri> outUri) {
                        
        Uri uri = RemoteFilterContract.Filter.CONTENT_URI;        
        ContentValues [] values = new ContentValues[] {
            getContentValues(QUERY_PKG1, 0, ACTION_IGNORE,  Log.VERBOSE, "1", "1"),            
            getContentValues(QUERY_PKG2, 0, ACTION_IGNORE,  Log.DEBUG,   "2", "2"),    
            getContentValues(QUERY_PKG2, 1, ACTION_EXCLUDE, Log.INFO,    "2", "2"),
            getContentValues(QUERY_PKG3, 0, ACTION_INCLUDE, Log.WARN,    "3", "3"),
            getContentValues(QUERY_PKG3, 1, ACTION_EXCLUDE, Log.ERROR,   "3", "3"),
            getContentValues(QUERY_PKG3, 2, ACTION_INCLUDE, Log.ASSERT,  "3", "3"),            
        };
           
        outValues.clear();
        outUri.clear();
        
        for(int i = 0; i < values.length; i++) {
            outValues.add(values[i]);
            Uri itemUri = resolver.insert(uri, values[i]);
            outUri.add(itemUri);
        }
    }
    
    /**
     * Verifies that querying records from root uri works correctly
     */
    public void testQuery_root() {
        List<ContentValues> values = new LinkedList<ContentValues>();
        List<Uri> uris = new LinkedList<Uri>();
        prepareQueryData(values, uris);
        
        Uri rootUri = RemoteFilterContract.Filter.CONTENT_URI;        
        Cursor cursor = getMockContentResolver().query(rootUri, null, null, null, null);
        
        assertNotNull(cursor);
        assertEquals(values.size(), cursor.getCount());
        
        checkCursorUnordered(values, cursor);       
    }
    
    private void subtestRootOrdering(String orderStatement, Comparator<ContentValues> comparator) {
        List<ContentValues> values = new LinkedList<ContentValues>();
        List<Uri> uris = new LinkedList<Uri>();
        prepareQueryData(values, uris);
        
        Uri rootUri = RemoteFilterContract.Filter.CONTENT_URI;        
        Cursor cursor = getMockContentResolver().query(rootUri, null, null, null, orderStatement);
        
        Collections.sort(values, comparator);
        checkCursorOrdered(values, cursor);
    }
    
    public void testQuery_root_defaultOrdering() {        
        subtestRootOrdering(null, DEFAULT_SORT_ORDER_COMPARATOR);
    }
    
    public void testQuery_root_direstOrdering() {        
        subtestRootOrdering(DEFAULT_SORT_ORDER, DEFAULT_SORT_ORDER_COMPARATOR);
    }
    
    public void testQuery_root_reversedOrdering() {        
        subtestRootOrdering(REVERESED_SORT_ORDER, REVERSED_SORT_ORDER_COMPARATOR);
    }
    
    /**
     * Verifies that querying records at package uri works correctly
     */
    public void testQuery_pkg() {
        List<ContentValues> values = new LinkedList<ContentValues>();
        List<Uri> uris = new LinkedList<Uri>();
        prepareQueryData(values, uris);
        
        String [] pkgs  = new String [] { QUERY_PKG1, QUERY_PKG2, QUERY_PKG3 };
        
        for(int pkgIndex = 0; pkgIndex < pkgs.length; pkgIndex++) {
            
            String pkg = pkgs[pkgIndex];            
            Uri pkgUri = Uri.withAppendedPath(RemoteFilterContract.Filter.CONTENT_PKG_URI, pkg);        
            Cursor cursor = getMockContentResolver().query(pkgUri, null, null, null, null);
            assertNotNull(cursor);
                        
            List<ContentValues> pkgValues = new LinkedList<ContentValues>();             
            for(int i = 0; i < values.size(); i++) {
                ContentValues curEntryValues = values.get(i);
                String curEntryPkg = curEntryValues.getAsString(RemoteFilterContract.Filter.PACKAGE);
                
                if(pkg.equals(curEntryPkg)) {
                    pkgValues.add(curEntryValues);
                }
            }
                        
            assertEquals(pkgValues.size(), cursor.getCount());
            checkCursorUnordered(pkgValues, cursor);
        }           
    }
    
    private void subtestPkgOrdering(String orderStatement, Comparator<ContentValues> comparator) {
        List<ContentValues> values = new LinkedList<ContentValues>();
        List<Uri> uris = new LinkedList<Uri>();
        prepareQueryData(values, uris);
            
        String pkg = QUERY_PKG3;   
        
        Uri pkgUri = Uri.withAppendedPath(RemoteFilterContract.Filter.CONTENT_PKG_URI, pkg);        
        Cursor cursor = getMockContentResolver().query(pkgUri, null, null, null, orderStatement);
        assertNotNull(cursor);
                        
        List<ContentValues> pkgValues = new LinkedList<ContentValues>();             
        for(int i = 0; i < values.size(); i++) {
            ContentValues curEntryValues = values.get(i);
            String curEntryPkg = curEntryValues.getAsString(RemoteFilterContract.Filter.PACKAGE);
                
            if(pkg.equals(curEntryPkg)) {
                pkgValues.add(curEntryValues);
            }
        }
        
        Collections.sort(pkgValues, comparator);
        checkCursorOrdered(pkgValues, cursor);
    }
    
    public void testQuery_pkg_defaultOrder() {        
        subtestPkgOrdering(null, DEFAULT_SORT_ORDER_COMPARATOR);                
    }
    
    public void testQuery_pkg_directOrder() {        
        subtestPkgOrdering(DEFAULT_SORT_ORDER, DEFAULT_SORT_ORDER_COMPARATOR);                
    }
    
    public void testQuery_pkg_reversedOrder() {        
        subtestPkgOrdering(REVERESED_SORT_ORDER, REVERSED_SORT_ORDER_COMPARATOR);                 
    }
    
    /**
     * Verifies that querying records at item uri works correctly
     */
    public void testQuery_item() {
        List<ContentValues> values = new LinkedList<ContentValues>();
        List<Uri> uris = new LinkedList<Uri>();
        prepareQueryData(values, uris);
                
        for(int i = 0; i < uris.size(); i++) {
            Uri itemUri = uris.get(i);
            
            Cursor cursor = getMockContentResolver().query(itemUri, null, null, null, null);
            assertNotNull(cursor);
            assertEquals(1, cursor.getCount());
            
            cursor.moveToFirst();
            checkCursor(values.get(i), cursor);
        }
    }    
    
    /************************************/
    /**            delete              **/
    /************************************/
    
    private static final String DELETE_PKG1 = QUERY_PKG1;
    private static final String DELETE_PKG2 = QUERY_PKG2;
    private static final String DELETE_PKG3 = QUERY_PKG3; 
    
    public void prepareDeleteData(ContentResolver resolver, 
                                  List<ContentValues> outValues,
                                  List<Uri> outUris) {        
        /* for now same data both as for query tests */
        prepareQueryData(resolver, outValues, outUris);
    }
    
    public void prepareDeleteData(List<ContentValues> outValues, List<Uri> outUris) {        
        prepareDeleteData(getMockContentResolver(), outValues, outUris);
    }
    
    public void testDelete_root() {
        Uri rootUri = RemoteFilterContract.Filter.CONTENT_URI;
        List<ContentValues> values = new LinkedList<ContentValues>();
        List<Uri> uris = new LinkedList<Uri>();
        prepareDeleteData(values, uris);
        
        int count = getMockContentResolver().delete(rootUri, null, null);
        assertEquals(values.size(), count);
        
        Cursor cursor = getMockContentResolver().query(rootUri, null, null, null, null);
        assertNotNull(cursor);
        assertEquals(0, cursor.getCount());
    }
    
    public void testDelete_root_notifications() {
        Uri rootUri = RemoteFilterContract.Filter.CONTENT_URI;
        Uri pkgUri = RemoteFilterContract.Filter.CONTENT_PKG_URI;
        List<ContentValues> values = new LinkedList<ContentValues>();
        List<Uri> uris = new LinkedList<Uri>();
        
        prepareDeleteData(getContext().getContentResolver(), values, uris);
        syncContentResolver();
        
        Uri pkg1uri= Uri.withAppendedPath(pkgUri, DELETE_PKG1);
        Uri pkg2uri= Uri.withAppendedPath(pkgUri, DELETE_PKG2);
        Uri pkg3uri= Uri.withAppendedPath(pkgUri, DELETE_PKG3);
        
        ContentObserverCounter rootCounter = newContentObserverCounter(rootUri, true);        
        ContentObserverCounter pkg1Counter = newContentObserverCounter(pkg1uri, false);
        ContentObserverCounter pkg2Counter = newContentObserverCounter(pkg2uri, false);
        ContentObserverCounter pkg3Counter = newContentObserverCounter(pkg3uri, false);
                        
        /* register for notifications */
        Context context = getContext();
        rootCounter.register(context);        
        pkg1Counter.register(context);
        pkg2Counter.register(context);
        pkg3Counter.register(context);
        
        /* delete & verify */    
        context.getContentResolver().delete(rootUri, null, null);        
        
        syncContentResolver();
        
        rootCounter.checkFired(1);        
        pkg1Counter.checkFired(1);
        pkg2Counter.checkFired(1);
        pkg3Counter.checkFired(1);
        
        /* delete & verify when no data is available */
        rootCounter.resetCount();        
        pkg1Counter.resetCount();
        pkg2Counter.resetCount();
        pkg3Counter.resetCount();
        
        context.getContentResolver().delete(rootUri, null, null);        
        syncContentResolver();        
        rootCounter.checkNotFired();        
        pkg1Counter.checkNotFired();
        pkg2Counter.checkNotFired();
        pkg3Counter.checkNotFired();
    }
    
    public void testDelete_pkg() {
        List<ContentValues> values = new LinkedList<ContentValues>();
        List<Uri> uris = new LinkedList<Uri>();
        prepareDeleteData(values, uris);
        
        Uri pkgUriPrefix = RemoteFilterContract.Filter.CONTENT_PKG_URI;
        String [] pkgs = new String[] { DELETE_PKG1, DELETE_PKG2, DELETE_PKG3 };
        
        for(int pkgIndex = 0; pkgIndex < pkgs.length; pkgIndex++ ){
            String pkg = pkgs[pkgIndex];
            Uri pkgUri = Uri.withAppendedPath(pkgUriPrefix, pkg);
            
            int count = 0;
            for(int i = 0; i < values.size(); i++) {
                ContentValues curValues = values.get(i);
                String curValuesPkg = curValues.getAsString(RemoteFilterContract.Filter.PACKAGE);
                if(curValuesPkg.equals(pkg)) {
                    count++;
                }
            }
                        
            Cursor cursor = getMockContentResolver().query(pkgUri, null, null, null, null);
            assertEquals(count, cursor.getCount());
            
            int delCount = getMockContentResolver().delete(pkgUri, null, null);
            assertEquals(count, delCount);
            
            cursor = getMockContentResolver().query(pkgUri, null, null, null, null);
            assertEquals(0, cursor.getCount());
        }
    }
    
    public void testDelete_pkg_notifications() {
        Uri rootUri = RemoteFilterContract.Filter.CONTENT_URI;
        Uri pkgUri = RemoteFilterContract.Filter.CONTENT_PKG_URI;
        List<ContentValues> values = new LinkedList<ContentValues>();
        List<Uri> uris = new LinkedList<Uri>();
        
        prepareDeleteData(getContext().getContentResolver(), values, uris);
        syncContentResolver();
        
        Uri pkg1uri= Uri.withAppendedPath(pkgUri, DELETE_PKG1);
        Uri pkg2uri= Uri.withAppendedPath(pkgUri, DELETE_PKG2);
        Uri pkg3uri= Uri.withAppendedPath(pkgUri, DELETE_PKG3);
        
        ContentObserverCounter rootCounter = newContentObserverCounter(rootUri, true);        
        ContentObserverCounter pkg1Counter = newContentObserverCounter(pkg1uri, false);
        ContentObserverCounter pkg2Counter = newContentObserverCounter(pkg2uri, false);
        ContentObserverCounter pkg3Counter = newContentObserverCounter(pkg3uri, false);
                        
        /* register for notifications */
        Context context = getContext();
        rootCounter.register(context);        
        pkg1Counter.register(context);
        pkg2Counter.register(context);
        pkg3Counter.register(context);
        
        /* delete & verify */    
        context.getContentResolver().delete(pkg1uri, null, null);        
        
        syncContentResolver();
        
        rootCounter.checkFired(1);        
        pkg1Counter.checkFired(1);
        pkg2Counter.checkNotFired();
        pkg3Counter.checkNotFired();
        
        /* delete & verify when no data is available */
        rootCounter.resetCount();        
        pkg1Counter.resetCount();
        pkg2Counter.resetCount();
        pkg3Counter.resetCount();
        
        context.getContentResolver().delete(pkg1uri, null, null);
        
        syncContentResolver();
        
        rootCounter.checkNotFired();        
        pkg1Counter.checkNotFired();
        pkg2Counter.checkNotFired();
        pkg3Counter.checkNotFired();
    }
    
    public void testDelete_item() {
        List<ContentValues> values = new LinkedList<ContentValues>();
        List<Uri> uris = new LinkedList<Uri>();
        prepareDeleteData(values, uris);
        
        for(Uri uri : uris) {
            Cursor cursor = getMockContentResolver().query(uri, null, null, null, null);
            assertEquals(1, cursor.getCount());
            
            int count = getMockContentResolver().delete(uri, null, null);
            assertEquals(1, count);
            
            cursor = getMockContentResolver().query(uri, null, null, null, null);
            assertEquals(0, cursor.getCount());
        }
    }
    
    public void testDelete_item_notification() {
        Uri rootUri = RemoteFilterContract.Filter.CONTENT_URI;
        Uri pkgUri = RemoteFilterContract.Filter.CONTENT_PKG_URI;
        List<ContentValues> values = new LinkedList<ContentValues>();
        List<Uri> uris = new LinkedList<Uri>();
        
        prepareDeleteData(getContext().getContentResolver(), values, uris);
        syncContentResolver();
        
        Uri pkg1uri= Uri.withAppendedPath(pkgUri, DELETE_PKG1);
        Uri pkg2uri= Uri.withAppendedPath(pkgUri, DELETE_PKG2);
        Uri pkg3uri= Uri.withAppendedPath(pkgUri, DELETE_PKG3);
        
        ContentObserverCounter rootCounter = newContentObserverCounter(rootUri, true);        
        ContentObserverCounter pkg1Counter = newContentObserverCounter(pkg1uri, false);
        ContentObserverCounter pkg2Counter = newContentObserverCounter(pkg2uri, false);
        ContentObserverCounter pkg3Counter = newContentObserverCounter(pkg3uri, false);
        
        /* register for notifications */
        Context context = getContext();
        rootCounter.register(context);        
        pkg1Counter.register(context);
        pkg2Counter.register(context);
        pkg3Counter.register(context);
                
        for(Uri uri : uris) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            assertEquals(1, cursor.getCount());
            
            cursor.moveToFirst();
            int pkgIndex = cursor.getColumnIndex(RemoteFilterContract.Filter.PACKAGE);
            String pkg = cursor.getString(pkgIndex);
            
            rootCounter.resetCount();        
            pkg1Counter.resetCount();
            pkg2Counter.resetCount();
            pkg3Counter.resetCount();
            
            context.getContentResolver().delete(uri, null, null);
            
            syncContentResolver();
            
            rootCounter.checkFired(1);
            if(DELETE_PKG1.equals(pkg)) {
                pkg1Counter.checkFired(1);
            } else {
                pkg1Counter.checkNotFired();
            }
            
            if(DELETE_PKG2.equals(pkg)) {
                pkg2Counter.checkFired(1);
            } else {
                pkg2Counter.checkNotFired();
            }
            
            if(DELETE_PKG3.equals(pkg)) {
                pkg3Counter.checkFired(1);
            } else {
                pkg3Counter.checkNotFired();
            }
        }
    }
    
    /************************************/
    /**            update              **/
    /************************************/
    
    private static final String UPDATE_PKG1 = QUERY_PKG1;
    private static final String UPDATE_PKG2 = QUERY_PKG2;
    private static final String UPDATE_PKG3 = QUERY_PKG3;
        
    private void prepareUpdateData(ContentResolver resolver, 
            List<ContentValues> outValues,
            List<Uri> outUri) {
        prepareQueryData(resolver, outValues, outUri);
    }
    
    private void prepareUpdateData(List<ContentValues> outValues, List<Uri> outUri) {
        prepareUpdateData(getMockContentResolver(), outValues, outUri);
    }
        
    /**
     * Updates one item and then queries this item to verify values are actually
     * updated.
     */
    public void testUpdate_single() { 
        List<ContentValues> values = new LinkedList<ContentValues>();
        List<Uri> uris = new LinkedList<Uri>();
        prepareUpdateData(values, uris);
        
        /* prepare update values */
        ContentValues oldValues = values.get(0);        
        int curPriority = oldValues.getAsInteger(RemoteFilterContract.Filter.PRIORITY);
        int newPriority = curPriority + 1;
        ContentValues newValues = new ContentValues(oldValues);
        newValues.put(RemoteFilterContract.Filter.PRIORITY, newPriority);
        
        /* update */
        int count = getMockContentResolver().update(uris.get(0), newValues, null, null);        
        assertEquals(1, count);
        
        /* query and verify */
        String [] projection = RemoteFilterContract.Filter.FULL_PROJECTION;
        Cursor cursor = getMockContentResolver().query(uris.get(0), projection, null, null, null);
        assertEquals(1, cursor.getCount());        
        
        cursor.moveToFirst();
        checkCursor(newValues, cursor);
        
        cursor.close();
    }
    
    public void testUpdate_single_notification() {
        List<ContentValues> values = new LinkedList<ContentValues>();
        List<Uri> uris = new LinkedList<Uri>();
        prepareUpdateData(getContext().getContentResolver(), values, uris);
        
        /* register for notification */
        Uri rootUri = RemoteFilterContract.Filter.CONTENT_URI;
        Uri pkgUri = RemoteFilterContract.Filter.CONTENT_PKG_URI;
        Uri pkg1uri= Uri.withAppendedPath(pkgUri, UPDATE_PKG1);
        Uri pkg2uri= Uri.withAppendedPath(pkgUri, UPDATE_PKG2);
        Uri pkg3uri= Uri.withAppendedPath(pkgUri, UPDATE_PKG3);
        
        ContentObserverCounter rootCounter = newContentObserverCounter(rootUri, true);        
        ContentObserverCounter pkg1Counter = newContentObserverCounter(pkg1uri, false);
        ContentObserverCounter pkg2Counter = newContentObserverCounter(pkg2uri, false);
        ContentObserverCounter pkg3Counter = newContentObserverCounter(pkg3uri, false);
                
        Context context = getContext();
        rootCounter.register(context);        
        pkg1Counter.register(context);
        pkg2Counter.register(context);
        pkg3Counter.register(context);
        
        /* prepare update values */
        ContentValues oldValues = values.get(0);        
        int curPriority = oldValues.getAsInteger(RemoteFilterContract.Filter.PRIORITY);
        int newPriority = curPriority + 1;
        ContentValues newValues = new ContentValues(oldValues);
        newValues.put(RemoteFilterContract.Filter.PRIORITY, newPriority);
        
        /* update */
        int count = context.getContentResolver().update(uris.get(0), newValues, null, null);        
        assertEquals(1, count);
        
        /* sync with notifications */
        syncContentResolver();
        
        /* verify */
        rootCounter.checkFired(1);
        pkg1Counter.checkFired(1);
        pkg2Counter.checkNotFired();
        pkg3Counter.checkNotFired();
    }
    
        
    /**
     * Two items from PACKAGE2, updating second item to have same priority as first one 
     */
    public void testUpdate_single_duplicate() {
        List<ContentValues> values = new LinkedList<ContentValues>();
        List<Uri> uris = new LinkedList<Uri>();
        prepareUpdateData(values, uris);
        
        ContentValues values1 = values.get(1);//pkg2, pri 0
        ContentValues values2 = values.get(2);//pkg2, pri 1
        
        Uri uri2 = uris.get(2);
        
        /* set priority from first item to second item */
        int pri1 = values1.getAsInteger(RemoteFilterContract.Filter.PRIORITY);
        ContentValues newValues2 = new ContentValues(values2);        
        newValues2.put(RemoteFilterContract.Filter.PRIORITY, pri1);
        
        try {
            getMockContentResolver().update(uri2, newValues2, null, null);
            fail();
        } catch (IllegalArgumentException ex) {
            //ok!
        }
    }
    
    public void testUpdate_single_duplicate_notification() {
        
        List<ContentValues> values = new LinkedList<ContentValues>();
        List<Uri> uris = new LinkedList<Uri>();
        prepareUpdateData(getContext().getContentResolver(), values, uris);
        
        /* register for notification */
        Uri rootUri = RemoteFilterContract.Filter.CONTENT_URI;
        Uri pkgUri = RemoteFilterContract.Filter.CONTENT_PKG_URI;
        Uri pkg1uri= Uri.withAppendedPath(pkgUri, UPDATE_PKG1);
        Uri pkg2uri= Uri.withAppendedPath(pkgUri, UPDATE_PKG2);
        Uri pkg3uri= Uri.withAppendedPath(pkgUri, UPDATE_PKG3);
        
        ContentObserverCounter rootCounter = newContentObserverCounter(rootUri, true);        
        ContentObserverCounter pkg1Counter = newContentObserverCounter(pkg1uri, false);
        ContentObserverCounter pkg2Counter = newContentObserverCounter(pkg2uri, false);
        ContentObserverCounter pkg3Counter = newContentObserverCounter(pkg3uri, false);
                
        Context context = getContext();
        rootCounter.register(context);        
        pkg1Counter.register(context);
        pkg2Counter.register(context);
        pkg3Counter.register(context);
        
        /* prepare data */
        ContentValues values1 = values.get(1);//pkg2, pri 0
        ContentValues values2 = values.get(2);//pkg2, pri 1
        
        Uri uri2 = uris.get(2);
        
        //set priority from first item to second item
        int pri1 = values1.getAsInteger(RemoteFilterContract.Filter.PRIORITY);
        ContentValues newValues2 = new ContentValues(values2);        
        newValues2.put(RemoteFilterContract.Filter.PRIORITY, pri1);
        
        
        /* update */
        try {
            context.getContentResolver().update(uri2, newValues2, null, null);
            fail();
        } catch (IllegalArgumentException ex) {
            //ok!
        }
        
        /* sync with notifications */
        syncContentResolver();
        
        /* verify */
        rootCounter.checkNotFired();
        pkg1Counter.checkNotFired();
        pkg2Counter.checkNotFired();
        pkg3Counter.checkNotFired();
    }
    
    /**
     * Updates a number of items by package URI. Verifies that updates passes
     * and items are actually updated. 
     */
    public void testUpdate_pkg() { 
        List<ContentValues> values = new LinkedList<ContentValues>();
        List<ContentValues> updatedValues = new LinkedList<ContentValues>();
        List<Uri> uris = new LinkedList<Uri>();
        prepareUpdateData(values, uris);
        
        String updatePkg = UPDATE_PKG3;
        String newMessage = "newMessage";
                
        int expectedUpdateCount = 0;
        for(ContentValues curValues : values) {
                        
            String curPkg = curValues.getAsString(RemoteFilterContract.Filter.PACKAGE);
            ContentValues copyValues = new ContentValues(curValues);
            
            if(curPkg.equals(updatePkg)) {
                expectedUpdateCount++;
                copyValues.put(RemoteFilterContract.Filter.MSG, newMessage);
            }
            
            updatedValues.add(copyValues);
        }
        
        /* prepare update values */        
        ContentValues updateValues = new ContentValues();
        updateValues.put(RemoteFilterContract.Filter.MSG, newMessage);        
        Uri updateUri = Uri.withAppendedPath(RemoteFilterContract.Filter.CONTENT_PKG_URI, updatePkg);
        
        /* update */
        int count = getMockContentResolver().update(updateUri, updateValues, null, null);        
        assertEquals(expectedUpdateCount, count);
        
        /* query and verify */
        Uri queryUri = RemoteFilterContract.Filter.CONTENT_URI;
        Cursor cursor = getMockContentResolver().query(queryUri, null, null, null, null);
        checkCursorUnordered(updatedValues, cursor);
        cursor.close();
    }
    
    public void testUpdate_pkg_notification() { 
        
        List<ContentValues> values = new LinkedList<ContentValues>();        
        List<Uri> uris = new LinkedList<Uri>();
        prepareUpdateData(getContext().getContentResolver(), values, uris);
        
        /* register for notification */
        Uri rootUri = RemoteFilterContract.Filter.CONTENT_URI;
        Uri pkgUri = RemoteFilterContract.Filter.CONTENT_PKG_URI;
        Uri pkg1uri= Uri.withAppendedPath(pkgUri, UPDATE_PKG1);
        Uri pkg2uri= Uri.withAppendedPath(pkgUri, UPDATE_PKG2);
        Uri pkg3uri= Uri.withAppendedPath(pkgUri, UPDATE_PKG3);
        
        ContentObserverCounter rootCounter = newContentObserverCounter(rootUri, true);        
        ContentObserverCounter pkg1Counter = newContentObserverCounter(pkg1uri, false);
        ContentObserverCounter pkg2Counter = newContentObserverCounter(pkg2uri, false);
        ContentObserverCounter pkg3Counter = newContentObserverCounter(pkg3uri, false);
                
        Context context = getContext();
        rootCounter.register(context);        
        pkg1Counter.register(context);
        pkg2Counter.register(context);
        pkg3Counter.register(context);
        
        /* prepare data */
        String updatePkg = UPDATE_PKG3;
        String newMessage = "newMessage";
                
        int expectedUpdateCount = 0;
        for(ContentValues curValues : values) {                        
            String curPkg = curValues.getAsString(RemoteFilterContract.Filter.PACKAGE);            
            if(curPkg.equals(updatePkg)) {
                expectedUpdateCount++;                
            }            
        }
        
        /* prepare update values */        
        ContentValues updateValues = new ContentValues();
        updateValues.put(RemoteFilterContract.Filter.MSG, newMessage);        
        Uri updateUri = Uri.withAppendedPath(RemoteFilterContract.Filter.CONTENT_PKG_URI, updatePkg);
        
        /* update */
        int count = context.getContentResolver().update(updateUri, updateValues, null, null);        
        assertEquals(expectedUpdateCount, count);
        
        /* sync with notifications */
        syncContentResolver();
        
        /* verify */
        rootCounter.checkFired(1);
        pkg1Counter.checkNotFired();
        pkg2Counter.checkNotFired();
        pkg3Counter.checkFired(1);
    }
    
    /**
     * Verifies that if update (using Package URI) results in duplicated entries,
     * error is thrown.
     */
    public void testUpdate_pkg_duplicate() { 
        List<ContentValues> values = new LinkedList<ContentValues>();        
        List<Uri> uris = new LinkedList<Uri>();
        prepareUpdateData(values, uris);
                        
        /* prepare update values */        
        ContentValues updateValues = new ContentValues();
        updateValues.put(RemoteFilterContract.Filter.PRIORITY, 5);        
        
        Uri updateUri = Uri.withAppendedPath(RemoteFilterContract.Filter.CONTENT_PKG_URI, UPDATE_PKG3);
        
        /* update */
        try {
            getMockContentResolver().update(updateUri, updateValues, null, null);
            fail("Duplicated data was inserted! This should not be allowed!");
        } catch(IllegalArgumentException ex) {
            //ok!
        }
    }
    
    public void testUpdate_pkg_duplicate_notification() {
        List<ContentValues> values = new LinkedList<ContentValues>();        
        List<Uri> uris = new LinkedList<Uri>();
        prepareUpdateData(getContext().getContentResolver(), values, uris);
        
        /* register for notification */
        Uri rootUri = RemoteFilterContract.Filter.CONTENT_URI;
        Uri pkgUri = RemoteFilterContract.Filter.CONTENT_PKG_URI;
        Uri pkg1uri= Uri.withAppendedPath(pkgUri, UPDATE_PKG1);
        Uri pkg2uri= Uri.withAppendedPath(pkgUri, UPDATE_PKG2);
        Uri pkg3uri= Uri.withAppendedPath(pkgUri, UPDATE_PKG3);
        
        ContentObserverCounter rootCounter = newContentObserverCounter(rootUri, true);        
        ContentObserverCounter pkg1Counter = newContentObserverCounter(pkg1uri, false);
        ContentObserverCounter pkg2Counter = newContentObserverCounter(pkg2uri, false);
        ContentObserverCounter pkg3Counter = newContentObserverCounter(pkg3uri, false);
                
        Context context = getContext();
        rootCounter.register(context);        
        pkg1Counter.register(context);
        pkg2Counter.register(context);
        pkg3Counter.register(context);
                        
        /* prepare update values */        
        ContentValues updateValues = new ContentValues();
        updateValues.put(RemoteFilterContract.Filter.PRIORITY, 5);        
        Uri updateUri = Uri.withAppendedPath(RemoteFilterContract.Filter.CONTENT_PKG_URI, UPDATE_PKG3);
        
        /* update */
        try {
            context.getContentResolver().update(updateUri, updateValues, null, null);
            fail("Duplicated data was inserted! This should not be allowed!");
        } catch(IllegalArgumentException ex) {
            //ok!
        }
        
        /* sync with notifications */
        syncContentResolver();
        
        /* verify */
        rootCounter.checkNotFired();
        pkg1Counter.checkNotFired();
        pkg2Counter.checkNotFired();
        pkg3Counter.checkNotFired();
    }
    
    /**
     * Updates a number of items by content URI. Verifies that updates passes
     * and items are actually updated. 
     */
    public void testUpdate_multiple() { 
        List<ContentValues> values = new LinkedList<ContentValues>();
        List<ContentValues> updatedValues = new LinkedList<ContentValues>();
        List<Uri> uris = new LinkedList<Uri>();
        prepareUpdateData(values, uris);
        
        int updatePriority = 0;
        String newMessage = "newMessage";
                
        int expectedUpdateCount = 0;
        for(ContentValues curValues : values) {
                        
            int curPriority = curValues.getAsInteger(RemoteFilterContract.Filter.PRIORITY);
            ContentValues copyValues = new ContentValues(curValues);
            
            if(curPriority == updatePriority) {
                expectedUpdateCount++;
                copyValues.put(RemoteFilterContract.Filter.MSG, newMessage);
            }
            
            updatedValues.add(copyValues);
        }
        
        /* prepare update values */        
        ContentValues updateValues = new ContentValues();
        updateValues.put(RemoteFilterContract.Filter.MSG, newMessage);        
        Uri updateUri = RemoteFilterContract.Filter.CONTENT_URI;
        String where = RemoteFilterContract.Filter.PRIORITY + " = ? ";
        String [] whereArgs = new String[] { Integer.toString(updatePriority) };
        
        /* update */
        int count = getMockContentResolver().update(updateUri, updateValues, where, whereArgs);        
        assertEquals(expectedUpdateCount, count);
        
        /* query and verify */
        Uri queryUri = RemoteFilterContract.Filter.CONTENT_URI;
        Cursor cursor = getMockContentResolver().query(queryUri, null, null, null, null);
        checkCursorUnordered(updatedValues, cursor);
        cursor.close();
    }
    
    public void testUpdate_multiple_notification() {
        List<ContentValues> values = new LinkedList<ContentValues>();        
        List<Uri> uris = new LinkedList<Uri>();
        prepareUpdateData(getContext().getContentResolver(), values, uris);
        
        /* register for notification */
        Uri rootUri = RemoteFilterContract.Filter.CONTENT_URI;
        Uri pkgUri = RemoteFilterContract.Filter.CONTENT_PKG_URI;
        Uri pkg1uri= Uri.withAppendedPath(pkgUri, UPDATE_PKG1);
        Uri pkg2uri= Uri.withAppendedPath(pkgUri, UPDATE_PKG2);
        Uri pkg3uri= Uri.withAppendedPath(pkgUri, UPDATE_PKG3);
        
        ContentObserverCounter rootCounter = newContentObserverCounter(rootUri, true);        
        ContentObserverCounter pkg1Counter = newContentObserverCounter(pkg1uri, false);
        ContentObserverCounter pkg2Counter = newContentObserverCounter(pkg2uri, false);
        ContentObserverCounter pkg3Counter = newContentObserverCounter(pkg3uri, false);
                
        Context context = getContext();
        rootCounter.register(context);        
        pkg1Counter.register(context);
        pkg2Counter.register(context);
        pkg3Counter.register(context);
        
        /* prepare data */
        int updatePriority = 0;
        String newMessage = "newMessage";
                
        int expectedUpdateCount = 0;
        for(ContentValues curValues : values) {                        
            int curPriority = curValues.getAsInteger(RemoteFilterContract.Filter.PRIORITY);
            if(curPriority == updatePriority) {
                expectedUpdateCount++;                
            }            
        }        
                
        ContentValues updateValues = new ContentValues();
        updateValues.put(RemoteFilterContract.Filter.MSG, newMessage);        
        Uri updateUri = RemoteFilterContract.Filter.CONTENT_URI;
        String where = RemoteFilterContract.Filter.PRIORITY + " = ? ";
        String [] whereArgs = new String[] { Integer.toString(updatePriority) };
        
        /* update */
        int count = context.getContentResolver().update(updateUri, updateValues, where, whereArgs);        
        assertEquals(expectedUpdateCount, count);
        
        /* sync with notifications */
        syncContentResolver();
        
        /* verify */
        rootCounter.checkFired(1);
        pkg1Counter.checkFired(1);
        pkg2Counter.checkFired(1);
        pkg3Counter.checkFired(1);
    }
    
    /**
     * Updates a number of items by content URI so these items have same package.
     * Verifies that update fails!
     */
    public void testUpdate_multiple_duplicate() { 
        List<ContentValues> values = new LinkedList<ContentValues>();        
        List<Uri> uris = new LinkedList<Uri>();
        prepareUpdateData(values, uris);
        
        int updatePriority = 0;
        String newPackage = "newPackage";
                
        /* prepare update values */        
        ContentValues updateValues = new ContentValues();
        updateValues.put(RemoteFilterContract.Filter.PACKAGE, newPackage);        
        Uri updateUri = RemoteFilterContract.Filter.CONTENT_URI;
        String where = RemoteFilterContract.Filter.PRIORITY + " = ? ";
        String [] whereArgs = new String[] { Integer.toString(updatePriority) };
        
        /* update */
        try {
            getMockContentResolver().update(updateUri, updateValues, where, whereArgs);
            fail("Duplicate data is now allowed!");
        } catch(IllegalArgumentException ex) {
            //ok
        }        
    }
    
    public void testUpdate_multiple_duplicate_notification() {
        List<ContentValues> values = new LinkedList<ContentValues>();        
        List<Uri> uris = new LinkedList<Uri>();
        prepareUpdateData(getContext().getContentResolver(), values, uris);
        
        /* register for notification */
        Uri rootUri = RemoteFilterContract.Filter.CONTENT_URI;
        Uri pkgUri = RemoteFilterContract.Filter.CONTENT_PKG_URI;
        Uri pkg1uri= Uri.withAppendedPath(pkgUri, UPDATE_PKG1);
        Uri pkg2uri= Uri.withAppendedPath(pkgUri, UPDATE_PKG2);
        Uri pkg3uri= Uri.withAppendedPath(pkgUri, UPDATE_PKG3);
        
        ContentObserverCounter rootCounter = newContentObserverCounter(rootUri, true);        
        ContentObserverCounter pkg1Counter = newContentObserverCounter(pkg1uri, false);
        ContentObserverCounter pkg2Counter = newContentObserverCounter(pkg2uri, false);
        ContentObserverCounter pkg3Counter = newContentObserverCounter(pkg3uri, false);
                
        Context context = getContext();
        rootCounter.register(context);        
        pkg1Counter.register(context);
        pkg2Counter.register(context);
        pkg3Counter.register(context);
        
        /* prepare update values */
        int updatePriority = 0;
        String newPackage = "newPackage";                
                
        ContentValues updateValues = new ContentValues();
        updateValues.put(RemoteFilterContract.Filter.PACKAGE, newPackage);        
        Uri updateUri = RemoteFilterContract.Filter.CONTENT_URI;
        String where = RemoteFilterContract.Filter.PRIORITY + " = ? ";
        String [] whereArgs = new String[] { Integer.toString(updatePriority) };
        
        /* update */
        try {
            context.getContentResolver().update(updateUri, updateValues, where, whereArgs);
            fail("Duplicate data is now allowed!");
        } catch(IllegalArgumentException ex) {
            //ok
        }
        

        /* sync with notifications */
        syncContentResolver();
        
        /* verify */
        rootCounter.checkNotFired();
        pkg1Counter.checkNotFired();
        pkg2Counter.checkNotFired();
        pkg3Counter.checkNotFired();
    }
}
