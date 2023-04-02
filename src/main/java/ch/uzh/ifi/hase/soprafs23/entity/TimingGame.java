package ch.uzh.ifi.hase.soprafs23.entity;

import javax.persistence.Entity;

@Entity
public class TimingGame extends Minigame {
    // public TimingGame() {
    //     super();
    // }

    public TimingGame(int scoreToGain) {
        super(scoreToGain);    
    }
    // public TimingGame(int scoreToGain) {
    //     super(scoreToGain);
    //     this.name = "TimingGame";
    //     this.description = "Shake your phone at the right time to catch the objects falling from the sky!";
    
    // }

    // public TimingGame(Minigame pattern) {
    //     super(pattern.getScoreToGain());
    //     this.name = pattern.getName();
    //     this.description = pattern.getDescription();
    // }

}
