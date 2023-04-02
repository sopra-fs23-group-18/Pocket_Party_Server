package ch.uzh.ifi.hase.soprafs23.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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
  private Random randomizer = new Random();

  @Id
  @GeneratedValue
  private Long id;
  
  @Column(nullable = false, unique = true)
   private int inviteCode;
  
  @Column(nullable = false)
  private int winningScore;

  //TODO: define Mapping of entities

  @OneToMany(cascade = CascadeType.ALL)
  private List<Team> teams = new ArrayList<Team>();

  @OneToMany(cascade = CascadeType.ALL)
  private List<Minigame> minigamesChoice = new ArrayList<Minigame>();

  @OneToMany(cascade = CascadeType.ALL)
  private LinkedList<Minigame> minigamesStarted = new LinkedList<Minigame>();

  @Column(nullable = false)
  private int currentMinigame = 0;

    //getters & setters
    public List<Minigame> getMinigamesStarted() {
        return minigamesStarted;
    }
    public void setMinigamesStarted(LinkedList<Minigame> minigamesStarted) {
        this.minigamesStarted = minigamesStarted;
    }  
    public List<Minigame> getMinigames() {
        return minigamesChoice;
    }
    public void setMinigames(List<Minigame> minigames) {
        this.minigamesChoice = minigames;
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
        if(minigamesChoice.size() == currentMinigame) {
            currentMinigame = 0;
            Collections.shuffle(minigamesChoice);
        }
        currentMinigame++;
        return minigamesChoice.get(currentMinigame-1);
    }

    public Minigame getNextMinigameRandom(){
        int index = randomizer.nextInt(minigamesChoice.size());
        Minigame suggestion = minigamesChoice.get(index);
        if (minigamesStarted.size() != 0){
        while (suggestion.getClass().equals(minigamesStarted.getLast().getClass())){
            minigamesChoice.get(randomizer.nextInt(minigamesChoice.size()));
        }}
        
        //TODO create instance via flyweight pattern

        return suggestion;



    }

    public void updateScore(Long teamId, int score) {
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
