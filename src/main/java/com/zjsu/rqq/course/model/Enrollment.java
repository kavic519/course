package com.zjsu.rqq.course.model;

import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.*;
import java.time.LocalDateTime;

// 报名信息
@Entity
@Table(name = "enrollments", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"course_id", "student_id"})
})
public class Enrollment {
    public enum EnrollmentStatus {
        ACTIVE, DROPPED, COMPLETED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    // 关联信息（查询时使用）
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(nullable = false)
    private LocalDateTime enrolledAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnrollmentStatus status = EnrollmentStatus.ACTIVE;

    @PrePersist
    protected void onCreate() {
        if (enrolledAt == null) {
            enrolledAt = LocalDateTime.now();
        }
        if (status == null) {
            status = EnrollmentStatus.ACTIVE;
        }
    }

    // 关联信息（查询时使用）
    //private Course course;
    //private Student student;

    // 构造方法
    public Enrollment() {
        this.enrolledAt = LocalDateTime.now();
    }

    // Getter和Setter方法
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public LocalDateTime getEnrolledAt() { return enrolledAt; }
    public void setEnrolledAt(LocalDateTime enrolledAt) { this.enrolledAt = enrolledAt; }

    public EnrollmentStatus getStatus() { return status; }
    public void setStatus(EnrollmentStatus status) { this.status = status; }
}
