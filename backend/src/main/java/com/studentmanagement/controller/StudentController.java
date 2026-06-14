package com.studentmanagement.controller;

import com.studentmanagement.dto.StudentDTO;
import com.studentmanagement.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentDTO> addStudent(@Valid @RequestBody StudentDTO studentDTO) {
        StudentDTO created = studentService.addStudent(studentDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
        StudentDTO student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        List<StudentDTO> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentDTO studentDTO
    ) {
        StudentDTO updated = studentService.updateStudent(id, studentDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<StudentDTO>> searchStudentByName(@RequestParam String name) {
        List<StudentDTO> students = studentService.searchStudentByName(name);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/search/department")
    public ResponseEntity<List<StudentDTO>> searchStudentByDepartment(@RequestParam String department) {
        List<StudentDTO> students = studentService.searchStudentByDepartment(department);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/search/roll-number")
    public ResponseEntity<StudentDTO> searchStudentByRollNumber(@RequestParam String rollNumber) {
        StudentDTO student = studentService.searchStudentByRollNumber(rollNumber);
        return ResponseEntity.ok(student);
    }
}
