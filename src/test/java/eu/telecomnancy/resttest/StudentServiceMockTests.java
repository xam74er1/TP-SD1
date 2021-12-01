package eu.telecomnancy.resttest;

import eu.telecomnancy.resttest.Interface.ClubRepository;
import eu.telecomnancy.resttest.Interface.StudentRepository;
import eu.telecomnancy.resttest.Model.Club;
import eu.telecomnancy.resttest.Model.Student;
import eu.telecomnancy.resttest.Service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class StudentServiceMockTests {

        @Autowired
        private StudentService studentService;

        /* Permet de simuler les accès aux données persistentes */
        @MockBean
        private StudentRepository studentRepository;

        /* Permet de simuler les accès aux données persistentes */
        @MockBean
        private ClubRepository clubRepository;

        // Appelé avant chaque test.
        //    Permet de mettre en place les réponses des méthodes des Beans mocked (ici les Repository)
        @BeforeEach
        public void setUp() {
            /* Création de deux étudiants pour la plupart des tests */
            Student alex = new Student("Alexandre");
            alex.setId(1L);
            Optional<Student> alexo=Optional.of(alex);
            Student maxime = new Student("Maxime");
            maxime.setId(2L);
            Optional<Student> maximo=Optional.of(maxime);

            /* Création d'une list pour le test de all() */
            List<Student> list = new ArrayList<>();
            list.add(alex);
            list.add(maxime);

            /* Simulation des méthodes des repository via Mockito */

            /* Création d'un club pour addClub() */
            Club club = new Club("Test");
            club.setId(1L);

            /* Mocking du Student alex */
            Mockito.when(studentRepository.findByName(alex.getName())).thenReturn(alexo);
            Mockito.when(studentRepository.findById(alex.getId())).thenReturn(alexo);

            /* Mocking du Student maxime */
            Mockito.when(studentRepository.findByName(maxime.getName())).thenReturn(maximo);
            Mockito.when(studentRepository.findById(maxime.getId())).thenReturn(maximo);

            /* Mocking de la méthode findAll() */
            Mockito.when(studentRepository.findAll()).thenReturn(list);

            /* Mocking de la méthode count() */
            Mockito.when(studentRepository.count()).thenReturn((long) list.size());

            /* Mocking nécessaire pour modify() et addClub()  */
            Mockito.when(studentRepository.save(alex)).thenReturn(alex);
            Mockito.when(clubRepository.findById(club.getId())).thenReturn(Optional.of(club));
            Mockito.when(clubRepository.save(club)).thenReturn(club);
            Mockito.when(clubRepository.findByName(club.getName())).thenReturn(Optional.of(club));
        }

        @Test
        public void whenValidName_ReturnStudent() {
            /* Test de la méthode findByName() */
            String name = "Alexandre";
            Student found = studentService.findByName(name);

            assertThat(found.getName()).isEqualTo(name);
        }

        @Test
        public void whenInvalidName_ReturnStudent() {
            /* Test de l'exception StudentNotFound dans la méthode findByName */
            String badName = "Oupsi";
            Exception exception = assertThrows(NameNotFoundException.class, () -> studentService.findByName(badName));

            String expectedMessage = "Could not find item named Oupsi";
            String foundMessage = exception.getMessage();

            assertTrue(foundMessage.contains(expectedMessage));
        }

        @Test
        public void whenValidId_ReturnStudent() {
            /* Test de la méthode findById() */
            Long id = 1L;
            Student found = studentService.findById(id);

            assertThat(found.getId()).isEqualTo(id);

        }

        @Test
        public void whenInvalidId_ReturnStudentNotFound() {
            /* Test de l'exception StudentNotFound dans la méthode findById */
            Long id = 3L;
            Exception exception = assertThrows(IdNotFoundException.class, () -> studentService.findById(id));

            String expectedMessage = "Could not find item 3";
            String foundMessage = exception.getMessage();

            assertTrue(foundMessage.contains(expectedMessage));
        }

        @Test
        public void whenFindAll_ReturnAllTheStudents() {
            /* Test de la méthode findAll() */

            /* Réplication de la liste utlisée dans Mockito */
            List<Student> list = new ArrayList<>();
            Student alex = new Student("Alexandre");
            Student max = new Student("Maxime");
            alex.setId(1L);
            max.setId(2L);
            list.add(alex);
            list.add(max);

            List<Student> found = studentService.getAllStudents();

            assertThat(found).isEqualTo(list);
        }

        @Test
        public void whenGetNumber_ReturnNumberOfStudents() {
            /* Test de la méthode getNumber() */

            Long expected = 2L;

            Long found = studentService.getStudentCount();

            assertThat(found).isEqualTo(expected);
        }

        @Test
        public void whenModify_EverythingGoesRight() {
            /* Test de la méthode modify() */
            Student alex = new Student("Alexandre");
            alex.setId(1L);
            Student maxime = new Student("Maxime");
            maxime.setId(1L);

            studentService.updateStudent(alex.getId(), "Maxime");

            assertThat(studentService.findById(1L).getName()).isEqualTo("Maxime");
        }

        @Test
        @Transactional
        public void whenAddClub_ReturnStudentWithAddedClub() {
            /* Test de la méthode addClub() */
            Club club1 = new Club("Test");
            club1.setId(1L);

            Set<Club> clubs = new HashSet<>();
            clubs.add(club1);

            Student alex = new Student("Alexandre");
            alex.setId(1L);

            /* Ce test force le besoin d'initialisation de la list de Club du Student dans la méthode addClub */
            studentService.addClub(alex.getId(), club1.getName());

            //TODO:  Check ce test. Problème avec les Mocks
            //assertThat(alex.getClubs()).isEqualTo(clubs);
        }

    }

