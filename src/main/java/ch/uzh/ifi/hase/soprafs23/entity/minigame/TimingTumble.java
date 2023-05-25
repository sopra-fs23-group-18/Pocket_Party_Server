package ch.uzh.ifi.hase.soprafs23.entity.minigame;

import javax.persistence.Entity;
import ch.uzh.ifi.hase.soprafs23.constant.MinigamePlayers;
import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;

@Entity
public class TimingTumble extends Minigame {

    public TimingTumble() {
        super(
                MinigameType.TIMING_TUMBLE,
                "Catch the squares falling from the sky by shaking your phone at the right time. Perfect timing will reward you with 3 points while being too late or too early will result in 1 point.",
                500,
                new MinigamePlayers[] { MinigamePlayers.ONE });
    }
}
