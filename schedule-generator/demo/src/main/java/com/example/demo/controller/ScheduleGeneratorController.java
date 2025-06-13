package com.example.demo.controller;

import com.example.demo.service.ScheduleGeneratorService;
import com.example.demo.service.TeacherSubjectGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/generator")
public class ScheduleGeneratorController {

    @Autowired
    private TeacherSubjectGeneratorService teacherSubjectGeneratorService;

    @Autowired
    private ScheduleGeneratorService scheduleGeneratorService;

    @PostMapping("/teacher-subjects")
    public ResponseEntity<String> generateTeacherSubjects() {
        try {
            teacherSubjectGeneratorService.generateTeacherSubjects();
            return ResponseEntity.ok("Teacher-subject relationships generated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error generating teacher-subject relationships: " + e.getMessage());
        }
    }

    @PostMapping("/schedule")
    public ResponseEntity<String> generateSchedule() {
        try {
            scheduleGeneratorService.generateSchedule();
            return ResponseEntity.ok("Schedule generated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error generating schedule: " + e.getMessage());
        }
    }
} 