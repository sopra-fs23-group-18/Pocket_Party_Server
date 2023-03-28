package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;

public class Minigame {
    private boolean isFinished = false;
    private Team winner;
    private MinigameType type;
    private int scroeToGain;

    public Minigame(MinigameType type){
        this.type = type;
    }

    public void setWinner(Team winner){
        this.winner = winner;
        isFinished = true;
    }


}
