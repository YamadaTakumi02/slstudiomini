package com.example.slstudiomini.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.slstudiomini.exception.MyLessonNotFoundException;
import com.example.slstudiomini.model.Course;
import com.example.slstudiomini.model.Lesson;
import com.example.slstudiomini.repository.LessonRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class LessonService {

    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseService courseService;

    // public List<Lesson> findAllLessons() {
    //     return lessonRepository.findAll();
    // }


    public List<Lesson> findAllLessons() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Lesson> cq = cb.createQuery(Lesson.class);
        Root<Lesson> lesson = cq.from(Lesson.class);
        
        // クエリをビルドして全件取得
        // deletedAtがnullのものを取得
        // 削除されたものはdeletedAtに日付が入るためnullの場合は削除されていない。
        cq.select(lesson)
        .where(cb.isNull(lesson.get("deletedAt")));
    
        // 実行する
        return entityManager.createQuery(cq).getResultList();
    }
    


    // public Lesson findLessonById(Long id) {
    //     return lessonRepository.findById(id)
    //         .orElseThrow(() -> new EntityNotFoundException("Lesson Not Found With id= " + id));
    // }

    //クエリビルダーを使ったクエリ構築
    public Lesson findLessonById(Long id) {
    
        lessonRepository.findById(id)
        .orElseThrow(() -> new MyLessonNotFoundException("存在しないレッスンです!"));

        
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Lesson> cq = cb.createQuery(Lesson.class);
        Root<Lesson> lesson = cq.from(Lesson.class);
		
        // ここからクエリをビルドする
        cq.select(lesson);
        cq.where(
            cb.equal(lesson.get("id"), id)
        );
		
        // 実行する
        //return entityManager.createQuery(cq).getResultList();
        return entityManager.createQuery(cq).getSingleResult();
        
    }

    @Transactional
    public Lesson save(Lesson lesson) {
        lesson.setCreatedAt(LocalDateTime.now());
        return lessonRepository.save(lesson);
    }

    @Transactional
    public Lesson update(Lesson updateLesson) {
        Lesson lesson = findLessonById(updateLesson.getId());
        
        lesson.setUpdatedAt(LocalDateTime.now());
        lesson.setName(updateLesson.getName());
        lesson.setContent(updateLesson.getContent());
        lesson.setDescription(updateLesson.getDescription());
        lesson.setUpdatedAt(LocalDateTime.now());

        Course course = courseService.findCourseById(updateLesson.getCourse().getId());
        lesson.setCourse(course);
        return lessonRepository.save(lesson);
    }
}