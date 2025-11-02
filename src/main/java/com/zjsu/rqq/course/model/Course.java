package com.zjsu.rqq.course.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

//课程实体类
@Entity
@Table(name = "courses", uniqueConstraints = {
        @UniqueConstraint(columnNames = "code")
})
@EntityListeners(AuditingEntityListener.class)
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false,unique = true)
    @NotBlank(message = "课程代码不能为空")
    private String code;

    @Column(nullable = false)
    @NotBlank(message = "课程名称不能为空")
    private String title;

    @Embedded
    @Valid
    @NotNull(message = "讲师信息不能为空")
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "instructor_id")),
            @AttributeOverride(name = "name", column = @Column(name = "instructor_name")),
            @AttributeOverride(name = "email", column = @Column(name = "instructor_email"))
    })
    private Instructor instructor;


    @Embedded
    @Valid
    @NotNull(message = "课程安排不能为空")
    @AttributeOverrides({
            @AttributeOverride(name = "dayOfWeek", column = @Column(name = "schedule_day", nullable = false)),
            @AttributeOverride(name = "startTime", column = @Column(name = "start_time", nullable = false)),
            @AttributeOverride(name = "endTime", column = @Column(name = "end_time", nullable = false)),
            @AttributeOverride(name = "expectedAttendance", column = @Column(name = "expected_attendance"))
    })

    private ScheduleSlot schedule;

    @Column(nullable = false)
    @Positive(message = "课程容量必须大于0")
    private Integer capacity;

    @Column(nullable = false)
    private Integer enrolled = 0;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
        if (enrolled == null) {
            enrolled = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // 构造方法
//    public Course() {
//        this.createdAt = LocalDateTime.now();
//        this.updatedAt = LocalDateTime.now();
//    }

    // Getter和Setter方法
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Instructor getInstructor() { return instructor; }
    public void setInstructor(Instructor instructor) { this.instructor = instructor; }

    public ScheduleSlot getSchedule() { return schedule; }
    public void setSchedule(ScheduleSlot schedule) { this.schedule = schedule; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public Integer getEnrolled() { return enrolled; }
    public void setEnrolled(Integer enrolled) { this.enrolled = enrolled; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
