package ch.uzh.ifi.hase.soprafs23.rest.dto;

import java.util.List;

import ch.uzh.ifi.hase.soprafs23.constant.TeamType;
import ch.uzh.ifi.hase.soprafs23.entity.Player;

public class WinnerTeamGetDTO {
    private Long id;
    private int score;
    private String name;
    // private TeamType color;
    private List<Player> players;

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    // public TeamType getColor() {
    //     return color;
    // }

    // public void setColor(TeamType color) {
    //     this.color = color;
    // }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
