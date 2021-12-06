package eu.telecomnancy.resttest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(ClubRepository clubrepository, StudentRepository studentrepository, RoomRepository roomRepository) {

        return args -> {
            log.info("Preloading " + clubrepository.save(new Club("Jeu")));
            log.info("Preloading " + clubrepository.save(new Club("MadPad")));
            log.info("Preloading " + studentrepository.save(new Student("Alexandre")));
            log.info("Preloading " + studentrepository.save(new Student("Jean")));
        };
    }


}