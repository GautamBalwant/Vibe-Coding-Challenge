# RealtyX - Real Estate Management Platform

## 🏠 Overview
RealtyX is a comprehensive **microservices-based** real estate management platform featuring service discovery, API gateway, circuit breakers, and a modern multi-role authentication system. Built with Spring Boot microservices and Node.js UI.

---

## 🏗️ Architecture

### Microservices Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    User Browser (Port 3001)                 │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ↓
┌─────────────────────────────────────────────────────────────┐
│              API Gateway (Port 8765)                        │
│  • Spring Cloud Gateway                                     │
│  • Circuit Breaker (Resilience4j)                          │
│  • Load Balancing                                           │
│  • Request Routing & Retry Logic                           │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ↓
┌─────────────────────────────────────────────────────────────┐
│          Service Registry - Eureka (Port 8761)              │
│  • Service Discovery                                        │
│  • Health Monitoring                                        │
│  • Dynamic Service Registration                             │
└─────────────┬───────────────────────────┬───────────────────┘
              │                           │
    ┌─────────┴─────────┐    ┌───────────┴──────────┐
    ↓                   ↓    ↓                      ↓
┌──────────────┐  ┌──────────────┐  ┌────────────────┐
│   Backend    │  │ Data Service │  │   (Future      │
│   Service    │  │   Service    │  │   Services)    │
│  Port 8080   │  │  Port 9090   │  │                │
│              │  │              │  │                │
│ • REST APIs  │  │ • JSON Data  │  │                │
│ • Business   │  │ • File I/O   │  │                │
│   Logic      │  │              │  │                │
└──────────────┘  └──────┬───────┘  └────────────────┘
                         │
                         ↓
                  ┌──────────────┐
                  │  JSON Files  │
                  │  (./data/)   │
                  └──────────────┘
```

---

## 📂 Project Structure

```
realtyx/
├── service-registry/       # Eureka Server (Port 8761)
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
│
├── api-gateway/            # Spring Cloud Gateway (Port 8765)
│   ├── src/
│   │   └── main/java/com/realtyx/gateway/
│   │       ├── config/
│   │       │   ├── GatewayConfig.java
│   │       │   ├── CircuitBreakerConfig.java
│   │       │   └── CorsConfig.java
│   │       ├── controller/FallbackController.java
│   │       └── filter/LoggingFilter.java
│   ├── pom.xml
│   └── Dockerfile
│
├── backend/                # Backend Service (Port 8080)
│   ├── src/main/java/com/realtyx/
│   │   ├── controller/
│   │   │   ├── AuthController.java
│   │   │   └── PropertyController.java
│   │   ├── model/
│   │   │   ├── Property.java
│   │   │   ├── AdminUser.java
│   │   │   ├── Agent.java
│   │   │   ├── Buyer.java
│   │   │   └── Seller.java
│   │   ├── service/
│   │   │   ├── JsonFileStore.java
│   │   │   └── PropertyService.java
│   │   └── config/SecurityConfig.java
│   ├── pom.xml
│   └── Dockerfile
│
├── data-service/           # Data Service (Port 9090)
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
│
├── ui/                     # Node.js UI (Port 3001)
│   ├── routes/
│   │   ├── home.js
│   │   ├── auth.js        # Unified login for all roles
│   │   ├── admin.js
│   │   ├── buyer.js
│   │   ├── seller.js
│   │   ├── agent.js
│   │   ├── explore.js
│   │   ├── directory.js   # Search agents/builders
│   │   └── property.js
│   ├── views/
│   │   ├── auth/
│   │   │   ├── login.ejs  # Unified login page
│   │   │   ├── signup-buyer.ejs
│   │   │   ├── signup-seller.ejs
│   │   │   └── signup-agent.ejs
│   │   ├── admin/
│   │   ├── buyer/
│   │   ├── seller/
│   │   ├── agent/
│   │   └── directory.ejs
│   ├── app.js
│   ├── package.json
│   └── Dockerfile
│
├── data/                   # JSON Data Storage
│   ├── properties.json     # 10,000 properties
│   ├── admin_users.json
│   ├── agents.json
│   ├── buyers.json
│   ├── sellers.json
│   ├── builders.json
│   ├── brokers.json
│   └── reviews.json
│
├── docker-compose.yml      # Docker orchestration
├── build-docker-images.ps1 # Docker build automation
├── start-microservices.ps1 # Automated startup
├── test-microservices.ps1  # Service testing
│
└── Documentation/
    ├── README.md
    ├── MICROSERVICES_ARCHITECTURE.md
    ├── MICROSERVICES_README.md
    ├── DOCKER_README.md
    └── DOCKER_SETUP_COMPLETE.md
