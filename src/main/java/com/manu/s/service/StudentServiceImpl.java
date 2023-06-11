package com.manu.s.service;

import com.manu.s.dto.StudentDTO;
import com.manu.s.entity.Student;
import com.manu.s.exception.EmailAlreadyRegisteredException;
import com.manu.s.exception.StudentIdNotFoundException;
import com.manu.s.repository.StudentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService
{
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public StudentDTO saveStudent(StudentDTO studentDTO) throws EmailAlreadyRegisteredException {
        Student student = modelMapper.map(studentDTO, Student.class);

        //check whether Student exits or not by Email
       Optional<Student> studentEmail = studentRepository.findByEmail(student.getEmail());

       if(studentEmail.isPresent()){
           throw new EmailAlreadyRegisteredException("Student already Registered with id : " + studentEmail.get().getEmail());
       }else {
           Student savedStudent = studentRepository.save(student);
           return modelMapper.map(savedStudent, StudentDTO.class);
       }
    }

    @Override
    public StudentDTO findByID(Long id)
    {
        Student byId = studentRepository.findById(id).orElseThrow(
                () -> new StudentIdNotFoundException("Student Id Not found please check id " + id)
        );
        return modelMapper.map(byId, StudentDTO.class);
    }

    @Override
    public List<Student> findAll() {
       return studentRepository.findAll()
               .stream()
               .filter(id -> id.getId() > 0)
               .distinct().collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id)
    {
        //first we check I'd be valid or not
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new StudentIdNotFoundException("Student Id Not found please check id " + id)
        );
         studentRepository.deleteById(id);
    }

    // Patch Mapping change only email
    @Override
    public StudentDTO changeEmail(StudentDTO studentDTO, Long id)
    {
        //convert dto into jpa
        Student student = modelMapper.map(studentDTO, Student.class);

        //first we check I'd be valid or not
        Optional<Student> optionalStudent = studentRepository.findById(student.getId());
        if(optionalStudent.isEmpty()){
            throw new StudentIdNotFoundException("Student Id Not found please check id " + id);
        }else {
            Student std = optionalStudent.get();
            std.setEmail(student.getEmail());

            Student emailChanged = studentRepository.save(std);
            return modelMapper.map(emailChanged, StudentDTO.class);
        }
    }

    @Override
    public StudentDTO updateStudent(StudentDTO studentDTO, Long id)
    {
        //convert dto to jpa
        Student student = modelMapper.map(studentDTO, Student.class);
        //first we check I'd be valid or not
        Student byId = studentRepository.findById(student.getId()).orElseThrow(
                () -> new StudentIdNotFoundException("Student Id Not found please check id " + id)
        );

        byId.setFirstName(studentDTO.getFirstName());
        byId.setLastName(studentDTO.getLastName());
        byId.setEmail(studentDTO.getEmail());
        byId.setCity(studentDTO.getCity());
        byId.setBranch(studentDTO.getBranch());

        Student updated = studentRepository.save(byId);
        return modelMapper.map(updated, StudentDTO.class);
        }

    @Override
    public Map<String, List<Student>> groupByBranch() {
        return studentRepository.findAll()
                .stream()
                .filter(b -> b.getBranch() != null)
                .collect(Collectors
                        .groupingBy(Student::getBranch));
    }

    @Override
    public List<Student> findStudentsByCity(String city) {
        return studentRepository.findByCity(city);
    }
}
