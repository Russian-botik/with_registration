package com.example.demo.service;

import com.example.demo.model.TeacherSubject;
import com.example.demo.repository.TeacherSubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherSubjectService {
    @Autowired
    private TeacherSubjectRepository teacherSubjectRepository;

    public TeacherSubject addTeacherSubject(TeacherSubject teacherSubject) {
        return teacherSubjectRepository.save(teacherSubject);
    }
} 