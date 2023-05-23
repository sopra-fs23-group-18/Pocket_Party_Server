package ch.uzh.ifi.hase.soprafs23.constant;

import java.util.EnumMap;
import java.util.Map;

import ch.uzh.ifi.hase.soprafs23.entity.minigame.HotPotato;
import ch.uzh.ifi.hase.soprafs23.entity.minigame.Minigame;
import ch.uzh.ifi.hase.soprafs23.entity.minigame.Pong;
import ch.uzh.ifi.hase.soprafs23.entity.minigame.RPS;
import ch.uzh.ifi.hase.soprafs23.entity.minigame.Strategy;
import ch.uzh.ifi.hase.soprafs23.entity.minigame.TappingGame;
import ch.uzh.ifi.hase.soprafs23.entity.minigame.TimingGame;
import ch.uzh.ifi.hase.soprafs23.entity.minigame.Vibration;

public final class MinigameMapper {

    private static EnumMap<MinigameType, Class<? extends Minigame>> minigameClasses = new EnumMap<MinigameType, Class<? extends Minigame>>(Map.ofEntries(
        Map.entry(MinigameType.TAPPING_GAME, TappingGame.class),
        Map.entry(MinigameType.TIMING_GAME, TimingGame.class),
        Map.entry(MinigameType.HOT_POTATO, HotPotato.class),
        Map.entry(MinigameType.VIBRATION_GAME, Vibration.class),
        Map.entry(MinigameType.PONG_GAME, Pong.class),
        Map.entry(MinigameType.RPS_GAME, RPS.class),
        Map.entry(MinigameType.STRATEGY_GAME, Strategy.class)
    ));

    private static EnumMap<MinigamePlayers, Integer> minigamePlayers = new EnumMap<MinigamePlayers, Integer>(Map.ofEntries(
        Map.entry(MinigamePlayers.ONE, 1),
        Map.entry(MinigamePlayers.TWO, 2)
    ));

    public static EnumMap<MinigamePlayers, Integer> getMinigamePlayers() {
        return minigamePlayers;
    }

    public static EnumMap<MinigameType, Class<? extends Minigame>> getMinigameClasses(){
        return minigameClasses;
    }
}
