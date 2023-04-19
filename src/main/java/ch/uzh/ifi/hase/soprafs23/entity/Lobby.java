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
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;

/**
 * Internal Lobby representation
 * This class composes the internal representation of the lobby and defines how
 * the lobby is stored in the database.
 */

@Entity
@Table(name = "LOBBY")
public class Lobby implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private int inviteCode;

    @Column(nullable = false)
    private int winningScore;

    // TODO: define Mapping of entities

    @OneToOne(cascade = CascadeType.ALL)
    private Minigame upcomingMinigame;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Team> teams = new ArrayList<Team>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "MinigameType", joinColumns = @JoinColumn(name = "id"))
    @Enumerated(EnumType.STRING)
    private List<MinigameType> minigamesChoice = new ArrayList<MinigameType>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<Minigame> minigamesPlayed = new ArrayList<Minigame>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<Player> unassignedPlayers = new ArrayList<Player>();

    // getters & setters

    public List<Player> getUnassignedPlayers() {
        return unassignedPlayers;
    }

    public void setUnassignedPlayers(List<Player> unassignedPlayers) {
        this.unassignedPlayers = unassignedPlayers;
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

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(int inviteCode) {
        this.inviteCode = inviteCode;
    }

    public int getWinningScore() {
        return winningScore;
    }

    public void setWinningScore(int winningScore) {
        this.winningScore = winningScore;
    }

    // additional methods to add and get single elements

    public void addToMinigamesPlayed(Minigame nextMinigame) {
        minigamesPlayed.add(nextMinigame);
    }

    public void addToUnassignedPlayers(Player player) {
        unassignedPlayers.add(player);
    }

    // Player names in each lobby need to be unique (per lobby)
    public void removeFromUnassignedPlayers(String playerName) {
        Player removedPlayer;
        for (Player player : unassignedPlayers) {
            if (player.getNickname().equals(playerName)) {
                removedPlayer = player;
                unassignedPlayers.remove(removedPlayer);
            }
        }
    }

    // move these methods into service at a later point

    public void updateScore(Long teamId, int score) {
        for (Team team : teams) {
            if (team.getId() == teamId) {
                team.setScore(team.getScore() + score);
            }
        }
    }

    public boolean isGameOver() {
        for (Team team : teams) {
            if (team.getScore() >= winningScore) {
                return true;
            }
        }
        return false;
    }

    public Team getWinner() {
        for (Team team : teams) {
            if (team.getScore() >= winningScore) {
                return team;
            }
        }
        return null;
        // TODO: what happens if both are over the winningScore?
    }

}
