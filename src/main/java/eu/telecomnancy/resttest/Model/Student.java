package eu.telecomnancy.resttest.Model;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Student {

    private @Id @GeneratedValue Long id;

    @Column(unique = true) // unique name for students
    private String name;

    @ManyToMany(mappedBy = "members")
    private Set<Club> clubs=new HashSet<>();

    @OneToMany(mappedBy = "president")
    private Set<Club> preside=new HashSet<>();
    /* Constructors */


    public Student(String name) {
        this.name = name;
    }

    public Student() {

    }

    /* Getters */

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Set<Club> getClubs() {
        return this.clubs;
    }

    /* Setters */

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    /* Adder */

    @Transactional(propagation = Propagation.SUPPORTS)
    public void addClub (Club club) {
        if (club.isActive()) {
            if (this.clubs == null) {
                this.clubs = new HashSet<>();
            }
            this.clubs.add(club);
            club.getMembers().add(this); // So that the relationships is bidirectional
        }
    }

    @Override
    public String toString() {
        return "Student{" + "id=" + this.id + ", name='" + this.name + '\'';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return Objects.equals(id , student.getId()) &&
                Objects.equals(name , student.getName()) &&
                Objects.equals(clubs, student.getClubs());
    }

    public Set<Club> getPreside() {
        return preside;
    }

    public void setPreside(Set<Club> preside) {
        this.preside = preside;
    }
}
