package eu.telecomnancy.resttest;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

@Entity
public class Room {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String name;
    private Long capacity;

    @OneToOne
    private Club host;

    public Room() {
    }

    public Room(String _name) {
        name=_name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCapacity() {
        return capacity;
    }

    public void setCapacity(Long capacity) {
        this.capacity = capacity;
    }

    public boolean isFree() {
        return (host==null);
    }

    public Club getHost() {
        return host;
    }

    @Transactional
    public void setHost(Club host) {
        if (host.isActive()) {
            this.host = host;
        }

    }
}
