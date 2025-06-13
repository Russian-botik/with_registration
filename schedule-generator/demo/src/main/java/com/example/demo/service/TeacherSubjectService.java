package com.example.demo.service;

import com.example.demo.model.TeacherSubject;
import com.example.demo.model.Teacher;
import com.example.demo.model.Subject;
import com.example.demo.repository.TeacherSubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherSubjectService {
    @Autowired
    private TeacherSubjectRepository teacherSubjectRepository;

    public TeacherSubject addTeacherSubject(TeacherSubject teacherSubject) {
        return teacherSubjectRepository.save(teacherSubject);
    }

    public List<TeacherSubject> getAllTeacherSubjects() {
        return teacherSubjectRepository.findAll();
    }

    public List<TeacherSubject> getTeacherSubjectsByTeacher(Teacher teacher) {
        return teacherSubjectRepository.findByTeacher(teacher);
    }

    public boolean existsByTeacherAndSubject(Teacher teacher, Subject subject) {
        return teacherSubjectRepository.existsByTeacherAndSubject(teacher, subject);
    }
} 