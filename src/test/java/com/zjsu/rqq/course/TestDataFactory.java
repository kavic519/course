package com.zjsu.rqq.course;

import com.zjsu.rqq.course.model.*;

public class TestDataFactory {

    public static Student createStudent() {
        Student student = new Student();
        student.setStudentId("S2024001");
        student.setName("张三");
        student.setMajor("计算机科学");
        student.setGrade(2024);
        student.setEmail("zhangsan@edu.cn");
        return student;
    }

    public static Student createStudent(String studentId, String name) {
        Student student = createStudent();
        student.setStudentId(studentId);
        student.setName(name);
        student.setEmail(studentId.toLowerCase() + "@edu.cn");
        return student;
    }

    public static Course createCourse() {
        Course course = new Course();
        course.setCode("CS101");
        course.setTitle("计算机科学导论");
        course.setCapacity(60);
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

        return course;
    }

    public static Course createCourse(String code, String title) {
        Course course = createCourse();
        course.setCode(code);
        course.setTitle(title);
        return course;
    }

    public static Enrollment createEnrollment(Student student, Course course) {
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setStatus(Enrollment.EnrollmentStatus.ACTIVE);
        return enrollment;
    }
}