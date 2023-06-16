package de.sybit.sygotchi.auth.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginRequest {

    @Schema(name = "username", example = "admin")
    private String username;

    @Schema(name = "password", example = "nimda")
    private String password;
}
