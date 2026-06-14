package com.studentmanagement.service;

import com.studentmanagement.dto.StudentDTO;

import java.util.List;

public interface StudentService {

    StudentDTO addStudent(StudentDTO studentDTO);

    StudentDTO getStudentById(Long id);

    List<StudentDTO> getAllStudents();

    StudentDTO updateStudent(Long id, StudentDTO studentDTO);

    void deleteStudent(Long id);

    List<StudentDTO> searchStudentByName(String name);

    List<StudentDTO> searchStudentByDepartment(String department);
}
