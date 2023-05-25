package ch.uzh.ifi.hase.soprafs23.entity.minigame;

import javax.persistence.Entity;
import ch.uzh.ifi.hase.soprafs23.constant.MinigamePlayers;
import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;

@Entity
public class GreedyGambit extends Minigame {

    public GreedyGambit() {
        super(
                MinigameType.GREEDY_GAMBIT,
                """
                        Chose the amount of money you would like to receive. But choose wisely! If your opponent chooses the same amount as you, you will both not receive anything at all!
                        You will play several rounds with the goal being to achieve a higher total score than your opponent.
                        """,
                500,
                new MinigamePlayers[] { MinigamePlayers.TWO });
    }
}
