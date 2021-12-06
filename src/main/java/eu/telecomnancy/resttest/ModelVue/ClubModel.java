package eu.telecomnancy.resttest.ModelVue;

import eu.telecomnancy.resttest.Model.Club;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.Date;

@Value
@AllArgsConstructor
public class ClubModel {

    Long id;
    String name;

    StudentModel president;

    RoomModel place;

    Date dateCreated;
    Date dateClosed;
    boolean active = true;

    public ClubModel(Club club) {
        id = club.getId();
        name = club.getName();
        if (club.getPresident() != null) {
            president = new StudentModel(club.getPresident());
        } else {
            president = null;
        }
        if (club.getPlace().isPresent()) {
            place = new RoomModel(club.getPlace().get());
        } else {
            place = null;
        }
        dateClosed = club.getDateClosed();
        dateCreated = club.getDateCreated();
    }
}
