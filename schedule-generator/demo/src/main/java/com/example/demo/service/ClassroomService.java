package com.example.demo.service;

import com.example.demo.model.Classroom;
import com.example.demo.repository.ClassroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassroomService {
    @Autowired
    private ClassroomRepository classroomRepository;

    public Classroom addClassroom(Classroom classroom) {
        return classroomRepository.save(classroom);
    }

    public void deleteClassroom(Long id) {
        classroomRepository.deleteById(id);
    }

    public List<Classroom> getAllClassrooms() {
        return classroomRepository.findAll();
    }
} 