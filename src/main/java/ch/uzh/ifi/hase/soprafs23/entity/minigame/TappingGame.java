package ch.uzh.ifi.hase.soprafs23.entity.minigame;

import javax.persistence.Entity;
import ch.uzh.ifi.hase.soprafs23.constant.MinigamePlayers;
import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;

@Entity
public class TappingGame extends Minigame {

    public TappingGame() {
        super(
                MinigameType.TAPPING_GAME,
                "Tap the screen as fast as you can! You have a timelimit and whoever has a higher score wins!",
                500,
                new MinigamePlayers[] { MinigamePlayers.ONE });
    }

}
