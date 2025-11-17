#!/bin/bash

# 创建学生测试脚本
set -e

echo "=== 学生管理功能测试 ==="
echo "开始时间: $(date)"

# 配置
APP_PORT=8081
BASE_URL="http://localhost:${APP_PORT}/api/students"

# 颜色定义
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m'

log_info() { echo -e "${BLUE}[INFO]${NC} $1"; }
log_success() { echo -e "${GREEN}[SUCCESS]${NC} $1"; }
log_warn() { echo -e "${YELLOW}[WARN]${NC} $1"; }
log_error() { echo -e "${RED}[ERROR]${NC} $1"; }

# 生成随机学号
generate_student_id() {
    echo "S$(date +%Y)$(shuf -i 1000-9999 -n 1)"
}

# 生成随机邮箱
generate_email() {
    local name=$1
    local random=$(shuf -i 100-999 -n 1)
    echo "${name}${random}@student.edu"
}

# 测试数据
declare -A TEST_STUDENTS=(
    ["valid_1"]='{
        "studentId": "S2024001",
        "name": "张三",
        "major": "计算机科学",
        "grade": 2024,
        "email": "zhangsan@student.edu"
    }'
    ["valid_2"]='{
        "studentId": "S2024002", 
        "name": "李四",
        "major": "软件工程",
        "grade": 2024,
        "email": "lisi@student.edu"
    }'
    ["valid_3"]='{
        "studentId": "S2023001",
        "name": "王五",
        "major": "人工智能",
        "grade": 2023,
        "email": "wangwu@student.edu"
    }'
    ["valid_4"]='{
        "studentId": "S2024003",
        "name": "赵六",
        "major": "数据科学",
        "grade": 2024,
        "email": "zhaoliu@student.edu"
    }'
    ["valid_5"]='{
        "studentId": "S2024004",
        "name": "钱七",
        "major": "网络安全",
        "grade": 2024,
        "email": "qianqi@student.edu"
    }'
)

# 无效数据测试
declare -A INVALID_STUDENTS=(
    ["invalid_id_format"]='{
        "studentId": "A1234567",
        "name": "测试学生",
        "major": "计算机科学",
        "grade": 2024,
        "email": "test1@student.edu"
    }'
    ["invalid_email"]='{
        "studentId": "S2024999",
        "name": "测试学生",
        "major": "计算机科学", 
        "grade": 2024,
        "email": "invalid-email"
    }'
    ["missing_name"]='{
        "studentId": "S2024998",
        "name": "",
        "major": "计算机科学",
        "grade": 2024,
        "email": "test2@student.edu"
    }'
    ["missing_major"]='{
        "studentId": "S2024997",
        "name": "测试学生",
        "major": "",
        "grade": 2024,
        "email": "test3@student.edu"
    }'
)

# 检查服务是否可用
check_service_availability() {
    log_info "检查服务可用性..."
    if curl -s http://localhost:${APP_PORT}/health/db > /dev/null; then
        log_success "服务可用"
        return 0
    else
        log_error "服务不可用，请先启动服务"
        exit 1
    fi
}

# 创建单个学生
create_student() {
    local student_data="$1"
    local test_name="$2"
    
    log_info "创建学生: $test_name"
    
    response=$(curl -s -w "\n%{http_code}" -X POST "${BASE_URL}" \
        -H "Content-Type: application/json" \
        -d "$student_data")
    
    http_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | head -n -1)
    
    if [ "$http_code" -eq 201 ]; then
        log_success "✓ 创建成功"
        student_id=$(echo "$body" | grep -o '"studentId":"[^"]*"' | cut -d'"' -f4)
        echo "   学号: $student_id"
        return 0
    else
        log_warn "✗ 创建失败 (HTTP $http_code)"
        echo "   错误信息: $body"
        return 1
    fi
}

# 批量创建有效学生
create_valid_students() {
    log_info "=== 测试1: 创建有效学生数据 ==="
    local success_count=0
    local total_count=0
    
    for student_key in "${!TEST_STUDENTS[@]}"; do
        if create_student "${TEST_STUDENTS[$student_key]}" "$student_key"; then
            success_count=$((success_count + 1))
        fi
        total_count=$((total_count + 1))
        echo
    done
    
    log_info "有效学生创建结果: $success_count/$total_count 成功"
    echo
}

# 测试无效数据验证
test_invalid_data() {
    log_info "=== 测试2: 测试数据验证 ==="
    local expected_fail_count=0
    local actual_fail_count=0
    
    for student_key in "${!INVALID_STUDENTS[@]}"; do
        log_info "测试: $student_key"
        
        response=$(curl -s -w "\n%{http_code}" -X POST "${BASE_URL}" \
            -H "Content-Type: application/json" \
            -d "${INVALID_STUDENTS[$student_key]}")
        
        http_code=$(echo "$response" | tail -n1)
        
        if [ "$http_code" -ne 201 ]; then
            log_success "✓ 验证正确 (期望失败，实际失败)"
            actual_fail_count=$((actual_fail_count + 1))
        else
            log_error "✗ 验证错误 (期望失败，实际成功)"
        fi
        expected_fail_count=$((expected_fail_count + 1))
        echo
    done
    
    log_info "数据验证测试: $actual_fail_count/$expected_fail_count 通过"
    echo
}

