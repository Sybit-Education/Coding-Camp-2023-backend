package de.sybit.sygotchi.user.controller;

import de.sybit.sygotchi.tamagotchi.Tamagotchi;
import de.sybit.sygotchi.user.TamagotchiUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {
    private TamagotchiUser user;
    private List<Tamagotchi> tamagotchis;
    private Tamagotchi currentTamagotchi;
}
