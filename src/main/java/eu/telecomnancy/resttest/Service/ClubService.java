package eu.telecomnancy.resttest.Service;

import eu.telecomnancy.resttest.IdNotFoundException;
import eu.telecomnancy.resttest.Interface.ClubRepository;
import eu.telecomnancy.resttest.Interface.RoomRepository;
import eu.telecomnancy.resttest.Model.Club;
import eu.telecomnancy.resttest.Model.Student;
import eu.telecomnancy.resttest.NameNotFoundException;
import eu.telecomnancy.resttest.Model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

// Gestion des clubs et des Rooms
@Service
@Transactional
public class ClubService {

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    private StudentService studentService;

    public Club createClub(String name) {
        return clubRepository.save(new Club(name));
    }

    public List<Club> getAllClubs() {
            List<Club> clubs=clubRepository.findAll();
            System.out.println(clubs);
            return clubs;
    }

    public Long getClubCount() {
        return clubRepository.count();
    }

    public Club findClubByName(String name) {
        return clubRepository.findByName(name).orElseThrow(() -> new NameNotFoundException(name));
    }

    public Club findClubById(Long id) {
        return clubRepository.findById(id).orElseThrow(() -> new IdNotFoundException(id));
    }


    public void deleteClub(Long id) {
        clubRepository.deleteById(id);
    }

    public Club updateClub(Long id, String clubName) {
        clubRepository.findById(id)
                .map(club -> {
                    club.setName(clubName);
                    return club;
                });
        throw new IdNotFoundException(id);
    }

    public void addStudentToClub(String clubName, String studentName) {
        Club club=this.findClubByName(clubName);
        Student student= studentService.findByName(studentName);
        student.addClub(club);
    }

    public Set<Student> getClubMembers(String clubName) {
        Club club=this.findClubByName(clubName);
        if (club==null) {
            throw new NameNotFoundException("Club does not exist :"+clubName);
        }
        return club.getMembers();
    }

    public Room createRoom(String name) {
        Room room=roomRepository.save(new Room(name));
        return room;
    }

    public void deleteRoom(String name) {
        roomRepository.findByName(name).ifPresent(room -> roomRepository.delete(room));
    }

    public Room getRoom(String name) {
        return roomRepository.findByName(name).orElseThrow(( )-> new NameNotFoundException(name));
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public void updateRoom(Long roomId,String name) {
        Room room = roomRepository.getById(roomId);
        room.setName(name);
        roomRepository.save(room);
    }

    public void setClubRoom(String clubName,String roomName) {
        clubRepository.findByName(clubName).ifPresent(club -> roomRepository.findByName(roomName).ifPresent(club::setPlace));
    }

    public Optional<Room> getClubRoom(String clubName) {
        Optional<Club> club=clubRepository.findByName(clubName);
        if (club.isPresent()) {
            return club.get().getPlace();
        } else throw new NameNotFoundException(clubName);
    }

    public Club getClub(Long id) {
        return clubRepository.getById(id);
    }

    public Room findRoomById(Long id) {
        return roomRepository.findById(id).orElseThrow(() -> new IdNotFoundException(id));
    }
}
