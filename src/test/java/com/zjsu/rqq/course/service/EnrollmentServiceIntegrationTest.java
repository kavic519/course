package com.zjsu.rqq.course.service;

import com.zjsu.rqq.course.model.*;
import com.zjsu.rqq.course.repository.CourseRepository;
import com.zjsu.rqq.course.repository.EnrollmentRepository;
import com.zjsu.rqq.course.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class EnrollmentServiceIntegrationTest {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Test
    public void whenEnrollStudent_thenSuccess() {
        // given
        Student student = new Student();
        student.setStudentId("S2024001");
        student.setName("张三");
        student.setMajor("计算机科学");
        student.setGrade(2024);
        student.setEmail("zhangsan@edu.cn");
        Student savedStudent = studentRepository.save(student);

        Course course = new Course();
        course.setCode("CS101");
        course.setTitle("计算机科学导论");
        course.setCapacity(10);
        course.setEnrolled(0);

        // 设置讲师信息
        Instructor instructor = new Instructor();
        instructor.setId("T001");
        instructor.setName("张教授");
        instructor.setEmail("zhang@edu.cn");
        course.setInstructor(instructor);

        // 设置课程安排
        ScheduleSlot schedule = new ScheduleSlot();
        schedule.setDayOfWeek(ScheduleSlot.DayOfWeek.MONDAY);
        schedule.setStartTime("08:00");
        schedule.setEndTime("10:00");
        schedule.setExpectedAttendance(50);
        course.setSchedule(schedule);

        Course savedCourse = courseRepository.save(course);

        // when
        Enrollment enrollment = enrollmentService.enrollStudent(savedCourse.getCode(), savedStudent.getStudentId());

        // then
        assertThat(enrollment).isNotNull();
        assertThat(enrollment.getStudent().getId()).isEqualTo(savedStudent.getId());
        assertThat(enrollment.getCourse().getId()).isEqualTo(savedCourse.getId());
        assertThat(enrollment.getStatus()).isEqualTo(Enrollment.EnrollmentStatus.ACTIVE);

        // verify course enrolled count is updated
        Course updatedCourse = courseRepository.findById(savedCourse.getId()).orElseThrow();
        assertThat(updatedCourse.getEnrolled()).isEqualTo(1);
    }
}