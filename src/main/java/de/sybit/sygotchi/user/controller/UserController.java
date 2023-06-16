package de.sybit.sygotchi.user.controller;

import de.sybit.sygotchi.tamagotchi.TamagotchiService;
import de.sybit.sygotchi.user.TamagotchiUser;
import de.sybit.sygotchi.user.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name = "User", description = "The User API")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("isAuthenticated()")
public class UserController {

    private final UserService userService;

    private final TamagotchiService tamagotchiService;

    public UserController(UserService userService, TamagotchiService tamagotchiService) {
        this.userService = userService;
        this.tamagotchiService = tamagotchiService;
    }

    @GetMapping("/{usernameOrId}")
    public UserDto getUserByUsername(@PathVariable String usernameOrId) {
        TamagotchiUser user = userService.getByUsernameOrId(usernameOrId);
        return new UserDto(user, tamagotchiService.getListOfDeadTamagotchis(user), tamagotchiService.getCurrentTamagotchi());
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        List<UserDto> users = new ArrayList<>();
        userService.getList().forEach(user -> users.add(new UserDto(user, tamagotchiService.getListOfDeadTamagotchis(user), tamagotchiService.getCurrentTamagotchi())));
        return users;
    }
}
