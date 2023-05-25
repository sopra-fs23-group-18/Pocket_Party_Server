package ch.uzh.ifi.hase.soprafs23.entity.minigame;

import javax.persistence.Entity;
import ch.uzh.ifi.hase.soprafs23.constant.MinigamePlayers;
import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;

@Entity
public class RPS extends Minigame {

    public RPS() {
        super(
                MinigameType.ROCK_PAPER_SCISSORS,
                """
                        Rock Paper Scissors: Choose one of the three, if you beat your opponent you get a point.
                        Rock > Scissors
                        Scissors > Paper
                        Paper > Rock
                        """,
                500,
                new MinigamePlayers[] { MinigamePlayers.ONE });
    }

}
