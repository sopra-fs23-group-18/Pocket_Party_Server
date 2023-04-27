package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.repository.PlayerRepository;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;

@WebAppConfiguration
@SpringBootTest
public class PlayerServiceTest {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PlayerRepository playerRepository;

    @Test
    public void createPlayer_success() {
        // given
        Player player = new Player();
        player.setNickname("test");

        // when
        Player createdPlayer = playerService.createPlayer(player);

        // then
        assertNotNull(createdPlayer);
        assertEquals(player.getNickname(), createdPlayer.getNickname());
    }

    @Test
    public void getPlayer_validId_success() {
        // given
        Player player = new Player();
        player.setNickname("test");

        // when
        Player createdPlayer = playerService.createPlayer(player);
        Player foundPlayer = playerService.getPlayer(createdPlayer.getId());

        // then
        assertNotNull(foundPlayer);
        assertEquals(createdPlayer.getNickname(), foundPlayer.getNickname());
    }

    @Test
    public void getPlayer_invalidId_throwsException() {
        // when
        assertThrows(ResponseStatusException.class, () -> {
            playerService.getPlayer(100L);
        });
    }

    @Test
    public void getMinigamePlayer_success() {
        // given
        Player player1 = new Player();
        player1.setId(1L);
        player1.setNickname("test1");
        Player player2 = new Player();
        player2.setId(2L);
        player2.setNickname("test2");

        List<String> nicknames = List.of(player1.getNickname(), player2.getNickname());
        List<Long> ids = List.of(player1.getId(), player2.getId());

        Team team = new Team();
        team.setPlayers(List.of(player1, player2));

        // when
        Player chosenPlayer = playerService.getMinigamePlayer(team);

        // then
        assertNotNull(chosenPlayer);
        assertEquals(true, ids.contains(chosenPlayer.getId()));
        assertEquals(true, nicknames.contains(chosenPlayer.getNickname()));
    }

    @Test
    public void updatePlayer_success() {
        // given
        Player player = new Player();
        player.setNickname("test");

        // when
        Player createdPlayer = playerService.createPlayer(player);
        playerService.updatePlayer(createdPlayer.getId());
        Player foundPlayer = playerService.getPlayer(createdPlayer.getId());

        // then
        assertNotNull(foundPlayer);
        assertEquals(createdPlayer.getNickname(), foundPlayer.getNickname());
        assertEquals(createdPlayer.getRoundsPlayed() + 1, foundPlayer.getRoundsPlayed());

    }

}
