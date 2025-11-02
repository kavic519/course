package com.zjsu.rqq.course.repository;

import com.zjsu.rqq.course.model.Course;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

// 课程仓库
@Repository
public interface CourseRepository extends JpaRepository<Course, String> {

    Optional<Course> findByCode(String code);

    List<Course> findByInstructorId(String instructorId);

    @Query("SELECT c FROM Course c WHERE c.title LIKE %:keyword%")
    List<Course> findByTitleContaining(@Param("keyword") String keyword);

    @Query("SELECT c FROM Course c WHERE c.enrolled < c.capacity")
    List<Course> findAvailableCourses();

    boolean existsByCode(String code);

    @Query("SELECT COUNT(c) > 0 FROM Course c WHERE c.code = :code AND c.id != :id")
    boolean existsByCodeAndIdNot(@Param("code") String code, @Param("id") String id);
}

//@Repository
//public class CourseRepository {
//    // 课程集合
//    private final Map<String, Course> courses = new ConcurrentHashMap<>();
//
//    //查找所有课程
//    public List<Course> findAll() {
//        return new ArrayList<>(courses.values());
//    }
//
//    // 根据ID查找课程
//    public Optional<Course> findById(String id) {
//        return Optional.ofNullable(courses.get(id));
//    }
//
//    // 根据课程代码查找课程
//    public Optional<Course> findByCode(String code) {
//        return courses.values().stream()
//                .filter(course -> course.getCode().equals(code))
//                .findFirst();
//    }
//
//    // 保存课程
//    public Course save(Course course) {
//        if (course.getId() == null) {
//            course.setId(UUID.randomUUID().toString());
//        }
//        course.setUpdatedAt(java.time.LocalDateTime.now());
//        courses.put(course.getId(), course);
//        return course;
//    }
//
//    // 删除课程
//    public void deleteById(String id) {
//        courses.remove(id);
//    }
//
//    // 判断课程是否存在
//    public boolean existsById(String id) {
//        return courses.containsKey(id);
//    }
//}
