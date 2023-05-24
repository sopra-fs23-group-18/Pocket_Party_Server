package ch.uzh.ifi.hase.soprafs23.entity.minigame;

import javax.persistence.Entity;
import ch.uzh.ifi.hase.soprafs23.constant.MinigamePlayers;
import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;

@Entity
public class RPS extends Minigame {

    public RPS() {
        super(
                MinigameType.RPS_GAME,
                "Rock Paper Scissors",
                500,
                new MinigamePlayers[] { MinigamePlayers.ONE });
    }

}
