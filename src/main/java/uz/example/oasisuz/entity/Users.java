package uz.example.oasisuz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String fullName;

    private String phoneNumber;

    private String password;

    private String email;

    private Integer noAdds;

    @OneToMany(mappedBy = "users")
    private List<Cottage> cottageList;
}
