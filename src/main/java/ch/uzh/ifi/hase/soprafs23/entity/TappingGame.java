package ch.uzh.ifi.hase.soprafs23.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "TAPPINGGAME")
public class TappingGame extends Minigame {
    // public TappingGame() {
    //     super();
    // }
    public TappingGame(int scoreToGain) {
        super(scoreToGain);        
    }
    // public TappingGame(int scoreToGain) {
    //     super(scoreToGain);
    //     this.name = "TappingGame";
    //     this.description = "Tap the screen as fast as you can!";
        
    // }

    // public TappingGame(Minigame pattern) {
    //     super(pattern.getScoreToGain());
    //     this.name = pattern.getName();
    //     this.description = pattern.getDescription();
    // }

}
