package com.example.slstudiomini.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.slstudiomini.model.Course;
import com.example.slstudiomini.service.CourseService;

@Controller
public class FileUploadController {

    @Autowired
    private CourseService courseService;

    // ファイルの保存先
    private static String UPLOADED_FOLDER = "uploads/images/course/";


    @GetMapping("/upload/{id}")
    public String uploadForm(@PathVariable("id") Long id, Model model) {
        Course course = courseService.findCourseById(id);
        model.addAttribute("course", course);
        return "admin/course-upload";
    }
    
    @PostMapping("/upload/{id}")
    public String singleFileUpload(@RequestParam(name = "file") MultipartFile file, @PathVariable("id") Long id) {
        
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase() : "";
        if (!fileExtension.equals("png")) {
            return "許可されていないファイル形式です";
        }

        String fileName = UPLOADED_FOLDER + id + ".png";

        //Courseテーブルの指定されたidのfilepathカラムにfileNameを保存
        courseService.fileUploadCouse(id, fileName);

        try {
            // 1. ファイルの保存先(ファイル名を含む)を決定
            Path path = Paths.get(fileName);
            // 2. 書き出す事で保存
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/admin/courses";
    }
}
