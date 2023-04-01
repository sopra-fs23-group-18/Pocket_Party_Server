package ch.uzh.ifi.hase.soprafs23.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * Internal Lobby representation
 * This class composes the internal representation of the lobby and defines how
 * the lobby is stored in the database.
 */

 @Entity
 @Table(name = "LOBBY")
public class Lobby implements Serializable{
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  private Long id;

  
  @Column(nullable = false)
   private int inviteCode;
  
  @Column(nullable = false)
  private int winningScore;

  @OneToMany(cascade = CascadeType.ALL)
  private List<Team> teams = new ArrayList<Team>();

  @OneToMany(cascade = CascadeType.ALL)
  private List<Minigame> minigames = new ArrayList<Minigame>();

  @Column(nullable = false)
  private int currentMinigame = 0;
    
public List<Minigame> getMinigames() {
    return minigames;
}

public void setMinigames(List<Minigame> minigames) {
    this.minigames = minigames;
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
public Minigame getNextMinigame() {
    if(minigames.size() == currentMinigame) {
        currentMinigame = 0;
        Collections.shuffle(minigames);
    }
    currentMinigame++;
    return minigames.get(currentMinigame-1);
}
public void updateScore(int teamId, int score) {
    for(Team team : teams) {
        if(team.getId() == teamId) {
            team.setScore(team.getScore() + score);
        }
    }
}
public boolean isGameOver() {
    for(Team team : teams) {
        if(team.getScore() >= winningScore) {
            return true;
        }
    }
    return false;
}
public Team getWinner() {
    for(Team team : teams) {
        if(team.getScore() >= winningScore) {
            return team;
        }
    }
    return null;
}

}