```

---

## 🚀 Prerequisites

- **Java 21+** (for Spring Boot microservices)
- **Node.js v24+** (for UI application)
- **Maven 3.8+** (for building Java services)
- **Docker Desktop** (optional - for containerized deployment)

---

## 🎯 Quick Start

### Option 1: Automated Startup (Recommended)

```powershell
cd c:\Users\289544\Downloads\realtyx
.\start-microservices.ps1
```

This will start all services in the correct order:
1. Service Registry (Eureka) - 8761
2. Data Service - 9090
3. Backend Service - 8080
4. API Gateway - 8765
5. UI Application - 3001

### Option 2: Manual Startup

**Start each service in separate terminals:**

```powershell
# Terminal 1 - Service Registry
cd service-registry
mvn spring-boot:run

# Terminal 2 - Data Service (wait 15s)
cd data-service
mvn spring-boot:run

# Terminal 3 - Backend (wait 12s)
cd backend
mvn spring-boot:run

# Terminal 4 - API Gateway (wait 12s)
cd api-gateway
mvn spring-boot:run

# Terminal 5 - UI Application (wait 12s)
cd ui
node app.js
```

### Option 3: Docker Deployment

```powershell
# Build and start all services
docker-compose up -d --build

# View logs
docker-compose logs -f

# Stop all services
docker-compose down
```

---

## 🌐 Access Points

Once all services are running:

| Service | URL | Description |
|---------|-----|-------------|
| **UI Application** | http://localhost:3001 | Main user interface |
| **Eureka Dashboard** | http://localhost:8761 | Service registry & health |
| **API Gateway** | http://localhost:8765 | All API requests route here |
| **Backend Service** | http://localhost:8080/api | Direct backend access |
| **Data Service** | http://localhost:9090/api | Data layer access |

---

## ✨ Features

### Core Functionality
- **10,000+ Properties** with real Unsplash images
- **Multi-role Authentication System** (Buyer, Seller, Agent, Admin)
- **Unified Login Page** - Single entry point for all user types
- **Professional Directory** - Search agents, builders, and brokers
- **Property Search & Filters** - Advanced search by type, price, location
- **Pagination** - 15 items per page for optimal performance
- **Responsive Design** - Mobile-first UI with modern styling

### Microservices Features
- **Service Discovery** - Automatic service registration with Eureka
- **API Gateway** - Centralized routing and load balancing
- **Circuit Breaker** - Fault tolerance with Resilience4j
  - Sliding window: 10 requests
  - Failure threshold: 50%
  - Wait duration: 10-15s
  - Fallback endpoints for graceful degradation
- **Health Monitoring** - Spring Boot Actuator endpoints
- **Request Retry** - Automatic retry on transient failures
- **Load Balancing** - Client-side load balancing via Eureka

### Admin Features
- **Dashboard** - Overview of properties and users
- **Property Management** - Create, edit, delete properties
- **User Management** - View and manage all user types
- **Analytics** - Property views and user activity

### User Roles
1. **Buyer** - Browse and favorite properties
2. **Seller** - List and manage properties
3. **Agent** - Professional directory listing
4. **Admin** - Full platform management

---

## 🔐 Authentication

### Default Credentials

**Admin Access:**
```
Email: admin@realtyx.com
Username: admin
Password: password123
URL: http://localhost:3001/auth/login?role=admin
```

**Test User Accounts:**
- Buyers, Sellers, Agents are created via signup pages
- Session-based authentication with bcryptjs
- Role-based access control (RBAC)

### Login Flow
1. Visit http://localhost:3001/auth/login
2. Select role (Buyer, Seller, Agent, or Admin)
3. Enter credentials (username/email + password)
4. Redirected to role-specific dashboard

---

## 🐳 Docker Deployment

### Quick Start with Docker Compose

```powershell
# Build and start all services
docker-compose up -d --build

# View logs
docker-compose logs -f [service-name]

# Check service status
docker-compose ps

# Stop all services
docker-compose down

# Restart specific service
docker-compose restart [service-name]
```

### Manual Docker Build

```powershell
# Build images individually
docker build -t realtyx-service-registry ./service-registry
docker build -t realtyx-data-service ./data-service
docker build -t realtyx-backend ./backend
docker build -t realtyx-api-gateway ./api-gateway
docker build -t realtyx-ui ./ui

