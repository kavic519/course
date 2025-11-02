# 课程管理系统

## 1. 项目说明

这是一个基于Spring Boot构建的课程管理系统，提供完整的课程管理、学生管理和选课功能。系统采用单体架构，使用内存存储数据，适合教学演示和小型应用场景。

### 主要功能

- **课程管理**: 创建、查询、更新、删除课程信息
- **学生管理**: 学生信息的增删改查
- **选课管理**: 学生选课、退课、课程容量控制
- **数据验证**: 完整的参数验证和错误处理
- **关联关系**: 课程、学生、选课记录之间的完整关联

### 技术栈

- **后端框架**: Spring Boot 3.x
- **数据存储**: 内存存储（ConcurrentHashMap）
- **数据验证**: Jakarta Validation
- **API文档**: 提供完整的HTTP测试用例

## 2. 如何运行项目

### 环境要求
- Java 17 或更高版本

- Maven 3.6 或更高版本

### 运行步骤

#### 1.克隆项目
```
git clone <项目地址>
cd course-management-system
```

#### 2.编译项目
```
mvn clean compile
```

#### 3.运行项目
```
mvn spring-boot:run
```

#### 4.验证启动
```
服务启动后，访问: http://localhost:8080
```

### 项目结构
```
src/main/java/com/zjsu/rqq/course/
├── model/          # 数据模型
├── repository/     # 数据访问层
├── service/        # 业务逻辑层
├── controller/     # 控制层
└── exception/      # 异常处理
```

## 3. API接口列表


### 课程管理

| 方法   | 端点                | 描述           | 状态码        |
|--------|---------------------|----------------|---------------|
| GET    | `/api/courses`        | 获取所有课程   | 200           |
| GET    | `/api/courses/{id}`   | 根据ID获取课程 | 200,404       |
| POST   | `/api/courses`        | 创建课程       | 201,400       |
| PUT    | `/api/courses/{id}`   | 更新课程       | 200,400,404   |
| DELETE | `/api/courses/{id}`   | 删除课程       | 204,404       |

### 学生管理

| 方法   | 端点                 | 描述           | 状态码        |
|--------|----------------------|----------------|---------------|
| GET    | `/api/students`        | 获取所有学生   | 200           |
| GET    | `/api/students/{id}`   | 根据ID获取学生 | 200,404       |
| POST   | `/api/students`        | 创建学生       | 201,400       |
| PUT    | `/api/students/{id}`   | 更新学生       | 200,400,404   |
| DELETE | `/api/students/{id}`   | 删除学生       | 204,400,404   |

### 选课管理


| 方法   | 端点                                  | 描述             | 状态码        |
|--------|---------------------------------------|------------------|---------------|
| GET    | `/api/enrollments`                      | 获取所有选课记录 | 200           |
| GET    | `/api/enrollments/course/{courseId}`    | 根据课程获取选课 | 200           |
| GET    | `/api/enrollments/student/{studentId}`  | 根据学生获取选课 | 200           |
| POST   | `/api/enrollments`                      | 学生选课         | 201,400,404   |
| DELETE | `/api/enrollments/{id}`                 | 学生退课         | 204,404       |
| DELETE | `/api/students/{id}`                    | 删除学生         | 204,400,404   |


## 4. 测试说明

### 测试文件

项目提供了完整的HTTP测试文件 `test-api.http`，包含以下测试场景：

#### 测试场景1：完整的课程管理流程

- 创建多个课程
- 查询所有课程
- 根据ID查询课程
- 更新课程信息
- 删除课程

#### 测试场景2：选课业务流程

- 创建容量有限的课程
- 创建多个学生
- 学生选课（正常、容量已满、重复选课）
- 验证课程已选人数更新

#### 测试场景3：学生管理流程

- 创建学生
- 查询学生
- 更新学生信息
- 删除学生（有选课记录和没有选课记录）

#### 测试场景4：错误处理

- 查询不存在的资源
- 参数验证失败
- 重复学号
- 无效邮箱格式

#### 运行测试
###### 使用IntelliJ IDEA HTTP Client
- 直接打开 test-api.http 文件

- 点击每个请求旁的运行按钮

#### 测试数据示例
###### 创建课程
```
POST http://localhost:8080/api/courses
Content-Type: application/json

{
  "code": "CS101",
  "title": "计算机科学导论",
  "instructor": {
    "id": "T001",
    "name": "张教授",
    "email": "zhang@example.edu.cn"
  },
  "schedule": {
    "dayOfWeek": "MONDAY",
    "startTime": "08:00",
    "endTime": "10:00",
    "expectedAttendance": 50
  },
  "capacity": 60
}
```

###### 学生选课
```
POST http://localhost:8080/api/enrollments
Content-Type: application/json

{
  "courseId": "课程ID",
  "studentId": "学生学号"
}
```

### 注意事项
- 系统使用内存存储，重启后数据会丢失

- 所有ID均为UUID自动生成




