package com.studentmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studentmanagement.dto.StudentDTO;
import com.studentmanagement.exception.StudentNotFoundException;
import com.studentmanagement.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService;

    @Test
    void addStudent_ShouldReturn201() throws Exception {
        StudentDTO dto = new StudentDTO(null, "S123", "John", "Doe",
                "john@test.com", "1234567890", "CS", 3, 8.5);
        StudentDTO saved = new StudentDTO(1L, "S123", "John", "Doe",
                "john@test.com", "1234567890", "CS", 3, 8.5);

        when(studentService.addStudent(any(StudentDTO.class))).thenReturn(saved);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void addStudent_ShouldReturn400_WhenInvalid() throws Exception {
        StudentDTO invalid = new StudentDTO(null, "", "", "",
                "invalid-email", "", "", -1, 15.0);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getStudentById_ShouldReturn200() throws Exception {
        StudentDTO dto = new StudentDTO(1L, "S123", "John", "Doe",
                "john@test.com", "1234567890", "CS", 3, 8.5);

        when(studentService.getStudentById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.rollNumber").value("S123"));
    }

    @Test
    void getStudentById_ShouldReturn404_WhenNotFound() throws Exception {
        when(studentService.getStudentById(99L)).thenThrow(new StudentNotFoundException(99L));

        mockMvc.perform(get("/api/students/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllStudents_ShouldReturn200() throws Exception {
        List<StudentDTO> students = List.of(
                new StudentDTO(1L, "S1", "John", "Doe", "j@t.com", "123", "CS", 3, 8.5),
                new StudentDTO(2L, "S2", "Jane", "Doe", "ja@t.com", "456", "ECE", 5, 9.0)
        );

        when(studentService.getAllStudents()).thenReturn(students);

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void updateStudent_ShouldReturn200() throws Exception {
        StudentDTO update = new StudentDTO(null, "S123", "Updated", "Name",
                "updated@test.com", "111", "ECE", 5, 9.0);
        StudentDTO updated = new StudentDTO(1L, "S123", "Updated", "Name",
                "updated@test.com", "111", "ECE", 5, 9.0);

        when(studentService.updateStudent(eq(1L), any(StudentDTO.class))).thenReturn(updated);

        mockMvc.perform(put("/api/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"));
    }

    @Test
    void deleteStudent_ShouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/students/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteStudent_ShouldReturn404_WhenNotFound() throws Exception {
        doThrow(new StudentNotFoundException(99L)).when(studentService).deleteStudent(99L);

        mockMvc.perform(delete("/api/students/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void searchStudentByName_ShouldReturn200() throws Exception {
        List<StudentDTO> students = List.of(
                new StudentDTO(1L, "S1", "John", "Smith", "j@t.com", "123", "CS", 3, 8.5)
        );

        when(studentService.searchStudentByName("John")).thenReturn(students);

        mockMvc.perform(get("/api/students/search/name")
                        .param("name", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void searchStudentByRollNumber_ShouldReturn200() throws Exception {
        StudentDTO dto = new StudentDTO(1L, "S123", "John", "Doe",
                "j@t.com", "123", "CS", 3, 8.5);

        when(studentService.searchStudentByRollNumber("S123")).thenReturn(dto);

        mockMvc.perform(get("/api/students/search/roll-number")
                        .param("rollNumber", "S123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rollNumber").value("S123"));
    }

    @Test
    void searchStudentByRollNumber_ShouldReturn404_WhenNotFound() throws Exception {
        when(studentService.searchStudentByRollNumber("X999"))
                .thenThrow(new StudentNotFoundException("Student not found with roll number: X999"));

        mockMvc.perform(get("/api/students/search/roll-number")
                        .param("rollNumber", "X999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void searchStudentByDepartment_ShouldReturn200() throws Exception {
        List<StudentDTO> students = List.of(
                new StudentDTO(1L, "S1", "John", "Doe", "j@t.com", "123", "CS", 3, 8.5)
        );

        when(studentService.searchStudentByDepartment("CS")).thenReturn(students);

        mockMvc.perform(get("/api/students/search/department")
                        .param("department", "CS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }
}
