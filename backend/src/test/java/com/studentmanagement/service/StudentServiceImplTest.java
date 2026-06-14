package com.studentmanagement.service;

import com.studentmanagement.dto.StudentDTO;
import com.studentmanagement.entity.Student;
import com.studentmanagement.exception.StudentNotFoundException;
import com.studentmanagement.mapper.StudentMapper;
import com.studentmanagement.repository.StudentRepository;
import com.studentmanagement.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    private StudentMapper studentMapper;
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        studentMapper = new StudentMapper();
        studentService = new StudentServiceImpl(studentRepository, studentMapper);
    }

    @Test
    void addStudent_ShouldReturnSavedStudent() {
        StudentDTO dto = new StudentDTO(null, "S123", "John", "Doe",
                "john@test.com", "1234567890", "CS", 3, 8.5);
        Student entity = studentMapper.toEntity(dto);
        entity.setId(1L);

        when(studentRepository.save(any(Student.class))).thenReturn(entity);

        StudentDTO result = studentService.addStudent(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("S123", result.getRollNumber());
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void getStudentById_ShouldReturnStudent_WhenFound() {
        Student student = new Student(1L, "S123", "John", "Doe",
                "john@test.com", "1234567890", "CS", 3, 8.5);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        StudentDTO result = studentService.getStudentById(1L);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(studentRepository).findById(1L);
    }

    @Test
    void getStudentById_ShouldThrow_WhenNotFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> studentService.getStudentById(99L));
        verify(studentRepository).findById(99L);
    }

    @Test
    void getAllStudents_ShouldReturnList() {
        List<Student> students = List.of(
                new Student(1L, "S1", "John", "Doe", "j@t.com", "123", "CS", 3, 8.5),
                new Student(2L, "S2", "Jane", "Doe", "ja@t.com", "456", "ECE", 5, 9.0)
        );

        when(studentRepository.findAll()).thenReturn(students);

        List<StudentDTO> result = studentService.getAllStudents();

        assertEquals(2, result.size());
        verify(studentRepository).findAll();
    }

    @Test
    void updateStudent_ShouldUpdateAndReturn_WhenFound() {
        Student existing = new Student(1L, "S123", "Old", "Name",
                "old@test.com", "000", "CS", 3, 8.0);
        StudentDTO update = new StudentDTO(null, "S123", "New", "Name",
                "new@test.com", "111", "ECE", 5, 9.0);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(studentRepository.save(any(Student.class))).thenAnswer(i -> i.getArgument(0));

        StudentDTO result = studentService.updateStudent(1L, update);

        assertEquals("New", result.getFirstName());
        assertEquals("ECE", result.getDepartment());
        assertEquals(9.0, result.getCgpa());
        verify(studentRepository).findById(1L);
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void updateStudent_ShouldThrow_WhenNotFound() {
        StudentDTO update = new StudentDTO(null, "S123", "New", "Name",
                "new@test.com", "111", "ECE", 5, 9.0);

        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> studentService.updateStudent(99L, update));
        verify(studentRepository).findById(99L);
        verify(studentRepository, never()).save(any());
    }

    @Test
    void deleteStudent_ShouldDelete_WhenFound() {
        Student student = new Student(1L, "S123", "John", "Doe",
                "j@t.com", "123", "CS", 3, 8.5);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        studentService.deleteStudent(1L);

        verify(studentRepository).findById(1L);
        verify(studentRepository).delete(student);
    }

    @Test
    void deleteStudent_ShouldThrow_WhenNotFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> studentService.deleteStudent(99L));
        verify(studentRepository).findById(99L);
        verify(studentRepository, never()).delete(any());
    }

    @Test
    void searchStudentByName_ShouldReturnMatchingStudents() {
        List<Student> students = List.of(
                new Student(1L, "S1", "John", "Smith", "j@t.com", "123", "CS", 3, 8.5)
        );

        when(studentRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase("John", "John"))
                .thenReturn(students);

        List<StudentDTO> result = studentService.searchStudentByName("John");

        assertEquals(1, result.size());
        verify(studentRepository).findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase("John", "John");
    }

    @Test
    void searchStudentByDepartment_ShouldReturnMatchingStudents() {
        List<Student> students = List.of(
                new Student(1L, "S1", "John", "Doe", "j@t.com", "123", "CS", 3, 8.5)
        );

        when(studentRepository.findByDepartmentIgnoreCase("CS")).thenReturn(students);

        List<StudentDTO> result = studentService.searchStudentByDepartment("CS");

        assertEquals(1, result.size());
        verify(studentRepository).findByDepartmentIgnoreCase("CS");
    }

    @Test
    void searchStudentByRollNumber_ShouldReturnStudent_WhenFound() {
        Student student = new Student(1L, "S123", "John", "Doe",
                "j@t.com", "123", "CS", 3, 8.5);

        when(studentRepository.findByRollNumber("S123")).thenReturn(Optional.of(student));

        StudentDTO result = studentService.searchStudentByRollNumber("S123");

        assertNotNull(result);
        assertEquals("S123", result.getRollNumber());
        verify(studentRepository).findByRollNumber("S123");
    }

    @Test
    void searchStudentByRollNumber_ShouldThrow_WhenNotFound() {
        when(studentRepository.findByRollNumber("X999")).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class,
                () -> studentService.searchStudentByRollNumber("X999"));
        verify(studentRepository).findByRollNumber("X999");
    }
}
