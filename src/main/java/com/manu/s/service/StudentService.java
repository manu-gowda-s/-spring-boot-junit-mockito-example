package com.manu.s.service;

import com.manu.s.dto.StudentDTO;
import com.manu.s.entity.Student;
import com.manu.s.exception.EmailAlreadyRegisteredException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface StudentService
{

    //save
    StudentDTO saveStudent(StudentDTO studentDTO) throws EmailAlreadyRegisteredException;

    StudentDTO findByID(Long id);

    List<Student> findAll();

    void deleteById(Long id);

    StudentDTO changeEmail(StudentDTO studentDTO, Long id);

    //put mapping
    StudentDTO updateStudent(StudentDTO studentDTO, Long id);

    Map<String, List<Student>> groupByBranch();

    List<Student> findStudentsByCity(String city);

}
