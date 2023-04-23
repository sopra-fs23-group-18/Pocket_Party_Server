package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;
import ch.uzh.ifi.hase.soprafs23.entity.Player;

public class MinigameGetDTO {
    private String description;
    private int scoreToGain;
    private Player team1Player;
    private Player team2Player;
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

    public Player getTeam1Player() {
        return team1Player;
    }

    public void setTeam1Player(Player team1Player) {
        this.team1Player = team1Player;
    } 

    public Player getTeam2Player() {
        return team2Player;
    }

    public void setTeam2Player(Player team2Player) {
        this.team2Player = team2Player;
    }

    public int getScoreToGain() {
        return scoreToGain;
    }

    public void setScoreToGain(int scoreToGain) {
        this.scoreToGain = scoreToGain;
    }
    
}
