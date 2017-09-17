package com.demo.app;

import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.collection.IsEmptyCollection.empty;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CachesManagementEndpointIntegrationTest {

	private static final String DEFAULT_CACHE_MANAGER = "cacheManager";
	private static final String NON_EXISTENT_CACHE_MANAGER = "non-existent";
	private static final String CACHE_NAME = "cacheName1";

	@LocalServerPort
	private int port;

	@Autowired
	private CacheManager cacheManager;

	@Before
	public void setup() {

		RestAssured.port = port;
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
	}

	@Test
	public void should_return_the_cache_name() {

		createCacheWithName(CACHE_NAME);

		when()
			.get("/management/caches")

		.then()
			.statusCode(HttpStatus.OK.value())
			.contentType(ContentType.JSON)
			.body(DEFAULT_CACHE_MANAGER, hasItem(CACHE_NAME));
	}

	@Test
	public void should_return_the_cache_name_when_existing_cache_manager() {

		createCacheWithName(CACHE_NAME);

		when()
			.get("/management/caches/{cacheManager}", DEFAULT_CACHE_MANAGER)

		.then()
			.statusCode(HttpStatus.OK.value())
			.contentType(ContentType.JSON)
			.body("$", hasItem(CACHE_NAME));
	}

	@Test
	public void should_not_find_cache_names_when_non_existent_cache_manager() {

		createCacheWithName(CACHE_NAME);

		when()
			.get("/management/caches/{cacheManager}", NON_EXISTENT_CACHE_MANAGER)

		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}

	@Test
	public void should_clear_the_cache() {

		Cache cache = createCacheWithName(CACHE_NAME);
		cache.put("key", "value");

		when()
			.delete("/management/caches/{cacheManager}/{cacheName}", DEFAULT_CACHE_MANAGER, CACHE_NAME)

		.then()
			.statusCode(HttpStatus.NO_CONTENT.value());

		Assert.assertThat(cache.get("key"), is(nullValue()));
	}

	@Test
	public void should_return_the_cache_keys() {

		Cache cache = createCacheWithName(CACHE_NAME);
		cache.put("key1", "value");
		cache.put("key2", "value");

		when()
			.get("/management/caches/{cacheManager}/{cacheName}/keys", DEFAULT_CACHE_MANAGER, CACHE_NAME)

		.then()
			.statusCode(HttpStatus.OK.value())
			.contentType(ContentType.JSON)
			.body("$", hasItems("key1", "key2"));
	}

	@Test
	public void should_not_find_keys_when_non_existent_cache_manager() {

		Cache cache = createCacheWithName(CACHE_NAME);
		cache.put("key1", "value");
		cache.put("key2", "value");

		when()
			.get("/management/caches/{cacheManager}/{cacheName}/keys", NON_EXISTENT_CACHE_MANAGER, CACHE_NAME)

		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}

	@Test
	public void should_not_find_keys_when_non_existent_cache_name() {

		Cache cache = createCacheWithName(CACHE_NAME);
		cache.put("key1", "value");
		cache.put("key2", "value");

		when()
			.get("/management/caches/{cacheManager}/{cacheName}/keys", DEFAULT_CACHE_MANAGER, "non-existent")

		.then()
			.statusCode(HttpStatus.OK.value())
			.contentType(ContentType.JSON)
			.body("$", is(empty()));
	}

	private Cache createCacheWithName(String name) {

		return cacheManager.getCache(name);
	}

}
