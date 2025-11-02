package com.zjsu.rqq.course.service;

import com.zjsu.rqq.course.TestDataFactory;
import com.zjsu.rqq.course.model.Student;
import com.zjsu.rqq.course.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student;

    @BeforeEach
    public void setUp() {
        student = TestDataFactory.createStudent();
    }

    @Test
    public void whenGetAllStudents_thenReturnStudentList() {
        // given
        Student student2 = TestDataFactory.createStudent("S2024002", "李四");
        List<Student> expectedStudents = Arrays.asList(student, student2);

        when(studentRepository.findAll()).thenReturn(expectedStudents);

        // when
        List<Student> actualStudents = studentService.getAllStudents();

        // then
        assertThat(actualStudents).hasSize(2);
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    public void whenGetStudentById_thenReturnStudent() {
        // given
        when(studentRepository.findById("1")).thenReturn(Optional.of(student));

        // when
        Optional<Student> found = studentService.getStudentById("1");

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("张三");
    }

    @Test
    public void whenCreateStudent_thenReturnSavedStudent() {
        // given
        when(studentRepository.existsByStudentId(student.getStudentId())).thenReturn(false);
        when(studentRepository.existsByEmail(student.getEmail())).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        // when
        Student saved = studentService.createStudent(student);

        // then
        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("张三");
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    public void whenCreateStudentWithDuplicateStudentId_thenThrowException() {
        // given
        when(studentRepository.existsByStudentId(student.getStudentId())).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> studentService.createStudent(student))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("学号已存在");

        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    public void whenCreateStudentWithDuplicateEmail_thenThrowException() {
        // given
        when(studentRepository.existsByStudentId(student.getStudentId())).thenReturn(false);
        when(studentRepository.existsByEmail(student.getEmail())).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> studentService.createStudent(student))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("邮箱已存在");

        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    public void whenDeleteExistingStudent_thenSuccess() {
        // given
        when(studentRepository.existsById("1")).thenReturn(true);

        // when
        studentService.deleteStudent("1");

        // then
        verify(studentRepository, times(1)).deleteById("1");
    }

    @Test
    public void whenDeleteNonExistingStudent_thenThrowException() {
        // given
        when(studentRepository.existsById("1")).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> studentService.deleteStudent("1"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("学生不存在");

        verify(studentRepository, never()).deleteById(anyString());
    }
}