package com.manu.s.repository;

import com.manu.s.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long >
{

    Optional<Student> findByEmail(String email);

    @Query("SELECT s FROM Student s WHERE s.city = :city")
    List<Student> findByCity(@Param("city") String city);

}
