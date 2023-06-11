package com.manu.s.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manu.s.dto.StudentDTO;
import com.manu.s.entity.Student;
import com.manu.s.service.StudentService;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(StudentController.class)
public class StudentControllerTest
{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;


    // Helper method to convert an object to JSON string
    private static String asJsonString(Object obj) throws JsonProcessingException
    {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    @Test
    @DisplayName("This test is used to create a new Student")
    public void test_SaveNewStudent_success() throws Exception
    {
        //create an StudentDTO object
        StudentDTO studentDtoTest = StudentDTO.builder().id(100L).firstName("Manu").lastName("Gowda")
                .email("mg@gmail.com").city("Mysore").branch("CS").build();

        // convert StudentDto into JPA
        Student student = modelMapper.map(studentDtoTest,Student.class);

        StudentDTO studentDTO = modelMapper.map(student, StudentDTO.class);

        // Mock the StudentService's saveStudent() method to return the created user
        Mockito.when(studentService.saveStudent(any(StudentDTO.class))).thenReturn(studentDTO);

        // Perform the POST request
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(studentDtoTest)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",is(studentDTO.getFirstName())))
                .andExpect(jsonPath("$.city", is(studentDTO.getCity())));
//                .andExpect(jsonPath("$", hasSize(1)));
//                .andExpect(jsonPath("$.city").value("manu@gamil.com"));

        // Verify that the student service's saveStudent method was called with the StudentDTO object
        verify(studentService,times(1)).saveStudent(any(StudentDTO.class));

    }

    @Test
    @DisplayName("This test will take the id as parameter and return the Student object")
    public void test_findByStudentId_success() throws Exception
    {
        //create Student Entity
        Student student = Student.builder().id(200L).firstName("Tanu").lastName("Gowda")
                .email("tanu@gmail.com").city("Bengaluru").branch("IS").build();

        //convert entity into DTO
       StudentDTO studentDTO = modelMapper.map(student, StudentDTO.class);

       when(studentService.findByID(200L)).thenReturn(studentDTO);

       mockMvc.perform(MockMvcRequestBuilders
               .get("/api/v1/student/{id}", student.getId())
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isFound())
               .andExpect(jsonPath("$.firstName",is(studentDTO.getFirstName())))
               .andExpect(jsonPath("$.id", is(studentDTO.getId().intValue())))
               .andExpect(jsonPath("$.email", is(studentDTO.getEmail())));

       verify(studentService, times(1)).findByID(200L);
    }

    @Test
    @DisplayName(value = "This test should return all the student list")
    public void test_findAllStudentsList_success() throws Exception
    {
        Student student1 = new Student(300L,"Smitha","Rai","smith@gmail.com","Delhi","CS");
        Student student2 = new Student(400L,"Harsha","Singh","h@gmail.com","Mumbai","ME");

        List<Student> studentList = new ArrayList<>(Arrays.asList(student1,student2));

        when(studentService.findAll()).thenReturn(studentList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/student/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)));

        verify(studentService,times(1)).findAll();
    }

    @Test
    @DisplayName(value = "This test will delete the student by using student ID")
    public void test_deleteStudent_success() throws Exception
    {
        //create Student Entity
        Student student = Student.builder().id(500L).firstName("Hobbs").lastName("Pug")
                .email("hobbs@gmail.com").city("Mysore").branch("CIVIL").build();

        // note When we cannot use because this method return void from StudentService

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/v1/student/{id}", student.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(studentService, times(1)).deleteById(500L);
    }

    @Test
    @DisplayName(value = "This test will  only update the student email by using Student ID")
    public void test_changeStudentEmail_success() throws Exception
    {
       StudentDTO studentDTO = new StudentDTO();
       studentDTO.setEmail("new@gmail.com");
       Long id = 20L;

       StudentDTO setNewMail = new StudentDTO();
       setNewMail.setEmail(studentDTO.getEmail());

       when(studentService.changeEmail(studentDTO, id)).thenReturn(setNewMail);

       mockMvc.perform(MockMvcRequestBuilders
               .patch("/api/v1/student/change/email/by/{id}", 20L)
               .contentType(MediaType.APPLICATION_JSON)
               .content(asJsonString(setNewMail)))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.email", is("new@gmail.com")));

       verify(studentService, times(1)).changeEmail(studentDTO, id);
    }


    @Test
    @DisplayName(value = "This test is used to update the student details by using Student ID")
    public void test_updateStudentDetails_success() throws Exception
    {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setFirstName("Manu");
        studentDTO.setLastName("Mike");
        studentDTO.setEmail("mm@gmail.com");
        studentDTO.setCity("Mumbai");
        studentDTO.setBranch("ME");
        Long id = 60L;

        Student student = new Student();
        student.setFirstName(studentDTO.getFirstName());
        student.setLastName(studentDTO.getLastName());
        student.setEmail(studentDTO.getEmail());
        student.setCity(studentDTO.getCity());
        student.setBranch(studentDTO.getBranch());

        StudentDTO updateStudentDto = modelMapper.map(student, StudentDTO.class);

        when(studentService.updateStudent(studentDTO,id)).thenReturn(updateStudentDto);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/api/v1/student/update/{id}",60L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updateStudentDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.firstName",is("Manu")))
                .andExpect(jsonPath("$.lastName",is("Mike")))
                .andExpect(jsonPath("$.email",is(updateStudentDto.getEmail())));
    }

    @Test
    @DisplayName(value = "This test will return groupByBranch with list of students by same branch")
    public void test_getStudentsByBranch_success() throws Exception
    {
        Map<String, List<Student>> listMap = new HashMap<>();
        List<Student> isStudentList = new ArrayList<>();
        List<Student> meStudentList = new ArrayList<>();

        Student student1 = Student.builder().id(200L).firstName("Tanu").lastName("Gowda")
                .email("tanu@gmail.com").city("Bengaluru").branch("IS").build();

        Student student2 = Student.builder().id(300L).firstName("Manu").lastName("Singh")
                .email("manu@gmail.com").city("Mysore").branch("ME").build();

        isStudentList.add(student1);
        listMap.put("IS",isStudentList);
        meStudentList.add(student2);
        listMap.put("ME", meStudentList);

        when(studentService.groupByBranch()).thenReturn(listMap);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/student/branch/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isFound());

        Mockito.verify(studentService, Mockito.times(1)).groupByBranch();

    }

    @Test
    @DisplayName(value = "This test is for return list os student by city")
    public void test_findAllStudentsByCity_success() throws Exception
    {
        String city = "Mysore";
        Student student2 = Student.builder().id(300L).firstName("Manu").lastName("Singh")
                .email("manu@gmail.com").city(city).branch("ME").build();
        Student student3 = Student.builder().id(500L).firstName("Jack").lastName("Kin")
                .email("manu@gmail.com").city(city).branch("ME").build();

        List<Student> studentList = new ArrayList<>(Arrays.asList(student2,student3));

        when(studentService.findStudentsByCity(city)).thenReturn(studentList);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/student/city/{city}", city)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(300L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].firstName").value("Manu"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].city").value("Mysore"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].id").value(500L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].firstName").value("Jack"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].city").value("Mysore"));


        verify(studentService,times(1)).findStudentsByCity(city);
    }

}
