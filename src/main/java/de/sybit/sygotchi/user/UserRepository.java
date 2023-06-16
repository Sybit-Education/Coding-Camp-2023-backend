package de.sybit.sygotchi.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<TamagotchiUser, String> {
    boolean existsByUsername(String username);

    Optional<TamagotchiUser> findByUsername(String username);

    @Query("select t from TamagotchiUser t where LOWER(t.username) like CONCAT(LOWER(?1), '%')")
    List<TamagotchiUser> searchByUsername(String username);
}
