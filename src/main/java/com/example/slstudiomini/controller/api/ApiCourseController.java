package com.example.slstudiomini.controller.api;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.slstudiomini.model.Course;
import com.example.slstudiomini.service.CourseService;

@RestController
@RequestMapping("/api/courses")
public class ApiCourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping
    public List<Course> index() {
        return courseService.findAllCourses();
    }
}
