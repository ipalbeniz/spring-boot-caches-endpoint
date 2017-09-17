package com.demo.autoconfiguration;

import java.util.Map;
import org.springframework.boot.actuate.condition.ConditionalOnEnabledEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;

@ConditionalOnClass(CacheManager.class)
@ConditionalOnBean(CacheManager.class)
@AutoConfigureAfter(CacheAutoConfiguration.class)
@ConditionalOnEnabledEndpoint(CachesEndpoint.ENDPOINT_NAME)
public class CachesEndpointAutoConfiguration {

	private final Map<String, CacheManager> cacheManagers;

	public CachesEndpointAutoConfiguration(Map<String, CacheManager> cacheManagers) {

		this.cacheManagers = cacheManagers;
	}

	@Bean
	public CachesEndpoint cachesEndpoint() {

		return new CachesEndpoint(this.cacheManagers);
	}

	@Bean
	public CachesMvcEndpoint cachesMvcEndpoint(CachesEndpoint cachesEndpoint) {

		return new CachesMvcEndpoint(cachesEndpoint);
	}
}
