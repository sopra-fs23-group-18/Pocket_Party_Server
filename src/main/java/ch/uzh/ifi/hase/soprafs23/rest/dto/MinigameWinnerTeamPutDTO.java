package ch.uzh.ifi.hase.soprafs23.rest.dto;

public class MinigameWinnerTeamPutDTO {
    private int score;
    //private TeamType color;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // public TeamType getColor() {
    //     return color;
    // }

    // public void setColor(TeamType color) {
    //     this.color = color;
    // }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
