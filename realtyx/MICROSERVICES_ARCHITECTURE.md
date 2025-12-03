# RealtyX Microservices Architecture

## Overview
Complete microservices architecture with Service Registry, API Gateway, and Circuit Breaker implementation.

## Architecture Components

### 1. Service Registry (Eureka Server) - Port 8761
**Purpose**: Service discovery and registration
- All microservices register themselves here
- Enables dynamic service discovery
- Health monitoring of all services
- Dashboard: http://localhost:8761

### 2. API Gateway (Spring Cloud Gateway) - Port 8765
**Purpose**: Single entry point for all client requests
- Route management
- Load balancing
- Circuit breaker (Resilience4j)
- Request/Response logging
- CORS handling
- Fallback mechanisms

### 3. Backend Service - Port 8080
**Purpose**: Main business logic service
- Property management
- User management
- Authentication & Authorization
- Registers with Eureka
- Circuit breaker protected

### 4. Data Service - Port 9090
**Purpose**: Data access layer
- CRUD operations on JSON files
- Can be replaced with database
- Registers with Eureka
- Circuit breaker protected

### 5. UI Application - Port 3001
**Purpose**: Frontend web application
- Node.js/Express server
- Accesses backend via API Gateway

## Architecture Diagram

```
┌─────────────┐
│   Browser   │
└──────┬──────┘
       │
       ▼
┌─────────────────┐         ┌──────────────────┐
│  UI (3001)      │◄────────┤  Service         │
└────────┬────────┘         │  Registry        │
         │                  │  (8761)          │
         │                  └────────▲─────────┘
         │                           │
         ▼                           │ Register
┌─────────────────┐                 │
│  API Gateway    │◄────────────────┤
│  (8765)         │                 │
│  + Circuit      │                 │
│    Breaker      │                 │
└────────┬────────┘                 │
         │                          │
         ├──────────────┬───────────┤
         │              │           │
         ▼              ▼           │
┌─────────────┐  ┌─────────────┐   │
│  Backend    │  │  Data       │   │
│  Service    │  │  Service    │   │
│  (8080)     │  │  (9090)     │   │
└─────────────┘  └──────┬──────┘   │
                        │           │
                        ▼           │
                   ┌─────────┐     │
                   │  JSON   │     │
                   │  Files  │     │
                   └─────────┘     │
                                   │
         Register/Health Check ────┘
```

## Circuit Breaker Implementation

### Features:
1. **Failure Detection**: Automatically detects service failures
2. **Fallback Responses**: Returns cached/default responses when services are down
3. **Auto Recovery**: Automatically tries to recover in half-open state
4. **Metrics**: Exposes circuit breaker metrics via Actuator

### Circuit Breaker States:
- **CLOSED**: Normal operation, requests pass through
- **OPEN**: Too many failures, requests fail fast with fallback
- **HALF_OPEN**: Testing if service has recovered

### Configuration:
- **Sliding Window Size**: 10 requests
- **Failure Rate Threshold**: 50%
- **Wait Duration in Open State**: 10-15 seconds
- **Slow Call Duration Threshold**: 2 seconds

## Port Configuration

| Service          | Port | URL                          |
|------------------|------|------------------------------|
| Service Registry | 8761 | http://localhost:8761        |
| API Gateway      | 8765 | http://localhost:8765        |
| Backend Service  | 8080 | http://localhost:8080        |
| Data Service     | 9090 | http://localhost:9090        |
| UI Application   | 3001 | http://localhost:3001        |

## API Routes via Gateway

### Access services through API Gateway:

**Data Service Routes:**
```
http://localhost:8765/data/properties
http://localhost:8765/data/agents
http://localhost:8765/data/buyers
http://localhost:8765/data/sellers
http://localhost:8765/data/builders
```

**Backend Service Routes:**
```
http://localhost:8765/api/properties
http://localhost:8765/api/agents
http://localhost:8765/api/buyers
http://localhost:8765/api/sellers
```

## Starting the Services

### Order Matters! Start in this sequence:

**1. Service Registry (First)**
```bash
cd service-registry
mvn spring-boot:run
```
Wait for: "RealtyX Service Registry (Eureka) is running"
Dashboard: http://localhost:8761

