package de.sybit.sygotchi.search;

import de.sybit.sygotchi.user.TamagotchiUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
@Tag(name = "Search", description = "Search API")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public SearchResult search(@RequestParam String name) {
        return searchService.search(name);
    }

    @GetMapping("/user")
    public List<TamagotchiUser> searchUser(@RequestParam String name) {
        return searchService.searchUser(name);
    }
}
