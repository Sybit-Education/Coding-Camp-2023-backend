package de.sybit.sygotchi.tamagotchi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TamagotchiRepository extends JpaRepository<Tamagotchi, String> {
    @Query("select t from Tamagotchi t where t.owner.id = ?1 and t.isDead = true")
    List<Tamagotchi> findAllDead(String id);

    @Query("select t from Tamagotchi t where t.owner.id = ?1 and t.isDead = false")
    Optional<Tamagotchi> findFirstAlive(String id);

    @Query("select (count(t) > 0) from Tamagotchi t where t.owner.id = ?1 and t.isDead = false")
    boolean existsByOwnerIdAndIsAlive(String id);

    @Query("select t from Tamagotchi t ORDER BY t.score DESC")
    List<Tamagotchi> findHighScore();

    @Query("select t from Tamagotchi t where LOWER(t.name) like CONCAT(LOWER(?1), '%')")
    List<Tamagotchi> searchByName(String name);

    long countByScoreGreaterThan(long score);


}
