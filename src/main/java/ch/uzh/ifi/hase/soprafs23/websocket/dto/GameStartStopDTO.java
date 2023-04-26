package ch.uzh.ifi.hase.soprafs23.websocket.dto;

import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;

enum GameSignal {
    START,
    STOP
}

public class GameStartStopDTO {
    private MinigameType minigame;
    private GameSignal signal;

    public MinigameType getMinigame() {
        return minigame;
    }
    public void setMinigame(MinigameType minigame) {
        this.minigame = minigame;
    }
   
    public GameSignal getSignal() {
        return signal;
    }
    public void setSignal(GameSignal signal) {
        this.signal = signal;
    }
}
