package com.example.slstudiomini.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.slstudiomini.model.Course;
import com.example.slstudiomini.repository.CourseRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class CourseService {
    
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CourseRepository courseRepository;

    public List<Course> findAllCourses() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Course> cq = cb.createQuery(Course.class);
        Root<Course> course = cq.from(Course.class);
        // クエリをビルドして全件取得
        cq.select(course)
        .where(cb.isNull(course.get("deletedAt")));
    
        // 実行する
        try {
            return entityManager.createQuery(cq).getResultList(); // getSingleResult()ではなくgetResultList()を使用
        } catch (NoResultException e) {
            throw new EntityNotFoundException("No courses found"); // メッセージを変更
        }
    }

    // public List<Course> findAllCourses() {
    //     return courseRepository.findAll();
    // }

    // public Course findCourseById(Long id) {
    //     return courseRepository.findById(id)
    //         .orElseThrow(() -> new EntityNotFoundException("Course Not Found With id = " + id));
    // }

    //画像ファイル名をCoures fileNameカラムに保存
    public Course fileUploadCouse(Long id, String fileName) {
        Course course = findCourseById(id);
        course.setFilepath(fileName);
        return courseRepository.save(course);
    }


    public Course findCourseById(Long id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Course> cq = cb.createQuery(Course.class);
        Root<Course> course = cq.from(Course.class);
		
        // ここからクエリをビルドする
        cq.select(course).where(
        cb.and(
            cb.equal(course.get("id"), id),
            cb.isNull(course.get("deletedAt"))
        )
        );
		
        // 実行する
        try {
        return entityManager.createQuery(cq).getSingleResult();
            } catch (NoResultException e) {
        throw new EntityNotFoundException("Course with id " + id + " not found");
        }
    }

    @Transactional
    public Course save(Course course) {
        if (course != null) {
            course.setCreatedAt(LocalDateTime.now());
            courseRepository.save(course);
        }
        return course;
    }

    @Transactional
    public Course update(Course updateCourse) {
        Course course = findCourseById(updateCourse.getId());
        course.setName(updateCourse.getName());
        course.setDescription(updateCourse.getDescription());
        course.setUpdatedAt(LocalDateTime.now());

        return courseRepository.save(course);
    }

    @Transactional
    public void delete(Course deletedCourse) {
        Course course = findCourseById(deletedCourse.getId());
        // コースの削除はdeletedAtに日付を入れる
        course.setDeletedAt(LocalDateTime.now());
        course.getLessons().forEach(lesson -> lesson.setDeletedAt(LocalDateTime.now()));
        courseRepository.save(course);
    }
}