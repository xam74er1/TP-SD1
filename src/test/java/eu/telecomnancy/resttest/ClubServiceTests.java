package eu.telecomnancy.resttest;

import eu.telecomnancy.resttest.Interface.ClubRepository;
import eu.telecomnancy.resttest.Interface.RoomRepository;
import eu.telecomnancy.resttest.Interface.StudentRepository;
import eu.telecomnancy.resttest.Model.Club;
import eu.telecomnancy.resttest.Model.Room;
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

@SpringBootTest
//@Testcontainers
public class ClubServiceTests {
    private static final String dockerImage="mariadb:latest";

//    @Container
//    public static MariaDBContainer mariadb= new MariaDBContainer(dockerImage);

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

    @Test
    @Transactional
    public void createARoom_andItExists(){
        String amphi="amphi";
        clubService.createRoom(amphi);
        Room room=clubService.getRoom(amphi);
        assertThat(room.getName()).isEqualTo(amphi);
    }

    @Test
    @Transactional
    public void findARoom_andItDoesntExists(){
        String amphi="amphi";
        clubService.createRoom(amphi);
        assertThrows(NameNotFoundException.class, () -> clubService.getRoom("crap"));
    }

    @Test
    @Transactional
    public void addARoomToAClub_andItWorks(){
        String amphi="amphi";
        String echec="echec";

        clubService.createClub(echec);
        clubService.createRoom(amphi);
        clubService.setClubRoom(echec,amphi);
        Club club=clubService.findClubByName(echec);
        assertThat(club.getPlace().get().getName()).isEqualTo(amphi);
    }

}
