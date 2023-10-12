package ch.uzh.ifi.hase.soprafs23.constant;

import java.util.EnumMap;
import java.util.Map;

import ch.uzh.ifi.hase.soprafs23.entity.minigame.Minigame;
import ch.uzh.ifi.hase.soprafs23.entity.minigame.Pong;
import ch.uzh.ifi.hase.soprafs23.entity.minigame.PushDown;
import ch.uzh.ifi.hase.soprafs23.entity.minigame.RPS;
import ch.uzh.ifi.hase.soprafs23.entity.minigame.GreedyGambit;
import ch.uzh.ifi.hase.soprafs23.entity.minigame.QuickFingers;
import ch.uzh.ifi.hase.soprafs23.entity.minigame.TimingTumble;
import ch.uzh.ifi.hase.soprafs23.entity.minigame.VibrationVoyage;

public final class MinigameMapper {

    private static EnumMap<MinigameType, Class<? extends Minigame>> minigameClasses = new EnumMap<MinigameType, Class<? extends Minigame>>(
            Map.ofEntries(
                    Map.entry(MinigameType.QUICK_FINGERS, QuickFingers.class),
                    Map.entry(MinigameType.TIMING_TUMBLE, TimingTumble.class),
                    Map.entry(MinigameType.VIBRATION_VOYAGE, VibrationVoyage.class),
                    Map.entry(MinigameType.POCKET_PONG, Pong.class),
                    Map.entry(MinigameType.ROCK_PAPER_SCISSORS, RPS.class),
                    Map.entry(MinigameType.GREEDY_GAMBIT, GreedyGambit.class),
                    Map.entry(MinigameType.PUSH_DOWN, PushDown.class)));

    private static EnumMap<MinigamePlayers, Integer> minigamePlayers = new EnumMap<MinigamePlayers, Integer>(
            Map.ofEntries(
                    Map.entry(MinigamePlayers.ONE, 1),
                    Map.entry(MinigamePlayers.TWO, 2)));

    public static EnumMap<MinigamePlayers, Integer> getMinigamePlayers() {
        return minigamePlayers;
    }

    public static EnumMap<MinigameType, Class<? extends Minigame>> getMinigameClasses() {
        return minigameClasses;
    }
}
