package com.zjsu.rqq.course.service;

import com.zjsu.rqq.course.model.Course;
import com.zjsu.rqq.course.model.Enrollment;
import com.zjsu.rqq.course.model.Student;
import com.zjsu.rqq.course.repository.CourseRepository;
import com.zjsu.rqq.course.repository.EnrollmentRepository;
import com.zjsu.rqq.course.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// 选课服务
@Service
public class EnrollmentService {

    // 选课仓库
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    // 课程仓库
    @Autowired
    private CourseRepository courseRepository;

    // 学生仓库
    @Autowired
    private StudentRepository studentRepository;

    // 课程服务
    @Autowired
    private CourseService courseService;

    // 获取所有选课记录
    public List<Enrollment> getAllEnrollments() {
        List<Enrollment> enrollments = enrollmentRepository.findAll();
        // 填充关联信息
        enrollments.forEach(this::fillAssociatedInfo);
        return enrollments;
    }

    // 根据课程ID获取选课记录
    public List<Enrollment> getEnrollmentsByCourseId(String courseId) {
        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);
        enrollments.forEach(this::fillAssociatedInfo);
        return enrollments;
    }

    // 根据学生ID获取选课记录
    public List<Enrollment> getEnrollmentsByStudentId(String studentId) {
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);
        enrollments.forEach(this::fillAssociatedInfo);
        return enrollments;
    }

    // 选课
    public Enrollment enrollStudent(String courseId, String studentId) {
        // 验证课程存在
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("课程不存在: " + courseId));

        // 验证学生存在
        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("学生不存在: " + studentId));

        // 检查是否已选课
        if (enrollmentRepository.existsByCourseIdAndStudentId(courseId, studentId)) {
            throw new IllegalArgumentException("学生已选该课程");
        }

        // 检查课程容量
        if (course.getEnrolled() >= course.getCapacity()) {
            throw new IllegalArgumentException("课程容量已满");
        }

        // 创建选课记录
        Enrollment enrollment = new Enrollment();
        enrollment.setCourseId(courseId);
        enrollment.setStudentId(studentId);

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        // 更新课程选课人数
        courseService.incrementEnrolledCount(courseId);

        // 填充关联信息
        fillAssociatedInfo(savedEnrollment);

        return savedEnrollment;
    }

    // 退课
    public void unenrollStudent(String enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("选课记录不存在: " + enrollmentId));

        String courseId = enrollment.getCourseId();
        enrollmentRepository.deleteById(enrollmentId);

        // 更新课程选课人数
        courseService.decrementEnrolledCount(courseId);
    }

    // 检查学生是否有选课记录
    public boolean hasStudentEnrollments(String studentId) {
        return enrollmentRepository.hasEnrollmentsByStudentId(studentId);
    }

    // 填充关联信息
    private void fillAssociatedInfo(Enrollment enrollment) {
        // 填充课程信息
        courseRepository.findById(enrollment.getCourseId())
                .ifPresent(enrollment::setCourse);

        // 填充学生信息
        studentRepository.findByStudentId(enrollment.getStudentId())
                .ifPresent(enrollment::setStudent);
    }
}
