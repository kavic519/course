package com.zjsu.rqq.course.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

//讲师实体类
public class Instructor {
    @NotBlank(message = "讲师ID不能为空")
    private String id;

    @NotBlank(message = "讲师姓名不能为空")
    private String name;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    // Getter和Setter方法
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
