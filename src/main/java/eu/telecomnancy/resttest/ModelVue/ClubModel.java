package eu.telecomnancy.resttest.ModelVue;

import eu.telecomnancy.resttest.Model.Club;
import eu.telecomnancy.resttest.Model.Student;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Value
@AllArgsConstructor
public class ClubModel {

    Long id;
    String name;

    Long presidentId;

    RoomModel place;

    Date dateCreated;
    Date dateClosed;
    boolean active = true;

    //Add a list of long for the id of the students
    List<Long> studentsId;

    public ClubModel(Club club) {
        id = club.getId();
        name = club.getName();
        if (club.getPresident() != null) {
            presidentId = club.getPresident().getId();
        } else {
            presidentId = null;
        }
        if (club.getPlace().isPresent()) {
            place = new RoomModel(club.getPlace().get());
        } else {
            place = null;
        }
        dateClosed = club.getDateClosed();
        dateCreated = club.getDateCreated();
        studentsId = new ArrayList<Long>();
        if(club.getMembers()!=null){
            for (Student student : club.getMembers()) {
                studentsId.add(student.getId());
            }
        }

    }
}
