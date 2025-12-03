# RealtyX - Docker Deployment Guide

## 📦 Docker Images

This project includes Dockerfiles for all microservices:

1. **Service Registry (Eureka)** - Port 8761
2. **Data Service** - Port 9090
3. **Backend Service** - Port 8080
4. **API Gateway** - Port 8765
5. **UI Application** - Port 3001

## 🚀 Quick Start

### Option 1: Using Docker Compose (Recommended)

Start all services with a single command:

```powershell
docker-compose up -d
```

This will:
- Build all Docker images
- Create a Docker network
- Start all services in the correct order
- Set up health checks
- Configure service dependencies

### Option 2: Build Images Manually

Run the build script:

```powershell
.\build-docker-images.ps1
```

Or build individually:

```powershell
# Service Registry
cd service-registry
docker build -t realtyx-service-registry:latest .

# Data Service
cd ../data-service
docker build -t realtyx-data-service:latest .

# Backend Service
cd ../backend
docker build -t realtyx-backend:latest .

# API Gateway
cd ../api-gateway
docker build -t realtyx-api-gateway:latest .

# UI Application
cd ../ui
docker build -t realtyx-ui:latest .
```

## 📋 Docker Commands

### View All Images
```powershell
docker images | findstr realtyx
```

### Start All Services
```powershell
docker-compose up -d
```

### View Running Containers
```powershell
docker ps
```

### View Logs
```powershell
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f backend
docker-compose logs -f ui
```

### Stop All Services
```powershell
docker-compose down
```

### Stop and Remove Volumes
```powershell
docker-compose down -v
```

### Restart a Single Service
```powershell
docker-compose restart backend
```

### Rebuild a Single Service
```powershell
docker-compose up -d --build backend
```

## 🔍 Service Health Checks

All services include health checks. View status:

```powershell
docker ps --format "table {{.Names}}\t{{.Status}}"
```

Individual health endpoints:
- Service Registry: http://localhost:8761/actuator/health
- Data Service: http://localhost:9090/actuator/health
- Backend: http://localhost:8080/actuator/health
- API Gateway: http://localhost:8765/actuator/health
- UI: http://localhost:3001

## 🌐 Access Points

Once all containers are running:

- **UI Application**: http://localhost:3001
- **Eureka Dashboard**: http://localhost:8761
- **API Gateway**: http://localhost:8765
- **Backend API**: http://localhost:8080/api
- **Data Service**: http://localhost:9090/api

## 🔧 Environment Variables

### Backend Service
- `SPRING_PROFILES_ACTIVE=docker`
- `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://service-registry:8761/eureka/`
- `DATA_SERVICE_URL=http://data-service:9090`

### Data Service
- `SPRING_PROFILES_ACTIVE=docker`
- `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://service-registry:8761/eureka/`

### API Gateway
- `SPRING_PROFILES_ACTIVE=docker`
- `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://service-registry:8761/eureka/`

### UI Application
- `NODE_ENV=production`
- `PORT=3001`
- `BACKEND_URL=http://api-gateway:8765`

## 📂 Volumes

The Data Service mounts the `./data` directory to persist JSON files:

```yaml
volumes:
  - ./data:/app/data
```

## 🔗 Network

All services run on a shared bridge network: `realtyx-network`

This allows services to communicate using container names:
- `http://service-registry:8761`
- `http://data-service:9090`
- `http://backend:8080`
- `http://api-gateway:8765`

## 🐛 Troubleshooting

### Services Not Starting

Check logs:
```powershell
docker-compose logs service-registry
docker-compose logs data-service
docker-compose logs backend
```

### Port Conflicts

If ports are already in use, modify `docker-compose.yml`:

```yaml
ports:
  - "18761:8761"  # Changed from 8761:8761
```

### Rebuild After Code Changes

```powershell
# Stop services
docker-compose down

# Rebuild and start
docker-compose up -d --build
```

### Clear Everything and Start Fresh

```powershell
# Stop and remove containers, networks, volumes
docker-compose down -v

# Remove images
docker rmi realtyx-service-registry realtyx-data-service realtyx-backend realtyx-api-gateway realtyx-ui

# Rebuild
docker-compose up -d --build
```

## 📊 Resource Requirements

Minimum recommended:
- **CPU**: 4 cores
- **RAM**: 8 GB
- **Disk**: 10 GB

## 🔐 Security Notes

For production deployment:
1. Change default passwords in `data/admin_users.json`
2. Use environment variables for secrets
3. Enable HTTPS/TLS
4. Configure proper firewall rules
5. Use Docker secrets for sensitive data

## 📈 Monitoring

All Spring Boot services expose Actuator endpoints:

- Health: `/actuator/health`
- Metrics: `/actuator/metrics`
- Info: `/actuator/info`
- Circuit Breakers: `/actuator/circuitbreakers`

Access through API Gateway:
```
http://localhost:8765/actuator/health
```

## 🚀 Production Deployment

For production:

1. **Use specific image tags** (not `latest`)
2. **Configure resource limits**:
   ```yaml
   deploy:
     resources:
       limits:
         cpus: '2'
         memory: 2G
   ```
3. **Set up log aggregation** (ELK, Splunk)
4. **Use orchestration** (Kubernetes, Docker Swarm)
5. **Implement CI/CD pipeline**

## 📝 Image Sizes

Approximate sizes:
- Service Registry: ~300 MB
- Data Service: ~300 MB
- Backend Service: ~350 MB
- API Gateway: ~320 MB
- UI Application: ~150 MB

**Total**: ~1.4 GB

## ✅ Verification Checklist

After `docker-compose up -d`:

1. ✓ All 5 containers running: `docker ps`
2. ✓ Services healthy: Check health endpoints
3. ✓ Eureka shows 3 services: http://localhost:8761
4. ✓ UI accessible: http://localhost:3001
5. ✓ Can login and browse properties

## 🆘 Support

If you encounter issues:

1. Check container logs: `docker-compose logs [service-name]`
2. Verify network: `docker network inspect realtyx-network`
3. Check health: `docker ps` (look for "healthy" status)
4. Ensure ports are available: `netstat -ano | findstr "8080 8761 8765 9090 3001"`

---

**Happy Containerizing! 🐳**
