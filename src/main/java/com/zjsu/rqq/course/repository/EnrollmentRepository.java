package com.zjsu.rqq.course.repository;

import com.zjsu.rqq.course.model.Enrollment;
import org.springframework.stereotype.Repository;
import com.zjsu.rqq.course.model.Enrollment.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

// 选课仓库
@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, String> {

    List<Enrollment> findByCourseId(String courseId);

    List<Enrollment> findByStudentId(String studentId);

    List<Enrollment> findByStatus(EnrollmentStatus status);

    List<Enrollment> findByCourseIdAndStatus(String courseId, EnrollmentStatus status);

    List<Enrollment> findByStudentIdAndStatus(String studentId, EnrollmentStatus status);

    @Query("SELECT e FROM Enrollment e WHERE e.course.id = :courseId AND e.student.id = :studentId AND e.status = 'ACTIVE'")
    Optional<Enrollment> findActiveByCourseAndStudent(@Param("courseId") String courseId, @Param("studentId") String studentId);

    boolean existsByCourseIdAndStudentIdAndStatus(String courseId, String studentId, EnrollmentStatus status);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.course.id = :courseId AND e.status = 'ACTIVE'")
    int countActiveByCourseId(@Param("courseId") String courseId);

    @Query("SELECT COUNT(e) > 0 FROM Enrollment e WHERE e.student.id = :studentId AND e.status = 'ACTIVE'")
    boolean hasActiveEnrollmentsByStudentId(@Param("studentId") String studentId);

    //按课程、学生、状态组合查询
    @Query("SELECT e FROM Enrollment e WHERE e.course.id = :courseId AND e.student.id = :studentId AND e.status = :status")
    List<Enrollment> findByCourseIdAndStudentIdAndStatus(String courseId, String studentId, EnrollmentStatus status);

}

//@Repository
//public class EnrollmentRepository {
//    // 选课集合
//    private final Map<String, Enrollment> enrollments = new ConcurrentHashMap<>();
//    // 课程-选课关系
//    private final Map<String, Set<String>> courseEnrollments = new ConcurrentHashMap<>();
//    // 学生-选课关系
//    private final Map<String, Set<String>> studentEnrollments = new ConcurrentHashMap<>();
//
//    // 查找所有选课
//    public List<Enrollment> findAll() {
//        return new ArrayList<>(enrollments.values());
//    }
//
//    // 根据ID查找选课
//    public Optional<Enrollment> findById(String id) {
//        return Optional.ofNullable(enrollments.get(id));
//    }
//
//    // 根据课程ID查找选课
//    public List<Enrollment> findByCourseId(String courseId) {
//        Set<String> enrollmentIds = courseEnrollments.getOrDefault(courseId, Collections.emptySet());
//        return enrollmentIds.stream()
//                .map(enrollments::get)
//                .filter(Objects::nonNull)
//                .collect(Collectors.toList());
//    }
//
//    // 根据学生ID查找选课
//    public List<Enrollment> findByStudentId(String studentId) {
//        // 根据学号查找ID
//        Set<String> enrollmentIds = studentEnrollments.getOrDefault(studentId, Collections.emptySet());
//        // 根据ID查找选课
//        return enrollmentIds.stream()
//                .map(enrollments::get)
//                .filter(Objects::nonNull)
//                .collect(Collectors.toList());
//    }
//
//    // 根据课程ID和学生ID查找选课
//    public boolean existsByCourseIdAndStudentId(String courseId, String studentId) {
//        // 根据学号查找ID
//        Set<String> studentEnrollmentIds = studentEnrollments.getOrDefault(studentId, Collections.emptySet());
//        // 根据ID查找选课
//        return studentEnrollmentIds.stream()
//                .map(enrollments::get)
//                .filter(Objects::nonNull)
//                .anyMatch(enrollment -> enrollment.getCourseId().equals(courseId));
//    }
//
//    // 保存选课
//    public Enrollment save(Enrollment enrollment) {
//        if (enrollment.getId() == null) {
//            enrollment.setId(UUID.randomUUID().toString());
//        }
//
//        // 保存选课
//        enrollments.put(enrollment.getId(), enrollment);
//
//        // 更新课程-选课关系
//        courseEnrollments.computeIfAbsent(enrollment.getCourseId(), k -> new HashSet<>())
//                .add(enrollment.getId());
//
//        // 更新学生-选课关系
//        studentEnrollments.computeIfAbsent(enrollment.getStudentId(), k -> new HashSet<>())
//                .add(enrollment.getId());
//
//        return enrollment;
//    }
//
//    // 删除选课
//    public void deleteById(String id) {
//        Enrollment enrollment = enrollments.get(id);
//        if (enrollment != null) {
//            // 从课程-选课关系中移除
//            Set<String> courseEnrollmentSet = courseEnrollments.get(enrollment.getCourseId());
//            if (courseEnrollmentSet != null) {
//                courseEnrollmentSet.remove(id);
//                if (courseEnrollmentSet.isEmpty()) {
//                    courseEnrollments.remove(enrollment.getCourseId());
//                }
//            }
//
//            // 从学生-选课关系中移除
//            Set<String> studentEnrollmentSet = studentEnrollments.get(enrollment.getStudentId());
//            if (studentEnrollmentSet != null) {
//                studentEnrollmentSet.remove(id);
//                if (studentEnrollmentSet.isEmpty()) {
//                    studentEnrollments.remove(enrollment.getStudentId());
//                }
//            }
//
//            enrollments.remove(id);
//        }
//    }
//
//    // 根据课程ID统计选课数量
//    public int countByCourseId(String courseId) {
//        Set<String> enrollmentIds = courseEnrollments.getOrDefault(courseId, Collections.emptySet());
//        return enrollmentIds.size();
//    }
//
//    // 根据学生ID判断是否已选课
//    public boolean hasEnrollmentsByStudentId(String studentId) {
//        Set<String> enrollmentIds = studentEnrollments.getOrDefault(studentId, Collections.emptySet());
//        return !enrollmentIds.isEmpty();
//    }
//}
