package ch.uzh.ifi.hase.soprafs23.entity.minigame;

import javax.persistence.Entity;
import ch.uzh.ifi.hase.soprafs23.constant.MinigamePlayers;
import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;

@Entity
public class QuickFingers extends Minigame {

    public QuickFingers() {
        super(
                MinigameType.QUICK_FINGERS,
                "Tap the screen as fast as you can! Whoever tapped more often after 20 seconds will win this round.",
                500,
                new MinigamePlayers[] { MinigamePlayers.ONE, MinigamePlayers.TWO });
    }
}
