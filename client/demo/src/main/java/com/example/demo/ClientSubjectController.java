package com.example.demo;

import com.example.demo.dto.SubjectDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client/subjects")
public class ClientSubjectController {
    @Autowired
    private ScheduleApiClient apiClient;

    @GetMapping
    public SubjectDto[] getSubjects() {
        return apiClient.getSubjects();
    }

    @PostMapping
    public SubjectDto addSubject(@RequestBody SubjectDto subject) {
        return apiClient.addSubject(subject);
    }

    @DeleteMapping("/{id}")
    public void deleteSubject(@PathVariable Long id) {
        apiClient.deleteSubject(id);
    }
} 