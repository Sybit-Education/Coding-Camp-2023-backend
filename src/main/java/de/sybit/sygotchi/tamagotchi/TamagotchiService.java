package de.sybit.sygotchi.tamagotchi;

import de.sybit.sygotchi.exception.CooldownException;
import de.sybit.sygotchi.exception.SleepingException;
import de.sybit.sygotchi.user.TamagotchiUser;
import de.sybit.sygotchi.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class TamagotchiService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final TamagotchiRepository repository;

    private final UserService userService;

    @Autowired
    public TamagotchiService(SimpMessagingTemplate simpMessagingTemplate, TamagotchiRepository repository, UserService userService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.repository = repository;
        this.userService = userService;
    }

    /**
     * Create a new Tamagotchi
     * <p>
     * If the user already has a Tamagotchi, you can't create a new one
     * If the user doesn't have a Tamagotchi, you can create a new one
     * If the user doesn't have a Tamagotchi and the name is not valid, you can't create a new one
     * If the user doesn't have a Tamagotchi and the name is valid, you can create a new one
     * The Tamagotchi's height and width must be between 100 and 150
     * </p>
     *
     * @param tamagotchi The Tamagotchi
     * @return The created Tamagotchi
     */
    public Tamagotchi createTamagotchi(Tamagotchi tamagotchi) {
        // TODO: checks
        final Tamagotchi result = repository.save(tamagotchi);
        result.setRank(getRank(result));
        return repository.save(result);
    }

    public long getRank(Tamagotchi tamagotchi) {
        return repository.countByScoreGreaterThan(tamagotchi.getScore()) + 1;
    }

    public List<Tamagotchi> getListOfDeadTamagotchis() {
        TamagotchiUser user = userService.getCurrentUser();
        return repository.findAllDead(user.getId());
    }

    public List<Tamagotchi> getListOfDeadTamagotchis(TamagotchiUser user) {
        return repository.findAllDead(user.getId());
    }

    public Tamagotchi getCurrentTamagotchi() {
        TamagotchiUser user = userService.getCurrentUser();
        return getCurrentTamagotchiByUser(user);
    }

    public Tamagotchi getCurrentTamagotchiByUser(TamagotchiUser user) {
        return repository.findFirstAlive(user.getId()).orElseThrow(() -> new IllegalStateException("You don't have a Tamagotchi yet"));
    }

    /**
     * Feed your Tamagotchi
     * <p>
     * This method allows the Tamagotchi to be fed and increases its score by 2 if it is not on cooldown.
     * If the Tamagotchi is sleeping, it throws an IllegalStateException with a message "Your Tamagotchi is sleeping".
     * If the FeedCooldown is not -1, it throws an IllegalStateException with a message "You can feed again in X Minutes",
     * where X is the remaining minutes before the Tamagotchi can be fed again.
     * When the Tamagotchi is fed, its FeedCooldown is set to 30.
     * After being fed, the Tamagotchi's hunger level is increased by 20 (up to a maximum of 100), and its mood is updated.
     * The updated Tamagotchi is then saved back to the repository.
     * This method should be called to feed your Tamagotchi when it is not on cooldown.
     *
     * @return The updated Tamagotchi
     */
    public Tamagotchi feed() {
        Tamagotchi tamagotchi = getCurrentTamagotchi();
        if (tamagotchi.isSleeping()) {
            throw new SleepingException("Your Tamagotchi is sleeping");
        }
        if ( tamagotchi.getFeedCooldown() != -1) {
            throw new CooldownException("You can feed again in " + tamagotchi.getFeedCooldown() + " Minutes");
        }
        tamagotchi.addScore(2);
        tamagotchi.setFeedCooldown(30);
        tamagotchi.setHunger(tamagotchi.getHunger() + 20);
        if (tamagotchi.getHunger() > 100) {
            tamagotchi.setHunger(100);
        }
        updateMood(tamagotchi);
        return repository.save(tamagotchi);
    }

    /**
     * Give your Tamagotchi something to drink
     * <p>
     * This method allows the Tamagotchi to drink and increases its score by 1 if it is not on cooldown.
     * If the Tamagotchi is sleeping, it throws an IllegalStateException with a message "Your Tamagotchi is sleeping".
     * If the Drinkcooldown is not -1, it throws an IllegalStateException with a message "Your Tamagotchi can drink again in X Minutes",
     * where X is the remaining minutes before the Tamagotchi can drink again.
     * When the Tamagotchi gets something to drink, its DrinkCooldown is set to 10.
     * After drinking, the Tamagotchi's thirst level is increased by 20 (up to a maximum of 100), and its mood is updated.
     * The updated Tamagotchi is then saved back to the repository.
     * This method should be called to give your Tamagotchi a drink when it is not on cooldown.
     *
     * @return the updated Tamagotchi
     */
    public Tamagotchi drink() {
        Tamagotchi tamagotchi = getCurrentTamagotchi();
        if (tamagotchi.isSleeping()) {
            throw new IllegalStateException("Your Tamagotchi is sleeping");
        }
        if (tamagotchi.getDrinkCooldown() != -1) {
            throw new IllegalStateException("Your Tamagotchi can drink again in " + tamagotchi.getDrinkCooldown() + " Minutes");
        }

        tamagotchi.setDrinkCooldown(10);
        tamagotchi.setThirst(Math.min(tamagotchi.getThirst() + 20.0, 100.0));
        tamagotchi.addScore(1);
        updateMood(tamagotchi);
        return repository.save(tamagotchi);
    }

    /**
     * Play with your Tamagotchi
     * <p>
     * This method allows the Tamagotchi to play and increases its score by 5 if it is not on cooldown.
     * If the Tamagotchi is sleeping, it throws an IllegalStateException with a message "Your Tamagotchi is sleeping".
     * If Playcooldown is not -1, it throws an IllegalStateException with a message "Your can play again in X Minutes",
     * where X is the remaining minutes before the Tamagotchi can play again.
     * When the Tamagotchi is played with, its Playcooldown is set to 60.
     * After playing, the Tamagotchi's boredom level is increased by 20 and its other levels (dirty, tired, hunger, thirst)
     * are all decreased by 10, and its mood is updated.
     * The updated Tamagotchi is then saved back to the repository.
     * This method should be called to allow the Tamagotchi to play when it is not on cooldown.
     *
     * @return the updated Tamagotchi
     */
    public Tamagotchi play() {
        Tamagotchi tamagotchi = getCurrentTamagotchi();
        if (tamagotchi.isSleeping()) {
            throw new IllegalStateException("Your Tamagotchi is sleeping");
        }

        if (tamagotchi.getPlayCooldown() == -1) {
            tamagotchi.setPlayCooldown(60);
            tamagotchi.addScore(5);
        } else {
            throw new IllegalStateException("Your can play again in " + tamagotchi.getPlayCooldown() + " Minutes");
        }
        tamagotchi.setBored(Math.min(tamagotchi.getBored() + 20.0, 100.0));
        tamagotchi.setDirty(Math.max(tamagotchi.getDirty() - 10.0, 0));
        tamagotchi.setTired(Math.max(tamagotchi.getTired() - 10.0, 0));
        tamagotchi.setHunger(Math.max(tamagotchi.getHunger() - 10.0, 0));
        tamagotchi.setThirst(Math.max(tamagotchi.getThirst() - 10.0, 0));
        updateMood(tamagotchi);
        return repository.save(tamagotchi);
    }

    /**
     * This method puts the Tamagotchi to sleep. If the Tamagotchi is already sleeping, it throws an IllegalStateException with a message "Your Tamagotchi is already sleeping".
     * After setting the Tamagotchi to sleep, the updated Tamagotchi is saved back to the repository.
     * SetSleelping to True;
     * SetStartedSleeping to current time;
     * This method should be called when the user wants to put the Tamagotchi to sleep.
     *
     * @return the updated Tamagotchi
     */
    public Tamagotchi sleep() {
        Tamagotchi tamagotchi = getCurrentTamagotchi();
        if (tamagotchi.isSleeping()) {
            throw new IllegalStateException("Your Tamagotchi is already sleeping");
        }
        tamagotchi.setSleeping(true);
        tamagotchi.setStartedSleeping(new Timestamp(new Date().getTime()));
        return repository.save(tamagotchi);
    }

    /**
     * This method wakes up the Tamagotchi if it is currently sleeping.
     * If the Tamagotchi is not sleeping, it throws an IllegalStateException with a message "Your Tamagotchi is not sleeping".
     * After waking up the Tamagotchi and updating its tiredness level, the updated Tamagotchi is saved back to the repository.
     * This method should be called to wake up the Tamagotchi when it is sleeping.
     *
     * @return the updated Tamagotchi
     */
    public Tamagotchi wakeUp() {
        Tamagotchi tamagotchi = getCurrentTamagotchi();
        if (!tamagotchi.isSleeping()) {
            throw new IllegalStateException("Your Tamagotchi is not sleeping");
        }
        tamagotchi.setSleeping(false);
        updateMood(tamagotchi);
        return repository.save(tamagotchi);
    }

    /**
     * Checks if the tamagochi is sleeping.
     * @return if the tamagotchi is sleeping
     */
    public boolean isSleeping() {
        return getCurrentTamagotchi().isSleeping();
    }

    /**
     * This method cleans the Tamagotchi and increases its score by 7 if it is dirty enough to be cleaned.
     * If the Tamagotchi is sleeping, it throws an IllegalStateException with a message "Your Tamagotchi is sleeping".
     * If the Tamagotchi is not dirty enough to be cleaned, it throws an IllegalStateException with a message "Your Tamagotchi is not dirty enough".
     * The Dirty level needs to be at least 50 to be cleaned.
     * The Tamagotchi's dirty level is set to 100 after Cleaning and its score is increased by 7.
     * After cleaning the Tamagotchi and updating its score, the updated Tamagotchi is saved back to the repository.
     * This method should be called to clean the Tamagotchi when it is dirty enough.
     *
     * @return the updated Tamagotchi
     */
    public Tamagotchi clean() {
        Tamagotchi tamagotchi = getCurrentTamagotchi();
        if (tamagotchi.isSleeping()) {
            throw new SleepingException("Your Tamagotchi is sleeping");
        }
        if(tamagotchi.getDirty() > 50){
            throw new IllegalStateException("Your Tamagotchi is not dirty enough");
        }
        tamagotchi.addScore(7);
        tamagotchi.setDirty(100);
        updateMood(tamagotchi);
        return repository.save(tamagotchi);
    }

    /**
     * Kills the Tamagotchi
     * <p>
     * This will set the dead attribute to true
     * </p>
     *
     * @return the killed Tamagotchi
     */
    public Tamagotchi kill() {
        Tamagotchi tamagotchi = getCurrentTamagotchi();
        return this.kill(tamagotchi);
    }

    public Tamagotchi kill(Tamagotchi tamagotchi) {
        tamagotchi.setDead(true);
        tamagotchi.setDiedAt(new Timestamp(new Date().getTime()));
        return repository.save(tamagotchi);
    }

    public Tamagotchi update() {
        Tamagotchi tamagotchi = getCurrentTamagotchi();
        return repository.save(tamagotchi);
    }

    /**
     * This method updates the mood of a given Tamagotchi based on its current levels of hunger, thirst, boredom, tiredness, and dirtiness.
     * The mood is set to the type of the lowest value of these levels.
     * If the lowest value is above or equal to 40.0, the mood is set to HAPPY.
     * After updating the mood, the Tamagotchi's death countdown is updated and the updated Tamagotchi is saved back to the repository.
     *
     * @param tamagotchi the tamagotchi to update
     *                   The mood is determined by the highest value of the following attributes:
     *                   - Hunger
     *                   - Thirst
     *                   - Bored
     *                   - Tired
     *                   If the value is equal for multiple attributes, the first one is chosen.
     */
    public void updateMood(Tamagotchi tamagotchi) {
        double lowestValue = Math.min(tamagotchi.getHunger(), Math.min(tamagotchi.getThirst(), Math.min(tamagotchi.getBored(), Math.min(tamagotchi.getTired(), tamagotchi.getDirty()))));
        if (lowestValue == tamagotchi.getHunger()) {
            tamagotchi.setMood(MoodType.HUNGRY);
        } else if (lowestValue == tamagotchi.getThirst()) {
            tamagotchi.setMood(MoodType.THIRSTY);
        } else if (lowestValue == tamagotchi.getBored()) {
            tamagotchi.setMood(MoodType.BORED);
        } else if (lowestValue == tamagotchi.getTired()) {
            tamagotchi.setMood(MoodType.TIRED);
        } else if (lowestValue == tamagotchi.getDirty()) {
            tamagotchi.setMood(MoodType.DIRTY);
        }
        if (lowestValue >= 40.0) {
            tamagotchi.setMood(MoodType.HAPPY);
        }
        updateDeathCountdown(tamagotchi);
        repository.save(tamagotchi);
    }

    /**
     * This method is an asynchronous scheduled task that runs every minute.
     * It updates all Tamagotchis in the repository by decreasing their hunger, thirst, boredom, and dirtiness levels, as well as their tiredness level if they are not sleeping.
     * It then updates each Tamagotchi's mood, cooldowns, and death countdown.
     * If a Tamagotchi is already dead, it is skipped.
     * After updating each Tamagotchi, its rank is calculated and updated, and the updated Tamagotchi is saved back to the repository.
     * Finally, the updated Tamagotchi is broadcast to all clients subscribed to the "/topic/update/" channel with the Tamagotchi's ID.
     * This method should be called regularly to ensure that all Tamagotchis are updated and their states remain accurate.
     * <p>
     * Hunger: 0.1
     * Thirst: 0.3
     * Bored: 0.05
     * Dirty: 0.05
     * Tired: 0.1
     */
    @Scheduled(fixedRate = 180000) // 3 Minuten
    @Async
    public void updateTamagotchi() {
        List<Tamagotchi> tamagotchis = repository.findAll();
        for (Tamagotchi tamagotchi : tamagotchis) {
            if (!tamagotchi.isDead()) {
                tamagotchi.setHunger(Math.max(tamagotchi.getHunger() - 0.3, 0));
                tamagotchi.setThirst(Math.max(tamagotchi.getThirst() - 0.9, 0));
                tamagotchi.setBored(Math.max(tamagotchi.getBored() - 0.15, 0));
                tamagotchi.setDirty(Math.max(tamagotchi.getDirty() - 0.15, 0));
                if (!tamagotchi.isSleeping()) {
                    tamagotchi.setTired(Math.max(tamagotchi.getTired() - 0.15, 0));
                } else if (tamagotchi.getTired() < 98) {
                    tamagotchi.setTired(tamagotchi.getTired() + 3);
                }
                autoWakeUp(tamagotchi);
                updateMood(tamagotchi);
                updateCooldowns(tamagotchi);
                updateDeathCountdown(tamagotchi);
            }

            tamagotchi.setRank(this.getRank(tamagotchi));

            repository.save(tamagotchi);
            simpMessagingTemplate.convertAndSend("/topic/update/" + tamagotchi.getId(), tamagotchi);
        }
    }

    /**
     * This method is an asynchronous scheduled task that runs every night at 3:00 AM.
     * It updates all Tamagotchis in the repository nightly by increasing their scores by 20 and checking their death status.
     * If a Tamagotchi is already dead, it is skipped.
     * After updating each Tamagotchi's score and death status, the updated Tamagotchi is saved back to the repository.
     * This method should be called every night to ensure that all Tamagotchis are updated regularly.
     */
    @Scheduled(cron = "0 0 3 * * *")
    @Async
    public void updateTamagotchiNightly() {
        List<Tamagotchi> tamagotchis = repository.findAll();
        for (Tamagotchi tamagotchi: tamagotchis) {
            if (tamagotchi.isDead()){
                continue;
            }else {
                tamagotchi.setScore(tamagotchi.getScore()+20);
                checkDeath(tamagotchi);
                repository.save(tamagotchi);
            }
        }
    }

    /**
     * This method updates the death countdown of a Tamagotchi based on its current hunger, thirst, and boredom levels.
     * If any of these levels reach zero, and the DeathCountdown is in its default stage = -1, its set to: [Hunger: 7][Thirst: 4][Bored: 14].
     * If any of these levels reach zero, and the DeathCountdown is not in its default stage, it does not change.
     * The countdown is represented as the number of days remaining until the Tamagotchi dies from the particular cause.
     * If the Tamagotchi's hunger, thirst, or boredom levels increase above zero, the corresponding death countdown is reset to -1.
     * This method should be called regularly to ensure that the Tamagotchi's death countdown is accurate.
     */
    private void updateDeathCountdown(Tamagotchi tamagotchi) {
        if (tamagotchi.getHunger() == 0) {
            if (tamagotchi.getHungerDeathIn() == -1)
                tamagotchi.setHungerDeathIn(7);
        } else {
            tamagotchi.setHungerDeathIn(-1);
        }

        if (tamagotchi.getThirst() == 0) {
            if (tamagotchi.getThirstDeathIn() == -1)
                tamagotchi.setThirstDeathIn(4);
        } else {
            tamagotchi.setThirstDeathIn(-1);
        }

        if (tamagotchi.getBored() == 0) {
            if (tamagotchi.getBoredDeathIn() == -1)
                tamagotchi.setBoredDeathIn(14);
        } else {
            tamagotchi.setBoredDeathIn(-1);
        }
    }

    /**
     * This method checks the death countdown of a Tamagotchi for each of its three needs: hunger, thirst, and boredom.
     * If any of these countdowns reaches zero, the Tamagotchi is killed.
     * Otherwise, the method decrements each countdown that is not -1 by 1.
     * This method should be called once per day to ensure that the Tamagotchi's death countdown is accurate.
     *
     * @param tamagotchi the tamagotchi to update
     */
    private void checkDeath(Tamagotchi tamagotchi) {
        if (tamagotchi.getHungerDeathIn() == 0) {
            this.kill(tamagotchi);
        } else if (tamagotchi.getHungerDeathIn() != -1) {
            tamagotchi.setHungerDeathIn(tamagotchi.getHungerDeathIn() - 1);
        }

        if (tamagotchi.getThirstDeathIn() == 0) {
            this.kill(tamagotchi);
        } else if (tamagotchi.getThirstDeathIn() != -1) {
            tamagotchi.setThirstDeathIn(tamagotchi.getThirstDeathIn() - 1);
        }

        if (tamagotchi.getBoredDeathIn() == 0) {
            this.kill(tamagotchi);
        } else if (tamagotchi.getBoredDeathIn() != -1) {
            tamagotchi.setBoredDeathIn(tamagotchi.getBoredDeathIn() - 1);
        }
    }

    /**
     * This method updates the cooldowns of a Tamagotchi's actions: drink, feed, and play.
     * If any of these cooldowns reaches zero, it is reset to -1.
     * Otherwise, the method decrements each non-negative cooldown by 1.
     * This method should be called regularly to ensure that the Tamagotchi's action cooldowns are accurate and ready for use.
     *
     * @param tamagotchi the tamagotchi to update
     */
    public void updateCooldowns(Tamagotchi tamagotchi) {
        if (tamagotchi.getDrinkCooldown() > 0) {
            tamagotchi.setDrinkCooldown(-3);
        }

        if (tamagotchi.getFeedCooldown() > 0) {
            tamagotchi.setFeedCooldown(-3);
        }

        if (tamagotchi.getPlayCooldown() > 0) {
            tamagotchi.setPlayCooldown(-3);
        }

        if (tamagotchi.getDrinkCooldown() > 0) {
            tamagotchi.setDrinkCooldown(tamagotchi.getDrinkCooldown() - 3);
        }

        if (tamagotchi.getFeedCooldown() > 0) {
            tamagotchi.setFeedCooldown(tamagotchi.getFeedCooldown() - 3);
        }

        if (tamagotchi.getPlayCooldown() > 0) {
            tamagotchi.setPlayCooldown(tamagotchi.getPlayCooldown() - 3);
        }
    }

    /**
     * This method checks if a Tamagotchi has been sleeping for more than 24 hours and if so, automatically wakes it up.
     * It takes a Tamagotchi object as a parameter and uses the current time and the time the Tamagotchi started sleeping
     * to calculate the time difference. If the difference is greater than or equal to 24 hours (in milliseconds),
     * the Tamagotchi is set to be awake, its started sleeping time is reset to null, and its tiredness score is set to 100.
     * This method should be called periodically to ensure that Tamagotchis are not sleeping for too long.
     *
     * @param tamagotchi the Tamagotchi to check
     */

    private void autoWakeUp(Tamagotchi tamagotchi) {
        // TODO: Check if Tamagotchi has been sleeping for more than 24 hours and wake it up if so
    }

    public List<Tamagotchi> getHighScoreList() {
        return repository.findHighScore();
    }

}
