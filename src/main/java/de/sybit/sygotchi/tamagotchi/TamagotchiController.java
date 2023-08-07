package de.sybit.sygotchi.tamagotchi;

import de.sybit.sygotchi.user.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * The TamagotchiController is responsible for handling all requests regarding the Tamagotchi.
 * <p>
 * The TamagotchiController is annotated with {@link RestController} to indicate that it is a controller.
 * The {@link RequestMapping} annotation is used to map the controller to the /tamagotchi path.
 * The {@link Tag} annotation is used to add a tag to the OpenAPI documentation.
 * The {@link PreAuthorize} annotation is used to specify that only authenticated users can access the controller.
 * The {@link SecurityRequirement} annotation is used to specify that the controller requires a bearer token.
 * The {@link GetMapping} annotation is used to map the method to the GET HTTP method.
 * The {@link PostMapping} annotation is used to map the method to the POST HTTP method.
 * The {@link RequestBody} annotation is used to map the request body to the method parameter.
 * The {@link PathVariable} annotation is used to map the path variable to the method parameter.
 * The {@link RequestParam} annotation is used to map the request parameter to the method parameter.
 * The {@link PutMapping} annotation is used to map the method to the PUT HTTP method.
 * The {@link DeleteMapping} annotation is used to map the method to the DELETE HTTP method.
 * </p>
 */
@RestController
@RequestMapping("/tamagotchi")
@Tag(name = "Tamagotchi", description = "The Tamagotchi API")
public class TamagotchiController {

    private final TamagotchiService service;

    private final UserService userService;

    public TamagotchiController(TamagotchiService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public Tamagotchi getCurrentTamagotchi() {
        return service.getCurrentTamagotchi();
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public Tamagotchi createTamagotchi(@RequestBody CreateTamagotchi dto) {
        Tamagotchi tamagotchi = new Tamagotchi(dto.name(), dto.color(), dto.eyes(), dto.shape(), dto.height(), dto.width(), userService.getCurrentUser());
        return service.createTamagotchi(tamagotchi);
    }

    @PutMapping("feed")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public Tamagotchi feedTamagotchi() {
        return service.feed();
    }

    @PostMapping("sleep")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public Tamagotchi sleep() {
        return service.sleep();
    }

    @PostMapping("wakeUp")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public Tamagotchi wakeUp() {
        return service.wakeUp();
    }

    @GetMapping("sleep")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public boolean isSleeping() {
        return service.isSleeping();
    }

    @PutMapping("drink")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public Tamagotchi drink() {
        return service.drink();
    }

    @PutMapping("clean")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public Tamagotchi cleanTamagotchi(){
        return service.clean();
    }

    @GetMapping("highscore")
    public List<Tamagotchi> highscore(){
        return service.getHighScoreList();
    }

    @PutMapping("play")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public Tamagotchi play(){
        return service.play();
    }
    // TODO: Implement all other methods from Service here

}
