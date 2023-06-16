package de.sybit.sygotchi.auth.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {

    @Schema(name = "JWT Token", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiZXhwI")
    private String token;

    private String type = "Bearer";

    @Schema(name = "User ID", example = "3edf5gthBg")
    private String id;

    @Schema(name = "Username", example = "user")
    private String username;

    public JwtResponse(String accessToken, String id, String username) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
    }
}
