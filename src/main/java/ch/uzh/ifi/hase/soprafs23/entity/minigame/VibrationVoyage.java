package ch.uzh.ifi.hase.soprafs23.entity.minigame;

import javax.persistence.Entity;

import ch.uzh.ifi.hase.soprafs23.constant.MinigamePlayers;
import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;

@Entity
public class VibrationVoyage extends Minigame {

    public VibrationVoyage() {
        super(
                MinigameType.VIBRATION_VOYAGE,
                " Prepare for a test of observation and memory in this intriguing game! Your phone will present you with three distinctive vibration patterns, each corresponding to a different shape: square, triangle, and circle. Pay careful attention to the patterns, as one of them will be replayed later. Your challenge is to accurately recall and select the matching shape you felt before.",
                200,
                new MinigamePlayers[] { MinigamePlayers.ONE });
    }

}
