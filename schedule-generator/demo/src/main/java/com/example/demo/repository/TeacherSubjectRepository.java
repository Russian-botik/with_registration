package com.example.demo.repository;

import com.example.demo.model.TeacherSubject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherSubjectRepository extends JpaRepository<TeacherSubject, Long> {
} 