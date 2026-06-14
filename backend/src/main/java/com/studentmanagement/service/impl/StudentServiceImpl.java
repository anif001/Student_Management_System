package com.studentmanagement.service.impl;

import com.studentmanagement.dto.StudentDTO;
import com.studentmanagement.entity.Student;
import com.studentmanagement.exception.StudentNotFoundException;
import com.studentmanagement.mapper.StudentMapper;
import com.studentmanagement.repository.StudentRepository;
import com.studentmanagement.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Override
    public StudentDTO addStudent(StudentDTO studentDTO) {
        Student student = studentMapper.toEntity(studentDTO);
        Student savedStudent = studentRepository.save(student);
        return studentMapper.toDTO(savedStudent);
    }

    @Override
    public StudentDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        return studentMapper.toDTO(student);
    }

    @Override
    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(studentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));

        existingStudent.setRollNumber(studentDTO.getRollNumber());
        existingStudent.setFirstName(studentDTO.getFirstName());
        existingStudent.setLastName(studentDTO.getLastName());
        existingStudent.setEmail(studentDTO.getEmail());
        existingStudent.setPhone(studentDTO.getPhone());
        existingStudent.setDepartment(studentDTO.getDepartment());
        existingStudent.setSemester(studentDTO.getSemester());
        existingStudent.setCgpa(studentDTO.getCgpa());

        Student updatedStudent = studentRepository.save(existingStudent);
        return studentMapper.toDTO(updatedStudent);
    }

    @Override
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        studentRepository.delete(student);
    }

    @Override
    public List<StudentDTO> searchStudentByName(String name) {
        return studentRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name)
                .stream()
                .map(studentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentDTO> searchStudentByDepartment(String department) {
        return studentRepository
                .findByDepartmentIgnoreCase(department)
                .stream()
                .map(studentMapper::toDTO)
                .collect(Collectors.toList());
    }
}
