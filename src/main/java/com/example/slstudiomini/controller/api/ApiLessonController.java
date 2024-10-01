package com.example.slstudiomini.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.slstudiomini.model.Lesson;
import com.example.slstudiomini.service.LessonService;

@RestController
@RequestMapping("/api/lessons")
public class ApiLessonController {
    @Autowired
    private LessonService lessonService;

    @GetMapping
    public List<Lesson> index() {
        return lessonService.findAllLessons();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Lesson> index(@PathVariable("id") Long id) {
        Lesson lesson = lessonService.findLessonById(id);
        if (lesson != null) {
            return ResponseEntity.ok(lesson);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
