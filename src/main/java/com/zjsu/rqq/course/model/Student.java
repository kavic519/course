package com.zjsu.rqq.course.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

// 学生实体类
public class Student {
    private String id;

    @NotBlank(message = "学号不能为空")
    @Pattern(regexp = "^S\\d{7}$", message = "学号格式必须为S+7位数字")
    private String studentId;

    @NotBlank(message = "学生姓名不能为空")
    private String name;

    @NotBlank(message = "专业不能为空")
    private String major;

    @NotNull(message = "入学年份不能为空")
    private Integer grade;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    private LocalDateTime createdAt;

    // 构造方法
    public Student() {
        this.createdAt = LocalDateTime.now();
    }

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
