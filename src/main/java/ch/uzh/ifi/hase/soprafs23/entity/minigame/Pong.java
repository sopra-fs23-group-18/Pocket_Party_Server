package ch.uzh.ifi.hase.soprafs23.entity.minigame;

import javax.persistence.Entity;
import ch.uzh.ifi.hase.soprafs23.constant.MinigamePlayers;
import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;

@Entity
public class Pong extends Minigame {

    public Pong() {
        super(
                MinigameType.POCKET_PONG,
                "Take control of the action using your smartphone as a paddle controller! Tilt your device to move the paddles and outmaneuver your opponent. The host screen displays the gameplay as you compete to be the first to reach 5 points. Experience the excitement of this classic arcade game in a whole new way.",
                500,
                new MinigamePlayers[] { MinigamePlayers.ONE });
    }

}
