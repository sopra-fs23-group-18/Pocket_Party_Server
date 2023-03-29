package ch.uzh.ifi.hase.soprafs23.entity;

public class TappingGame extends Minigame {

    public TappingGame(int scoreToGain) {
        super(scoreToGain);
        this.name = "TappingGame";
        this.description = "Tap the screen as fast as you can!";
    }

}