# Or use automation script
.\build-docker-images.ps1
```

### Docker Services

| Service | Image | Port | Health Check |
|---------|-------|------|--------------|
| service-registry | realtyx-service-registry | 8761 | http://localhost:8761/actuator/health |
| data-service | realtyx-data-service | 9090 | http://localhost:9090/actuator/health |
| backend | realtyx-backend | 8080 | http://localhost:8080/actuator/health |
| api-gateway | realtyx-api-gateway | 8765 | http://localhost:8765/actuator/health |
| ui | realtyx-ui | 3001 | http://localhost:3001/health |

---

## 📊 Monitoring & Health Checks

### Eureka Dashboard
Access http://localhost:8761 to view:
- Registered services
- Service health status
- Instance metadata
- Replication info

### Actuator Endpoints

All Spring Boot services expose actuator endpoints:

```
# Health check
GET http://localhost:{port}/actuator/health

# Circuit breaker status (Gateway only)
GET http://localhost:8765/actuator/circuitbreakers

# Service info
GET http://localhost:{port}/actuator/info

# Metrics
GET http://localhost:{port}/actuator/metrics
```

### Circuit Breaker Monitoring

Monitor circuit breaker states:
```powershell
# Gateway circuit breakers
curl http://localhost:8765/actuator/circuitbreakers

# Backend health
curl http://localhost:8765/actuator/health

# Test fallback
curl http://localhost:8765/fallback/properties
```

---

## 🛠️ Development

### Building Services

**Backend Services (Java):**
```powershell
cd service-registry
mvn clean install

cd data-service
mvn clean install

cd backend
mvn clean install

cd api-gateway
mvn clean install
```

**UI Application (Node.js):**
```powershell
cd ui
npm install
npm start
```

### Running Tests

```powershell
# Test microservices connectivity
.\test-microservices.ps1

# Java service tests
mvn test

# UI tests (if available)
npm test
```

### Environment Variables

**Backend Services:**
```properties
EUREKA_SERVER_URL=http://localhost:8761/eureka
DATA_SERVICE_URL=http://localhost:9090
BACKEND_SERVICE_URL=http://localhost:8080
```

**UI Application:**
```bash
BACKEND_URL=http://localhost:8765
PORT=3001
SESSION_SECRET=your-secret-key
```

---

## 🔧 Configuration

### Service Registry (Eureka)
```yaml
server:
  port: 8761
eureka:
  instance:
    hostname: localhost
  client:
    registerWithEureka: false
    fetchRegistry: false
```

### API Gateway
```yaml
server:
  port: 8765
