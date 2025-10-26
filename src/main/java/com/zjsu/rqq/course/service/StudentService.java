package com.zjsu.rqq.course.service;

import com.zjsu.rqq.course.model.Student;
import com.zjsu.rqq.course.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// 学生服务
@Service
public class StudentService {

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

    // 创建学生
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    // 更新学生
    public Student updateStudent(String id, Student student) {
        if (!studentRepository.existsById(id)) {
            throw new IllegalArgumentException("学生不存在: " + id);
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
}
