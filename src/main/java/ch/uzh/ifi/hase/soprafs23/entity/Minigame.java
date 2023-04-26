package ch.uzh.ifi.hase.soprafs23.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;

import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;

import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Minigame implements Serializable{
    
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "minigameId")
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private MinigameType type;

    @Column(nullable = false)
    private boolean isFinished = false;

    @Column(nullable = true)
    private String winnerTeamName;

    @Column(nullable = false)
    private int scoreToGain;

    @OneToOne(cascade = CascadeType.ALL)
    private Player team1Player;

    @OneToOne(cascade = CascadeType.ALL)
    private Player team2Player;

    @Column(nullable = false)
    protected String description;

    public Minigame() {
    }

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

    public Player getTeam2Player() {
        return team2Player;
    }

    public void setTeam2Player(Player team2Player) {
        this.team2Player = team2Player;
    }

    public Player getTeam1Player() {
        return team1Player;
    }

    public void setTeam1Player(Player team1Player) {
        this.team1Player = team1Player;
    }

    public int getScoreToGain() {
        return scoreToGain;
    }

    public void setScoreToGain(int scoreToGain){
        this.scoreToGain = scoreToGain;
    }

    public String getWinner() {
        return winnerTeamName;
    }

    public void setWinner(String winnerTeamName) {
        this.winnerTeamName = winnerTeamName;
        if (this.winnerTeamName != null){setIsFinished(true);}
    }

    public void setIsFinished(boolean isFinished){
        this.isFinished = isFinished;
    }

    public boolean getIsFinished() {
        return isFinished;
    }

    

}
