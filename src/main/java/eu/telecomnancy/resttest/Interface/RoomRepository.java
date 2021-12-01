package eu.telecomnancy.resttest.Interface;

import eu.telecomnancy.resttest.Model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query
    Optional<Room> findByName(String name);
}