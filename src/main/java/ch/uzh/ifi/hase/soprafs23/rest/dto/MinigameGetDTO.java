package ch.uzh.ifi.hase.soprafs23.rest.dto;

import java.util.List;

import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;
import ch.uzh.ifi.hase.soprafs23.entity.Player;

public class MinigameGetDTO {
    private String description;
    private int scoreToGain;
    private List<Player> team1Players;
    private List<Player> team2Players;
    private MinigameType type;


    public MinigameType getType() {
        return type;
    }

    public void setType(MinigameType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Player> getTeam1Players() {
        return team1Players;
    }

    public void setTeam1Players(List<Player> team1Players) {
        this.team1Players = team1Players;
    } 

    public List<Player> getTeam2Players() {
        return team2Players;
    }

    public void setTeam2Players(List<Player> team2Players) {
        this.team2Players = team2Players;
    }

    public int getScoreToGain() {
        return scoreToGain;
    }

    public void setScoreToGain(int scoreToGain) {
        this.scoreToGain = scoreToGain;
    }
    
}
