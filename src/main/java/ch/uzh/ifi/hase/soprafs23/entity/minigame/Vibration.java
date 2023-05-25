package ch.uzh.ifi.hase.soprafs23.entity.minigame;

import javax.persistence.Entity;

import ch.uzh.ifi.hase.soprafs23.constant.MinigamePlayers;
import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;

@Entity
public class Vibration extends Minigame {

    public Vibration() {
        super(
                MinigameType.VIBRATION_GAME,
                "You will be shown 3 vibration patterns on your phone. Then one of the 3 patterns will be shown again and you need to choose which one it was that you have already felt.",
                200,
                new MinigamePlayers[] { MinigamePlayers.ONE });
    }

}
