package ch.uzh.ifi.hase.soprafs23.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;

import javax.persistence.InheritanceType;

import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Minigame implements Serializable{
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private MinigameType type;

    @Column(nullable = false)
    private boolean isFinished = false;

    @Column(nullable = true)
    private Team winner;

    @Column(nullable = false)
    private int scoreToGain;

    @Column(nullable = false)
    private String team1Player = "No Player";

    @Column(nullable = false)
    private String team2Player = "No Player";

    // @Column(nullable = false)
    // protected String name;

    @Column(nullable = false)
    protected String description;

    public Minigame() {
    }

    public Minigame(int scoreToGain) {
        this.scoreToGain = scoreToGain;
    }

    // public Minigame(int scoreToGain, MinigameType type, String description) {
    //     this.scoreToGain = scoreToGain;
    //     this.type = type;
    //     this.description = description;
    // }

    // public Minigame(Minigame pattern) {
    //     this.scoreToGain = pattern.getScoreToGain();
    // }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MinigameType getType() {
        return type;
    }

    public void setType(MinigameType type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    // public String getName() {
    //     return name;
    // }

    public String getTeam2Player() {
        return team2Player;
    }

    public void setTeam2Player(String team2Player) {
        this.team2Player = team2Player;
    }

    public String getTeam1Player() {
        return team1Player;
    }

    public void setTeam1Player(String team1Player) {
        this.team1Player = team1Player;
    }

    public int getScoreToGain() {
        return scoreToGain;
    }

    public void setScoreToGain(int scoreToGain){
        this.scoreToGain = scoreToGain;
    }

    public Team getWinner() {
        return winner;
    }

    public void setWinner(Team winner) {
        this.winner = winner;
        if (this.winner != null){setIsFinished(true);}
    }

    public void setIsFinished(boolean isFinished){
        this.isFinished = isFinished;
    }

    public boolean getIsFinished() {
        return isFinished;
    }

    

}
