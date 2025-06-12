package com.example.demo;

import com.example.demo.dto.ScheduleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client/schedules")
public class ClientScheduleController {
    @Autowired
    private ScheduleApiClient apiClient;

    @GetMapping
    public ScheduleDto[] getSchedules(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return apiClient.getSchedules(page, size);
    }

    @PostMapping
    public ScheduleDto addSchedule(@RequestBody ScheduleDto schedule) {
        return apiClient.addSchedule(schedule);
    }

    @DeleteMapping("/{id}")
    public void deleteSchedule(@PathVariable Long id) {
        apiClient.deleteSchedule(id);
    }
} 