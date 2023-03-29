package ch.uzh.ifi.hase.soprafs23.entity;

public class TappingGame extends Minigame {

    public TappingGame(int scoreToGain) {
        super(scoreToGain);
        this.name = "TappingGame";
    }

    public String toString() {
        return this.name;
    };

}
