package ch.uzh.ifi.hase.soprafs23.entity;

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

import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;

@Entity
public class TappingGame extends Minigame{

    public TappingGame() {
        super(MinigameType.TAPPING_GAME, "Tap the screen as fast as you can!");
    }

    

}
