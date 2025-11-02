package com.zjsu.rqq.course.repository;

import com.zjsu.rqq.course.TestDataFactory;
import com.zjsu.rqq.course.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class StudentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void whenFindByStudentId_thenReturnStudent() {
        // given
        Student student = TestDataFactory.createStudent();
        entityManager.persistAndFlush(student);

        // when
        Optional<Student> found = studentRepository.findByStudentId(student.getStudentId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo(student.getName());
    }

    @Test
    public void whenFindByEmail_thenReturnStudent() {
        // given
        Student student = TestDataFactory.createStudent();
        entityManager.persistAndFlush(student);

        // when
        Optional<Student> found = studentRepository.findByEmail(student.getEmail());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getStudentId()).isEqualTo(student.getStudentId());
    }

    @Test
    public void whenFindByMajor_thenReturnStudents() {
        // given
        Student student1 = TestDataFactory.createStudent("S2024001", "张三");
        Student student2 = TestDataFactory.createStudent("S2024002", "李四");
        student2.setMajor("软件工程");

        entityManager.persist(student1);
        entityManager.persist(student2);
        entityManager.flush();

        // when
        List<Student> found = studentRepository.findByMajor("计算机科学");

        // then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getName()).isEqualTo("张三");
    }

    @Test
    public void whenExistsByStudentId_thenReturnTrue() {
        // given
        Student student = TestDataFactory.createStudent();
        entityManager.persistAndFlush(student);

        // when
        boolean exists = studentRepository.existsByStudentId(student.getStudentId());

        // then
        assertThat(exists).isTrue();
    }

    @Test
    public void whenNotExistsByStudentId_thenReturnFalse() {
        // when
        boolean exists = studentRepository.existsByStudentId("NON_EXISTENT");

        // then
        assertThat(exists).isFalse();
    }
}