**2. Data Service (Second)**
```bash
cd data-service
mvn spring-boot:run
```
Wait for service to register with Eureka

**3. Backend Service (Third)**
```bash
cd backend
mvn spring-boot:run
```
Wait for service to register with Eureka

**4. API Gateway (Fourth)**
```bash
cd api-gateway
mvn spring-boot:run
```
Wait for gateway to discover all services

**5. UI Application (Last)**
```bash
cd ui
node app.js
```

### Or use the automated script:
```powershell
.\start-microservices.ps1
```

## Monitoring & Health Checks

### Service Registry Dashboard
- URL: http://localhost:8761
- View all registered services
- Check instance status
- Monitor heartbeats

### API Gateway Actuator
- Health: http://localhost:8765/actuator/health
- Circuit Breakers: http://localhost:8765/actuator/circuitbreakers
- Metrics: http://localhost:8765/actuator/metrics
- All Endpoints: http://localhost:8765/actuator

### Backend Service Actuator
- Health: http://localhost:8080/actuator/health
- Metrics: http://localhost:8080/actuator/metrics

### Data Service Actuator
- Health: http://localhost:9090/actuator/health
- Metrics: http://localhost:9090/actuator/metrics

## Testing Circuit Breaker

### 1. Test Normal Operation
```bash
# Access via gateway
curl http://localhost:8765/data/properties
```

### 2. Simulate Service Failure
```bash
# Stop data service
# Gateway should return fallback response
curl http://localhost:8765/data/properties

# Response will be:
{
  "status": "CIRCUIT_OPEN",
  "service": "data-service",
  "message": "Data Service is currently unavailable. Please try again later.",
  "fallback": true
}
```

### 3. Check Circuit Breaker Status
```bash
curl http://localhost:8765/actuator/circuitbreakers
```

### 4. Monitor Circuit Breaker Events
```bash
curl http://localhost:8765/actuator/circuitbreakerevents
```

## Benefits of This Architecture

### 1. **Service Discovery**
- Services find each other dynamically
- No hard-coded URLs
- Easy to scale horizontally

### 2. **Load Balancing**
- Automatic client-side load balancing
- Multiple instances of same service
- Better resource utilization

### 3. **Fault Tolerance**
- Circuit breaker prevents cascade failures
- Fallback responses keep system functional
- Auto-recovery when services come back

### 4. **Centralized Routing**
- Single entry point (Gateway)
- Easy to add security, rate limiting, etc.
- Simplified client configuration

### 5. **Monitoring**
- Centralized metrics
- Circuit breaker statistics
- Health checks for all services

### 6. **Scalability**
- Add more service instances easily
- Scale services independently
- Eureka handles discovery automatically

## Troubleshooting

### Service not registering with Eureka
- Check if Eureka server is running
- Verify `eureka.client.service-url.defaultZone` configuration
- Check network connectivity

### Circuit breaker not triggering
- Verify failure rate threshold is met
- Check minimum number of calls setting
- Monitor circuit breaker events

### Gateway not routing requests
- Verify service is registered in Eureka
- Check route configuration
- Review gateway logs

### High latency
- Check circuit breaker slow call threshold
- Monitor service response times
- Review retry configuration

## Production Considerations

1. **Security**: Add authentication/authorization at gateway
2. **HTTPS**: Enable SSL/TLS for all services
3. **Rate Limiting**: Configure rate limits per client
4. **Caching**: Add Redis for distributed caching
5. **Logging**: Centralize logs with ELK stack
6. **Monitoring**: Use Prometheus + Grafana
7. **Tracing**: Implement distributed tracing (Zipkin)
8. **Database**: Replace JSON files with proper database
9. **Config Server**: Use Spring Cloud Config for centralized configuration
10. **Message Queue**: Add Kafka/RabbitMQ for async communication

## Next Steps

1. Test all services individually
2. Test via API Gateway
3. Simulate failures and observe circuit breaker
4. Monitor Eureka dashboard
5. Review metrics and logs
6. Load test the system
7. Implement additional patterns as needed
