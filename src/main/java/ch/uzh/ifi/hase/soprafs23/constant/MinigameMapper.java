package ch.uzh.ifi.hase.soprafs23.constant;

import java.util.EnumMap;
import java.util.Map;

import ch.uzh.ifi.hase.soprafs23.entity.HotPotato;
import ch.uzh.ifi.hase.soprafs23.entity.Minigame;
import ch.uzh.ifi.hase.soprafs23.entity.Pong;
import ch.uzh.ifi.hase.soprafs23.entity.RPS;
import ch.uzh.ifi.hase.soprafs23.entity.TappingGame;
import ch.uzh.ifi.hase.soprafs23.entity.TimingGame;
import ch.uzh.ifi.hase.soprafs23.entity.Vibration;

public final class MinigameMapper {

    private static EnumMap<MinigameType, Class<? extends Minigame>> minigameClasses = new EnumMap<>(Map.ofEntries(
        Map.entry(MinigameType.TAPPING_GAME, TappingGame.class),
        Map.entry(MinigameType.TIMING_GAME, TimingGame.class),
        Map.entry(MinigameType.HOT_POTATO, HotPotato.class),
        Map.entry(MinigameType.VIBRATION_GAME, Vibration.class),
        Map.entry(MinigameType.PONG_GAME, Pong.class),
        Map.entry(MinigameType.RPS_GAME, RPS.class)
));

    public static EnumMap<MinigameType, Class<? extends Minigame>> getMinigameMapper(){
        return minigameClasses;
    }
}
