package ch.uzh.ifi.hase.soprafs23.entity.minigame;

import javax.persistence.Entity;
import ch.uzh.ifi.hase.soprafs23.constant.MinigamePlayers;
import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;

@Entity
public class Pong extends Minigame {

    public Pong() {
        super(
                MinigameType.PONG_GAME,
                "Move your paddle by rotating your phone and try to stop the ball from hitting your side. If you miss the other player gets a point!",
                500,
                new MinigamePlayers[] { MinigamePlayers.ONE });
    }

}
