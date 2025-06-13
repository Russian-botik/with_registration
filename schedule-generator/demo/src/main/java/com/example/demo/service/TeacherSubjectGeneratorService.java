package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class TeacherSubjectGeneratorService {
    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private TeacherSubjectRepository teacherSubjectRepository;

    public void generateTeacherSubjects() {
        List<Teacher> teachers = teacherRepository.findAll();
        List<Subject> subjects = subjectRepository.findAll();
        List<Classroom> classrooms = classroomRepository.findAll();

        if (teachers.isEmpty() || subjects.isEmpty() || classrooms.isEmpty()) {
            throw new RuntimeException("No teachers, subjects, or classrooms found in the database");
        }

        Random random = new Random();

        // Для каждого преподавателя назначаем 1-3 случайных предмета
        for (Teacher teacher : teachers) {
            int numberOfSubjects = random.nextInt(3) + 1; // 1-3 предмета
            int attempts = 0;
            int assignedSubjects = 0;

            while (assignedSubjects < numberOfSubjects && attempts < subjects.size() * 2) {
                Subject subject = subjects.get(random.nextInt(subjects.size()));
                Classroom classroom = classrooms.get(random.nextInt(classrooms.size()));

                // Проверяем, не назначен ли уже этот предмет преподавателю
                if (!teacherSubjectRepository.existsByTeacherAndSubject(teacher, subject)) {
                    TeacherSubject teacherSubject = new TeacherSubject();
                    teacherSubject.setTeacher(teacher);
                    teacherSubject.setSubject(subject);
                    teacherSubject.setClassroom(classroom);
                    teacherSubjectRepository.save(teacherSubject);
                    assignedSubjects++;
                }
                attempts++;
            }
        }
    }
} 