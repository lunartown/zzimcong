#!/bin/bash

# 로그 함수
log() {
    echo "[$(date +'%Y-%m-%d %H:%M:%S')] $1"
}

# 에러 체크 함수
check_error() {
    if [ $? -ne 0 ]; then
        log "Error: $1"
        exit 1
    fi
}

# 환경 체크
command -v docker >/dev/null 2>&1 || { log "Docker is not installed. Please install Docker first."; exit 1; }
command -v docker-compose >/dev/null 2>&1 || { log "Docker Compose is not installed. Please install Docker Compose first."; exit 1; }
command -v java >/dev/null 2>&1 || { log "Java is not installed. Please install Java 22."; exit 1; }

# 프로젝트 디렉토리로 이동
cd "$(dirname "$0")" || exit
log "Changed to project directory: $(pwd)"

# 각 서비스 JAR 파일 빌드
services=("user-service" "product-service" "order-service" "api-gateway")
for service in "${services[@]}"; do
    log "Building JAR for $service..."
    (
        cd "../backend/$service" || exit
        ./gradlew clean build -x test
        check_error "Failed to build JAR for $service"
    )
done

# Docker 이미지 빌드 및 컨테이너 실행
log "Building Docker images and starting containers..."
docker-compose up --build -d
check_error "Failed to start Docker containers"

# 컨테이너가 모두 시작될 때까지 대기
log "Waiting for all services to start..."
sleep 60

# 헬스 체크
log "Performing health checks..."
curl -f http://localhost:8080/actuator/health || log "Warning: API Gateway health check failed"
curl -f http://localhost:8081/actuator/health || log "Warning: User Service health check failed"
curl -f http://localhost:8082/actuator/health || log "Warning: Product Service health check failed"
curl -f http://localhost:8083/actuator/health || log "Warning: Order Service health check failed"

log "All services are up and running!"
log "Access the API Gateway at: http://localhost:8080"
log "Access Swagger UI for each service at:"
log "- User Service: http://localhost:8081/swagger-ui.html"
log "- Product Service: http://localhost:8082/swagger-ui.html"
log "- Order Service: http://localhost:8083/swagger-ui.html"

# 종료 처리
trap 'log "Shutting down..."; docker-compose down; exit' INT TERM

# 스크립트가 종료되지 않도록 대기
while true; do sleep 1; done