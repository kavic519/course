package com.zjsu.rqq.course.repository;

import com.zjsu.rqq.course.model.Course;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// 课程仓库
@Repository
public class CourseRepository {
    // 课程集合
    private final Map<String, Course> courses = new ConcurrentHashMap<>();

    //查找所有课程
    public List<Course> findAll() {
        return new ArrayList<>(courses.values());
    }

    // 根据ID查找课程
    public Optional<Course> findById(String id) {
        return Optional.ofNullable(courses.get(id));
    }

    // 根据课程代码查找课程
    public Optional<Course> findByCode(String code) {
        return courses.values().stream()
                .filter(course -> course.getCode().equals(code))
                .findFirst();
    }

    // 保存课程
    public Course save(Course course) {
        if (course.getId() == null) {
            course.setId(UUID.randomUUID().toString());
        }
        course.setUpdatedAt(java.time.LocalDateTime.now());
        courses.put(course.getId(), course);
        return course;
    }

    // 删除课程
    public void deleteById(String id) {
        courses.remove(id);
    }

    // 判断课程是否存在
    public boolean existsById(String id) {
        return courses.containsKey(id);
    }
}
