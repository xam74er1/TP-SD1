package eu.telecomnancy.resttest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/*
## Test de l'entity Student
Ici, on teste l'entity student et la création de relations avec les clubs.

* On utilise TestContainer qui démarre un container docker avec MariaDB chaque fois que les tests sont lancés.

* Chaque test est @Transactional. L'exécution est annulée à la fin du test ce qui permet de conserver une base propre

* Disclaimer : certains tests ne sont pas "optimisés"
* Disclaimer 2 : l'utilisation de Optional est parfois approximative.
 */

//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
class StudentRepositoryTests {

    @Autowired
    private DataSource dataSource;
    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private EntityManager entityManager;
    @Autowired private StudentRepository studentRepository;
    @Autowired private ClubRepository clubRepository;
    @Autowired private RoomRepository roomRepository;

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
    public void whenSaved_thenFindsByName() {
        studentRepository.save(new Student("Canard"));
        assertThat(studentRepository.findByName("Canard")).isNotNull();
    }

    @Test
    public void whenSaved_thenFindsById() {
        Student s=new Student("Bobard");
        studentRepository.save(s);
        assertThat(studentRepository.findById(s.getId())).isNotNull();
    }

    @Transactional
//org.hibernate.LazyInitializationException: failed to lazily initialize a collection of role: eu.telecomnancy.jpatest.Student.clubs, could not initialize proxy - no Session
    @Test void assignStudentToClub() {
        Student s=new Student("Louis");
        Club c=new Club("Pinard");
        clubRepository.save(c);
        studentRepository.save(s);
        s.addClub(c);
        assertThat(clubRepository.findByName("Pinard")).isPresent();
        assertThat(studentRepository.findByName("Louis").get().getClubs().size()).isEqualTo(1);
    }

    @Test void assignStudentToClub2() {
        Student s=new Student("André");
        Student t=new Student("Francine");
        Student u=new Student("Zébulon");
        Club c=new Club("Cheval");
        Club d = new Club("Piscine");

        clubRepository.save(c);
        clubRepository.save(d);
        studentRepository.save(s);
        studentRepository.save(t);
        studentRepository.save(u);
        c.addMember(s);
        c.addMember(t);
        c.addMember(u);
        d.addMember(s);
        assertThat(clubRepository.findByName("Cheval")).isNotNull();
        assertThat(clubRepository.findByName("Piscine")).isNotNull();
        Set<Student> cheval=c.getMembers();
        assertThat(cheval.size()).isEqualTo(3);
        Set<Student> piscine=d.getMembers();
        assertThat(piscine.size()).isEqualTo(1);

    }

    @Transactional
    @Test void assignSeveralStudentsToClub() {
        Student s=new Student("Hélène");
        Student t=new Student("Raymond");
        Club c=new Club("Dancing");
        Club d=new Club("Programming");


        clubRepository.save(c);
        clubRepository.save(d);
        studentRepository.save(s);
        studentRepository.save(t);

        s.addClub(c);
        s.addClub(d);
        t.addClub(c);

        // voir https://www.baeldung.com/assertj-exception-assertion
//        assertThatExceptionOfType(DataIntegrityViolationException.class).isThrownBy(()->studentRepository.saveAndFlush(t));

        assertThat(clubRepository.findByName("Dancing").isPresent()).isTrue();
        Optional<Club> club=clubRepository.findByName("Dancing");
        Optional<Student> student=studentRepository.findByName("Hélène");
        assertThat(studentRepository.findByName("Hélène")).isNotNull();
        if (club.isPresent()) {
            Set<Student> students = club.get().getMembers();
            Set<Club> clubs = student.get().getClubs();
            assertThat(clubs.size()).isEqualTo(2);
            System.out.println("CLUBS " + clubs);
        }
    }

    @Transactional
    @Test void assignSeveralStudentsAndPresidentToClub() {
        Student s=new Student("Hélène");
        Student t=new Student("Raymond");
        Club c=new Club("Dancing");
        Club d=new Club("Programming");


        clubRepository.save(c);
        clubRepository.save(d);
        studentRepository.save(s);
        studentRepository.save(t);

        s.addClub(c);
        s.addClub(d);
        t.addClub(c);
        c.setPresident(s);
        d.setPresident(s);

        // voir https://www.baeldung.com/assertj-exception-assertion
//        assertThatExceptionOfType(DataIntegrityViolationException.class).isThrownBy(()->studentRepository.saveAndFlush(t));

        assertThat(clubRepository.findByName("Dancing").isPresent()).isTrue();
        Optional<Club> club=clubRepository.findByName("Dancing");
        Student student=studentRepository.findByName("Hélène").get();
        // Helene existe dans la base
        assertThat(studentRepository.findByName("Hélène")).isNotNull();
        if (club.isPresent()) {
            Set<Student> students = club.get().getMembers();
            Set<Club> clubs = student.getClubs();
            // Helene est membre de 2 clubs
            assertThat(clubs.size()).isEqualTo(2);
            System.out.println("CLUBS " + clubs);
            // Helene préside 2 clubs
            assertThat(student.getPreside().size()).isEqualTo(2);
        }
    }

