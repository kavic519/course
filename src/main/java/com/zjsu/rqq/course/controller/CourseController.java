package com.zjsu.rqq.course.controller;

import com.zjsu.rqq.course.model.ApiResponse;
import com.zjsu.rqq.course.model.Course;
import com.zjsu.rqq.course.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 课程控制器
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    // 课程服务
    @Autowired
    private CourseService courseService;

    // 获取所有课程
    @GetMapping
    public ResponseEntity<ApiResponse<List<Course>>> getAllCourses() {
        // 调用课程服务获取所有课程
        List<Course> courses = courseService.getAllCourses();
        // 返回成功响应
        return ResponseEntity.ok(ApiResponse.success(courses));
    }

    // 获取课程详情
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Course>> getCourseById(@PathVariable String id) {
        // 调用课程服务获取课程详情
        return courseService.getCourseById(id)
                .map(course -> ResponseEntity.ok(ApiResponse.success(course)))// 返回成功响应
                .orElse(ResponseEntity.status(404)// 返回错误响应
                        .body(ApiResponse.error(404, "课程不存在")));// 返回错误消息
    }

    // 创建课程
    @PostMapping
    public ResponseEntity<ApiResponse<Course>> createCourse(@Valid @RequestBody Course course) {
        try {
            Course createdCourse = courseService.createCourse(course);// 调用课程服务创建课程
            return ResponseEntity.status(201)// 返回创建成功的响应
                    .body(ApiResponse.created("课程创建成功", createdCourse));// 返回创建成功的消息
        } catch (IllegalArgumentException e) {// 处理参数验证错误
            return ResponseEntity.status(400)// 返回错误响应
                    .body(ApiResponse.error(400, e.getMessage()));// 返回错误消息
        }
    }

    // 更新课程
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Course>> updateCourse(
            @PathVariable String id,// 课程ID
            @Valid @RequestBody Course course) {// 课程数据
        try {
            Course updatedCourse = courseService.updateCourse(id, course);// 调用课程服务更新课程
            return ResponseEntity.ok(ApiResponse.success("课程更新成功", updatedCourse));// 返回更新成功的消息
        } catch (IllegalArgumentException e) {// 处理参数验证错误
            if (e.getMessage().contains("不存在")) {// 处理课程不存在错误
                return ResponseEntity.status(404)// 返回错误响应
                        .body(ApiResponse.error(404, e.getMessage()));// 返回错误消息
            }
            return ResponseEntity.status(400)// 返回错误响应
                    .body(ApiResponse.error(400, e.getMessage()));// 返回错误消息
        }
    }

    // 删除课程
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(@PathVariable String id) {
        try {
            courseService.deleteCourse(id);// 调用课程服务删除课程
            return ResponseEntity.ok(ApiResponse.noContent("课程删除成功"));// 返回删除成功的消息
        } catch (IllegalArgumentException e) {// 处理课程不存在错误
            return ResponseEntity.status(404)// 返回错误响应
                    .body(ApiResponse.error(404, e.getMessage()));// 返回错误消息
        }
    }
}
