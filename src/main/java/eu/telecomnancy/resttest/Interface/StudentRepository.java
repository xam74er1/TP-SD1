package eu.telecomnancy.resttest.Interface;

import eu.telecomnancy.resttest.Model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query
    Optional<Student> findByName(String name);
}
