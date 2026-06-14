package com.studentmanagement.mapper;

import com.studentmanagement.dto.StudentDTO;
import com.studentmanagement.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentMapperTest {

    private StudentMapper studentMapper;

    @BeforeEach
    void setUp() {
        studentMapper = new StudentMapper();
    }

    @Test
    void toDTO_ShouldMapAllFields() {
        Student student = new Student(1L, "S123", "John", "Doe",
                "john@test.com", "1234567890", "CS", 3, 8.5);

        StudentDTO dto = studentMapper.toDTO(student);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("S123", dto.getRollNumber());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals("john@test.com", dto.getEmail());
        assertEquals("1234567890", dto.getPhone());
        assertEquals("CS", dto.getDepartment());
        assertEquals(3, dto.getSemester());
        assertEquals(8.5, dto.getCgpa());
    }

    @Test
    void toDTO_ShouldReturnNull_WhenStudentIsNull() {
        assertNull(studentMapper.toDTO(null));
    }

    @Test
    void toEntity_ShouldMapAllFields() {
        StudentDTO dto = new StudentDTO(1L, "S123", "John", "Doe",
                "john@test.com", "1234567890", "CS", 3, 8.5);

        Student student = studentMapper.toEntity(dto);

        assertNotNull(student);
        assertEquals(1L, student.getId());
        assertEquals("S123", student.getRollNumber());
        assertEquals("John", student.getFirstName());
        assertEquals("Doe", student.getLastName());
        assertEquals("john@test.com", student.getEmail());
        assertEquals("1234567890", student.getPhone());
        assertEquals("CS", student.getDepartment());
        assertEquals(3, student.getSemester());
        assertEquals(8.5, student.getCgpa());
    }

    @Test
    void toEntity_ShouldReturnNull_WhenDTOIsNull() {
        assertNull(studentMapper.toEntity(null));
    }
}
