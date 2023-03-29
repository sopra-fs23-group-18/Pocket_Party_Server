package ch.uzh.ifi.hase.soprafs23.rest.dto;

public class MinigameGetDTO {
    private String name;
    private String description;
    private int scoreToGain;
    private String team1Player;
    private String team2Player;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTeam1Player() {
        return team1Player;
    }

    public void setTeam1Player(String team1Player) {
        this.team1Player = team1Player;
    } 

    public String getTeam2Player() {
        return team2Player;
    }

    public void setTeam2Player(String team2Player) {
        this.team2Player = team2Player;
    }

    public int getScoreToGain() {
        return scoreToGain;
    }

    public void setScoreToGain(int scoreToGain) {
        this.scoreToGain = scoreToGain;
    }
    
}
