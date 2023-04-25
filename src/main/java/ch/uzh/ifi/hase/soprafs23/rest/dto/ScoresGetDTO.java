package ch.uzh.ifi.hase.soprafs23.rest.dto;

import java.util.List;


public class ScoresGetDTO {
    private int winningScore;
    private List<TeamGetDTO> teams;

   

    public List<TeamGetDTO> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamGetDTO> teams) {
        this.teams = teams;
    }

    public int getWinningScore() {
        return winningScore;
    }

    public void setWinningScore(int winningScore) {
        this.winningScore = winningScore;
    }

    
}
