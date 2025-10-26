package com.zjsu.rqq.course.repository;

import com.zjsu.rqq.course.model.Student;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// 学生仓库
@Repository
public class StudentRepository {
    // 学生集合
    private final Map<String, Student> students = new ConcurrentHashMap<>();
    // 学号到ID的映射
    private final Map<String, String> studentIdToIdMap = new ConcurrentHashMap<>();

    // 查找所有学生
    public List<Student> findAll() {
        return new ArrayList<>(students.values());
    }

    // 根据ID查找学生
    public Optional<Student> findById(String id) {
        return Optional.ofNullable(students.get(id));
    }

    // 根据学号查找学生
    public Optional<Student> findByStudentId(String studentId) {
        // 根据学号查找ID
        String id = studentIdToIdMap.get(studentId);
        if (id != null) {
            return Optional.ofNullable(students.get(id));
        }
        return Optional.empty();
    }

    // 保存学生
    public Student save(Student student) {
        // 生成ID
        if (student.getId() == null) {
            // 使用UUID生成ID
            student.setId(UUID.randomUUID().toString());
        }

        // 检查学号是否重复
        if (studentIdToIdMap.containsKey(student.getStudentId())) {
            String existingId = studentIdToIdMap.get(student.getStudentId());
            // 如果ID不一致，则抛出异常
            if (!existingId.equals(student.getId())) {
                throw new IllegalArgumentException("学号已存在: " + student.getStudentId());
            }
        }
        // 保存学生
        students.put(student.getId(), student);
        studentIdToIdMap.put(student.getStudentId(), student.getId());
        return student;
    }

    // 删除学生
    public void deleteById(String id) {
        Student student = students.get(id);
        if (student != null) {
            studentIdToIdMap.remove(student.getStudentId());
            students.remove(id);
        }
    }

    // 判断学生是否存在
    public boolean existsById(String id) {
        return students.containsKey(id);
    }

    // 判断学号是否存在
    public boolean existsByStudentId(String studentId) {
        return studentIdToIdMap.containsKey(studentId);
    }
}