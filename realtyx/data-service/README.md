# RealtyX Data Service

## Overview
The Data Service is a separate Spring Boot microservice that handles all data access operations for the RealtyX application. It provides a clean abstraction layer between the application and the data storage (currently JSON files, easily replaceable with database).

## Architecture Benefits
- **Separation of Concerns**: Data access logic is isolated from business logic
- **Easy Migration**: Switch from file system to database without changing the main application
- **Scalability**: Data service can be scaled independently
- **Reusability**: Multiple applications can use the same data service
- **Maintainability**: Data access code is centralized in one place

## Port Configuration
- **Data Service**: Port 9090
- **Main Backend**: Port 8080
- **UI Application**: Port 3001

## API Endpoints

### Properties
- `GET /api/data/properties` - Get all properties
- `GET /api/data/properties/{id}` - Get property by ID
- `GET /api/data/properties/featured` - Get featured properties
- `GET /api/data/properties/status/{status}` - Get properties by status
- `GET /api/data/properties/agent/{agentId}` - Get properties by agent
- `GET /api/data/properties/seller/{sellerId}` - Get properties by seller
- `GET /api/data/properties/builder/{builderId}` - Get properties by builder
- `POST /api/data/properties` - Create new property
- `PUT /api/data/properties/{id}` - Update property
- `DELETE /api/data/properties/{id}` - Delete property

### Agents
- `GET /api/data/agents` - Get all agents
- `GET /api/data/agents/{id}` - Get agent by ID
- `GET /api/data/agents/email/{email}` - Get agent by email
- `POST /api/data/agents` - Create new agent
- `PUT /api/data/agents/{id}` - Update agent
- `DELETE /api/data/agents/{id}` - Delete agent

### Buyers
- `GET /api/data/buyers` - Get all buyers
- `GET /api/data/buyers/{id}` - Get buyer by ID
- `POST /api/data/buyers` - Create new buyer
- `PUT /api/data/buyers/{id}` - Update buyer
- `DELETE /api/data/buyers/{id}` - Delete buyer

### Sellers
- `GET /api/data/sellers` - Get all sellers
- `GET /api/data/sellers/{id}` - Get seller by ID
- `POST /api/data/sellers` - Create new seller
- `PUT /api/data/sellers/{id}` - Update seller
- `DELETE /api/data/sellers/{id}` - Delete seller

### Builders
- `GET /api/data/builders` - Get all builders
- `GET /api/data/builders/{id}` - Get builder by ID
- `POST /api/data/builders` - Create new builder
- `PUT /api/data/builders/{id}` - Update builder
- `DELETE /api/data/builders/{id}` - Delete builder

### Brokers
- `GET /api/data/brokers` - Get all brokers
- `GET /api/data/brokers/{id}` - Get broker by ID
- `POST /api/data/brokers` - Create new broker
- `PUT /api/data/brokers/{id}` - Update broker
- `DELETE /api/data/brokers/{id}` - Delete broker

## Running the Service

### Using Maven
```bash
cd data-service
mvn spring-boot:run
```

### Using Java
```bash
cd data-service
mvn clean package
java -jar target/data-service-1.0.0.jar
```

## Configuration

The data service is configured via `application.yml`:

```yaml
data:
  directory: ../data
  files:
    properties: properties.json
    brokers: brokers.json
    agents: agents.json
    # ... other files
```

## Migrating to Database

To migrate from JSON files to a database:

1. Add database dependencies (e.g., Spring Data JPA, MySQL connector)
2. Create JPA entities from the existing models
3. Create JPA repositories
4. Replace `DataAccessService` implementation to use repositories
5. Update `application.yml` with database configuration
6. **No changes needed in the main backend application!**

Example migration:

```java
// Before (File System)
@Service
public class DataAccessService {
    private JsonFileStore fileStore;
    // ... file operations
}

// After (Database)
@Service
public class DataAccessService {
    private PropertyRepository propertyRepository;
    private AgentRepository agentRepository;
    // ... repository operations with same method signatures
}
```

## Health Check

Check if the service is running:
```bash
curl http://localhost:9090/actuator/health
```

## CORS Configuration

The service allows requests from:
- http://localhost:3001 (UI application)
- http://localhost:8080 (Main backend)

## Data Files

The service reads/writes JSON files from the `../data` directory:
- properties.json
- agents.json
- buyers.json
- sellers.json
- builders.json
- brokers.json
- reviews.json
- property_inquiries.json
- admin_users.json
- import_jobs.json
- favourites.json

## Thread Safety

All file operations are thread-safe using `ReentrantReadWriteLock`:
- Multiple concurrent reads allowed
- Exclusive write access
- Automatic backup and restore on write failures
