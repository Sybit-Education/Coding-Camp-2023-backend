package de.sybit.sygotchi.search;

import de.sybit.sygotchi.user.TamagotchiUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SearchTamagotchiDto {

    String id;
    String name;
    TamagotchiUser owner;
}
