package eu.telecomnancy.resttest.Controler;

import eu.telecomnancy.resttest.Model.Student;
import eu.telecomnancy.resttest.ModelVue.StudentModel;
import eu.telecomnancy.resttest.Service.ClubService;
import eu.telecomnancy.resttest.ModelVue.ClubModel;
import eu.telecomnancy.resttest.Model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Long.parseLong;



@RestController
public class ClubController {

    @Autowired
    private ClubService clubService;


    /* Avec GET et ce mapping on récupère tous les clubs exisants. */
    @GetMapping("/clubs")
    public Iterable<ClubModel> getAllClubs(){
        return clubService.getAllClubs().stream().map(ClubModel::new).collect(Collectors.toSet());
    }

    /* Avec POST et ce mapping on créé un nouveau club passé dans le corps de la requête. */
    @PostMapping("/clubs")
    public ResponseEntity<ClubModel> createClub(@RequestBody ClubModel newClub) {
        System.out.println("createClub "+newClub);
        return ResponseEntity.ok(new ClubModel(clubService.createClub(newClub.getName())));
    }

    /* Avec Get et ce mapping on ne récupère que le club dont l'id ou le nom est précisé. */
    @GetMapping("/clubs/{id}")
    public ResponseEntity<ClubModel> getClub(@PathVariable Long id) {
        return ResponseEntity.ok(new ClubModel(clubService.findClubById(id)));
    }

    /* Avec PUT et ce mapping on peut mettre a jour le club dont l'id est précisé ou le créer si il n'existe pas
     à partir des éléments dans le corps de la requête. */
    @PutMapping("/clubs/{id}")
    public ResponseEntity<ClubModel> replaceClub(@RequestBody ClubModel newClub, @PathVariable Long id) {
         return ResponseEntity.ok(new ClubModel(clubService.updateClub(id, newClub.getName())));
    }

    /* Avec DELETE et ce mapping on supprime le club dont l'id est précisé. */
    @DeleteMapping("/clubs/{id}")
    public void deleteClub(@PathVariable Long id) {
        clubService.deleteClub(id);
    }

    //Get all students of a club
    @GetMapping("/clubs/{id}/students")
    public Set<StudentModel> getAllStudentsOfClub(@PathVariable Long id) {
        System.out.println("DEBUG : "+clubService.findClubById(id).getMembers().size());
        return clubService.findClubById(id).getMembers().stream().map(StudentModel::new).collect(Collectors.toSet());
    }

    @PostMapping("/room/")
    public ResponseEntity<Room> createClub(@RequestBody Room newRoom) {
        return ResponseEntity.ok(clubService.createRoom(newRoom.getName()));
    }

    @GetMapping("/room/")
    public Iterable<Room> getAllRooms() {
        return clubService.getAllRooms();
    }

    @GetMapping("/room/{id}")
    public ResponseEntity<Room> getRoom(@PathVariable Long id) {
        return ResponseEntity.ok(clubService.findRoomById(id));
    }

}
