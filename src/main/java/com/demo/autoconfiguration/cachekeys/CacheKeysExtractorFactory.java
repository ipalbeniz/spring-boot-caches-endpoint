package com.demo.autoconfiguration.cachekeys;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.ehcache.EhCacheCache;
import org.springframework.data.redis.cache.RedisCache;

public class CacheKeysExtractorFactory {

	private CacheKeysExtractorFactory() {
		// Avoid instantiation
	}

	private static final Map<Class, CacheKeysExtractor> KEYS_EXTRACTORS_BY_CACHE_CLASS = new ConcurrentHashMap<>();

	public static CacheKeysExtractor getKeyExtractor(Cache cache) {

		return KEYS_EXTRACTORS_BY_CACHE_CLASS.computeIfAbsent(cache.getClass(),
			k -> createCacheKeysExtractor(cache));
	}

	private static CacheKeysExtractor createCacheKeysExtractor(Cache cache) {

		if (cache instanceof ConcurrentMapCache) {

			return new ConcurrentMapCacheKeysExtractor();

		} else if (cache instanceof EhCacheCache) {

			return new EhCacheKeysExtractor();

		} else if (cache instanceof RedisCache) {

			return new RedisCacheKeysExtractor();

		} else {

			return new NoOpCacheKeysExtractor();
		}
	}
}
