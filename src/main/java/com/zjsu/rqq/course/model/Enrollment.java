package com.zjsu.rqq.course.model;

import java.time.LocalDateTime;

// 报名信息
public class Enrollment {
    private String id;
    private String courseId;
    private String studentId;
    private LocalDateTime enrolledAt;

    // 关联信息（查询时使用）
    private Course course;
    private Student student;

    // 构造方法
    public Enrollment() {
        this.enrolledAt = LocalDateTime.now();
    }

    // Getter和Setter方法
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public LocalDateTime getEnrolledAt() { return enrolledAt; }
    public void setEnrolledAt(LocalDateTime enrolledAt) { this.enrolledAt = enrolledAt; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
}
