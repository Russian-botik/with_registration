package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

@Service
public class ScheduleGeneratorService {
    @Autowired
    private TeacherSubjectRepository teacherSubjectRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    private static final LocalTime[] TIME_SLOTS = {
        LocalTime.of(9, 0),  // 9:00
        LocalTime.of(10, 30), // 10:30
        LocalTime.of(12, 0),  // 12:00
        LocalTime.of(13, 30), // 13:30
        LocalTime.of(15, 0),  // 15:00
        LocalTime.of(16, 30)  // 16:30
    };

    private static final DayOfWeek[] WORK_DAYS = {
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY
    };

    public void generateSchedule() {
        List<TeacherSubject> teacherSubjects = teacherSubjectRepository.findAll();
        List<Classroom> classrooms = classroomRepository.findAll();

        if (teacherSubjects.isEmpty() || classrooms.isEmpty()) {
            throw new RuntimeException("No teacher-subject pairs or classrooms found in the database");
        }

        // Очищаем существующее расписание
        scheduleRepository.deleteAll();

        Random random = new Random();
        Map<DayOfWeek, Map<LocalTime, Set<Long>>> occupiedClassrooms = new HashMap<>();
        Map<DayOfWeek, Map<LocalTime, Set<Long>>> occupiedTeachers = new HashMap<>();

        // Инициализация структур для отслеживания занятости
        for (DayOfWeek day : WORK_DAYS) {
            occupiedClassrooms.put(day, new HashMap<>());
            occupiedTeachers.put(day, new HashMap<>());
            for (LocalTime time : TIME_SLOTS) {
                occupiedClassrooms.get(day).put(time, new HashSet<>());
                occupiedTeachers.get(day).put(time, new HashSet<>());
            }
        }

        // Перемешиваем пары преподаватель-предмет
        Collections.shuffle(teacherSubjects);

        for (TeacherSubject teacherSubject : teacherSubjects) {
            // Определяем количество занятий для пары (1-2 раза в неделю)
            int numberOfLessons = random.nextInt(2) + 1;

            for (int i = 0; i < numberOfLessons; i++) {
                boolean scheduled = false;
                int attempts = 0;
                final int MAX_ATTEMPTS = 50;

                while (!scheduled && attempts < MAX_ATTEMPTS) {
                    // Выбираем случайный день и время
                    DayOfWeek day = WORK_DAYS[random.nextInt(WORK_DAYS.length)];
                    LocalTime startTime = TIME_SLOTS[random.nextInt(TIME_SLOTS.length)];
                    LocalTime endTime = startTime.plusMinutes(90); // 1.5 часа

                    // Проверяем доступность аудитории
                    List<Classroom> availableClassrooms = new ArrayList<>();
                    for (Classroom classroom : classrooms) {
                        if (!occupiedClassrooms.get(day).get(startTime).contains(classroom.getId())) {
                            availableClassrooms.add(classroom);
                        }
                    }

                    if (!availableClassrooms.isEmpty() && 
                        !occupiedTeachers.get(day).get(startTime).contains(teacherSubject.getTeacher().getId())) {
                        // Выбираем случайную доступную аудиторию
                        Classroom classroom = availableClassrooms.get(random.nextInt(availableClassrooms.size()));

                        // Создаем занятие
                        Schedule schedule = new Schedule();
                        schedule.setTeacherSubject(teacherSubject);
                        schedule.setClassroom(classroom);
                        schedule.setDayOfWeek(day);
                        schedule.setStartTime(startTime);
                        schedule.setEndTime(endTime);

                        try {
                            // Сохраняем занятие
                            scheduleRepository.save(schedule);

                            // Обновляем информацию о занятости
                            occupiedClassrooms.get(day).get(startTime).add(classroom.getId());
                            occupiedTeachers.get(day).get(startTime).add(teacherSubject.getTeacher().getId());

                            scheduled = true;
                        } catch (Exception e) {
                            System.err.println("Error saving schedule: " + e.getMessage());
                            attempts++;
                        }
                    } else {
                        attempts++;
                    }
                }

                if (!scheduled) {
                    System.err.println("Could not schedule lesson for teacher " + 
                        teacherSubject.getTeacher().getLastName() + " and subject " + 
                        teacherSubject.getSubject().getName());
                }
            }
        }
    }
} 