# Migration Guide: Main Backend to Use Data Service

## Overview
This guide explains how to update the main RealtyX backend (port 8080) to use the Data Service (port 9090) instead of directly accessing JSON files.

## Architecture Before
```
Main Backend (8080)
    ↓ (direct file access)
JSON Files (../data/)
```

## Architecture After
```
Main Backend (8080)
    ↓ (HTTP REST calls)
Data Service (9090)
    ↓ (file access)
JSON Files (../data/)
```

## Step-by-Step Migration

### Step 1: Add RestTemplate or WebClient to Main Backend

Add to `backend/pom.xml` if not already present:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
```

### Step 2: Create Data Service Client Configuration

Create `backend/src/main/java/com/realtyx/config/DataServiceConfig.java`:
```java
@Configuration
public class DataServiceConfig {
    
    @Bean
    public WebClient dataServiceClient() {
        return WebClient.builder()
            .baseUrl("http://localhost:9090/api/data")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }
}
```

### Step 3: Update Services to Use Data Service

**Before (PropertyService.java):**
```java
@Service
public class PropertyService {
    private final JsonFileStore<Property> fileStore;
    private static final String PROPERTIES_FILE = "../data/properties.json";
    
    public List<Property> getAllProperties() {
        return fileStore.readList(PROPERTIES_FILE, new TypeReference<>() {});
    }
}
```

**After (PropertyService.java):**
```java
@Service
public class PropertyService {
    private final WebClient dataServiceClient;
    
    public PropertyService(WebClient dataServiceClient) {
        this.dataServiceClient = dataServiceClient;
    }
    
    public List<Property> getAllProperties() {
        return dataServiceClient.get()
            .uri("/properties")
            .retrieve()
            .bodyToFlux(Property.class)
            .collectList()
            .block();
    }
    
    public Property getPropertyById(long id) {
        return dataServiceClient.get()
            .uri("/properties/{id}", id)
            .retrieve()
            .bodyToMono(Property.class)
            .block();
    }
    
    public Property saveProperty(Property property) {
        if (property.id == 0) {
            return dataServiceClient.post()
                .uri("/properties")
                .bodyValue(property)
                .retrieve()
                .bodyToMono(Property.class)
                .block();
        } else {
            return dataServiceClient.put()
                .uri("/properties/{id}", property.id)
                .bodyValue(property)
                .retrieve()
                .bodyToMono(Property.class)
                .block();
        }
    }
}
```

### Step 4: Update All Entity Services

Apply the same pattern for:
- `AgentService` → `/agents`
- `BuyerService` → `/buyers`
- `SellerService` → `/sellers`
- `BuilderService` → `/builders`
- `BrokerService` → `/brokers`

### Step 5: Remove Direct File Access Dependencies

From `backend/pom.xml`, you can optionally remove or keep `JsonFileStore` class. The backend no longer directly accesses files.

### Step 6: Update application.yml

Add data service configuration:
```yaml
data-service:
  url: http://localhost:9090/api/data
  timeout: 5000
```

### Step 7: Add Error Handling

Add retry and fallback logic:
```java
public List<Property> getAllProperties() {
    try {
        return dataServiceClient.get()
            .uri("/properties")
            .retrieve()
            .bodyToFlux(Property.class)
            .collectList()
            .retry(3)
            .block();
    } catch (Exception e) {
        log.error("Error fetching properties from data service", e);
        return Collections.emptyList();
    }
}
```

## Benefits of This Approach

1. **Decoupling**: Main backend doesn't need to know about data storage
2. **Easy Database Migration**: Replace data service implementation without touching main backend
3. **Scalability**: Scale data service independently
4. **Caching**: Add caching layer in data service
5. **Multiple Backends**: Multiple applications can use same data service
6. **Testing**: Mock data service for unit tests

## Running Both Services

### Terminal 1 - Data Service
```bash
cd data-service
mvn spring-boot:run
```

### Terminal 2 - Main Backend
```bash
cd backend
mvn spring-boot:run
```

### Terminal 3 - UI Application
```bash
cd ui
node app.js
```

## Current Setup (Without Migration)

Currently, you have:
- **Data Service (9090)**: Running and ready to serve data
- **Main Backend (8080)**: Still accessing files directly
- **UI (3001)**: Accessing main backend

You can:
1. **Keep current setup**: Both services run independently, no changes needed
2. **Migrate backend**: Follow this guide to make backend use data service
3. **Future migration**: Replace data service file storage with database - no backend changes needed!

## Testing Data Service

Test the data service directly:
```bash
# Get all properties
curl http://localhost:9090/api/data/properties

# Get property by ID
curl http://localhost:9090/api/data/properties/1

# Get featured properties
curl http://localhost:9090/api/data/properties/featured
```

## Next Steps: Database Migration

When ready to migrate to database:
1. Update data-service with JPA dependencies
2. Create JPA entities and repositories
3. Replace DataAccessService implementation
4. Add database configuration
5. **Main backend requires ZERO changes!**

This is the power of the microservice architecture!
