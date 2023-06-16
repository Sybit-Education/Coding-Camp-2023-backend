package de.sybit.sygotchi.tamagotchi;

import de.sybit.sygotchi.user.TamagotchiUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "tamagotchi")
public class Tamagotchi {

    @Id
    @GenericGenerator(name = "id", strategy = "de.sybit.sygotchi.IdGenerator")
    @GeneratedValue(generator = "id")
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "rank", nullable = false, columnDefinition = "bigint default 0")
    private long rank;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @Column(name = "score", columnDefinition = "bigint default 0")
    private long score = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "mood", nullable = false)
    private MoodType mood = MoodType.HAPPY;

    @Column(name = "color", nullable = false)
    private String color;

    @Enumerated(EnumType.STRING)
    @Column(name = "eyes", nullable = false)
    private TamagotchiEyes eyes;

    @Enumerated(EnumType.STRING)
    @Column(name = "shape", nullable = false)
    private TamagotchiShapes shape;

    @Column(name = "height", nullable = false)
    private int height;

    @Column(name = "width", nullable = false)
    private int width;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private TamagotchiUser owner;

    @Column(name = "hunger", nullable = false)
    private double hunger = 100;

    @Column(name = "thirst", nullable = false)
    private double thirst = 100;

    @Column(name = "bored", nullable = false)
    private double bored = 100;

    @Column(name = "tired", nullable = false)
    private double tired = 100;

    @Column(name = "dirty", nullable = false)
    private double dirty = 100;

    @Column(name = "feed_cooldown", columnDefinition = "int default -1")
    private int feedCooldown = -1;

    @Column(name = "drink_cooldown", columnDefinition = "int default -1")
    private int drinkCooldown = -1;

    @Column(name = "play_cooldown", columnDefinition = "int default -1")
    private int playCooldown = -1;

    @Column(name = "hunger_death_in", columnDefinition = "int default -1")
    private int hungerDeathIn = -1;

    @Column(name = "thirst_death_in", columnDefinition = "int default -1")
    private int thirstDeathIn = -1;

    @Column(name = "bored_death_in", columnDefinition = "int default -1")
    private int boredDeathIn = -1;

    @Column(name = "is_dead", nullable = false, columnDefinition = "boolean default false")
    private boolean isDead = false;

    @Column(name = "is_sleeping", nullable = false, columnDefinition = "boolean default false")
    private boolean isSleeping = false;

    @Column(name = "started_sleeping")
    private Timestamp startedSleeping;

    @Column(name = "died_at")
    private Timestamp diedAt;

    public Tamagotchi() {
    }

    public Tamagotchi(String name, String color, TamagotchiEyes eyes, TamagotchiShapes shape, int height, int width, TamagotchiUser owner) {
        this.name = name;
        this.color = color;
        this.eyes = eyes;
        this.shape = shape;
        this.height = height;
        this.width = width;
        this.owner = owner;
    }

    public void addScore(int score) {
        this.score += score;
    }
}
