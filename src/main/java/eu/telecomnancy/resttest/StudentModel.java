package eu.telecomnancy.resttest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Value
@AllArgsConstructor
public class StudentModel {

         Long id;
         String name;
         Set<ClubModel> preside;

        public StudentModel(Student student) {
                id=student.getId();
                name=student.getName();
                if (student.getPreside()!=null) {
                        preside = student.getPreside().stream().map(club -> new ClubModel(club)).collect(Collectors.toSet());
                } else preside=null;
        }

}
