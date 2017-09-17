package com.demo.autoconfiguration.cachekeys;

import java.util.Collection;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCache;

public class EhCacheKeysExtractor implements CacheKeysExtractor {

	@Override
	public Collection extractKeys(Cache cache) {
		return ((EhCacheCache) cache).getNativeCache().getKeys();
	}
}
