package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

@Service
public class TeacherSubjectGeneratorService {
    private static final Logger logger = LoggerFactory.getLogger(TeacherSubjectGeneratorService.class);

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private TeacherSubjectRepository teacherSubjectRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    private void checkDatabaseInitialization() {
        List<Teacher> teachers = teacherRepository.findAll();
        List<Subject> subjects = subjectRepository.findAll();
        List<Classroom> classrooms = classroomRepository.findAll();
        List<TeacherSubject> teacherSubjects = teacherSubjectRepository.findAll();
        List<Schedule> schedules = scheduleRepository.findAll();

        logger.info("Database initialization check:");
        logger.info("Teachers count: {}", teachers.size());
        for (Teacher teacher : teachers) {
            logger.info("Teacher: {} {} (ID: {})", 
                teacher.getFirstName(), teacher.getLastName(), teacher.getId());
        }

        logger.info("Subjects count: {}", subjects.size());
        for (Subject subject : subjects) {
            logger.info("Subject: {} (ID: {})", 
                subject.getName(), subject.getId());
        }

        logger.info("Classrooms count: {}", classrooms.size());
        for (Classroom classroom : classrooms) {
            logger.info("Classroom: {} (ID: {})", 
                classroom.getName(), classroom.getId());
        }

        logger.info("Teacher-Subject relationships count: {}", teacherSubjects.size());
        for (TeacherSubject ts : teacherSubjects) {
            logger.info("Teacher-Subject: {} - {} (ID: {})", 
                ts.getTeacher().getLastName(), 
                ts.getSubject().getName(), 
                ts.getId());
        }

        logger.info("Schedules count: {}", schedules.size());
        for (Schedule schedule : schedules) {
            logger.info("Schedule: {} - {} on {} at {}", 
                schedule.getTeacherSubject().getTeacher().getLastName(),
                schedule.getTeacherSubject().getSubject().getName(),
                schedule.getDayOfWeek(),
                schedule.getStartTime());
        }

        if (teachers.isEmpty() || subjects.isEmpty() || classrooms.isEmpty()) {
            throw new RuntimeException("Database is not properly initialized. Please check data.sql");
        }
    }

    public void generateTeacherSubjects() {
        try {
            // Проверяем инициализацию базы данных
            checkDatabaseInitialization();

            // Проверяем существующие расписания
            List<Schedule> existingSchedules = scheduleRepository.findAll();
            if (!existingSchedules.isEmpty()) {
                logger.info("Found {} existing schedules. Deleting them first.", existingSchedules.size());
                scheduleRepository.deleteAll();
            }

            // Удаляем существующие связи
            logger.info("Deleting existing teacher-subject relationships");
            teacherSubjectRepository.deleteAll();

            List<Teacher> teachers = teacherRepository.findAll();
            List<Subject> subjects = subjectRepository.findAll();
            List<Classroom> classrooms = classroomRepository.findAll();

            Random random = new Random();

            // Для каждого преподавателя назначаем 1-2 предмета
            for (Teacher teacher : teachers) {
                int numberOfSubjects = random.nextInt(2) + 1; // 1 или 2 предмета
                List<Subject> availableSubjects = new java.util.ArrayList<>(subjects);
                java.util.Collections.shuffle(availableSubjects);

                logger.info("Assigning {} subjects to teacher {}", numberOfSubjects, teacher.getLastName());

                for (int i = 0; i < numberOfSubjects && !availableSubjects.isEmpty(); i++) {
                    Subject subject = availableSubjects.remove(0);
                    Classroom classroom = classrooms.get(random.nextInt(classrooms.size()));

                    TeacherSubject teacherSubject = new TeacherSubject();
                    teacherSubject.setTeacher(teacher);
                    teacherSubject.setSubject(subject);
                    teacherSubject.setClassroom(classroom);

                    logger.info("Creating teacher-subject relationship: {} - {} in classroom {}", 
                        teacher.getLastName(), subject.getName(), classroom.getName());

                    teacherSubjectRepository.save(teacherSubject);
                }
            }

            logger.info("Successfully generated teacher-subject relationships");
        } catch (Exception e) {
            logger.error("Error generating teacher-subject relationships", e);
            throw new RuntimeException("Error generating teacher-subject relationships: " + e.getMessage());
        }
    }
} 