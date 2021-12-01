package eu.telecomnancy.resttest.Interface;

import eu.telecomnancy.resttest.Model.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
    @Query
    Optional<Club> findByName(String name);
}