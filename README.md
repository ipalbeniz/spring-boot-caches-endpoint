# Introduction

I was looking for a *cache* Spring Boot *actuator* endpoint because I wanted to clear some caches in a running application.
But after some googling I only found this open issue: https://github.com/spring-projects/spring-boot/issues/2625

So I decided to code my own *actuator* endpoint as a PoC.


# Running the application

Run the demo application with `mvn spring-boot:run`

For the sake of simplicity a *ConcurrentMapCache* is configured.
```ini
# application.properties
spring.cache.type=simple
```

Anyway, you should be able to choose a different cache implementation:
```shell
mvn spring-boot:run -Dspring.cache.type=redis
```

# Running the tests

Run the tests with `mvn clean verify`

Kudos to [REST-assured](http://rest-assured.io/) for the great testing library!


# Allowed operations

> **Warning!** Some operations may not be suitable for production environments.

### Get cache names grouped by cache manager

```shell
> curl http://localhost:8080/caches
```
```json
{
    "cacheManager": [
        "cacheName1",
        "cacheName2"
    ]
}
```

### Get cache names by cache manager

```shell
> curl http://localhost:8080/caches/cacheManager
```
```json
[
    "cacheName1",
    "cacheName2"
]
```

### Get cache keys

For now, only ConcurrentMap, Redis & EhCache are supported.

```shell
> curl http://localhost:8080/caches/cacheManager/cacheName1/keys
```
```json
[
    "key10",
    "key1",
    "key2",
    "key5",
    "key6",
    "key3",
    "key4",
    "key9",
    "key7",
    "key8"
]
```

### Clear cache

```shell
> curl -X DELETE http://localhost:8080/caches/cacheManager/cacheName1
> curl http://localhost:8080/caches/cacheManager/cacheName1/keys
```
```json
[]
```