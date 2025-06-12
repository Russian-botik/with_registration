package com.example.demo;

import com.example.demo.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ScheduleApiClient {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${schedule.api.url:http://localhost:8080}")
    private String scheduleApiUrl;

    public RoomDto[] getRooms() {
        return restTemplate.getForObject(scheduleApiUrl + "/rooms", RoomDto[].class);
    }

    public SubjectDto[] getSubjects() {
        return restTemplate.getForObject(scheduleApiUrl + "/subjects", SubjectDto[].class);
    }

    public TeacherDto[] getTeachers() {
        return restTemplate.getForObject(scheduleApiUrl + "/teachers", TeacherDto[].class);
    }

    public AssignmentDto[] getAssignments() {
        return restTemplate.getForObject(scheduleApiUrl + "/assignments", AssignmentDto[].class);
    }

    public ScheduleDto[] getSchedules(int page, int size) {
        String url = String.format(scheduleApiUrl + "/schedules?page=%d&size=%d", page, size);
        return restTemplate.getForObject(url, ScheduleDto[].class);
    }

    public RoomDto addRoom(RoomDto room) {
        return restTemplate.postForObject(scheduleApiUrl + "/rooms", room, RoomDto.class);
    }

    public void deleteRoom(Long id) {
        restTemplate.delete(scheduleApiUrl + "/rooms/" + id);
    }

    public SubjectDto addSubject(SubjectDto subject) {
        return restTemplate.postForObject(scheduleApiUrl + "/subjects", subject, SubjectDto.class);
    }

    public void deleteSubject(Long id) {
        restTemplate.delete(scheduleApiUrl + "/subjects/" + id);
    }

    public TeacherDto addTeacher(TeacherDto teacher) {
        return restTemplate.postForObject(scheduleApiUrl + "/teachers", teacher, TeacherDto.class);
    }

    public void deleteTeacher(Long id) {
        restTemplate.delete(scheduleApiUrl + "/teachers/" + id);
    }

    public AssignmentDto addAssignment(AssignmentDto assignment) {
        return restTemplate.postForObject(scheduleApiUrl + "/assignments", assignment, AssignmentDto.class);
    }

    public void deleteAssignment(Long id) {
        restTemplate.delete(scheduleApiUrl + "/assignments/" + id);
    }

    public ScheduleDto addSchedule(ScheduleDto schedule) {
        return restTemplate.postForObject(scheduleApiUrl + "/schedules", schedule, ScheduleDto.class);
    }

    public void deleteSchedule(Long id) {
        restTemplate.delete(scheduleApiUrl + "/schedules/" + id);
    }
} 