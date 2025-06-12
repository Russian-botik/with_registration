package com.example.demo;

import com.example.demo.dto.AssignmentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client/assignments")
public class ClientAssignmentController {
    @Autowired
    private ScheduleApiClient apiClient;

    @GetMapping
    public AssignmentDto[] getAssignments() {
        return apiClient.getAssignments();
    }

    @PostMapping
    public AssignmentDto addAssignment(@RequestBody AssignmentDto assignment) {
        return apiClient.addAssignment(assignment);
    }

    @DeleteMapping("/{id}")
    public void deleteAssignment(@PathVariable Long id) {
        apiClient.deleteAssignment(id);
    }
} 