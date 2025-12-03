# RealtyX - Complete Microservices Architecture Setup

## 🎯 What's Been Implemented

You now have a **production-ready microservices architecture** with:

### ✅ Core Services
1. **Service Registry (Eureka)** - Port 8761
2. **API Gateway with Circuit Breaker** - Port 8765
3. **Backend Service** - Port 8080
4. **Data Service** - Port 9090
5. **UI Application** - Port 3001

### ✅ Key Features
- ✅ Service Discovery (Netflix Eureka)
- ✅ API Gateway (Spring Cloud Gateway)
- ✅ Circuit Breaker (Resilience4j)
- ✅ Load Balancing (Client-side)
- ✅ Fallback Mechanisms
- ✅ Health Monitoring
- ✅ Request Logging
- ✅ CORS Configuration
- ✅ Retry Logic
- ✅ Metrics & Monitoring

## 🚀 Quick Start

### Option 1: Automated Script (Recommended)
```powershell
cd c:\Users\289544\Downloads\realtyx
.\start-microservices.ps1
```

### Option 2: Manual Start (Step by Step)

**Important: Start in this exact order!**

#### 1. Service Registry (First - Wait 15 seconds)
```bash
cd service-registry
mvn spring-boot:run
```
✅ Wait for: "RealtyX Service Registry (Eureka) is running"
✅ Dashboard: http://localhost:8761

#### 2. Data Service (Second - Wait 12 seconds)
```bash
cd data-service
mvn spring-boot:run
```
✅ Service will register with Eureka

#### 3. Backend Service (Third - Wait 12 seconds)
```bash
cd backend
mvn spring-boot:run
```
✅ Service will register with Eureka

#### 4. API Gateway (Fourth - Wait 12 seconds)
```bash
cd api-gateway
mvn spring-boot:run
```
✅ Gateway will discover all services

#### 5. UI Application (Last)
```bash
cd ui
node app.js
```

## 📊 Access Points

### User-Facing
- **UI Application**: http://localhost:3001
- **Eureka Dashboard**: http://localhost:8761

### API Endpoints (via Gateway)
- **Properties**: http://localhost:8765/data/properties
- **Agents**: http://localhost:8765/data/agents
- **Backend API**: http://localhost:8765/api/properties

### Monitoring
- **Gateway Health**: http://localhost:8765/actuator/health
- **Circuit Breakers**: http://localhost:8765/actuator/circuitbreakers
- **Circuit Events**: http://localhost:8765/actuator/circuitbreakerevents
- **Metrics**: http://localhost:8765/actuator/metrics

## 🔧 Testing Circuit Breaker

### Test 1: Normal Operation
```bash
# Access via gateway
curl http://localhost:8765/data/properties
```
✅ Should return property data

### Test 2: Service Failure Simulation
```bash
# Stop data service (Ctrl+C in its terminal)
# Then try accessing:
curl http://localhost:8765/data/properties
```
✅ Should return fallback response:
```json
{
  "status": "CIRCUIT_OPEN",
  "service": "data-service",
  "message": "Data Service is currently unavailable. Please try again later.",
  "fallback": true
}
```

### Test 3: Monitor Circuit Breaker
```bash
# Check circuit breaker status
curl http://localhost:8765/actuator/circuitbreakers

# Watch circuit breaker events
curl http://localhost:8765/actuator/circuitbreakerevents
```

### Run All Tests
```powershell
.\test-microservices.ps1
```

## 📈 Circuit Breaker Configuration

### Data Service Circuit Breaker
- **Sliding Window**: 10 requests
- **Failure Rate Threshold**: 50%
- **Wait Duration (Open)**: 10 seconds
- **Half-Open Calls**: 3 requests

### Backend Service Circuit Breaker
- **Sliding Window**: 10 requests
- **Failure Rate Threshold**: 50%
- **Wait Duration (Open)**: 15 seconds
- **Slow Call Threshold**: 2 seconds
- **Slow Call Rate**: 50%

### Retry Configuration
- **Max Attempts**: 3
- **Wait Duration**: 500ms
- **Exponential Backoff**: Enabled
- **Multiplier**: 2x

## 🎓 How It Works

### Service Discovery Flow
```
1. Services start and register with Eureka
2. Eureka maintains registry of all services
3. API Gateway queries Eureka for service locations
4. Gateway routes requests to healthy instances
5. Load balancing happens automatically
```

### Circuit Breaker Flow
```
1. Request comes to API Gateway
2. Gateway forwards to backend service
3. If service fails repeatedly:
   - Circuit opens (fast fail)
   - Fallback response returned
4. After wait duration:
   - Circuit goes half-open
   - Test requests sent
5. If tests pass:
   - Circuit closes (normal operation)
```

