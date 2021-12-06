package eu.telecomnancy.resttest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;
import javax.sql.DataSource;

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
        Student norbert=new Student("Norbert");
        studentRepository.saveAndFlush(norbert);
        Student found = studentService.findById(norbert.getId());
        assertThat(found.getName()).isEqualTo(norbert.getName());
    }


}
