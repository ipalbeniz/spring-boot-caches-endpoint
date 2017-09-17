package com.demo.autoconfiguration;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;

@ConfigurationProperties(prefix = "endpoints.caches")
public class CachesEndpoint extends AbstractEndpoint<Map<String, Collection<String>>> {

	public static final String ENDPOINT_NAME = "caches";

	private final Map<String, CacheManager> cacheManagers;

	public CachesEndpoint(Map<String, CacheManager> cacheManagers) {

		super(ENDPOINT_NAME);
		this.cacheManagers = cacheManagers;
	}

	@Override
	public Map<String, Collection<String>> invoke() {

		return cacheManagers.entrySet().stream()
			.collect(Collectors.toMap(Entry::getKey, entry -> entry.getValue().getCacheNames()));
	}

	public CacheManager getCacheManagerByName(String cacheManager) {
		return cacheManagers.get(cacheManager);
	}
}
