package de.sybit.sygotchi;

import de.sybit.sygotchi.tamagotchi.Tamagotchi;
import de.sybit.sygotchi.tamagotchi.TamagotchiEyes;
import de.sybit.sygotchi.tamagotchi.TamagotchiService;
import de.sybit.sygotchi.tamagotchi.TamagotchiShapes;
import de.sybit.sygotchi.user.TamagotchiUser;
import de.sybit.sygotchi.user.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TamagotchiServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private TamagotchiUser tamagotchiUser;

    @Mock
    private Tamagotchi tamagotchi;

    @Mock
    private TamagotchiService tamagotchiService;

    private final static String USER_ID = "1234567890";
    private final static String USERNAME = "test";
    private final static String TAMAGOTCHI_ID = "12345abcde";

    @BeforeEach
    public void init() {
        when(tamagotchi.getScore()).thenReturn(100L);

        when(userService.getCurrentUser()).thenReturn(tamagotchiUser);

        when(tamagotchi.getDirty()).thenReturn(100.0);
        when(tamagotchi.getHunger()).thenReturn(100.0);
        when(tamagotchi.getThirst()).thenReturn(100.0);
        when(tamagotchi.getHeight()).thenReturn(120);
        when(tamagotchi.getTired()).thenReturn(100.0);
        when(tamagotchi.getColor()).thenReturn("#FFFFF");
        when(tamagotchi.getEyes()).thenReturn(TamagotchiEyes.LARGE);
        when(tamagotchi.getShape()).thenReturn(TamagotchiShapes.CIRCLE);
        when(tamagotchi.getOwner()).thenReturn(tamagotchiUser);
        when(tamagotchi.getId()).thenReturn(TAMAGOTCHI_ID);

        when(tamagotchiUser.getId()).thenReturn(USER_ID);
        when(tamagotchiUser.getUsername()).thenReturn(USERNAME);

        when(tamagotchiService.getCurrentTamagotchi()).thenReturn(tamagotchi);

        when(userService.getCurrentUser()).thenReturn(tamagotchiUser);
    }

    @Test
    public void createTamagotchi() {
        Tamagotchi tamagotchi = new Tamagotchi("Test", "#FFFFF", TamagotchiEyes.LARGE, TamagotchiShapes.CIRCLE, 120, 120, tamagotchiUser);
        tamagotchiService.createTamagotchi(tamagotchi);
        verify(tamagotchiService).createTamagotchi(tamagotchi);
    }

    @Test
    public void createTamagotchiWithInvalidHeight() {
        Tamagotchi tamagotchi = new Tamagotchi("Test", "#FFFFF", TamagotchiEyes.LARGE, TamagotchiShapes.CIRCLE, 0, 120, tamagotchiUser);
        tamagotchiService.createTamagotchi(tamagotchi);
        given(tamagotchiService.createTamagotchi(tamagotchi)).willThrow(new IllegalArgumentException());
    }

    @Test
    public void createTamagotchiWithInvalidWidth() {
        Tamagotchi tamagotchi = new Tamagotchi("Test", "#FFFFF", TamagotchiEyes.LARGE, TamagotchiShapes.CIRCLE, 120, 0, tamagotchiUser);
        tamagotchiService.createTamagotchi(tamagotchi);
        given(tamagotchiService.createTamagotchi(tamagotchi)).willThrow(new IllegalArgumentException());
    }

    @Test
    public void feed() {
        tamagotchiService.feed();
        verify(tamagotchiService).feed();
    }

    @Test
    public void feedWhenTamagotchiIsSleeping() {
        when(tamagotchi.isSleeping()).thenReturn(true);
        tamagotchiService.feed();
        given(tamagotchiService.feed()).willThrow(new IllegalStateException());
    }

    @Test
    public void feedWhenCooldownIsNotOver() {
        when(tamagotchi.getFeedCooldown()).thenReturn(1000);
        tamagotchiService.feed();
        given(tamagotchiService.feed()).willThrow(new IllegalStateException());
    }

    @Test
    public void drink() {
        tamagotchiService.drink();
        verify(tamagotchiService).drink();
    }

    @Test
    public void drinkWhenTamagotchiIsSleeping() {
        when(tamagotchi.isSleeping()).thenReturn(true);
        tamagotchiService.drink();
        given(tamagotchiService.drink()).willThrow(new IllegalStateException());
    }

    @Test
    public void drinkWhenCooldownIsNotOver() {
        when(tamagotchi.getDrinkCooldown()).thenReturn(1000);
        tamagotchiService.drink();
        given(tamagotchiService.drink()).willThrow(new IllegalStateException());
    }

    @Test
    public void play() {
        tamagotchiService.play();
        verify(tamagotchiService).play();
    }

    @Test
    public void playWhenTamagotchiIsSleeping() {
        when(tamagotchi.isSleeping()).thenReturn(true);
        tamagotchiService.play();
        given(tamagotchiService.play()).willThrow(new IllegalStateException());
    }

    @Test
    public void playWhenCooldownIsNotOver() {
        when(tamagotchi.getPlayCooldown()).thenReturn(1000);
        tamagotchiService.play();
        given(tamagotchiService.play()).willThrow(new IllegalStateException());
    }

    @Test
    public void sleep() {
        tamagotchiService.sleep();
        verify(tamagotchiService).sleep();
    }

    @Test
    public void sleepWhenTamagotchiIsSleeping() {
        when(tamagotchi.isSleeping()).thenReturn(true);
        tamagotchiService.sleep();
        given(tamagotchiService.sleep()).willThrow(new IllegalStateException());
    }

    @Test
    public void sleepWhenTamagotchiIsNotTiredEnough() {
        when(tamagotchi.getTired()).thenReturn(100.0);
        tamagotchiService.sleep();
        given(tamagotchiService.sleep()).willThrow(new IllegalStateException());
    }

    @Test
    public void wakeUp() {
        tamagotchiService.wakeUp();
        verify(tamagotchiService).wakeUp();
    }

    @Test
    public void wakeUpWhenTamagotchiIsSleeping() {
        when(tamagotchi.isSleeping()).thenReturn(false);
        tamagotchiService.wakeUp();
        given(tamagotchiService.wakeUp()).willThrow(new IllegalStateException());
    }

    @Test
    public void clean() {
        tamagotchiService.clean();
        verify(tamagotchiService).clean();
    }

    @Test
    public void cleanWhenTamagotchiIsSleeping() {
        when(tamagotchi.isSleeping()).thenReturn(true);
        tamagotchiService.clean();
        given(tamagotchiService.clean()).willThrow(new IllegalStateException());
    }

    @Test
    public void cleanWhenTamagotchiIsNotDirtyEnough() {
        when(tamagotchi.getDirty()).thenReturn(100.0);
        tamagotchiService.clean();
        given(tamagotchiService.clean()).willThrow(new IllegalStateException());
    }
}