    @Transactional
    @Test void closeClub() {
        Student s=new Student("Gérard");
        Student t=new Student("Violette");
        Club c=new Club("Gulping");

        clubRepository.save(c);
        studentRepository.save(s);
        studentRepository.save(t);

        s.addClub(c);
        t.addClub(c);

        c.closeClub();
        assertThat(clubRepository.findByName("Gulping").isPresent()).isTrue();
        assertThat(clubRepository.findByName("Gulping").get().getMembers()).isEmpty();

    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Test
    void addAMemberToAClub_IncreaseTheSizeOfTheClub() {
        Student s=new Student("Sylvain");
        Student t=new Student("Aline");
        Club c=new Club("Running");
        clubRepository.save(c);
        studentRepository.save(s);
        studentRepository.save(t);
        c.addMember(s);
        c.addMember(t);

        clubRepository.saveAndFlush(c);
        assertThat(clubRepository.findByName("Running").isPresent()).isTrue();
        assertThat(clubRepository.findByName("Running").get().getMembers().size()).isEqualTo(2);
        System.out.println(c.getMembers());
        System.out.println(s.getClubs());

    }

    @Transactional
    @Test
    void addAMemberToAAClosedClub_shouldDoNothing() {
        Student s=new Student("Hubert");
        Student t=new Student("Régine");
        Club c=new Club("ProblemSolving");
        clubRepository.save(c);
        studentRepository.save(s);
        studentRepository.save(t);
        s.addClub(c);
        t.addClub(c);

        c.closeClub();
        assertThat(clubRepository.findByName("ProblemSolving").isPresent()).isTrue();
        assertThat(clubRepository.findByName("ProblemSolving").get().getMembers()).isEmpty();
        Student r=new Student("Adeline");
        r.addClub(c);
        studentRepository.save(r);
        assertThat(clubRepository.findByName("ProblemSolving").isPresent()).isTrue();
        assertThat(clubRepository.findByName("ProblemSolving").get().getMembers()).isEmpty();
    }

    @Transactional
    @Test void closeaClosedClub_isOk() {
        Student s=new Student("Alain");
        Student t=new Student("Isabelle");
        Club c=new Club("Climbing");
        clubRepository.save(c);
        studentRepository.save(s);
        studentRepository.save(t);
        s.addClub(c);
        t.addClub(c);

        c.closeClub();
        c.closeClub();
        assertThat(clubRepository.findByName("Climbing").isPresent()).isTrue();
        assertThat(clubRepository.findByName("Climbing").get().getMembers()).isEmpty();
    }

    @Transactional
    @Test void youMustbeAMember_ToBeAPresident() {
        Student s=new Student("Louis");
        Club c=new Club("Pinard");
        clubRepository.save(c);
        studentRepository.save(s);
        s.addClub(c);
        c.setPresident(s);

        assertThat(clubRepository.findByName("Pinard").isPresent()).isTrue();
        assertThat(studentRepository.findByName("Louis").get().getClubs().size()).isEqualTo(1);
        assertThat(clubRepository.findByName("Pinard").get().getPresident().getName()).isEqualTo("Louis");
    }

    @Transactional
    @Test void youMustbeAMember_ToBeAPresident2() {
        Student s=new Student("Louis");
        Student t=new Student("Angie");
        Club c=new Club("Pinard");
        clubRepository.save(c);
        studentRepository.save(s);
        t.addClub(c);
        c.setPresident(s);

        assertThat(clubRepository.findByName("Pinard").isPresent()).isTrue();
        assertThat(studentRepository.findByName("Louis").get().getClubs().size()).isEqualTo(0);
        assertThat(clubRepository.findByName("Pinard").get().getPresident()).isNull();
    }

   @Transactional
    @Test void aMemberIsAMember() {
        Student s=new Student("Louis");
        Student t=new Student("Angie");
        Student u=new Student("Agnès");

        Club c=new Club("Pinard");
        clubRepository.save(c);
        studentRepository.save(s);
        s.addClub(c);
        t.addClub(c);

        assertThat(c.isMember(s)).isTrue();
        assertThat(c.isMember(u)).isFalse();
    }
    @Transactional
    @Test void assignARoomToAClosedClub_isNotOk() {
        Club babyfoot=new Club("BabyFoot");
        Room room1 = new Room("Room1");
        babyfoot.closeClub();
        clubRepository.save(babyfoot);
        roomRepository.save(room1);
        babyfoot.setPlace(room1);

        Optional<Club> club=clubRepository.findByName("BabyFoot");
 //       assertThat(club.get().getPlace()).isNull();
        assertThat(room1.getHost()).isNull();
    }

    @Transactional
    @Test void assignARoomToAClub_isOk() {
        Club babyfoot=new Club("BabyFoot");
        Room room1 = new Room("Room1");
        clubRepository.save(babyfoot);
        roomRepository.save(room1);
        babyfoot.setPlace(room1);

        Optional<Club> club=clubRepository.findByName("BabyFoot");
        assertThat(club.get().getPlace().get().getName()).isEqualTo("Room1");
        assertThat(room1.getHost().getName()).isEqualTo("BabyFoot");
    }
}

