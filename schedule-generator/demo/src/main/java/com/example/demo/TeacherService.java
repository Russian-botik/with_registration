package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {
    @Autowired
    private TeacherRepository teacherRepository;

    public Teacher addTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    public Page<Teacher> getAllTeachers(Pageable pageable) {
        return teacherRepository.findAll(pageable);
    }

    public Optional<Teacher> getTeacher(Long id) {
        return teacherRepository.findById(id);
    }

    public Optional<Teacher> updateTeacher(Long id, Teacher teacher) {
        return teacherRepository.findById(id)
                .map(existingTeacher -> {
                    existingTeacher.setName(teacher.getName());
                    return teacherRepository.save(existingTeacher);
                });
    }

    public boolean deleteTeacher(Long id) {
        if (teacherRepository.existsById(id)) {
            teacherRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Teacher> searchTeachers(String name) {
        return teacherRepository.findByNameContainingIgnoreCase(name);
    }
} 