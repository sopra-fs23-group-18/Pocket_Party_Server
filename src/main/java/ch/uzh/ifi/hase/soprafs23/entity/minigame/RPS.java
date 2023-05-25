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
                    Engage in an epic Rock Paper Scissors battle on your smartphones! Compete to be the first to reach three points. Choose your move wisely by tapping on your device, whether it's rock, paper, or scissors. With the right strategy, can you outsmart your opponents and claim victory?
                        """,
                500,
                new MinigamePlayers[] { MinigamePlayers.ONE });
    }

}
