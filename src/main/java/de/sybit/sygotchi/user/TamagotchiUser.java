package de.sybit.sygotchi.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "`user`")
@Getter
@Setter
public class TamagotchiUser {

    @Id
    @GenericGenerator(name = "id", strategy = "de.sybit.sygotchi.IdGenerator")
    @GeneratedValue(generator = "id")
    @Column(name = "id", updatable = false, nullable = false, unique = true, length = 10)
    private String id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @JsonIgnore
    private String password;

    public TamagotchiUser() {
    }

    public TamagotchiUser(String id, String username) {
        this.id = id;
        this.username = username;
    }

    public TamagotchiUser(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }
}
