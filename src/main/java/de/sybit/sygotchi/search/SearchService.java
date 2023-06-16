package de.sybit.sygotchi.search;

import de.sybit.sygotchi.tamagotchi.Tamagotchi;
import de.sybit.sygotchi.tamagotchi.TamagotchiRepository;
import de.sybit.sygotchi.user.TamagotchiUser;
import de.sybit.sygotchi.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private final UserRepository userRepository;

    private final TamagotchiRepository tamagotchiRepository;

    public SearchService(UserRepository userRepository, TamagotchiRepository tamagotchiRepository) {
        this.userRepository = userRepository;
        this.tamagotchiRepository = tamagotchiRepository;
    }
    public SearchResult search(String name) {
        return new SearchResult(userRepository.searchByUsername(name), getSearchedTamagotchi(name));
    }

    public List<TamagotchiUser> searchUser(String name){
        return userRepository.searchByUsername(name);
    }

    public List<SearchTamagotchiDto> getSearchedTamagotchi(String name){
        return tamagotchiRepository.searchByName(name).stream()
                .map(tamagotchi -> new SearchTamagotchiDto(tamagotchi.getId(), tamagotchi.getName(), tamagotchi.getOwner()))
                .collect(Collectors.toList());
    }
}
