package com.demo.app;

import java.util.stream.IntStream;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableCaching
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	/**
	 * Initial cache load for demo purposes
	 *
	 * @param cacheManager the CacheManager auto configured by Spring Boot
	 *
	 * @return the CommandLineRunner
	 */
	@Bean
	public CommandLineRunner runner(CacheManager cacheManager) {

		Cache cache = cacheManager.getCache("cacheName1");

		return args -> IntStream.rangeClosed(1,10)
			.forEach(intValue -> cache.put("key" + intValue, "value"));
	}
}
