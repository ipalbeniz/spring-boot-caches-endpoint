package com.demo.autoconfiguration.cachekeys;

import java.util.Collection;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCache;

public class ConcurrentMapCacheKeysExtractor implements CacheKeysExtractor {

	@Override
	public Collection extractKeys(Cache cache) {

		return ((ConcurrentMapCache)cache).getNativeCache().keySet();
	}
}
