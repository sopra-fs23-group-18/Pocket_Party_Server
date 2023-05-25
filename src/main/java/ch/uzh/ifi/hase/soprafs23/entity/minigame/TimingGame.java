package ch.uzh.ifi.hase.soprafs23.entity.minigame;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;

import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;

import ch.uzh.ifi.hase.soprafs23.constant.MinigamePlayers;
import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;

@Entity
public class TimingGame extends Minigame{

    public TimingGame() {
        super(
            MinigameType.TIMING_GAME, 
            "Shake your phone at the right time to catch the objects falling from the sky! The player with a higher score wins!", 
            500,
            new MinigamePlayers[] {MinigamePlayers.ONE}
        );
    }

    

}
