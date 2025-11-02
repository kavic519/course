package com.zjsu.rqq.course.service;

import com.zjsu.rqq.course.model.Course;
import com.zjsu.rqq.course.model.Enrollment;
import com.zjsu.rqq.course.model.Student;
import com.zjsu.rqq.course.repository.CourseRepository;
import com.zjsu.rqq.course.repository.EnrollmentRepository;
import com.zjsu.rqq.course.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 选课服务
@Service
@Transactional
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
//        if(true) {
//            throw new IllegalArgumentException("ddd" + enrollments.get(0).getStudent().getId());
//        }
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

    // 根据课程ID获取激活的选课记录
    public List<Enrollment> getActiveEnrollmentsByCourseId(String courseId) {
        return enrollmentRepository.findByCourseIdAndStatus(courseId, Enrollment.EnrollmentStatus.ACTIVE);
    }

    // 根据学生ID获取激活的选课记录
    public List<Enrollment> getActiveEnrollmentsByStudentId(String studentId) {
        return enrollmentRepository.findByStudentIdAndStatus(studentId, Enrollment.EnrollmentStatus.ACTIVE);
    }


    // 选课
    @Transactional
    public Enrollment enrollStudent(String courseCode, String studentId) {
        // 验证课程存在
        Course course = courseRepository.findByCode(courseCode)
                .orElseThrow(() -> new IllegalArgumentException("课程不存在: " + courseCode));

        // 验证学生存在
        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("学生不存在: " + studentId));

        // 检查是否已选课
        String courseId = course.getId();
//        if(true){
//            throw new IllegalArgumentException("courseId: "+ courseId +"   studentId: " + studentId);
//        }
        String student_Id = student.getId();

        if (enrollmentRepository.existsByCourseIdAndStudentIdAndStatus(courseId, student_Id, Enrollment.EnrollmentStatus.ACTIVE)) {
            throw new IllegalArgumentException("学生已选该课程");
        }

        // 检查课程容量
        else if (course.getEnrolled() >= course.getCapacity()) {
            throw new IllegalArgumentException("课程容量已满");
        }

        // 创建选课记录
        Enrollment enrollment = new Enrollment();
        enrollment.setCourse(course);
        enrollment.setStudent(student);
        enrollment.setStatus(Enrollment.EnrollmentStatus.ACTIVE);

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        // 更新课程选课人数
        courseService.incrementEnrolledCount(courseId);

        // 填充关联信息
        fillAssociatedInfo(savedEnrollment);

        return savedEnrollment;
    }

    // 退课
    @Transactional
    public void unenrollStudent(String enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("选课记录不存在: " + enrollmentId));

        if (enrollment.getStatus() != Enrollment.EnrollmentStatus.ACTIVE) {
            throw new IllegalArgumentException("选课记录不是活跃状态");
        }
        String courseId = enrollment.getCourse().getId();
        enrollment.setStatus(Enrollment.EnrollmentStatus.DROPPED);
        enrollmentRepository.save(enrollment);

        // 更新课程选课人数
        courseService.decrementEnrolledCount(courseId);
    }

    // 检查学生是否有选课记录
    public boolean hasStudentEnrollments(String studentId) {
        return enrollmentRepository.hasActiveEnrollmentsByStudentId(studentId);
    }

    //按课程、学生、状态组合查询
    public List<Enrollment> getEnrollmentsByCourseAndStudentAndStatus(String courseCode, String studentId, Enrollment.EnrollmentStatus status) {
        Course course = courseRepository.findByCode(courseCode)
                .orElseThrow(() -> new IllegalArgumentException("课程不存在: " + courseCode));
        String courseId = course.getId();
        String student_Id = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("学生不存在: " + studentId)).getId();

//        if(true){
//            throw new IllegalArgumentException("courseId: "+ courseId +"   studentId: " + student_Id + "   status: " + status);
//        }

        return enrollmentRepository.findByCourseIdAndStudentIdAndStatus(courseId, student_Id, status);
    }

    // 获取课程选课人数
    public int getCourseEnrollmentCount(String courseCode) {
        Course course = courseRepository.findByCode(courseCode)
                .orElseThrow(() -> new IllegalArgumentException("课程不存在: " + courseCode));
        String courseId = course.getId();
        return enrollmentRepository.countActiveByCourseId(courseId);
    }

    //判断学生是否已选课
    public boolean isStudentEnrolled(String courseCode, String studentId) {
        String course_Id = courseRepository.findByCode(courseCode)
                .orElseThrow(() -> new IllegalArgumentException("课程不存在: " + courseCode)).getId();
        String student_Id = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("学生不存在: " + studentId)).getId();
        return enrollmentRepository.existsByCourseIdAndStudentIdAndStatus(course_Id, student_Id, Enrollment.EnrollmentStatus.ACTIVE);
    }




    // 填充关联信息
    private void fillAssociatedInfo(Enrollment enrollment) {
        // 填充课程信息
        courseRepository.findById(enrollment.getCourse().getId())
                .ifPresent(enrollment::setCourse);

        // 填充学生信息
        studentRepository.findByStudentId(enrollment.getStudent().getId())
                .ifPresent(enrollment::setStudent);
    }
}
