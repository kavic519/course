package com.zjsu.rqq.course.controller;

import com.zjsu.rqq.course.model.ApiResponse;
import com.zjsu.rqq.course.model.Enrollment;
import com.zjsu.rqq.course.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Enrollment>>> getAllEnrollments() {
        List<Enrollment> enrollments = enrollmentService.getAllEnrollments();
        return ResponseEntity.ok(ApiResponse.success(enrollments));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<List<Enrollment>>> getEnrollmentsByCourseId(
            @PathVariable String courseId) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByCourseId(courseId);
        return ResponseEntity.ok(ApiResponse.success(enrollments));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<Enrollment>>> getEnrollmentsByStudentId(
            @PathVariable String studentId) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudentId(studentId);
        return ResponseEntity.ok(ApiResponse.success(enrollments));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Enrollment>> enrollStudent(
            @RequestBody Map<String, String> request) {
        try {
            String courseId = request.get("courseId");
            String studentId = request.get("studentId");

            if (courseId == null || studentId == null) {
                return ResponseEntity.status(400)
                        .body(ApiResponse.error(400, "courseId和studentId不能为空"));
            }

            Enrollment enrollment = enrollmentService.enrollStudent(courseId, studentId);
            return ResponseEntity.status(201)
                    .body(ApiResponse.success("选课成功", enrollment));
        } catch (IllegalArgumentException e) {
            int statusCode = e.getMessage().contains("不存在") ? 404 : 400;
            return ResponseEntity.status(statusCode)
                    .body(ApiResponse.error(statusCode, e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> unenrollStudent(@PathVariable String id) {
        try {
            enrollmentService.unenrollStudent(id);
            return ResponseEntity.ok(ApiResponse.noContent("退选成功"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404)
                    .body(ApiResponse.error(404, e.getMessage()));
        }
    }
}
