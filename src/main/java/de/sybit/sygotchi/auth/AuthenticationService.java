package de.sybit.sygotchi.auth;

import de.sybit.sygotchi.auth.controller.request.LoginRequest;
import de.sybit.sygotchi.auth.controller.response.JwtResponse;
import de.sybit.sygotchi.auth.jwt.JwtService;
import de.sybit.sygotchi.exception.AuthenticationException;
import de.sybit.sygotchi.user.TamagotchiUser;
import de.sybit.sygotchi.user.UserDetailsImpl;
import de.sybit.sygotchi.user.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserService userService;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder encoder;

    public AuthenticationService(UserService userService, JwtService jwtService, AuthenticationManager authenticationManager, PasswordEncoder encoder) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
    }

    public JwtResponse login(LoginRequest loginRequest) throws EntityNotFoundException {
        TamagotchiUser user = null;
        if (loginRequest.getUsername() != null && !loginRequest.getUsername().isEmpty()) {
            user = userService.getByUsername(loginRequest.getUsername());
        }
        assert user != null;
        var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), loginRequest.getPassword()));
        userService.update(user);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtService.generateJwtToken(userDetails);
        return this.jwtService.buildJwtResponse(jwt, user);
    }

    public JwtResponse register(LoginRequest registerRequest) throws AuthenticationException {
        if (Boolean.TRUE.equals(userService.existsByUsername(registerRequest.getUsername()))) {
            throw new AuthenticationException("Username is already taken");
        }
        var user = new TamagotchiUser();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(encoder.encode(registerRequest.getPassword()));
        TamagotchiUser tamagotchiUser = userService.save(user);
        String jwt = jwtService.generateJwtToken(tamagotchiUser.getUsername());
        return this.jwtService.buildJwtResponse(jwt, tamagotchiUser);
    }
}
