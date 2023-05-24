package ch.uzh.ifi.hase.soprafs23.entity.minigame;

import javax.persistence.Entity;
import ch.uzh.ifi.hase.soprafs23.constant.MinigamePlayers;
import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;

@Entity
public class Strategy extends Minigame {

    public Strategy() {
        super(
                MinigameType.STRATEGY_GAME,
                "Choose how much you want, but you can only get the amount you chose if your opponent chose something different.",
                500,
                new MinigamePlayers[] { MinigamePlayers.TWO });
    }

}
