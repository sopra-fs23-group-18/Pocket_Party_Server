package ch.uzh.ifi.hase.soprafs23.constant;

import java.util.EnumMap;
import java.util.Map;

public final class MinigameDescription {

    private static EnumMap<MinigameType, String> minigames = new EnumMap<>(Map.ofEntries(
        Map.entry(MinigameType.TAPPING_GAME, "Tap the screen as fast as you can!"), 
        Map.entry(MinigameType.TIMING_GAME, "Shake your phone at the right time to catch the objects falling from the sky!")
        ));
    
    private MinigameDescription(){

    }

    public static EnumMap<MinigameType, String> getMinigamesDescriptions(){
        return minigames;
    }
}
