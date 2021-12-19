package eu.telecomnancy.resttest.Controler;

import eu.telecomnancy.resttest.ModelVue.ClubModel;
import eu.telecomnancy.resttest.ModelVue.StudentModel;
import eu.telecomnancy.resttest.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
public class StudentController {
    @Autowired
    private StudentService studentService;

    //Make a get request to /students who return s all the students
    @GetMapping("/students")
    public Iterable<StudentModel> getAllStudents() {
     return studentService.getAllStudents().stream().map(StudentModel::new).collect(Collectors.toSet());

    }
    //Make a get request to /students/id where id is the student id
    @GetMapping("/students/{id}")
    public ResponseEntity<StudentModel> getStudentById(@PathVariable long id) {
        return ResponseEntity.ok(new StudentModel(studentService.findById( id)));
    }
    //Make a post request to /students who create a new student
    @PostMapping("/students")
    public ResponseEntity<StudentModel> createStudent(@RequestBody StudentModel studentModel) {
        return ResponseEntity.ok(new StudentModel(studentService.createStudent(studentModel.getName())));
    }

    //Create a methode to update a student
    @PutMapping("/students/{id}")
    public ResponseEntity<StudentModel> updateStudent(@PathVariable long id, @RequestBody StudentModel studentModel) {
        return ResponseEntity.ok(new StudentModel(studentService.updateStudent(id, studentModel.getName())));
    }

    //Create a metode to set this student president
    @PutMapping("/students/{id}/president/{clubId}")
    public ResponseEntity<StudentModel> setPresident(@PathVariable long id, @PathVariable long clubId) {
        return ResponseEntity.ok(new StudentModel(studentService.setPresident(id, clubId)));
    }

    @DeleteMapping("/students/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }
}
