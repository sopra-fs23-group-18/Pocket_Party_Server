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
public class Vibration extends Minigame{

    public Vibration() {
        super(
            MinigameType.VIBRATION_GAME, 
            "test", 
            200,
            new MinigamePlayers[] {MinigamePlayers.ONE}
        );
    }

    

}
