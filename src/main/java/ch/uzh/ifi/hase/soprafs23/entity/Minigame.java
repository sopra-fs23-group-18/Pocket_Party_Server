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
import javax.persistence.Transient;

import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Minigame implements Serializable{
    
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "minigameId")
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private final MinigameType type;

    @Column(nullable = false)
    private boolean isFinished = false;

    @Column(nullable = true)
    private String winnerTeamName;

    @Transient
    private final int scoreToGain = 500;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Player> team1Players = new ArrayList<Player>();

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Player> team2Players = new ArrayList<Player>();

    @Transient
    private final String description;

    public Minigame(MinigameType type, String description) {
        this.type = type;
        this.description = description;
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

    // public void setType(MinigameType type){
    //     this.type = type;
    // }

    // public void setDescription(String description) {
    //     this.description = description;
    // }

    public String getDescription() {
        return description;
    }

    public List<Player> getTeam2Players() {
        return team2Players;
    }

    public void setTeam2Players(List<Player> team2Players) {
        this.team2Players = team2Players;
    }

    public List<Player> getTeam1Players() {
        return team1Players;
    }

    public void setTeam1Players(List<Player> team1Players) {
        this.team1Players = team1Players;
    }

    public int getScoreToGain() {
        return scoreToGain;
    }

    // public void setScoreToGain(int scoreToGain){
    //     this.scoreToGain = scoreToGain;
    // }

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
