package de.sybit.sygotchi.search;

import de.sybit.sygotchi.tamagotchi.Tamagotchi;
import de.sybit.sygotchi.user.TamagotchiUser;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchResult {
    private List<TamagotchiUser> users;
    private List<SearchTamagotchiDto> tamagotchis;

    public SearchResult(List<TamagotchiUser> users, List<SearchTamagotchiDto> tamagotchis) {
        this.users = users;
        this.tamagotchis = tamagotchis;
    }
}
