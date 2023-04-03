package ch.uzh.ifi.hase.soprafs23.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
import javax.persistence.Table;

import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;


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

  @Column(nullable = true)
  private Minigame upcomingMinigame;


  //TODO: define Mapping of entities


  @OneToMany(cascade = CascadeType.ALL)
  private List<Team> teams = new ArrayList<Team>();

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "MinigameType", joinColumns = @JoinColumn(name = "id"))
  @Enumerated(EnumType.STRING)
  private List<MinigameType> minigamesChoice = new ArrayList<MinigameType>();

  @OneToMany(cascade = CascadeType.ALL)
  private List<Minigame> minigamesPlayed = new ArrayList<Minigame>();



//   @OneToMany(cascade = CascadeType.ALL)
//   private List<Minigame> minigames = new ArrayList<Minigame>();

//   @Column(nullable = false)
//   private int currentMinigame = 0;

    //getters & setters

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
    // public List<Minigame> getMinigames() {
    //     return minigames;
    // }
    // public void setMinigames(List<Minigame> minigames) {
    //     this.minigames = minigames;
    // }
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


    //additional methods to add and get single elements

    public void addToMinigamesPlayed(Minigame nextMinigame){
        minigamesPlayed.add(nextMinigame);
    }

    // public Minigame getNextMinigame() {
    //     if(minigames.size() == currentMinigame) {
    //         currentMinigame = 0;
    //         Collections.shuffle(minigames);
    //     }
    //     currentMinigame++;
    //     return minigames.get(currentMinigame-1);
    // }

    // public MinigameType getNextMinigameType(){
    //     int index = randomizer.nextInt(minigamesChoice.size());
    //     MinigameType suggestion = minigamesChoice.get(index);
    //     if (minigamesPlayed.size() != 0){
    //     while (suggestion.equals(minigamesPlayed.get(minigamesPlayed.size()-1).getType())){
    //         suggestion = minigamesChoice.get(randomizer.nextInt(minigamesChoice.size()));
    //     }}

    //     //add description from enumMap
    //     // String description = MinigameDescription.getMinigamesDescriptions().get(suggestion);
    //     // Minigame nextMinigame = new Minigame(500, suggestion, description);


    //     // while (suggestion.getClass().equals(minigamesPlayed.getLast().getClass())){
    //     //     minigamesChoice.get(randomizer.nextInt(minigamesChoice.size()));
    //     // }}

    //     return suggestion;
    // }

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
        //TODO: what happens if both are over the winningScore?
    }

}
