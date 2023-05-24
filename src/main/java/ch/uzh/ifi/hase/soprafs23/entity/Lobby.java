package ch.uzh.ifi.hase.soprafs23.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @Column(name = "lobbyId")
    @GeneratedValue
    private Long id;

    // FOREIGN KEY TO GAME
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "gameId")
    private Game game;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    private List<Game> finishedGames = new ArrayList<Game>();

    @Column(nullable = false, unique = true)
    private int inviteCode;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Team> teams = new ArrayList<Team>();
    
    @OneToMany(cascade = CascadeType.ALL)
    private List<Player> unassignedPlayers = new ArrayList<Player>();


    


    // getters & setters

    public List<Game> getFinishedGames() {
        return finishedGames;
    }

    public void setFinishedGames(List<Game> finishedGames) {
        this.finishedGames = finishedGames;
    }
    
    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
    

    public List<Player> getUnassignedPlayers() {
        return unassignedPlayers;
    }

    public void setUnassignedPlayers(List<Player> unassignedPlayers) {
        this.unassignedPlayers = unassignedPlayers;
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

    // additional methods to add and get single elements

    public void addToUnassignedPlayers(Player player) {
        unassignedPlayers.add(player);
    }

    // Player names in each lobby need to be unique (per lobby)
    // public void removeFromUnassignedPlayers(String playerName) {
    // Player removedPlayer;
    // for (Player player : unassignedPlayers) {
    // if (player.getNickname().equals(playerName)) {
    // removedPlayer = player;
    // unassignedPlayers.remove(removedPlayer);
    // }
    // }
    // }

}