# 测试学号重复
test_duplicate_student_id() {
    log_info "=== 测试3: 测试学号重复验证 ==="
    
    local duplicate_student='{
        "studentId": "S2024001",
        "name": "重复学生",
        "major": "计算机科学",
        "grade": 2024,
        "email": "duplicate@student.edu"
    }'
    
    log_info "尝试创建学号重复的学生..."
    response=$(curl -s -w "\n%{http_code}" -X POST "${BASE_URL}" \
        -H "Content-Type: application/json" \
        -d "$duplicate_student")
    
    http_code=$(echo "$response" | tail -n1)
    
    if [ "$http_code" -eq 400 ]; then
        log_success "✓ 学号重复验证正确"
        echo "   错误信息: $(echo "$response" | head -n -1)"
    else
        log_error "✗ 学号重复验证失败"
    fi
    echo
}

# 测试邮箱重复
test_duplicate_email() {
    log_info "=== 测试4: 测试邮箱重复验证 ==="
    
    local duplicate_email_student='{
        "studentId": "S2024999",
        "name": "邮箱重复学生",
        "major": "计算机科学",
        "grade": 2024,
        "email": "zhangsan@student.edu"
    }'
    
    log_info "尝试创建邮箱重复的学生..."
    response=$(curl -s -w "\n%{http_code}" -X POST "${BASE_URL}" \
        -H "Content-Type: application/json" \
        -d "$duplicate_email_student")
    
    http_code=$(echo "$response" | tail -n1)
    
    if [ "$http_code" -eq 400 ]; then
        log_success "✓ 邮箱重复验证正确"
        echo "   错误信息: $(echo "$response" | head -n -1)"
    else
        log_error "✗ 邮箱重复验证失败"
    fi
    echo
}

# 查询学生列表
query_students() {
    log_info "=== 测试5: 查询学生列表 ==="
    
    log_info "获取所有学生..."
    response=$(curl -s -w "\n%{http_code}" "${BASE_URL}")
    http_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | head -n -1)
    
    if [ "$http_code" -eq 200 ]; then
        student_count=$(echo "$body" | grep -o '"studentId"' | wc -l)
        log_success "✓ 查询成功"
        echo "   学生总数: $student_count"
        
        # 显示前几个学生信息
        echo "   前3个学生:"
        echo "$body" | grep -o '"studentId":"[^"]*","name":"[^"]*"' | head -3 | while read line; do
            echo "     - $line"
        done
    else
        log_error "✗ 查询失败"
    fi
    echo
}

# 按条件查询学生
query_students_by_condition() {
    log_info "=== 测试6: 条件查询测试 ==="
    
    # 按专业查询
    log_info "按专业查询: 计算机科学"
    response=$(curl -s -w "\n%{http_code}" "${BASE_URL}?major=计算机科学")
    http_code=$(echo "$response" | tail -n1)
    if [ "$http_code" -eq 200 ]; then
        count=$(echo "$response" | head -n -1 | grep -o '"studentId"' | wc -l)
        log_success "✓ 找到 $count 个计算机科学专业学生"
    fi
    
    # 按学号查询
    log_info "按学号查询: S2024001"
    response=$(curl -s -w "\n%{http_code}" "${BASE_URL}?studentid=S2024001")
    http_code=$(echo "$response" | tail -n1)
    if [ "$http_code" -eq 200 ]; then
        log_success "✓ 学号查询成功"
    fi
    
    # 按年级查询
    log_info "按年级查询: 2024"
    response=$(curl -s -w "\n%{http_code}" "${BASE_URL}?grade=2024")
    http_code=$(echo "$response" | tail -n1)
    if [ "$http_code" -eq 200 ]; then
        count=$(echo "$response" | head -n -1 | grep -o '"studentId"' | wc -l)
        log_success "✓ 找到 $count 个2024年级学生"
    fi
    echo
}

# 创建随机学生数据（压力测试）
create_random_students() {
    local count=${1:-5}
    log_info "=== 测试7: 创建 $count 个随机学生 ==="
    
    local success_count=0
    for i in $(seq 1 $count); do
        student_id=$(generate_student_id)
        name="随机学生$i"
        email=$(generate_email "student$i")
        
        random_student=$(cat <<EOF
{
    "studentId": "$student_id",
    "name": "$name",
    "major": "测试专业",
    "grade": 2024,
    "email": "$email"
}
EOF
)
        if create_student "$random_student" "随机学生$i" > /dev/null; then
            success_count=$((success_count + 1))
        fi
    done
    
    log_info "随机学生创建: $success_count/$count 成功"
    echo
}

# 测试总结
test_summary() {
    echo
    log_info "=== 学生管理功能测试总结 ==="
    log_success "✅ 所有测试完成"
    log_info "访问以下地址查看学生列表:"
    echo "   ${BASE_URL}"
    echo
    log_info "测试时间: $(date)"
}

# 主函数
main() {
    check_service_availability
    create_valid_students
    test_invalid_data
    test_duplicate_student_id
    test_duplicate_email
    query_students
    query_students_by_condition
    create_random_students 3
    test_summary
}

# 执行主函数
main "$@"