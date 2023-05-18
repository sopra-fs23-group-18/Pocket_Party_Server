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

import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;

@Entity
public class HotPotato extends Minigame{

    public HotPotato() {
        super(MinigameType.HOT_POTATO, "The players toss a potato to each other while time is counting down. The player who is holding the object when the timer reaches 0 is eliminated.");
    }

    

}
