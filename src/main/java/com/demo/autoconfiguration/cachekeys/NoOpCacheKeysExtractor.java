package com.demo.autoconfiguration.cachekeys;

import java.util.Collection;
import java.util.Collections;
import org.springframework.cache.Cache;

public class NoOpCacheKeysExtractor implements CacheKeysExtractor {

	@Override
	public Collection extractKeys(Cache cache) {
		return Collections.emptyList();
	}
}
