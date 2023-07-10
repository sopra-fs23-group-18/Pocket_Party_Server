package ch.uzh.ifi.hase.soprafs23.entity.minigame;

import javax.persistence.Entity;
import ch.uzh.ifi.hase.soprafs23.constant.MinigamePlayers;
import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;

@Entity
public class QuickFingers extends Minigame {

    public QuickFingers() {
        super(
                MinigameType.QUICK_FINGERS,
                "Test your finger dexterity in this high-speed tapping challenge! Compete to tap your smartphone screen faster and accumulate the most taps in 20 seconds. It's a battle of nimble fingers. Can you secure the Quick Fingers champion title?",
                500,
                new MinigamePlayers[] { MinigamePlayers.ONE, MinigamePlayers.TWO });
    }
}
