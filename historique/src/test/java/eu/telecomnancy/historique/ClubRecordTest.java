package eu.telecomnancy.historique;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ClubRecordTest {

    @Autowired
    private ClubRecordRepository clubRecordRepository;

    @BeforeEach
    public void setUp() {
        clubRecordRepository.deleteAll();
    }

    @Test
    public void shoudAddARecord() {
        String id = UUID.randomUUID().toString();
        ClubRecord clubRecord = new ClubRecord(id,"clubic","cluboc");
        clubRecord.setDateCreation(new Date());
        ClubRecord saved = clubRecordRepository.save(clubRecord);
        assertThat(saved).isNotNull();
        assertThat(clubRecordRepository.count()).isEqualTo(1);

    }
}
