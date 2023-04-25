package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.repository.TeamRepository;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Team;
import ch.uzh.ifi.hase.soprafs23.constant.TeamType;
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
public class TeamServiceTest {

    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamRepository teamRepository;

    @BeforeEach
    public void setup() {
        teamRepository.deleteAll();
    }

    @Test
    public void createTeam_success() {
        // given
        Lobby lobby = new Lobby();
        lobby.setWinningScore(100);

        // when
        Team createdTeam = teamService.createTeam(TeamType.BLUE, lobby, "test");

        // then
        assertNotNull(createdTeam);
        assertEquals(TeamType.BLUE, createdTeam.getColor());
        assertEquals("test", createdTeam.getName());
        assertEquals(lobby, createdTeam.getLobby());
    }

    @Test
    public void addPlayer_success() {
        // given
        Lobby lobby = new Lobby();
        lobby.setWinningScore(100);
        Team team = teamService.createTeam(TeamType.BLUE, lobby, "test");
        Player player = new Player();
        player.setNickname("test");

        // when
        teamService.addPlayer(lobby, TeamType.BLUE, player);

        Team foundTeam = teamService.getTeam(team.getId());

        // then
        List<Player> players = foundTeam.getPlayers();
        assertNotNull(players);
    }

    @Test
    public void addPlayer_invalid_throwsException() {
        // given
        Lobby lobby = new Lobby();
        lobby.setWinningScore(100);
        Player player = new Player();
        player.setNickname("test");

        // when
        assertThrows(ResponseStatusException.class, () -> {
            teamService.addPlayer(lobby, null, player);
        });
    }

    @Test
    public void removePlayer_success() {
        // given
        Lobby lobby = new Lobby();
        lobby.setWinningScore(100);

        Player player = new Player();
        player.setNickname("test");

        teamService.createTeam(TeamType.BLUE, lobby, "test");

        // when
        teamService.addPlayer(lobby, TeamType.BLUE, player);
        teamService.removePlayer(lobby, TeamType.BLUE, player);
    }

    @Test
    public void removePlayer_invalid_throwsException() {
        // given
        Lobby lobby = new Lobby();
        lobby.setWinningScore(100);

        // when
        assertThrows(ResponseStatusException.class, () -> {
            teamService.removePlayer(lobby, TeamType.BLUE, null);
        });
    }

    @Test
    public void getTeam_success() {
        // given
        Lobby lobby = new Lobby();
        lobby.setWinningScore(100);
        Team team = teamService.createTeam(TeamType.BLUE, lobby, "test");

        // when
        Team foundTeam = teamService.getTeam(team.getId());

        // then
        assertNotNull(foundTeam);
        assertEquals(TeamType.BLUE, foundTeam.getColor());
        assertEquals("test", foundTeam.getName());
        assertEquals(lobby.getId(), foundTeam.getLobby().getId());
    }

    @Test
    public void getTeam_invalid_throwsException() {
        // when
        assertThrows(ResponseStatusException.class, () -> {
            teamService.getTeam(0L);
        });
    }

    @Test
    public void updateScore_success() {
        // given
        Lobby lobby = new Lobby();
        lobby.setWinningScore(100);
        Team team = teamService.createTeam(TeamType.BLUE, lobby, "test");

        // when
        teamService.updateScore(lobby, TeamType.BLUE, 10);

        Team foundTeam = teamService.getTeam(team.getId());

        // then
        assertEquals(10, foundTeam.getScore());
    }

    @Test
    public void getByColorAndLobby_success() {
        // given
        Lobby lobby = new Lobby();
        lobby.setWinningScore(100);

        teamService.createTeam(TeamType.BLUE, lobby, "test");
        // when
        Team foundTeam = teamService.getByColorAndLobby(lobby, TeamType.BLUE);

        // then
        assertNotNull(foundTeam);
        assertEquals(TeamType.BLUE, foundTeam.getColor());
        assertEquals("test", foundTeam.getName());
        assertEquals(lobby.getId(), foundTeam.getLobby().getId());
    }

}