spring:
  cloud:
    gateway:
      routes:
        - id: backend-service
          uri: lb://BACKEND-SERVICE
          predicates:
            - Path=/api/**
          filters:
            - name: CircuitBreaker
              args:
                name: backendCircuitBreaker
                fallbackUri: forward:/fallback/properties
```

### Circuit Breaker Settings
```yaml
resilience4j:
  circuitbreaker:
    instances:
      backendCircuitBreaker:
        slidingWindowSize: 10
        failureRateThreshold: 50
        waitDurationInOpenState: 10000
        permittedNumberOfCallsInHalfOpenState: 3
```

---

## 📝 API Documentation

### Property Endpoints
```
GET    /api/properties          - Get all properties (paginated)
GET    /api/properties/{id}     - Get property by ID
POST   /api/properties          - Create property (Admin/Seller)
PUT    /api/properties/{id}     - Update property (Admin/Seller)
DELETE /api/properties/{id}     - Delete property (Admin)
GET    /api/properties/search   - Search properties
```

### Authentication Endpoints
```
POST   /api/auth/login          - Login (all roles)
POST   /api/auth/logout         - Logout
GET    /api/auth/me             - Get current user
POST   /api/auth/signup/buyer   - Buyer signup
POST   /api/auth/signup/seller  - Seller signup
POST   /api/auth/signup/agent   - Agent signup
```

### Admin Endpoints
```
GET    /api/admin/users         - Get all users
GET    /api/admin/properties    - Get all properties
GET    /api/admin/analytics     - Get analytics data
```

---

## 🚨 Troubleshooting

### Service won't start
**Check if port is in use:**
```powershell
netstat -ano | findstr ":8761"
netstat -ano | findstr ":8080"
netstat -ano | findstr ":3001"
```

**Kill process on port:**
```powershell
taskkill /PID <process_id> /F
```

### Service not registering with Eureka
1. Check Eureka is running: http://localhost:8761
2. Wait 30 seconds for registration
3. Check application.yml has correct eureka config
4. Verify @EnableDiscoveryClient annotation

### Circuit breaker always open
1. Check backend service health
2. Verify circuit breaker configuration
3. Check actuator endpoint: http://localhost:8765/actuator/circuitbreakers
4. Reduce failureRateThreshold for testing

### Docker issues
**Container won't start:**
```powershell
# Check logs
docker-compose logs [service-name]

# Rebuild specific service
docker-compose up -d --build [service-name]

# Remove all containers and rebuild
docker-compose down -v
docker-compose up -d --build
```

**Services can't communicate:**
```powershell
# Check network
docker network ls
docker network inspect realtyx-network

# Check if services are on same network
docker inspect [container-name] | findstr NetworkMode
```

### Admin login fails
- Username: `admin`
- Email: `admin@realtyx.com`
- Password: `password123`
- Check backend is running: http://localhost:8080/actuator/health
- Verify data/admin_users.json exists

---

## 📚 Documentation

- **[MICROSERVICES_ARCHITECTURE.md](MICROSERVICES_ARCHITECTURE.md)** - Complete architecture guide
- **[MICROSERVICES_README.md](MICROSERVICES_README.md)** - Quick start and usage
- **[DOCKER_README.md](DOCKER_README.md)** - Docker setup and commands
- **[DOCKER_SETUP_COMPLETE.md](DOCKER_SETUP_COMPLETE.md)** - Docker installation guide

---

## 🎯 Technology Stack

### Backend
- **Spring Boot 3.1.5** - Application framework
- **Spring Cloud 2022.0.4** - Microservices framework
- **Netflix Eureka** - Service discovery
- **Spring Cloud Gateway** - API gateway and routing
- **Resilience4j** - Circuit breaker implementation
- **Spring Boot Actuator** - Health checks and monitoring
- **Java 21** - Programming language
- **Maven** - Build tool

### Frontend
- **Node.js 24.7.0** - JavaScript runtime
- **Express 4.x** - Web framework
- **EJS** - Template engine
- **cookie-session** - Session management
- **bcryptjs** - Password hashing
- **axios** - HTTP client

### DevOps
- **Docker** - Containerization
- **Docker Compose** - Multi-container orchestration
- **PowerShell** - Automation scripts

### Data
- **JSON Files** - File-based storage (development)
- **Unsplash API** - Property images

---

## 🔮 Future Enhancements

### Planned Features
- [ ] PostgreSQL/MongoDB integration
- [ ] Kubernetes deployment configuration
- [ ] CI/CD pipeline (GitHub Actions)
- [ ] Centralized logging (ELK Stack)
- [ ] Monitoring dashboards (Grafana + Prometheus)
- [ ] API rate limiting
- [ ] OAuth2 integration (Google, Facebook)
- [ ] Real-time notifications (WebSocket)
- [ ] Property comparison feature
- [ ] Virtual property tours
- [ ] Payment gateway integration
- [ ] Advanced analytics dashboard
- [ ] Mobile app (React Native)

### Scalability
- [ ] Kubernetes auto-scaling
- [ ] Redis caching layer
- [ ] CDN for static assets
- [ ] Database sharding
- [ ] Message queue (RabbitMQ/Kafka)
- [ ] API versioning
- [ ] GraphQL gateway

---

## 📄 License

This project is for educational and demonstration purposes.

---

## 👥 Contributors

- Development Team - RealtyX Platform

---

## 📞 Support

For issues and questions:
1. Check the troubleshooting section above
2. Review documentation in `/docs` folder
3. Check service health endpoints
4. Review logs: `docker-compose logs -f` or terminal output

---

**Version:** 2.0.0 (Microservices Architecture)  
**Last Updated:** 2024  
**Status:** Active Development
- Data stored in flat JSON files
- SEO, accessibility, and analytics ready
- Bulk import via CSV (backend)
- Rate limiting and security headers

---

## Sample Data
- `data/properties.json` — Example property
- `data/brokers.json`, `data/admin_users.json`, `data/import_jobs.json`, `data/favourites.json` — Empty arrays (add as needed)

---

## Demo Accounts
- Add admin users to `admin_users.json` with bcrypt-hashed passwords

---

## Troubleshooting
- Ensure ports 8080 (backend) and 3000 (UI) are free
- Check `.env` and `application.yml` for config
- Review logs for errors

---

## Contributing
PRs welcome! Please follow the project structure and add tests where possible.

---

## License
MIT
