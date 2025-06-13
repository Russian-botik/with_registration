package com.example.demo.controller;

import com.example.demo.model.TeacherSubject;
import com.example.demo.service.TeacherSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teacher-subjects")
public class TeacherSubjectController {
    @Autowired
    private TeacherSubjectService teacherSubjectService;

    @PostMapping
    public ResponseEntity<TeacherSubject> addTeacherSubject(@RequestBody TeacherSubject teacherSubject) {
        return ResponseEntity.ok(teacherSubjectService.addTeacherSubject(teacherSubject));
    }
} 