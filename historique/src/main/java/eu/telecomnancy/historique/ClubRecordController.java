package eu.telecomnancy.historique;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
public class ClubRecordController {

    @Autowired
    private ClubRecordRepository clubRecordRepository;

    /* Avec GET et ce mapping on récupère tous les clubs exisants. */
    @GetMapping("/clubrecord")
    public Iterable<ClubRecord> getAllClubs(){
        return clubRecordRepository.findAll();
    }

    /* Avec POST et ce mapping on créé un nouveau club passé dans le corps de la requête. */
    @PostMapping("/clubrecord")
    public ResponseEntity<ClubRecord> createClub(@RequestBody ClubRecord newClub) {
        System.out.println("createClub "+newClub);
        ClubRecord clubRecord=new ClubRecord();
        return ResponseEntity.ok(clubRecord);
    }
}
