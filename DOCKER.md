# Docker Deployment Guide for Quantity Measurement App

## Overview
This guide explains how to build and run the Quantity Measurement App using Docker and Docker Compose.

## Prerequisites
- Docker (version 20.10+)
- Docker Compose (version 1.29+)

## Quick Start with Docker Compose

### 1. Setup Environment Variables
```bash
# Copy the example environment file
cp .env.example .env

# Edit .env with your configuration
# nano .env  # or use your preferred editor
```

### 2. Build and Start Services
```bash
# Build and start all services
docker-compose up -d

# Check service status
docker-compose ps

# View application logs
docker-compose logs -f app

# View database logs
docker-compose logs -f mysql
```

### 3. Access the Application
- **API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Documentation**: http://localhost:8080/api-docs
- **Health Check**: http://localhost:8080/actuator/health
- **H2 Console** (if enabled): http://localhost:8080/h2-console

### 4. Stop Services
```bash
# Stop all running services
docker-compose down

# Stop and remove volumes (database data will be deleted)
docker-compose down -v
```

## Building Docker Image Manually

### Build the Image
```bash
docker build -t quantity-measurement-app:latest .
```

### Run the Container
```bash
# Run with environment variables
docker run -d \
  --name quantity-measurement-app \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/quantitymeasurementdb \
  -e SPRING_DATASOURCE_USERNAME=qm_user \
  -e SPRING_DATASOURCE_PASSWORD=qm_password \
  quantity-measurement-app:latest
```

## Production Deployment

### Docker Image Optimization
The Dockerfile uses a multi-stage build to reduce image size:
1. **Stage 1 (Builder)**: Compiles the Maven project
2. **Stage 2 (Runtime)**: Creates a lightweight JRE image with only the JAR

### Security Best Practices
1. Use strong JWT secrets: Generate a random string for `APP_JWT_SECRET`
2. Change default credentials: Update `APP_DEFAULT_PASSWORD` and `APP_DEFAULT_ROLES`
3. Use environment variables for sensitive data
4. Run containers with non-root users (optional enhancement)
5. Use Docker secrets for production deployments with Docker Swarm

### Performance Tuning
- Adjust `JAVA_OPTS` for heap memory: `-Xmx1024m -Xms512m`
- Configure appropriate database connection pool size
- Use health checks to monitor application status

## Environment Variables

### Database Configuration
- `SPRING_DATASOURCE_URL`: JDBC URL for the database
- `SPRING_DATASOURCE_USERNAME`: Database user
- `SPRING_DATASOURCE_PASSWORD`: Database password

### JWT Configuration
- `APP_JWT_SECRET`: Secret key for signing JWTs (change in production)
- `APP_JWT_EXPIRATION_MS`: Token expiration time in milliseconds (default: 3600000)
- `APP_JWT_ISSUER`: JWT issuer name

### User Configuration
- `APP_DEFAULT_USERNAME`: Default admin username
- `APP_DEFAULT_PASSWORD`: Default admin password (change in production)
- `APP_DEFAULT_EMAIL`: Default admin email
- `APP_DEFAULT_FULL_NAME`: Default admin full name
- `APP_DEFAULT_ROLES`: Comma-separated user roles
- `APP_DEFAULT_USER_ENABLED`: Enable/disable default user

### OAuth2 Configuration
- `GOOGLE_OAUTH_ENABLED`: Enable/disable Google OAuth2
- `GOOGLE_CLIENT_ID`: Google OAuth2 client ID
- `GOOGLE_CLIENT_SECRET`: Google OAuth2 client secret
- `GOOGLE_REDIRECT_URI`: OAuth2 redirect URI
- `GOOGLE_AUTHORIZED_REDIRECT_URI`: Authorized redirect URI

### Application Configuration
- `SPRING_PROFILES_ACTIVE`: Active Spring profiles (dev, prod, docker)
- `JPA_DDL_AUTO`: Hibernate DDL strategy (create, update, validate, none)
- `APP_PORT`: Application port (default: 8080)
- `JAVA_OPTS`: JVM options

## Troubleshooting

### Container Won't Start
```bash
# Check logs
docker-compose logs app

# Check image exists
docker images | grep quantity-measurement-app

# Rebuild image
docker-compose build --no-cache
```

### Database Connection Issues
```bash
# Check MySQL container status
docker-compose ps mysql

# Check MySQL logs
docker-compose logs mysql

# Verify network connectivity
docker-compose exec app ping mysql
```

### Health Check Failures
```bash
# Check application logs
docker-compose logs app

# Verify endpoint accessibility
docker-compose exec app curl http://localhost:8080/actuator/health
```

## Docker Compose Structure

```yaml
services:
  - mysql: MySQL 8.0 database service
  - app: Quantity Measurement Spring Boot application
```

### Networks
- `qm_network`: Bridge network connecting MySQL and app services

### Volumes
- `mysql_data`: Persistent storage for MySQL database

## Additional Commands

### View Container Logs
```bash
# Real-time logs
docker-compose logs -f

# Last 50 lines
docker-compose logs --tail=50

# Show timestamps
docker-compose logs -t
```

### Execute Commands in Container
```bash
# Access application container shell
docker-compose exec app sh

# Access MySQL container
docker-compose exec mysql mysql -u qm_user -p -D quantitymeasurementdb
```

### Database Management
```bash
# Backup database
docker-compose exec mysql mysqldump -u qm_user -p quantitymeasurementdb > backup.sql

# Restore database
docker-compose exec mysql mysql -u qm_user -p quantitymeasurementdb < backup.sql
```

## Performance Monitoring

### Check Resource Usage
```bash
docker stats quantity-measurement-app
```

### View Application Metrics
```bash
curl http://localhost:8080/actuator/metrics
```

## CI/CD Integration

### GitHub Actions Example
```yaml
- name: Build and push Docker image
  uses: docker/build-push-action@v4
  with:
    context: .
    push: true
    tags: myregistry/quantity-measurement-app:${{ github.sha }}
```

## References
- [Spring Boot Docker Guide](https://spring.io/guides/gs/spring-boot-docker/)
- [Docker Best Practices](https://docs.docker.com/develop/dev-best-practices/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)

