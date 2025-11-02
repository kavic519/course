package com.zjsu.rqq.course.repository;

import com.zjsu.rqq.course.TestDataFactory;
import com.zjsu.rqq.course.model.Course;
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
public class CourseRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CourseRepository courseRepository;

    @Test
    public void whenFindByCode_thenReturnCourse() {
        // given
        Course course = TestDataFactory.createCourse();
        entityManager.persistAndFlush(course);

        // when
        Optional<Course> found = courseRepository.findByCode(course.getCode());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo(course.getTitle());
    }

    @Test
    public void whenFindByInstructorId_thenReturnCourses() {
        // given
        Course course1 = TestDataFactory.createCourse("CS101", "计算机科学导论");
        Course course2 = TestDataFactory.createCourse("MA101", "高等数学");
        course2.getInstructor().setId("T002");

        entityManager.persist(course1);
        entityManager.persist(course2);
        entityManager.flush();

        // when
        List<Course> found = courseRepository.findByInstructorId("T001");

        // then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getCode()).isEqualTo("CS101");
    }

    @Test
    public void whenFindAvailableCourses_thenReturnAvailableCourses() {
        // given
        Course course1 = TestDataFactory.createCourse("CS101", "计算机科学导论");
        course1.setCapacity(10);
        course1.setEnrolled(5);

        Course course2 = TestDataFactory.createCourse("MA101", "高等数学");
        course2.setCapacity(10);
        course2.setEnrolled(10); // 已满

        entityManager.persist(course1);
        entityManager.persist(course2);
        entityManager.flush();

        // when
        List<Course> availableCourses = courseRepository.findAvailableCourses();

        // then
        assertThat(availableCourses).hasSize(1);
        assertThat(availableCourses.get(0).getCode()).isEqualTo("CS101");
    }

    @Test
    public void whenFindByTitleContaining_thenReturnMatchingCourses() {
        // given
        Course course1 = TestDataFactory.createCourse("CS101", "计算机科学导论");
        Course course2 = TestDataFactory.createCourse("MA101", "高等数学");

        entityManager.persist(course1);
        entityManager.persist(course2);
        entityManager.flush();

        // when
        List<Course> found = courseRepository.findByTitleContaining("计算机");

        // then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getCode()).isEqualTo("CS101");
    }
}