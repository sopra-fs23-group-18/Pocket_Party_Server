package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;

public class Minigame {
    private boolean isFinished = false;
    private Team winner;
    private MinigameType type;
    private int scoreToGain;
    private String team1Player;
    private String team2Player;

    public Minigame(MinigameType type, int scoreToGain){
        this.type = type;
        this.scoreToGain = scoreToGain;
    }

    public void setWinner(Team winner){
        this.winner = winner;
        isFinished = true;
    }


}
