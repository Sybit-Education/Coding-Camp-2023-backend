package de.sybit.sygotchi.user.controller;

import de.sybit.sygotchi.tamagotchi.TamagotchiService;
import de.sybit.sygotchi.user.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
@Tag(name = "Account", description = "The Account API")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("isAuthenticated()")
public class AccountController {

    private final UserService userService;

    private final TamagotchiService tamagotchiService;

    public AccountController(UserService userService, TamagotchiService tamagotchiService) {
        this.userService = userService;
        this.tamagotchiService = tamagotchiService;
    }

    @GetMapping
    public UserDto getCurrentAccount() {
        return new UserDto(userService.getCurrentUser(), tamagotchiService.getListOfDeadTamagotchis(), tamagotchiService.getCurrentTamagotchi());
    }
}
