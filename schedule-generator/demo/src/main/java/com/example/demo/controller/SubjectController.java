package com.example.demo.controller;

import com.example.demo.model.Subject;
import com.example.demo.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {
    @Autowired
    private SubjectService subjectService;

    @PostMapping
    public ResponseEntity<Subject> addSubject(@RequestBody Subject subject) {
        return ResponseEntity.ok(subjectService.addSubject(subject));
    }
} 