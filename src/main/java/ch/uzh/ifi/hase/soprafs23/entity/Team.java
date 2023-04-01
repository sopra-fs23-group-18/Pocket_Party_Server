package ch.uzh.ifi.hase.soprafs23.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "TEAM")
public class Team implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private int score = 0;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Player> players = new ArrayList<Player>();
    public List<Player> getPlayers() {
        return players;
    }
    public void addPlayer(Player player) {
        this.players.add(player);
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
}
