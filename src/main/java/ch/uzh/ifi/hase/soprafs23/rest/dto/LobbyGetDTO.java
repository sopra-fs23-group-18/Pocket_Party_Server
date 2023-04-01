package ch.uzh.ifi.hase.soprafs23.rest.dto;

import java.util.List;

import ch.uzh.ifi.hase.soprafs23.entity.Minigame;
import ch.uzh.ifi.hase.soprafs23.entity.Team;

public class LobbyGetDTO {
    private Long id;
    private int inviteCode;
    private int winningScore;
    private List<Team> teams;
    private List<Minigame> minigames;

    
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

    public int getWinningScore() {
        return winningScore;
    }

    public void setWinningScore(int winningScore) {
        this.winningScore = winningScore;
    }

    public int getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(int inviteCode) {
        this.inviteCode = inviteCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
}
