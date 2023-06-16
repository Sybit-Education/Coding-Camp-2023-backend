package de.sybit.sygotchi.user.services;

import de.sybit.sygotchi.exception.EntityNotFoundException;
import de.sybit.sygotchi.user.TamagotchiUser;
import de.sybit.sygotchi.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public TamagotchiUser save(TamagotchiUser user) {
        return userRepository.save(user);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public TamagotchiUser getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public TamagotchiUser getById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public TamagotchiUser getByUsernameOrId(String usernameOrId) {
        if (this.userRepository.existsByUsername(usernameOrId)) {
            return this.getByUsername(usernameOrId);
        } else if (this.userRepository.existsById(usernameOrId)) {
            return this.getById(usernameOrId);
        } else {
            throw new EntityNotFoundException("User not found!");
        }
    }

    public TamagotchiUser update(TamagotchiUser user) {
        return userRepository.save(user);
    }

    public List<TamagotchiUser> getList() {
        return userRepository.findAll();
    }

    public TamagotchiUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getAuthorities().stream().map(Object::toString).anyMatch("ROLE_ANONYMOUS"::equals)) {
            return null;
        } else {
            return this.getByUsername(authentication.getName());
        }
    }
}
