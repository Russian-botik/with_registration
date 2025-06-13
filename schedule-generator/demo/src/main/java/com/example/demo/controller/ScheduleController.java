package com.example.demo.controller;

import com.example.demo.model.Schedule;
import com.example.demo.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<Page<Schedule>> getSchedule(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(scheduleService.getSchedule(PageRequest.of(page, size)));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Schedule>> getAllSchedules() {
        return ResponseEntity.ok(scheduleService.getAllSchedules());
    }

    @PostMapping
    public ResponseEntity<Schedule> addSchedule(@RequestBody Schedule schedule) {
        return ResponseEntity.ok(scheduleService.addSchedule(schedule));
    }
} 