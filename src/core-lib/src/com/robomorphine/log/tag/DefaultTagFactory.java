package com.robomorphine.log.tag;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.google.common.base.Preconditions;

/**
 * Default implementation of TagFactory.
 * 
 * Each generated tag consists of two parts: prefix and class name.
 * Class name is always taken using Class.getSimpleName() function. 
 * While tag prefix is identified using next algorithm:
 * 
 *    getPackagePrefix(String preferredPrefix, String packageName) -> prefix
 *   
 *    1) If clazz.getPackage().getName() matches any package prefix,
 *       associated tag prefix is returned.
 *    2) If no matching package prefixes are found, preferredPrefix is returned
 *    3) If preferredPrefix is null, default prefix is returned (see setDefaultPrefix(..) )
 *  
 *  The 1) step needs more detailed coverage. User can add custom package prefixes using 
 *  addPackagePrefix(..) function. The matching process is simple: if package name starts
 *  with package prefix, its considered a match. For example:
 *  
 *  Package prefix: com.example
 *  Package prefix matches      : com.example, com.example.sub , com.example.sub.sub
 *  Package prefix doesn't match: com.exampl, com.sub, com.robomorphine
 *  
 *  If there are several potential matches for specific package, the first one added using
 *  addPackagePrefix(..) wins. For example:
 *  
 *  addPackagePrefix("com.", "comPrefix");
 *  addPackagePrefix("com.example", "examplePrefix");
 *  
 *  Will result in tag prefix "comPrefix" for "com.", "com.test" and "com.example" package names. 
 *  A more correct order of adding package prefixes would be:
 *  
 *  addPackagePrefix("com.example", "examplePrefix");
 *  addPackagePrefix("com.", "comPrefix");
 *  
 *  This way "com.example" gives tag prefix "examplePrefix", 
 *  while "com." and "com.test" both result in tag prefix "comPrefix".  
 *
 */
@ThreadSafe
public class DefaultTagFactory implements TagFactory {
		
    private static class Pair {
        public Pair(String packagePrefix, String tagPrefix) {
            this.packagePrefix = packagePrefix;
            this.tagPrefix = tagPrefix;
        }

        public String packagePrefix;
        public String tagPrefix;
    }
	
    private final List<Pair> mPrefixes;
    private String mDefault = "";

    /**
     * Creates factory with empty default tag prefix.
     */
    public DefaultTagFactory() {
        this("");
    }

    /**
     * Creates factory with custom default tag prefix.
     * 
     * @param defPrefix default tag prefix, must not be null
     */
    public DefaultTagFactory(String defPrefix) {
        Preconditions.checkNotNull(defPrefix);
        mPrefixes = new CopyOnWriteArrayList<Pair>();
        mDefault = defPrefix;
    }

    /**
     * Set default tag prefix
     * 
     * @param prefix, must not be null
     */
    public void setDefaultPrefix(String prefix) {
        Preconditions.checkNotNull(prefix);
        mDefault = prefix;
    }
	
    /**
     * Returns current default tag prefix.
     * 
     * @return default tag prefix, never returns null
     */
    public String getDefaultPrefix() {
        return mDefault;
    }	

    /**
     * Adds association between package name prefix and tag prefix. Later when
     * tag is generated this information is used to generated tag prefix. See
     * description of this class for more details.
     * 
     * @param packagePrefix package name prefix
     * @param tagPrefix tag prefix to associate with package prefix name
     */
    public void addPackagePrefix(String packagePrefix, String tagPrefix) {
        Preconditions.checkNotNull(packagePrefix);
        Preconditions.checkNotNull(tagPrefix);
        mPrefixes.add(new Pair(packagePrefix, tagPrefix));
    }

    /**
     * Identify tag prefix based on package name.
     * 
     * @param preferredPrefix - desired prefix if no package name prefix matches are found
     * @param packageName - package name to match against
     * @return tag prefix (without ".")
     */
    public String getPackagePrefix(@Nullable
    String preferredPrefix, String packageName) {
        Preconditions.checkNotNull(packageName);
        String prefix = preferredPrefix;
        if (prefix == null) {
            prefix = mDefault;
        }

        for (Pair pair : mPrefixes) {
            if (packageName.startsWith(pair.packagePrefix)) {
                prefix = pair.tagPrefix;
                break;
            }
        }
        return prefix;
    }
		
	/**
	 * Creates a tag as described in TagFactory
	 */
	@Override
	public String createTag(@Nullable String preferredPrefix, Class<?> clazz) {
		Preconditions.checkNotNull(clazz);
		
		String packageName = clazz.getPackage().getName();
		String prefix = getPackagePrefix(preferredPrefix, packageName);
		
		String tag = clazz.getSimpleName();
		if (prefix.length() > 0 ) {
			tag = prefix + "." + tag; //NOPMD
		}
		
		return tag; 
	}
}