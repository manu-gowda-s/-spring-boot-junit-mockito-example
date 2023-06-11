package com.manu.s.repository;

import com.manu.s.config.TestDatabaseConfig;
import com.manu.s.entity.Student;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
@SpringJUnitConfig(TestDatabaseConfig.class)
public class StudentRepositoryTest
{
    // this test class is to test the repository layer

    @Autowired
    private StudentRepository studentRepository;

    @Test
    @DisplayName(value = "This test is to save the Student to the database")
    public void givenStudentObject_whenSave_thenReturnStudent()
    {
        //GIVEN
        Student student = Student.builder().id(101L).firstName("Tanu").lastName("Gowda")
                .email("tanu@gmail.com").city("Bengaluru").branch("IS").build();

        //WHEN
        Student savedStudent = studentRepository.save(student);

        //THEN
        Assertions.assertNotNull(savedStudent);
        Assertions.assertEquals(student.getFirstName(), savedStudent.getFirstName());
        Assertions.assertEquals(savedStudent.getEmail(),"tanu@gmail.com");
        Assertions.assertNotNull(savedStudent.getId());
    }

    @Test
    @DisplayName(value = "Thi test will return a student based on the student ID")
    public void giveId_whenStudentId_thenReturnThatStudent()
    {
        //GIVEN
        Student student = Student.builder().id(102L).firstName("Kamal").lastName("Kumar")
                .email("kmal@gmail.com").city("Bengaluru").branch("CS").build();

        //WHEN
        Student savedStudent = studentRepository.save(student);
        Student optionalStudent = studentRepository.findById(savedStudent.getId()).get();

        //then
        Assertions.assertNotNull(optionalStudent);
        Assertions.assertEquals(optionalStudent.getEmail(),"kmal@gmail.com");
        Assertions.assertEquals(optionalStudent.getCity(),"Bengaluru");
        Assertions.assertEquals(optionalStudent.getBranch(),"CS");
    }

    @Test
    @DisplayName(value = "This test will return list of all the students")
    public void given_whenListOfStudent_thenReturnStudentList()
    {
        //GIVEN
        Student student1 = Student.builder().id(103L).firstName("Sanvi").lastName("Gowda")
                .email("Sanvi@gmail.com").city("Bengaluru").branch("IS").build();
        Student student2 = Student.builder().id(104L).firstName("Kiran").lastName("Kumar")
                .email("Kiran@gmail.com").city("Mysore").branch("ME").build();

        List<Student> studentList = new ArrayList<>(Arrays.asList(student1,student2));
        studentRepository.saveAll(studentList);

        //WHEN
        List<Student> students = studentRepository.findAll();

        Assertions.assertNotNull(students);
        Assertions.assertEquals(students.size(),2);
        Assertions.assertEquals(students.get(0).getCity(),"Bengaluru");
        Assertions.assertEquals(students.get(0).getFirstName(),"Sanvi");
        Assertions.assertEquals(students.get(1).getBranch(),"ME");
        Assertions.assertEquals(students.get(1).getEmail(),"Kiran@gmail.com");
    }

    @Test
    @DisplayName(value = "this test will take student email and return student")
    public void givenEmail_whenEmail_thenReturnStudent()
    {
        //WHEN
        String email = "newMail@gmail.com";
        Student student1 = Student.builder().id(107L).firstName("Sanvi").lastName("Gowda")
                .email(email).city("Bengaluru").branch("EEE").build();

        Student student = studentRepository.save(student1);

        Optional<Student> student2 = studentRepository.findByEmail(student.getEmail());

        Assertions.assertNotNull(student2);
        Assertions.assertTrue(student2.isPresent());
        Assertions.assertEquals(student2.get().getEmail(),"newMail@gmail.com");
        Assertions.assertEquals(student2.get().getFirstName(),student1.getFirstName());
    }

    @Test
    @DisplayName(value = "")
    public void givenCity_whenCity_thenReturnListOfStudent()
    {
        //GIVEN
        Student student1 = Student.builder().id(201L).firstName("Sanvi").lastName("Gowda")
                .email("sg@gmail.com").city("Bengaluru").branch("IS").build();
        Student student2 = Student.builder().id(203L).firstName("Kiran").lastName("Kumar")
                .email("kk@gmail.com").city("Mysore").branch("ME").build();
        Student student3 = Student.builder().id(205L).firstName("Smitha").lastName("Kiran")
                .email("smtha@gmail.com").city("Bengaluru").branch("IS").build();
        Student student4 = Student.builder().id(208L).firstName("Hanuma").lastName("Kumar")
                .email("h@gmail.com").city("Mysore").branch("ME").build();

        List<Student>studentList= new ArrayList<>(Arrays.asList(student1,student2,student3,student4));
        studentRepository.saveAll(studentList);

        //WHEN
        List<Student> mysoreStudents = studentRepository.findByCity("Mysore");

        //THEN
        Assertions.assertFalse(mysoreStudents.isEmpty());
        Assertions.assertEquals(mysoreStudents.size(),2);
        Assertions.assertEquals(mysoreStudents.get(0).getCity(),"Mysore");
        Assertions.assertEquals(mysoreStudents.get(1).getEmail(),"h@gmail.com");
    }


}
