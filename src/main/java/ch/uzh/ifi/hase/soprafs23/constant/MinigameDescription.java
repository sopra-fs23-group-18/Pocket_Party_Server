package ch.uzh.ifi.hase.soprafs23.constant;

import java.util.EnumMap;
import java.util.Map;

public final class MinigameDescription {

    private static EnumMap<MinigameType, String> minigames = new EnumMap<>(Map.ofEntries(
        Map.entry(MinigameType.TAPPING_GAME, "Tap the screen as fast as you can!"), 
        Map.entry(MinigameType.TIMING_GAME, "Shake your phone at the right time to catch the objects falling from the sky!"),
        Map.entry(MinigameType.HOT_POTATO, "The players toss a potato to each other while time is counting down. The player who is holding the object when the timer reaches 0 is eliminated."),
        Map.entry(MinigameType.VIBRATION_GAME, "Fix"),
        Map.entry(MinigameType.PONG_GAME, "Fix"),
        Map.entry(MinigameType.RPS_GAME, "Fix")
        ));
    
    private MinigameDescription(){

    }

    public static EnumMap<MinigameType, String> getMinigamesDescriptions(){
        return minigames;
    }
}
