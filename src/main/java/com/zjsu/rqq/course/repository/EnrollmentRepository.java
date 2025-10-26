package com.zjsu.rqq.course.repository;

import com.zjsu.rqq.course.model.Enrollment;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

// 选课仓库
@Repository
public class EnrollmentRepository {
    // 选课集合
    private final Map<String, Enrollment> enrollments = new ConcurrentHashMap<>();
    // 课程-选课关系
    private final Map<String, Set<String>> courseEnrollments = new ConcurrentHashMap<>();
    // 学生-选课关系
    private final Map<String, Set<String>> studentEnrollments = new ConcurrentHashMap<>();

    // 查找所有选课
    public List<Enrollment> findAll() {
        return new ArrayList<>(enrollments.values());
    }

    // 根据ID查找选课
    public Optional<Enrollment> findById(String id) {
        return Optional.ofNullable(enrollments.get(id));
    }

    // 根据课程ID查找选课
    public List<Enrollment> findByCourseId(String courseId) {
        Set<String> enrollmentIds = courseEnrollments.getOrDefault(courseId, Collections.emptySet());
        return enrollmentIds.stream()
                .map(enrollments::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    // 根据学生ID查找选课
    public List<Enrollment> findByStudentId(String studentId) {
        // 根据学号查找ID
        Set<String> enrollmentIds = studentEnrollments.getOrDefault(studentId, Collections.emptySet());
        // 根据ID查找选课
        return enrollmentIds.stream()
                .map(enrollments::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    // 根据课程ID和学生ID查找选课
    public boolean existsByCourseIdAndStudentId(String courseId, String studentId) {
        // 根据学号查找ID
        Set<String> studentEnrollmentIds = studentEnrollments.getOrDefault(studentId, Collections.emptySet());
        // 根据ID查找选课
        return studentEnrollmentIds.stream()
                .map(enrollments::get)
                .filter(Objects::nonNull)
                .anyMatch(enrollment -> enrollment.getCourseId().equals(courseId));
    }

    // 保存选课
    public Enrollment save(Enrollment enrollment) {
        if (enrollment.getId() == null) {
            enrollment.setId(UUID.randomUUID().toString());
        }

        // 保存选课
        enrollments.put(enrollment.getId(), enrollment);

        // 更新课程-选课关系
        courseEnrollments.computeIfAbsent(enrollment.getCourseId(), k -> new HashSet<>())
                .add(enrollment.getId());

        // 更新学生-选课关系
        studentEnrollments.computeIfAbsent(enrollment.getStudentId(), k -> new HashSet<>())
                .add(enrollment.getId());

        return enrollment;
    }

    // 删除选课
    public void deleteById(String id) {
        Enrollment enrollment = enrollments.get(id);
        if (enrollment != null) {
            // 从课程-选课关系中移除
            Set<String> courseEnrollmentSet = courseEnrollments.get(enrollment.getCourseId());
            if (courseEnrollmentSet != null) {
                courseEnrollmentSet.remove(id);
                if (courseEnrollmentSet.isEmpty()) {
                    courseEnrollments.remove(enrollment.getCourseId());
                }
            }

            // 从学生-选课关系中移除
            Set<String> studentEnrollmentSet = studentEnrollments.get(enrollment.getStudentId());
            if (studentEnrollmentSet != null) {
                studentEnrollmentSet.remove(id);
                if (studentEnrollmentSet.isEmpty()) {
                    studentEnrollments.remove(enrollment.getStudentId());
                }
            }

            enrollments.remove(id);
        }
    }

    // 根据课程ID统计选课数量
    public int countByCourseId(String courseId) {
        Set<String> enrollmentIds = courseEnrollments.getOrDefault(courseId, Collections.emptySet());
        return enrollmentIds.size();
    }

    // 根据学生ID判断是否已选课
    public boolean hasEnrollmentsByStudentId(String studentId) {
        Set<String> enrollmentIds = studentEnrollments.getOrDefault(studentId, Collections.emptySet());
        return !enrollmentIds.isEmpty();
    }
}
