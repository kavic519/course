
CREATE TABLE IF NOT EXISTS students (
    id VARCHAR(36) PRIMARY KEY,
    student_id VARCHAR(8) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    major VARCHAR(100) NOT NULL,
    grade INT NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_student_id (student_id),
    INDEX idx_email (email)
);

CREATE TABLE IF NOT EXISTS courses (
    id VARCHAR(36) PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    title VARCHAR(200) NOT NULL,
    instructor_id VARCHAR(100) NOT NULL,
    instructor_name VARCHAR(100) NOT NULL,
    instructor_email VARCHAR(100) NOT NULL,
    schedule_day VARCHAR(20) NOT NULL DEFAULT 'MONDAY',
    start_time VARCHAR(5) NOT NULL DEFAULT '08:00',
    end_time VARCHAR(5) NOT NULL DEFAULT '10:00',
    expected_attendance INT DEFAULT 0,
    capacity INT NOT NULL,
    enrolled INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
+
);

CREATE TABLE IF NOT EXISTS enrollments (
    id VARCHAR(36) PRIMARY KEY,
    course_id VARCHAR(36) NOT NULL,
    student_id VARCHAR(36) NOT NULL,
    enrolled_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    UNIQUE KEY uk_course_student_active (course_id, student_id, status),
    INDEX idx_course_id (course_id),
    INDEX idx_student_id (student_id)
);
