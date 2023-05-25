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
import javax.persistence.Transient;

import ch.uzh.ifi.hase.soprafs23.constant.MinigamePlayers;
import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;
import ch.uzh.ifi.hase.soprafs23.constant.OutcomeType;
import ch.uzh.ifi.hase.soprafs23.entity.Player;

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

    // @Column(nullable = false)
    // private boolean isFinished = false;

    @Column(nullable = false)
    private OutcomeType minigameOutcome = OutcomeType.NOT_FINISHED;

    @Column(nullable = true)
    private String winnerTeamName;

    @Transient
    private final int scoreToGain;

    @Transient
    private final MinigamePlayers[] amntPlayersOptions;

    @Column(nullable = false)
    private MinigamePlayers amountOfPlayers;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Player> team1Players = new ArrayList<Player>();

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Player> team2Players = new ArrayList<Player>();

    @Transient
    private final String description;

    public Minigame(MinigameType type, String description, int scoreToGain, MinigamePlayers[] amntMinigamePlayers) {
        this.type = type;
        this.description = description;
        this.scoreToGain = scoreToGain;
        this.amntPlayersOptions = amntMinigamePlayers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MinigamePlayers getAmountOfPlayers() {
        return amountOfPlayers;
    }

    public void setAmountOfPlayers(MinigamePlayers amountOfPlayers) {
        this.amountOfPlayers = amountOfPlayers;
    }

    public MinigameType getType() {
        return type;
    }

    public MinigamePlayers[] getAmntPlayersOptions() {
        return amntPlayersOptions;
    }

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

    public String getWinner() {
        return winnerTeamName;
    }

    public void setWinner(String winnerTeamName) {
        this.winnerTeamName = winnerTeamName;
        if (this.winnerTeamName.equals("")){setMinigameOutcome(OutcomeType.DRAW);}
        else{
            setMinigameOutcome(OutcomeType.WINNER);
        }
    }

    // public void setIsFinished(boolean isFinished){
    //     this.isFinished = isFinished;
    // }

    // public boolean getIsFinished() {
    //     return isFinished;
    // }

    public OutcomeType getMinigameOutcome() {
        return minigameOutcome;
    }

    public void setMinigameOutcome(OutcomeType minigameOutcome) {
        this.minigameOutcome = minigameOutcome;
    }
}
