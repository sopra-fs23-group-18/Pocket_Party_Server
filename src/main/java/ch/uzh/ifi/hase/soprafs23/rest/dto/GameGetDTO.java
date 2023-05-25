package ch.uzh.ifi.hase.soprafs23.rest.dto;

public class GameGetDTO {
    private Long id;
    private int winningScore;

    public int getWinningScore() {
        return winningScore;
    }

    public void setWinningScore(int winningScore) {
        this.winningScore = winningScore;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    } 
}
