package com.zjsu.rqq.course.service;

import com.zjsu.rqq.course.model.Student;
import com.zjsu.rqq.course.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

// 学生服务
@Service
@Transactional
public class StudentService {
//    private final StudentRepository studentRepository;
//
//    public StudentService(StudentRepository studentRepository) {
//        this.studentRepository = studentRepository;
//    }

    // 学生仓库
    @Autowired
    private StudentRepository studentRepository;

    // 获取所有学生
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // 根据ID获取学生
    public Optional<Student> getStudentById(String id) {
        return studentRepository.findById(id);
    }

    // 根据学号获取学生
    public Optional<Student> getStudentByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId);
    }

    // 根据邮箱获取学生
    public Optional<Student> getStudentByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    // 创建学生
    public Student createStudent(Student student) {
        if (studentRepository.existsByStudentId(student.getStudentId())){
            throw new IllegalArgumentException("学号已存在: " + student.getStudentId());
        }
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new IllegalArgumentException("邮箱已存在: " + student.getEmail());
        }

        return studentRepository.save(student);
    }

    // 更新学生
    public Student updateStudent(String id, Student student) {
        if (!studentRepository.existsById(id)) {
            throw new IllegalArgumentException("学生不存在: " + id);
        }
        if (studentRepository.existsByStudentIdAndIdNot(student.getStudentId(), id)) {
            throw new IllegalArgumentException("学号已存在: " + student.getStudentId());
        }
        if (studentRepository.existsByEmailAndIdNot(student.getEmail(), id)) {
            throw new IllegalArgumentException("邮箱已存在: " + student.getEmail());
        }

        student.setId(id);
        return studentRepository.save(student);
    }

    // 删除学生
    public void deleteStudent(String id) {
        if (!studentRepository.existsById(id)) {
            throw new IllegalArgumentException("学生不存在: " + id);
        }
        studentRepository.deleteById(id);
    }

    // 判断学生是否存在
    public boolean existsById(String id) {
        return studentRepository.existsById(id);
    }

    // 根据专业获取学生
    public List<Student> getStudentsByMajor(String major) {
        return studentRepository.findByMajor(major);
    }

    // 根据年级获取学生
    public List<Student> getStudentsByGrade(Integer grade) {
        return studentRepository.findByGrade(grade);
    }

    //判重检查
    public boolean existsByStudentId(String studentId) {
        return studentRepository.existsByStudentId(studentId);
    }
}
