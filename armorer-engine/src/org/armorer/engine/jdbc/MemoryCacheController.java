package org.armorer.engine.jdbc;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MemoryCacheController {

	private int referenceType = MemoryCacheController.WEAK;
	public final static int WEAK = 1;
	public final static int SOFT = 2;
	public final static int STRONG = 3;
	private Map cache = Collections.synchronizedMap(new HashMap());

	/**
	 * Add an object to the cache
	 * 
	 * @param cacheModel
	 *            The cacheModel
	 * @param key
	 *            The key of the object to be cached
	 * @param value
	 *            The object to be cached
	 */
	public void putObject(Object key, Object value) {
		Object reference = null;
		if (referenceType == MemoryCacheController.WEAK) {
			reference = new WeakReference(value);
		} else if (referenceType == MemoryCacheController.SOFT) {
			reference = new SoftReference(value);
		} else if (referenceType == MemoryCacheController.STRONG) {
			reference = new StrongReference(value);
		}
		cache.put(key, reference);
	}

	/**
	 * Get an object out of the cache.
	 * 
	 * @param cacheModel
	 *            The cache model
	 * @param key
	 *            The key of the object to be returned
	 * @return The cached object (or null)
	 */
	public Object getObject(Object key) {
		Object value = null;
		Object ref = cache.get(key);
		if (ref != null) {
			if (ref instanceof StrongReference) {
				value = ((StrongReference) ref).get();
			} else if (ref instanceof SoftReference) {
				value = ((SoftReference) ref).get();
			} else if (ref instanceof WeakReference) {
				value = ((WeakReference) ref).get();
			}
		}
		return value;
	}

	public Object removeObject(Object key) {
		Object value = null;
		Object ref = cache.remove(key);
		if (ref != null) {
			if (ref instanceof StrongReference) {
				value = ((StrongReference) ref).get();
			} else if (ref instanceof SoftReference) {
				value = ((SoftReference) ref).get();
			} else if (ref instanceof WeakReference) {
				value = ((WeakReference) ref).get();
			}
		}
		return value;
	}

	/**
	 * Flushes the cache.
	 * 
	 * @param cacheModel
	 *            The cache model
	 */
	public void flush() {
		cache.clear();
	}

	/**
	 * Class to implement a strong (permanent) reference.
	 */
	private static class StrongReference {
		private Object object;

		/**
		 * StrongReference constructor for an object
		 * 
		 * @param object
		 *            - the Object to store
		 */
		public StrongReference(Object object) {
			this.object = object;
		}

		/**
		 * Getter to get the object stored in the StrongReference
		 * 
		 * @return - the stored Object
		 */
		public Object get() {
			return object;
		}
	}
}
