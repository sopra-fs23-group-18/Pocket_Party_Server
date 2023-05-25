package ch.uzh.ifi.hase.soprafs23.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.JoinColumn;

import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;
import ch.uzh.ifi.hase.soprafs23.constant.OutcomeType;
import ch.uzh.ifi.hase.soprafs23.constant.PlayerChoice;
import ch.uzh.ifi.hase.soprafs23.entity.minigame.Minigame;

@Entity
@Table(name = "GAME")
public class Game implements Serializable{
    
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "gameId")
    @GeneratedValue
    private Long id;

    // FOREIGN KEY TO LOBBY
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lobbyId")
    private Lobby lobby;

    @Column(nullable = false)
    private int winningScore;    

    // @Column(nullable = false)
    // private boolean isFinished = false;

    @OneToOne(cascade = CascadeType.ALL)
    private Minigame upcomingMinigame;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "MinigameType", joinColumns = @JoinColumn(name = "id"))
    @Enumerated(EnumType.STRING)
    private List<MinigameType> minigamesChoice = new ArrayList<MinigameType>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<Minigame> minigamesPlayed = new ArrayList<Minigame>();

    @Column(nullable = false)
    private PlayerChoice playerChoice;

    @Column(nullable = true)
    private OutcomeType gameOutcome = OutcomeType.NOT_FINISHED;

    

    //getters & setters

    public OutcomeType getGameOutcome() {
        return gameOutcome;
    }

    public void setGameOutcome(OutcomeType gameOutcome) {
        this.gameOutcome = gameOutcome;
    }

    public PlayerChoice getPlayerChoice() {
        return playerChoice;
    }

    public void setPlayerChoice(PlayerChoice playerChoice) {
        this.playerChoice = playerChoice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    public Minigame getUpcomingMinigame() {
        return upcomingMinigame;
    }

    public void setUpcomingMinigame(Minigame upcomingMinigame) {
        this.upcomingMinigame = upcomingMinigame;
    }

    public List<MinigameType> getMinigamesChoice() {
        return minigamesChoice;
    }

    public void setMinigamesChoice(List<MinigameType> minigamesChoice) {
        this.minigamesChoice = minigamesChoice;
    }

    public List<Minigame> getMinigamesPlayed() {
        return minigamesPlayed;
    }

    public void setMinigamesPlayed(List<Minigame> minigamesPlayed) {
        this.minigamesPlayed = minigamesPlayed;
    }

    public int getWinningScore() {
        return winningScore;
    }

    public void setWinningScore(int winningScore) {
        this.winningScore = winningScore;
    }

    

    // public boolean getIsFinished() {
    //     return isFinished;
    // }

    // public void setIsFinished(boolean isFinished) {
    //     this.isFinished = isFinished;
    // }

    // additional methods to add and get single elements

    public void addToMinigamesPlayed(Minigame nextMinigame) {
        minigamesPlayed.add(nextMinigame);
    }
}
