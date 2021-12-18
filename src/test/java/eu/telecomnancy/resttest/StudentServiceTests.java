package eu.telecomnancy.resttest;

import eu.telecomnancy.resttest.Interface.ClubRepository;
import eu.telecomnancy.resttest.Interface.RoomRepository;
import eu.telecomnancy.resttest.Interface.StudentRepository;
import eu.telecomnancy.resttest.Model.Club;
import eu.telecomnancy.resttest.Model.Student;
import eu.telecomnancy.resttest.Service.ClubService;
import eu.telecomnancy.resttest.Service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;


import javax.sql.DataSource;
import javax.persistence.EntityManager;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class StudentServiceTests {

    @Autowired
    private DataSource dataSource;
    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private EntityManager entityManager;
    @Autowired private StudentRepository studentRepository;
    @Autowired private ClubRepository clubRepository;
    @Autowired private RoomRepository roomRepository;

    @Autowired
    private StudentService studentService;
    @Autowired
    private ClubService clubService;

    @Test
    void contextLoads() {
    }

    @Test
    public void injectedComponentsAreNotNull(){
        assertThat(dataSource).isNotNull();
        assertThat(jdbcTemplate).isNotNull();
        assertThat(entityManager).isNotNull();
        assertThat(studentRepository).isNotNull();
    }

    @Transactional
    @Test
    public void whenValidName_returnStudent() {
        studentRepository.save(new Student("Norbert"));
        Student norbert=studentRepository.findByName("Norbert").get();
        assertThat(norbert.getName()).isEqualTo("Norbert");
    }

    @Transactional
    @Test
    public void whenInvalidName_ReturnStudent() {
    /* Test de l'exception StudentNotFound dans la méthode findByName */
        String badName = "Oupsi";
        Exception exception = assertThrows(NameNotFoundException.class, () -> studentService.findByName(badName));

        String expectedMessage = "Could not find item named Oupsi";
        String foundMessage = exception.getMessage();

        assertTrue(foundMessage.contains(expectedMessage));
    }

    @Transactional
    @Test
    public void whenValidId_ReturnStudent() {
        /* Test de la méthode findById() */
        Student norbert=studentService.createStudent("Norbert");
        studentRepository.saveAndFlush(norbert);
        Student found = studentService.findById(norbert.getId());
        assertThat(found.getName()).isEqualTo(norbert.getName());
    }

    @Transactional
    @Test
    public void whenInvalidId_ReturnStudent() {
long id = 6666;
        Exception exception = assertThrows(IdNotFoundException.class, () -> studentService.findById(id));

        String expectedMessage = "Could not find item " + id;
        String foundMessage = exception.getMessage();
        assertTrue(foundMessage.contains(expectedMessage));
    }

    @Test
    @Transactional
    public void ValidStudentInClub_andItWorks(){
        Student max = studentService.createStudent("Max");
        Club games = clubService.createClub("games");
        max.addClub(games);
        clubRepository.saveAndFlush(games);
        assertTrue(max.getClubs().contains(clubService.findClubByName("games")));
    }

    @Test
    @Transactional
    public void InvalidStudentInClub_andItWorks(){
        Student max = studentService.createStudent("Max");
        Club games = clubService.createClub("games");
        clubService.addStudentToClub(games.getId(),max.getId());
        assertTrue(max.getClubs().contains(clubService.findClubByName("games")));
    }



    @Transactional
    @Test
    public void setStudentofClubPresident() {
        Student norbert=studentService.createStudent("Norbert");
        Student Test = studentService.findById(norbert.getId());
        Club echec= clubService.createClub("Echec");
        norbert.addClub(echec);;
        echec.setPresident(norbert);
        assertThat(Test.getId()).isEqualTo(echec.getPresident().getId());
    }

    @Transactional
    @Test
    public void StudentNotClubPresident() {
        Student norbert = studentService.createStudent("Norbert");
        Student marie = studentService.createStudent("Marie");
        Club echec= clubService.createClub("Echec");
        norbert.addClub(echec);
        marie.addClub(echec);
        echec.setPresident(norbert);
        assertThat(marie.getId()).isNotEqualTo(echec.getPresident().getId());
    }

    @Transactional
    @Test
    public void StudentCreated(){
        studentService.createStudent("Norbert");
        assertThat(studentService.findByName("Norbert")).isNotNull();
    }

    @Transactional
    @Test
    public void StudentDeleted(){
        studentService.createStudent("Norbert");
        Student norbert= studentService.findByName("Norbert");
        studentService.deleteStudent(norbert.getId());
        //Assert a error is thrown
        assertThrows(IdNotFoundException.class, () -> studentService.findById(norbert.getId()));
    }

}

