package com.demo.autoconfiguration.cachekeys;

import java.util.Collection;
import org.springframework.cache.Cache;

public interface CacheKeysExtractor {

	/**
	 * Get keys from a cache
	 *
	 * @return the cache keys
	 */
	Collection extractKeys(Cache cache);
}
