package com.demo.autoconfiguration;

import com.demo.autoconfiguration.cachekeys.CacheKeysExtractor;
import com.demo.autoconfiguration.cachekeys.CacheKeysExtractorFactory;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.endpoint.mvc.ActuatorMediaTypes;
import org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter;
import org.springframework.boot.actuate.endpoint.mvc.HypermediaDisabled;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@ConfigurationProperties(prefix = "endpoints.caches")
public class CachesMvcEndpoint extends EndpointMvcAdapter {

	private final Logger logger = LoggerFactory.getLogger(CachesMvcEndpoint.class);

	private final CachesEndpoint cachesEndpoint;

	public CachesMvcEndpoint(CachesEndpoint cachesEndpoint) {

		super(cachesEndpoint);
		this.cachesEndpoint = cachesEndpoint;
	}

	@RequestMapping(value = "/{cacheManager:.*}", method = RequestMethod.GET, produces = {
		ActuatorMediaTypes.APPLICATION_ACTUATOR_V1_JSON_VALUE,
		MediaType.APPLICATION_JSON_VALUE})
	@HypermediaDisabled
	public ResponseEntity<Collection<String>> getCachesByCacheManager(@PathVariable("cacheManager") String cacheManagerName) {

		if (!cachesEndpoint.isEnabled()) {
			return ResponseEntity.notFound().build();
		}

		CacheManager cacheManager = cachesEndpoint.getCacheManagerByName(cacheManagerName);

		if (cacheManager == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(cacheManager.getCacheNames());
	}

	@DeleteMapping("/{cacheManager:.*}/{cacheName:.*}")
	@HypermediaDisabled
	public ResponseEntity<Void> clearCache(@PathVariable("cacheManager") String cacheManagerName,
		@PathVariable("cacheName") String cacheName) {

		if (!cachesEndpoint.isEnabled()) {
			return ResponseEntity.notFound().build();
		}

		CacheManager cacheManager = cachesEndpoint.getCacheManagerByName(cacheManagerName);

		if (cacheManager == null) {
			return ResponseEntity.notFound().build();
		}

		Cache cache = cacheManager.getCache(cacheName);

		if (cache == null) {
			return ResponseEntity.notFound().build();
		}

		cache.clear();
		logger.info("Cache {} from CacheManager {} cleared", cacheName, cacheManagerName);

		return ResponseEntity.noContent().build();

	}

	@RequestMapping(value = "/{cacheManager:.*}/{cacheName:.*}/keys", method = RequestMethod.GET, produces = {
		ActuatorMediaTypes.APPLICATION_ACTUATOR_V1_JSON_VALUE,
		MediaType.APPLICATION_JSON_VALUE})
	@HypermediaDisabled
	public ResponseEntity<Collection> getCacheKeys(@PathVariable("cacheManager") String cacheManagerName,
		@PathVariable("cacheName") String cacheName) {

		if (!cachesEndpoint.isEnabled()) {
			return ResponseEntity.notFound().build();
		}

		CacheManager cacheManager = cachesEndpoint.getCacheManagerByName(cacheManagerName);

		if (cacheManager == null) {
			return ResponseEntity.notFound().build();
		}

		Cache cache = cacheManager.getCache(cacheName);

		if (cache == null) {
			return ResponseEntity.notFound().build();
		}

		CacheKeysExtractor keyExtractor = CacheKeysExtractorFactory.getKeyExtractor(cache);
		return ResponseEntity.ok(keyExtractor.extractKeys(cache));
	}

}
