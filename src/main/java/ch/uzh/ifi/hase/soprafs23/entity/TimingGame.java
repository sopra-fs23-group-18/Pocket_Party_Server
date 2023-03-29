package ch.uzh.ifi.hase.soprafs23.entity;

public class TimingGame extends Minigame {

    public TimingGame(int scoreToGain) {
        super(scoreToGain);
        this.name = "TimingGame";
        this.description = "Shake your phone at the right time to catch the objects falling from the sky!";
    }

}
