package ch.uzh.ifi.hase.soprafs23.entity.minigame;

import javax.persistence.Entity;

import ch.uzh.ifi.hase.soprafs23.constant.MinigamePlayers;
import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;

@Entity
public class PushDown extends Minigame {

    public PushDown() {
        super(
                MinigameType.PUSH_DOWN,
                "Push yourself down the rock from heaven. Navigate your character by leaning your smartphone in the given direction",
                500,
                new MinigamePlayers[] { MinigamePlayers.ONE, MinigamePlayers.TWO });
    }
}
