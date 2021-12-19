package eu.telecomnancy.rabbit;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile({"club", "work-queues"})
@Configuration
public class MessageConfig {

    @Bean
    public Queue clubcreate() {
        return new Queue("clubcreate");
    }

    @Profile("receiver")
    private static class ReceiverConfig {

        @Bean
        public ClubReceiver receiver1() {
            return new ClubReceiver(1);
        }

        @Bean
        public ClubReceiver receiver2() {
            return new ClubReceiver(2);
        }
    }

    @Profile("sender")
    @Bean
    public ClubSender sender() {
        return new ClubSender();
    }
}