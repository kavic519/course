package com.zjsu.rqq.course.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

// 学生实体类
@Entity
@Table(name = "students", uniqueConstraints = {
        @UniqueConstraint(columnNames = "studentId"),
        @UniqueConstraint(columnNames = "email")
})
@EntityListeners(AuditingEntityListener.class)
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false,unique = true)
    @NotBlank(message = "学号不能为空")
    @Pattern(regexp = "^S\\d{7}$", message = "学号格式必须为S+7位数字")
    private String studentId;

    @Column(nullable = false)
    @NotBlank(message = "学生姓名不能为空")
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "专业不能为空")
    private String major;

    @Column(nullable = false)
    @NotNull(message = "入学年份不能为空")
    private Integer grade;

    @Column(nullable = false)
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }


    // 构造方法
//    public Student() {
//        this.createdAt = LocalDateTime.now();
//    }

    // Getter和Setter方法
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }

    public Integer getGrade() { return grade; }
    public void setGrade(Integer grade) { this.grade = grade; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
