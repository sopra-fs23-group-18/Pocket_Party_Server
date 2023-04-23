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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ch.uzh.ifi.hase.soprafs23.constant.TeamType;

@Entity
@Table(name = "TEAM")
public class Team implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "teamId")
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private int score = 0;

    // FOREIGN KEY TO LOBBY
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lobbyId")
    private Lobby lobby;


    private TeamType color; 

    @Column(nullable = false)
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Player> players = new ArrayList<Player>();

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Player> getPlayers() {
        return players;
    }

    // public void addPlayer(Player player) {
    //     this.players.add(player);
    // }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
   
    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }

    public TeamType getColor() {
        return color;
    }

    public void setColor(TeamType color) {
        this.color = color;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }
}
