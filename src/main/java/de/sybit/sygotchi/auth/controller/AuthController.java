package de.sybit.sygotchi.auth.controller;

import de.sybit.sygotchi.auth.AuthenticationService;
import de.sybit.sygotchi.auth.controller.request.LoginRequest;
import de.sybit.sygotchi.auth.controller.response.JwtResponse;
import de.sybit.sygotchi.auth.jwt.JwtService;
import de.sybit.sygotchi.exception.ApiExceptionModel;
import de.sybit.sygotchi.exception.AuthenticationException;
import de.sybit.sygotchi.exception.EntityNotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "The Authentication API")
public class AuthController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            return ResponseEntity.ok(authenticationService.login(loginRequest));
        } catch (EntityNotFoundException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new ApiExceptionModel("Bad credentials", HttpStatus.BAD_REQUEST.value(), "/login"));
        }
    }

    @PostMapping("/register")
    public JwtResponse registerUser(@RequestBody LoginRequest signUpRequest) throws AuthenticationException {
        return this.authenticationService.register(signUpRequest);
    }
}
