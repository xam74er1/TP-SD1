package eu.telecomnancy.historique;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class ClubRecord {
    @Id
    private String id;
    private String name;
    private String operation;
    private Date dateCreation;

    public ClubRecord(String _id, String _name, String _operation) {
        id=_id;
        name=_name;
        operation=_operation;
        dateCreation=new Date();
    }

    public ClubRecord() {
        dateCreation=new Date();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }
}
