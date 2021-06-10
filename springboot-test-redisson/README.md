

## Map

#### Map eviction, local cache and data partitioning

Redisson provides various Map structure implementations with three important features:

**local cache** - so called `near cache` used to speed up read operations and avoid network roundtrips. It caches Map entries on Redisson side and executes read operations up to **45x faster** in comparison with common implementation. Local cache instances with the same name connected to the same pub/sub channel. This channel is used for exchanging of update/invalidate events between instances. Local cache store doesn't use `hashCode()`/`equals()` methods of key object, instead it uses hash of serialized state.

**data partitioning** - data partitioning in cluster mode. It allows to scale available memory, read/write operations and entry eviction process for individual Map instance in Redis cluster.

**eviction** - allows to define `time to live` or `max idle time` for each map entry.

Below is the list of all available Map implementations:

| RedissonClient method name                                   | Local cache | Data partitioning | Eviction | Ultra-fast read/write |
| ------------------------------------------------------------ | ----------- | ----------------- | -------- | --------------------- |
| getMap() *open-source version*                               | ❌           | ❌                 | ❌        | ❌                     |
| getMapCache() *open-source version*                          | ❌           | ❌                 | ✔️        | ❌                     |
| getLocalCachedMap() *open-source version*                    | ✔️           | ❌                 | ❌        | ❌                     |
| getMap() *[Redisson PRO](https://redisson.pro/) version*     | ❌           | ❌                 | ❌        | ✔️                     |
| getMapCache() *[Redisson PRO](https://redisson.pro/) version* | ❌           | ❌                 | ✔️        | ✔️                     |
| getLocalCachedMap() *[Redisson PRO](https://redisson.pro/) version* | ✔️           | ❌                 | ❌        | ✔️                     |
| getLocalCachedMapCache() *available only in [Redisson PRO](https://redisson.pro/)* | ✔️           | ❌                 | ✔️        | ✔️                     |
| getClusteredMap() *available only in [Redisson PRO](https://redisson.pro/)* | ❌           | ✔️                 | ❌        | ✔️                     |
| getClusteredMapCache() *available only in [Redisson PRO](https://redisson.pro/)* | ❌           | ✔️                 | ✔️        | ✔️                     |
| getClusteredLocalCachedMap() *available only in [Redisson PRO](https://redisson.pro/)* | ✔️           | ✔️                 | ❌        | ✔️                     |
| getClusteredLocalCachedMapCache() *available only in [Redisson PRO](https://redisson.pro/)* | ✔️           | ✔️                 | ✔️        | ✔️                     |

Redisson also provides [Spring Cache](https://github.com/redisson/redisson/wiki/14.-Integration-with-frameworks/#142-spring-cache) and [JCache](https://github.com/redisson/redisson/wiki/14.-Integration-with-frameworks/#144-jcache-api-jsr-107-implementation) implementations.



## Set

#### Set eviction and data partitioning

Redisson provides various Set structure implementations with two important features:

**data partitioning** - data partitioning in cluster mode. It allows to scale available memory, read/write operations and entry eviction process for individual Set instance in Redis cluster.

**eviction** - allows to define `time to live` per set value.

Below is the list of all available Map implementations:

| RedissonClient method name                                   | Data partitioning support | Eviction support | Ultra-fast read/write |
| ------------------------------------------------------------ | ------------------------- | ---------------- | --------------------- |
| getSet() *open-source version*                               | ❌                         | ❌                | ❌                     |
| getSetCache() *open-source version*                          | ❌                         | ✔️                | ❌                     |
| getSet() *[Redisson PRO](https://redisson.pro/) version*     | ❌                         | ❌                | ✔️                     |
| getSetCache() *[Redisson PRO](https://redisson.pro/) version* | ❌                         | ✔️                | ✔️                     |
| getClusteredSet() *available only in [Redisson PRO](https://redisson.pro/)* | ✔️                         | ❌                | ✔️                     |
| getClusteredSetCache() *available only in [Redisson PRO](https://redisson.pro/)* | ✔️                         | ✔️                | ✔️                     |

#### 

