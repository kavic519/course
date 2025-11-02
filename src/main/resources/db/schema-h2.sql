-- 设置字符集
SET MODE MySQL;

-- 删除现有表（如果存在）
DROP TABLE IF EXISTS enrollments;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS students;

-- 创建学生表
CREATE TABLE students (
    id VARCHAR(36) PRIMARY KEY,
    student_id VARCHAR(8) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    major VARCHAR(100) NOT NULL,
    grade INT NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 其他表结构保持不变...