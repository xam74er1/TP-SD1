package eu.telecomnancy.resttest.Service;

import eu.telecomnancy.resttest.*;
import eu.telecomnancy.resttest.Interface.ClubRepository;
import eu.telecomnancy.resttest.Interface.StudentRepository;
import eu.telecomnancy.resttest.Model.Club;
import eu.telecomnancy.resttest.Model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository repository;

    @Autowired
    private ClubRepository clubRepository;

    public List<Student> getAllStudents() {return repository.findAll();}

    public Student findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new IdNotFoundException(id));
    }

    public Student findByName(String name) {
        Student s = repository.findByName(name).orElseThrow(() -> new NameNotFoundException(name));
        if (s == null) {
            throw new NameNotFoundException(name);
        }
        return s;
    }

    public Long getStudentCount() { return repository.count();}


    public void deleteStudent(Long id) {
        repository.deleteById(id);
    }

    public void deleteStudent(String name) {
        repository.findByName(name).ifPresent(theUser -> repository.delete(theUser));
    }

    /* Mise à jour du nom d'un étudiant */
    public void updateStudent(Long id, String newName) {
        repository.findById(id)
                .map(student -> {
                    student.setName(newName);
                    return repository.save(student);
                });
    }

    public Student createStudent(String name) {
       return repository.save(new Student(name));
    }

    public void addClub(Long studentId, String clubName) {
        var student = repository.findById(studentId).orElseThrow(() -> new IdNotFoundException(studentId));

        var updatedClub = clubRepository.findByName(clubName)
                .orElseGet(() -> clubRepository.save(new Club(clubName)));

        student.addClub(updatedClub);
    }


}
