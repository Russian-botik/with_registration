package com.example.demo.repository;

import com.example.demo.model.TeacherSubject;
import com.example.demo.model.Teacher;
import com.example.demo.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TeacherSubjectRepository extends JpaRepository<TeacherSubject, Long> {
    boolean existsByTeacherAndSubject(Teacher teacher, Subject subject);
    List<TeacherSubject> findByTeacher(Teacher teacher);
} 