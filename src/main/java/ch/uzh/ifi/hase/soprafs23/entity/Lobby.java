package ch.uzh.ifi.hase.soprafs23.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

  @OneToMany(mappedBy = "id")
  private List<Team> teams = new ArrayList<Team>();
	
    
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



}
