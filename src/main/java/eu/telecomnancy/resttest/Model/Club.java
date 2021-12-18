package eu.telecomnancy.resttest.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.*;

@Entity
@AllArgsConstructor
@Builder
public class Club {

    private @Id @GeneratedValue Long id;

    /* Constraints on the column */
    @Column(unique = true)
    private String name;

   @ManyToMany(cascade = {CascadeType.ALL})
    private Set<Student> members=new HashSet<>();
   @ManyToOne
    private Student president;
   @OneToOne(mappedBy = "host")
   @JsonIgnore
   private Room place;

    private Date dateCreated = new Date();
    private Date dateClosed;

    private boolean active = true;

    public void setMembers(Set<Student> members) {
        this.members = members;
    }



    /* Constructors */

    public Club(String name) {
        this.name = name;
    }

    public Club() {
    }

    /* Getters */

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    /* Setters */

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    /* Adder */

    @Override
    public String toString() {
        return "Club{" + "id=" + this.id + ", name='" + this.name +'\'' + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || obj.getClass() != getClass()) return false;
        Club club = (Club) obj;
        return Objects.equals(id, club.id) &&
                Objects.equals(name, club.name);
    }

    public Set<Student> getMembers() {
        return members;
    }

    public Date getDateClosed() {
        return dateClosed;
    }

    private void setDateClosed(Date dateClosed) {
        this.dateClosed = dateClosed;
    }

    @Transactional
    public void closeClub() {
        this.setDateClosed(new Date());
        this.setActive(false);
        // Supprime les membres
        this.members.clear();
        // Libère la salle
        this.setPlace(null);
    }

    public boolean isActive() {
        return active;
    }

    private void setActive(boolean active) {
        this.active = active;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void addMember(Student s) {
        if (isActive()) {
            this.getMembers().add(s);
            s.getClubs().add(this);
        }
    }

    public Student getPresident() {
        return president;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void setPresident(Student president) {
        if (isActive()) {
            if (isMember(president)) {
                if(this.president != null){
                    this.president.getClubs().remove(this);
                }
                this.president = president;
                president.getPreside().add(this);
            }
        }
    }

    //Check the key, not the object
    public boolean isMember(Student student) {
        for(Student s: members)
            if (s.getName().equals(student.getName()))
                return true;
        return false;
    }

    public Optional<Room> getPlace() {
        return Optional.ofNullable(place);
    }

    @Transactional
    public void setPlace(Room _place) {
        if (isActive()) {
            this.place = _place;
            place.setHost(this);
        }
    }

    public Date getDateCreated() {
        return dateCreated;
    }
}