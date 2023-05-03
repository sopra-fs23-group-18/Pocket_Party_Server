package ch.uzh.ifi.hase.soprafs23.websocket.dto;

import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;

enum GameSignal {
    START,
    STOP,
    PLAY
}

public class GameStartStopDTO {
    private Object data;
    
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

    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }
}
