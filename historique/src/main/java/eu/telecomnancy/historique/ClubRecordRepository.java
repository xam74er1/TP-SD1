package eu.telecomnancy.historique;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubRecordRepository extends CrudRepository<ClubRecord, String> {
}
