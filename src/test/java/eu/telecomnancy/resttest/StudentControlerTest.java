package eu.telecomnancy.resttest;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.telecomnancy.resttest.Model.Club;
import eu.telecomnancy.resttest.Model.Student;
import eu.telecomnancy.resttest.ModelVue.ClubModel;
import eu.telecomnancy.resttest.ModelVue.StudentModel;
import eu.telecomnancy.resttest.Service.ClubService;
import eu.telecomnancy.resttest.Service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class StudentControlerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @MockBean
    ClubService clubService;
    @MockBean
    StudentService studentService;

    Club RECORD_1 = new Club("Coffee");
    Club RECORD_2 = new Club("Beer");
    Club RECORD_3 = new Club("Danse");

    ClubModel club1 = new ClubModel(RECORD_1);
    ClubModel club2 = new ClubModel(RECORD_2);
    ClubModel club3 = new ClubModel(RECORD_3);

    Student student = new Student("Student1");
    Student student2 = new Student("Student2");
    Student student3 = new Student("Student3");
    Student president;
    Set<Student> studentSet = new HashSet<>();
    Set<StudentModel> studentModelsSet = new HashSet<>();
    
    //create a setup metode
    @BeforeEach
    public void setUp() {

        student.setId(1L);
        student2.setId(2L);
        student3.setId(3L);
        RECORD_1.setId(1L);
        RECORD_2.setId(2L);
        RECORD_3.setId(3L);



        president = new Student("President");
        president.setName(student.getName());
        president.setId(student.getId());
        president.getPreside().add(RECORD_1);

        studentSet.add(student);
        studentSet.add(student2);
        studentSet.add(student3);
        studentModelsSet.add(new StudentModel(student));
        studentModelsSet.add(new StudentModel(student2));
        studentModelsSet.add(new StudentModel(student3));
        //make moking for the clubService
        Mockito.when(clubService.getAllClubs()).thenReturn(Arrays.asList(RECORD_1, RECORD_2, RECORD_3));
        Mockito.when(clubService.findClubById(RECORD_1.getId())).thenReturn(RECORD_1);
        Mockito.when(clubService.findClubById(RECORD_2.getId())).thenReturn(RECORD_2);
        Mockito.when(clubService.findClubById(RECORD_3.getId())).thenReturn(RECORD_3);
       //make mokito for studentService
        Mockito.when(studentService.getAllStudents()).thenReturn(Arrays.asList(student, student2, student3));
        Mockito.when(studentService.findById(student.getId())).thenReturn(student);
        Mockito.when(studentService.findById(student2.getId())).thenReturn(student2);
        Mockito.when(studentService.findById(student3.getId())).thenReturn(student3);
        Mockito.when(studentService.findByName(student.getName())).thenReturn(student);
        Mockito.when(studentService.findByName(student2.getName())).thenReturn(student2);
        Mockito.when(studentService.findByName(student3.getName())).thenReturn(student3);
        Mockito.when(studentService.findByName("Student1")).thenReturn(student);
        Mockito.when(studentService.findByName("Student2")).thenReturn(student2);
        Mockito.when(studentService.findByName("Student3")).thenReturn(student3);
        Mockito.when(studentService.findByName("Student4")).thenReturn(null);
        Mockito.when(studentService.createStudent(student.getName())).thenReturn(student);
        Mockito.when(studentService.createStudent(student2.getName())).thenReturn(student2);
        Mockito.when(studentService.createStudent(student3.getName())).thenReturn(student3);
        Mockito.when(studentService.setPresident(student.getId(), RECORD_1.getId())).thenReturn(president);

        
    }

    //Make a test who get all student
    @Test
    public void getAllStudent_Sucesse() throws Exception {


        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }
    //Make a test to get a specific student
    @Test
    public void getStudent_Sucesse() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/{id}", student.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect( jsonPath("$.id", is(student.getId().intValue())));
    }
    //make a test to post a student
    @Test
    public void postStudent_Sucesse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.id", is(student.getId().intValue())));
    }
    //create a metode to set a student president
    @Test
    public void setPresident_Sucesse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/students/{id}/president/{clubId}", student.getId(), RECORD_1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.preside[0].name", is(RECORD_1.getName())))
                .andExpect((ResultMatcher) jsonPath("$.name", is(student.getName())));
        ;
    }
    //create a metode to delete a student
    @Test
    public void deleteStudent_Sucesse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/students/{id}", student.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(student)))
                .andExpect(status().isOk());
    }
}