### Request Flow
```
Browser → UI (3001)
         ↓
    API Gateway (8765) ← Eureka (8761)
         ↓
    ├─ Backend (8080)
    └─ Data Service (9090)
              ↓
         JSON Files
```

## 📋 Project Structure

```
realtyx/
├── service-registry/          # Eureka Server (8761)
│   ├── src/
│   └── pom.xml
├── api-gateway/               # Spring Cloud Gateway (8765)
│   ├── src/
│   │   └── main/java/.../gateway/
│   │       ├── config/
│   │       │   ├── GatewayConfig.java
│   │       │   ├── CircuitBreakerConfig.java
│   │       │   └── CorsConfig.java
│   │       └── filter/
│   │           ├── FallbackController.java
│   │           └── LoggingFilter.java
│   └── pom.xml
├── backend/                   # Backend Service (8080)
├── data-service/              # Data Service (9090)
├── ui/                        # UI Application (3001)
├── data/                      # JSON data files
├── start-microservices.ps1    # Automated startup
├── test-microservices.ps1     # Testing script
└── MICROSERVICES_ARCHITECTURE.md
```

## 🛠️ Troubleshooting

### Service Not Registering with Eureka
**Problem**: Service doesn't appear in Eureka dashboard
**Solution**:
1. Ensure Eureka is running first
2. Check `application.yml` for correct Eureka URL
3. Wait 30 seconds for registration
4. Check service logs for errors

### Circuit Breaker Not Opening
**Problem**: Circuit stays closed despite failures
**Solution**:
1. Check failure rate threshold (50%)
2. Ensure minimum number of calls (5) is met
3. Review circuit breaker events
4. Check logs for exceptions

### Gateway Not Routing
**Problem**: 404 errors from gateway
**Solution**:
1. Verify service is registered in Eureka
2. Check gateway route configuration
3. Use correct service name (e.g., `lb://data-service`)
4. Review gateway logs

### Port Already in Use
**Problem**: Service fails to start
**Solution**:
```powershell
# Stop all services
Get-Process java -ErrorAction SilentlyContinue | Stop-Process -Force
Get-Process node -ErrorAction SilentlyContinue | Stop-Process -Force

# Restart services
.\start-microservices.ps1
```

## 📊 Monitoring Dashboard

Visit **http://localhost:8761** to see:
- All registered services
- Instance status (UP/DOWN)
- Number of instances per service
- Last heartbeat time
- Service URLs

## 🎯 Benefits

### 1. Fault Tolerance
- Services can fail without bringing down the entire system
- Circuit breaker prevents cascade failures
- Fallback responses keep system responsive

### 2. Scalability
- Add more service instances easily
- Automatic load balancing
- No client configuration changes needed

### 3. Flexibility
- Replace/update services independently
- Easy to add new services
- Service versions can coexist

### 4. Observability
- Centralized service registry
- Health monitoring
- Circuit breaker metrics
- Request/response logging

### 5. Performance
- Client-side load balancing
- Reduced latency
- Better resource utilization

## 🚀 Next Steps

### Immediate
1. ✅ Start all services
2. ✅ Access Eureka dashboard
3. ✅ Test API Gateway routes
4. ✅ Simulate failures
5. ✅ Monitor circuit breakers

### Short Term
- Add authentication to Gateway
- Implement rate limiting
- Add Redis caching
- Set up centralized logging

### Long Term
- Replace JSON files with database
- Add distributed tracing (Zipkin)
- Implement Spring Cloud Config
- Add Kafka for async messaging
- Set up Prometheus + Grafana

## 📚 Documentation

- **MICROSERVICES_ARCHITECTURE.md** - Detailed architecture guide
- **DATA_SERVICE_SUMMARY.md** - Data service overview
- **MIGRATION_GUIDE.md** - Database migration guide
- **QUICKSTART.md** - Quick start guide

## 🎉 Success Indicators

You'll know everything is working when:
- ✅ All 5 services appear in Eureka dashboard
- ✅ UI loads at http://localhost:3001
- ✅ Properties load through gateway
- ✅ Circuit breaker returns fallback when service is down
- ✅ Services auto-recover when restarted

## 📞 Support

For issues:
1. Check terminal logs for errors
2. Verify all services are running
3. Check Eureka dashboard for service status
4. Review circuit breaker events
5. Restart services in correct order

---

**Congratulations!** 🎊 You now have a complete microservices architecture with service discovery, API gateway, and circuit breaker implementation!
