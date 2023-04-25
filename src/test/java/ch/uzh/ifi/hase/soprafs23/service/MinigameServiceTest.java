package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;
import ch.uzh.ifi.hase.soprafs23.entity.Minigame;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.repository.MinigameRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;

@WebAppConfiguration
@SpringBootTest
public class MinigameServiceTest {

    @Autowired
    private MinigameService minigameService;

    @Autowired
    private MinigameRepository minigameRepository;

    @Test
    public void chosenMinigames_success() {
        // given
        List<MinigameType> minigames = Arrays.asList(MinigameType.values());

        // when
        List<MinigameType> chosenMinigames = minigameService.chosenMinigames();

        // then
        assertNotNull(chosenMinigames);
        assertEquals(minigames.size(), chosenMinigames.size());
        assertEquals(minigames.get(0), chosenMinigames.get(0));
    }

    @Test
    public void createMinigame_success() {
        // given
        MinigameType minigameType = MinigameType.TIMING_GAME;

        Player player1 = new Player();
        player1.setNickname("test1");

        Player player2 = new Player();
        player2.setNickname("test2");

        // when
        Minigame createdMinigame = minigameService.createMinigame(minigameType, player1, player2);

        // then
        assertNotNull(createdMinigame);
        assertEquals(minigameType, createdMinigame.getType());
        assertEquals(player1, createdMinigame.getTeam1Player());
        assertEquals(player2, createdMinigame.getTeam2Player());
    }

    @Test
    public void getMinigame_validId_success() {
        // given
        MinigameType minigameType = MinigameType.TIMING_GAME;

        Player player1 = new Player();
        player1.setNickname("test1");

        Player player2 = new Player();
        player2.setNickname("test2");

        Minigame createdMinigame = minigameService.createMinigame(minigameType, player1, player2);

        // when
        Minigame foundMinigame = minigameService.getMinigame(createdMinigame.getId());

        // then
        assertNotNull(foundMinigame);
        assertEquals(createdMinigame.getId(), foundMinigame.getId());
        assertEquals(createdMinigame.getType(), foundMinigame.getType());
        assertEquals(createdMinigame.getTeam1Player(), foundMinigame.getTeam1Player());
        assertEquals(createdMinigame.getTeam2Player(), foundMinigame.getTeam2Player());
    }

    @Test
    public void getMinigame_invalidId_throwsException() {
        // given
        MinigameType minigameType = MinigameType.TIMING_GAME;

        Player player1 = new Player();
        player1.setNickname("test1");

        Player player2 = new Player();
        player2.setNickname("test2");

        // when
        Minigame createdMinigame = minigameService.createMinigame(minigameType, player1, player2);

        // then
        assertThrows(ResponseStatusException.class, () -> {
            minigameService.getMinigame(createdMinigame.getId() + 1);
        });
    }

    @Test
    public void updateMinigame_success() {
        // given
        MinigameType minigameType = MinigameType.TIMING_GAME;

        Player player1 = new Player();
        player1.setNickname("test1");

        Player player2 = new Player();
        player2.setNickname("test2");

        // when
        Minigame createdMinigame = minigameService.createMinigame(minigameType, player1, player2);
        minigameService.updateMinigame(createdMinigame.getId(), "test");
        Minigame foundMinigame = minigameService.getMinigame(createdMinigame.getId());

        // then
        assertEquals("test", foundMinigame.getWinner());
    }

}
