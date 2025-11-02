-- 初始化学生数据
INSERT INTO students (id, student_id, name, major, grade, email) VALUES
('s001', 'S2024001', '张三', '计算机科学', 2024, 'zhangsan@edu.cn'),
('s002', 'S2024002', '李四', '软件工程', 2024, 'lisi@edu.cn'),
('s003', 'S2024003', '王五', '网络工程', 2024, 'wangwu@edu.cn');

-- 初始化课程数据
INSERT INTO courses (id, code, title, instructor_id, instructor_name, instructor_email, schedule_day, start_time, end_time, expected_attendance, capacity) VALUES
('c001', 'CS101', '计算机科学导论', 'T001', '张教授', 'zhang@edu.cn', 'MONDAY', '08:00', '10:00', 50, 60),
('c002', 'MA101', '高等数学', 'T002', '李教授', 'li@edu.cn', 'TUESDAY', '10:00', '12:00', 40, 50),
('c003', 'PH101', '大学物理', 'T003', '王教授', 'wang@edu.cn', 'WEDNESDAY', '14:00', '16:00', 45, 55);

-- 初始化选课数据
INSERT INTO enrollments (id, course_id, student_id, status) VALUES
('e001', 'c001', 's001', 'ACTIVE'),
('e002', 'c002', 's002', 'ACTIVE'),
('e003', 'c001', 's003', 'ACTIVE');