# RealtyX - Quick Start Guide

## Services Overview

RealtyX now consists of **3 separate services**:

1. **Data Service** (Port 9090) - Handles all data access
2. **Main Backend** (Port 8080) - Business logic and APIs
3. **UI Application** (Port 3001) - Frontend web interface

## Quick Start

### Option 1: Using PowerShell Script (Recommended)
```powershell
# Start all services at once
.\start-all.ps1

# Stop all services
.\stop-all.ps1
```

### Option 2: Manual Start (3 Terminals)

**Terminal 1 - Data Service:**
```bash
cd data-service
mvn spring-boot:run
```

**Terminal 2 - Main Backend:**
```bash
cd backend
mvn spring-boot:run
```

**Terminal 3 - UI Application:**
```bash
cd ui
node app.js
```

## Access URLs

- **UI Application**: http://localhost:3001
- **Main Backend API**: http://localhost:8080/api
- **Data Service API**: http://localhost:9090/api/data
- **Data Service Health**: http://localhost:9090/actuator/health

## Test Accounts

### Buyers
- Email: suresh.buyer@gmail.com
- Email: kavita.buyer@gmail.com
- Password: password123

### Sellers
- Email: vikram.seller@gmail.com
- Email: anjali.seller@gmail.com
- Password: password123

### Agents
- Email: rajesh.agent@realtyx.com
- Email: priya.agent@realtyx.com
- Email: amit.agent@realtyx.com
- Password: password123

## Features

- ✅ 10,000 properties with real images
- ✅ Multi-role system (Buyer, Seller, Agent, Builder)
- ✅ Authentication & session management
- ✅ Professional directory
- ✅ Real-time search & filtering
- ✅ Pagination (15 items per page)
- ✅ Admin dashboard with reports
- ✅ PDF/Excel export
- ✅ Microservice architecture

## Architecture

### Current Setup
```
UI (3001) → Main Backend (8080) → JSON Files
                ↓
    Data Service (9090) → JSON Files
```

### Future (After Migration)
```
UI (3001) → Main Backend (8080) → Data Service (9090) → Database
```

## Troubleshooting

### Port Already in Use
```powershell
# Check what's using a port
netstat -ano | findstr :9090
netstat -ano | findstr :8080
netstat -ano | findstr :3001

# Kill a process by PID
taskkill /F /PID <PID>

# Stop all services
.\stop-all.ps1
```

### Data Service Not Responding
```bash
# Check health
curl http://localhost:9090/actuator/health

# Test API
curl http://localhost:9090/api/data/properties
```

### Session Issues
If you're asked to login repeatedly:
1. Clear browser cookies for localhost:3001
2. Restart the UI service
3. Try in incognito/private mode

## Documentation

- **DATA_SERVICE_SUMMARY.md** - Overview of the data service
- **MIGRATION_GUIDE.md** - How to migrate backend to use data service
- **data-service/README.md** - Data service API documentation
- **README.md** - Main project documentation

## Development Workflow

1. **Start all services** using `start-all.ps1`
2. **Make code changes**
3. **Restart affected service** only
4. **Test changes** in browser
5. **Stop all services** using `stop-all.ps1`

## Production Deployment

For production, consider:
- Use Docker containers for each service
- Add nginx reverse proxy
- Implement service discovery (Consul/Eureka)
- Add centralized logging (ELK stack)
- Implement API gateway
- Add circuit breakers
- Configure proper database instead of JSON files

## Next Steps

1. ✅ Data service is ready and running
2. Optional: Migrate main backend to use data service
3. Future: Replace JSON files with database
4. Future: Add Redis caching layer
5. Future: Implement API documentation (Swagger)

## Support

For issues or questions:
1. Check terminal logs for error messages
2. Verify all services are running on correct ports
3. Clear browser cache and cookies
4. Restart services using stop-all.ps1 and start-all.ps1
