package ch.uzh.ifi.hase.soprafs23.rest.dto;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;

public class GamePostDTO {
    private List<MinigameType> minigamesChoice = new ArrayList<MinigameType>();

    public List<MinigameType> getMinigamesChoice() {
        return minigamesChoice;
    }

    public void setMinigamesChoice(List<MinigameType> minigamesChoice) {
        this.minigamesChoice = minigamesChoice;
    }
    
}
