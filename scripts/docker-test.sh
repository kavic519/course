#!/bin/bash

# 修复版容器化测试脚本
set -e

echo "=== 开始容器化测试 ==="
echo "开始时间: $(date)"

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 端口配置（根据您的实际映射）
APP_PORT=8081
MYSQL_PORT=3307

# 日志函数
log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 检查命令是否存在
check_command() {
    if ! command -v $1 &> /dev/null; then
        log_error "命令 $1 未找到，请先安装"
        return 1
    fi
    return 0
}

# 等待服务启动
wait_for_services() {
    log_info "等待服务启动..."
    
    # 等待MySQL健康检查通过
    log_info "等待MySQL服务..."
    max_attempts=30
    attempt=0
    
    while [ $attempt -lt $max_attempts ]; do
        if docker compose ps mysql | grep -q "healthy"; then
            log_info "MySQL服务已就绪"
            break
        fi
        attempt=$((attempt + 1))
        sleep 5
        echo -n "."
    done
    
    if [ $attempt -eq $max_attempts ]; then
        log_error "MySQL服务启动超时"
        exit 1
    fi
    
    # 等待应用启动
    log_info "等待应用服务..."
    attempt=0
    
    while [ $attempt -lt $max_attempts ]; do
        # 使用更简单的方式检查应用是否就绪
        if curl -s http://localhost:${APP_PORT}/health/db > /dev/null 2>&1; then
            log_info "应用服务已就绪"
            break
        fi
        attempt=$((attempt + 1))
        sleep 5
        echo -n "."
    done
    
    if [ $attempt -eq $max_attempts ]; then
        log_warn "应用服务启动较慢，继续测试..."
    fi
}

# 检查容器状态
check_container_status() {
    log_info "=== 检查容器状态 ==="
    docker compose ps
}

# 修复的健康检查测试
test_health_check() {
    log_info "=== 健康检查测试 ==="
    
    # 使用更稳健的方法获取HTTP状态码
    response=$(curl -s -o response_body.txt -w "%{http_code}" http://localhost:${APP_PORT}/health/db)
    http_code=$response
    
    if [ -f response_body.txt ]; then
        body=$(cat response_body.txt)
        rm -f response_body.txt
    else
        body="无法读取响应体"
    fi
    
    if [ "$http_code" -eq 200 ]; then
        log_info "健康检查成功 (HTTP $http_code)"
        echo "响应内容: $body"
    else
        log_error "健康检查失败，HTTP状态码: $http_code"
        echo "响应内容: $body"
        return 1
    fi
}

# 简化的API测试函数
test_api_endpoint() {
    local endpoint=$1
    local name=$2
    
    log_info "=== 测试${name} ==="
    
    response=$(curl -s -o response_body.txt -w "%{http_code}" "http://localhost:${APP_PORT}${endpoint}")
    http_code=$response
    
    if [ -f response_body.txt ]; then
        body=$(cat response_body.txt)
        rm -f response_body.txt
    fi
    
    if [ "$http_code" -eq 200 ]; then
        log_info "${name}测试成功 (HTTP $http_code)"
        # 简单统计数量
        count=$(echo "$body" | grep -o '"id"' | wc -l || echo "0")
        echo "返回记录数量: $count"
    else
        log_warn "${name}返回状态码: $http_code"
        echo "响应内容: $body"
    fi
}

# 网络连通性测试
test_network() {
    log_info "=== 网络连通性测试 ==="
    
    # 测试应用容器到数据库容器的连通性
    if docker exec coursehub-app ping -c 2 mysql > /dev/null 2>&1; then
        log_info "应用容器到数据库容器的网络连通性正常"
    else
        log_warn "网络连通性测试失败，但继续测试..."
    fi
}

# 查看应用日志
check_app_logs() {
    log_info "=== 查看应用日志（最后20行） ==="
    docker compose logs app --tail=20
}

# 查看健康检查详情
check_health_details() {
    log_info "=== 容器健康检查状态 ==="
    log_info "MySQL容器: $(docker inspect --format='{{.State.Health.Status}}' coursehub-mysql)"
    log_info "应用容器: $(docker inspect --format='{{.State.Health.Status}}' coursehub-app)"
}

# 主函数
main() {
    log_info "开始执行容器化测试套件"
    
    # 检查必要命令
    check_command curl || exit 1
    check_command docker || exit 1
    
    # 执行测试
    wait_for_services
    check_container_status
    check_health_details
    test_network
    test_health_check
    test_api_endpoint "/api/courses" "课程API"
    test_api_endpoint "/api/students" "学生API"
    test_api_endpoint "/api/enrollments" "选课API"
    
    check_app_logs
    
    log_info "=== 测试完成 ==="
    log_info "结束时间: $(date)"
    log_info "所有测试项目执行完毕"
}

# 执行主函数
main "$@"