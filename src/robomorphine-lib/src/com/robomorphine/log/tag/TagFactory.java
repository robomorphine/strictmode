package com.robomorphine.log.tag;

import javax.annotation.Nullable;

/**
 * Tag factory interface that should be implemented in order
 * to specify custom tag generator in TagsBase class.
 */
public interface TagFactory {
	
	/**
	 * Create tag using information class and preferred tag 
	 * prefix. Both parameters can be ignored by factory.
	 * 
	 * @param preferredPrefix - optional preferred prefix for the generated tag (can be null)
	 * @param clazz - reference to class that will use this tag (cann't be null)
	 * @return tag (should never return null).
	 */
    String createTag(@Nullable String preferredPrefix, Class<?> clazz);	
}