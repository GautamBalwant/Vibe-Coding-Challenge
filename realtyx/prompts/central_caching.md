# Prompt: Add Central Caching to RealtyX

Use this prompt to propose and scaffold a central caching layer for the microservices platform (e.g., Redis). This helps developers implement caching for hot endpoints and reduce load on JSON file reads.

Context to provide:
- Target services to cache: (backend | data-service | api-gateway | ui)
- Cache technology preference: (Redis | Memcached | Hazelcast)
- Data to cache: (properties list, property details, directory search results, auth sessions)
- Cache TTL requirements: (short | medium | long)
- Eviction policy: (LRU | LFU | TTL-only)
- Persistence needs: (yes/no)

Prompt Template:
```
You are a seasoned system architect helping add central caching to the RealtyX microservices.

Context:
- Services: {{services}}
- Cache tech: {{cache_tech}}
- Data types: {{data_types}}
- TTL: {{ttl}}
- Persistence: {{persistence}}

Tasks:
1. Provide an architecture diagram (ascii) showing where Redis (or chosen cache) sits in relation to services and Eureka/Gateway.
2. Recommend cache keys and data shapes for each cached item (include examples).
3. Provide code snippets for connecting to Redis in Java (Spring Boot) and Node.js (UI/backend) with recommended libraries (Lettuce/Redisson for Java, ioredis/node-redis for Node).
4. Provide sample annotations or interceptors for automatic caching in Spring (Cacheable, CacheEvict) and manual cache usage examples.
5. Show configuration changes and environment variables required for local and Docker deployment.
6. Provide a migration/rollout strategy (enable for read-heavy endpoints first, monitor cache hit rate).
7. Provide tests and verification commands (cache hit/miss checks, TTL tests).

Deliverables:
- Key naming conventions
- Example code (Java + Node) for get/set/delete operations
- Docker Compose service snippet to add Redis
- Monitoring suggestions (Redis INFO, Prometheus exporter)
```

Notes:
- For session storage, recommend using Redis-backed session stores to replace cookie-session if needed.
- Consider data invalidation patterns when properties are updated or deleted (listen to events or call CacheEvict endpoints).