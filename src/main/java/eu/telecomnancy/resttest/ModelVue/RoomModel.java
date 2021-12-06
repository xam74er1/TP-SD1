package eu.telecomnancy.resttest.ModelVue;


import eu.telecomnancy.resttest.Model.Room;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class RoomModel {
    Long id;

    String name;
    Long capacity;

    ClubModel host;

    public RoomModel(Room room) {
        id= room.getId();
        name=room.getName();
        capacity= room.getCapacity();
        if (room.getHost()!=null) {
            host=new ClubModel(room.getHost());
        } else host=null;
    }
}
