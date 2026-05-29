package springtest.email.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
    @Column(unique = true)
	private String email;
    
    @ManyToMany(mappedBy = "email.id")
    private List<Email> emails;
}
