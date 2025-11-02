package com.zjsu.rqq.course.service;

import com.zjsu.rqq.course.model.Course;
import com.zjsu.rqq.course.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

// 课程服务
@Service
@Transactional
public class CourseService {

    // 课程仓库
    @Autowired
    private CourseRepository courseRepository;

    // 获取所有课程
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // 根据ID获取课程
    public Optional<Course> getCourseById(String id) {
        return courseRepository.findById(id);
    }

    // 创建课程
    public Course createCourse(Course course) {
        // 检查课程代码是否重复
        if (courseRepository.findByCode(course.getCode()).isPresent()) {
            throw new IllegalArgumentException("课程代码已存在: " + course.getCode());
        }
        return courseRepository.save(course);
    }

    // 更新课程
    public Course updateCourse(String id, Course course) {
        if (!courseRepository.existsById(id)) {
            throw new IllegalArgumentException("课程不存在: " + id);
        }

        // 检查课程代码是否重复（排除自身）
        Optional<Course> existingCourse = courseRepository.findByCode(course.getCode());
        if (existingCourse.isPresent() && !existingCourse.get().getId().equals(id)) {
            throw new IllegalArgumentException("课程代码已存在: " + course.getCode());
        }

        course.setId(id);
        return courseRepository.save(course);
    }

    // 删除课程
    public void deleteCourse(String id) {
        if (!courseRepository.existsById(id)) {
            throw new IllegalArgumentException("课程不存在: " + id);
        }
        courseRepository.deleteById(id);
    }

    // 递增课程的已选人数
    @Transactional
    public void incrementEnrolledCount(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("课程不存在: " + courseId));
        course.setEnrolled(course.getEnrolled() + 1);
        courseRepository.save(course);
    }

    // 递减课程的已选人数
    @Transactional
    public void decrementEnrolledCount(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("课程不存在: " + courseId));
        course.setEnrolled(Math.max(0, course.getEnrolled() - 1));
        courseRepository.save(course);
    }

    // 获取所有可用课程
    public List<Course> getAvailableCourses() {
        return courseRepository.findAvailableCourses();
    }

    //根据课程代码查询课程
    public Optional<Course> getCourseByCode(String code) {
        return courseRepository.findByCode(code);
    }

    //根据讲师ID查询课程
    public List<Course> getCourseByInstructorId(String instructorId) {
        return courseRepository.findByInstructorId(instructorId);
    }

    // 根据标题搜索课程
    public List<Course> getCourseByTitle(String keyword) {
        return courseRepository.findByTitleContaining(keyword);
    }
}