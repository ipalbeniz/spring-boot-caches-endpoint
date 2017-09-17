package com.demo.autoconfiguration.cachekeys;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisCacheKeysExtractor implements CacheKeysExtractor {

	@Override
	public Collection extractKeys(Cache cache) {

		RedisConnection redisConnection = getRedisConnection((RedisCache)cache);
		Set<byte[]> binaryKeys = redisConnection.keys((cache.getName() + ":*").getBytes());
		redisConnection.close();

		return binaryKeys.stream()
			.map(keyBytes -> new String(keyBytes, StandardCharsets.UTF_8))
			.collect(Collectors.toSet());
	}

	private RedisConnection getRedisConnection(RedisCache redisCache) {

		RedisTemplate redisTemplate = (RedisTemplate) redisCache.getNativeCache();
		return redisTemplate.getConnectionFactory().getConnection();
	}
}
