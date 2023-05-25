package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.constant.OutcomeType;

public class GameOverGetDTO {
    private OutcomeType gameOutcome;

    public OutcomeType getGameOutcome() {
        return gameOutcome;
    }

    public void setGameOutcome(OutcomeType gameOutcome) {
        this.gameOutcome = gameOutcome;
    }
    // private boolean isFinished;

    // public boolean getIsFinished() {
    //     return isFinished;
    // }

    // public void setIsFinished(boolean isFinished) {
    //     this.isFinished = isFinished;
    // }
}
