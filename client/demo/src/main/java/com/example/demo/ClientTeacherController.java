package com.example.demo;

import com.example.demo.dto.TeacherDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client/teachers")
public class ClientTeacherController {
    @Autowired
    private ScheduleApiClient apiClient;

    @GetMapping
    public TeacherDto[] getTeachers() {
        return apiClient.getTeachers();
    }

    @PostMapping
    public TeacherDto addTeacher(@RequestBody TeacherDto teacher) {
        return apiClient.addTeacher(teacher);
    }

    @DeleteMapping("/{id}")
    public void deleteTeacher(@PathVariable Long id) {
        apiClient.deleteTeacher(id);
    }
} 