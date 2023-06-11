package com.manu.s.controller;

import com.manu.s.dto.StudentDTO;
import com.manu.s.entity.Student;
import com.manu.s.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1")
public class StudentController
{

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentService studentService;

    // Save Student
    @PostMapping("/student")
    public ResponseEntity<StudentDTO> saveNewStudent(@RequestBody StudentDTO studentDTO)
    {
        StudentDTO savedStudentDto = studentService.saveStudent(studentDTO);
        return new ResponseEntity<>(savedStudentDto, HttpStatus.CREATED);
    }

    // find by id
    @GetMapping("/student/{id}")
    public ResponseEntity<StudentDTO> findByStudentId(@PathVariable Long id)
    {
        StudentDTO byID = studentService.findByID(id);
        LOGGER.info(String.format("Student for this id %s",byID.toString()));
        return new ResponseEntity<>(byID, HttpStatus.FOUND);
    }

    @GetMapping("/student/all")
    public ResponseEntity<List<Student>> findAllStudentsList()
    {
        List<Student> allStudents = studentService.findAll();
        return new ResponseEntity<>(allStudents, HttpStatus.FOUND);
    }

    @DeleteMapping("/student/{id}")
    public  ResponseEntity<String> deleteStudent(@PathVariable Long id)
    {
        studentService.deleteById(id);
        LOGGER.info(String.format("STUDENT ID Removed %s" , id.toString()));
        return new ResponseEntity<>("Student Removed !!",HttpStatus.OK);
    }

    // Patch Mapping Change Email by student ID
    @PatchMapping("/student/change/email/by/{id}")
    public ResponseEntity<StudentDTO> changeStudentEmail(@RequestBody StudentDTO studentDTO, @PathVariable Long id)
    {
        StudentDTO emailChangedStudent = studentService.changeEmail(studentDTO,id);
        LOGGER.info(String.format("Student Email is Changed to --> %s", emailChangedStudent.getEmail()));
        return new ResponseEntity<>(emailChangedStudent, HttpStatus.OK);
    }

    // put mapping
    @PutMapping("/student/update/{id}")
    public ResponseEntity<StudentDTO> updateStudentDetails(@RequestBody StudentDTO studentDTO, @PathVariable Long id)
    {
        StudentDTO dto = studentService.updateStudent(studentDTO, id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    // streams
    @GetMapping("/student/branch/all")
    public ResponseEntity<Map<String, List<Student>>> getStudentsByBranch()
    {
        Map<String, List<Student>> listMap = studentService.groupByBranch();
        return new ResponseEntity<>(listMap,HttpStatus.FOUND);
    }

    // Get the List of students  by city
    @GetMapping("/student/city/{city}")
    public ResponseEntity<List<Student>> findAllStudentsByCity(@PathVariable String city)
    {
        List<Student> students = studentService.findStudentsByCity(city);
        return new ResponseEntity<>(students, HttpStatus.FOUND);

    }


}
