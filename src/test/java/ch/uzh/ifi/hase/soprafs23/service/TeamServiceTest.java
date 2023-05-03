package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.TeamType;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
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

@WebAppConfiguration
@SpringBootTest
public class TeamServiceTest {

    @Autowired
    private TeamService teamService;

    @Autowired
    private LobbyManagement lobbyManager;

    @Test
    public void addPlayer_success() {
        // given
        Lobby lobby = new Lobby();
        lobby.setWinningScore(100);
        Lobby createdLobby = lobbyManager.createLobby(lobby);

        Player player = new Player();
        player.setNickname("test");

        // when
        teamService.addPlayer(createdLobby, TeamType.BLUE, player);

        Team team = teamService.getByColorAndLobby(createdLobby, TeamType.BLUE);
        List<Player> players = team.getPlayers();

        // then
        assertNotNull(players);
    }

    @Test
    public void addPlayer_fail() {
        // given
        Lobby lobby = new Lobby();
        lobby.setWinningScore(100);
        Lobby createdLobby = lobbyManager.createLobby(lobby);

        Player player = new Player();
        player.setNickname("test");

        // when
        // then
        assertThrows(ResponseStatusException.class, () -> {
            teamService.addPlayer(createdLobby, TeamType.BLUE, null);
        });
    }

    @Test
    public void removePlayer_success() {
        // given
        Lobby lobby = new Lobby();
        lobby.setWinningScore(100);
        Lobby createdLobby = lobbyManager.createLobby(lobby);

        Player player = new Player();
        player.setNickname("test");

        // when
        teamService.addPlayer(createdLobby, TeamType.BLUE, player);
        teamService.removePlayer(createdLobby, TeamType.BLUE, player);

        Team team = teamService.getByColorAndLobby(createdLobby, TeamType.BLUE);
        List<Player> players = team.getPlayers();

        // then
        assertNotNull(players);
    }

    @Test
    public void removePlayer_fail() {
        // given
        Lobby lobby = new Lobby();
        lobby.setWinningScore(100);
        Lobby createdLobby = lobbyManager.createLobby(lobby);

        Player player = new Player();
        player.setNickname("test");

        // when
        // then
        assertThrows(ResponseStatusException.class, () -> {
            teamService.removePlayer(createdLobby, TeamType.BLUE, null);
        });
    }

    @Test
    public void getTeam_success() {
        // given
        Lobby lobby = new Lobby();
        lobby.setWinningScore(100);
        Lobby createdLobby = lobbyManager.createLobby(lobby);

        Team team = createdLobby.getTeams().get(0);

        // when
        Team foundTeam = teamService.getTeam(team.getId());

        // then
        assertEquals(team.getId(), foundTeam.getId());
        assertEquals(team.getScore(), foundTeam.getScore());
    }

    @Test
    public void getTeam_fail() {
        // given
        Lobby lobby = new Lobby();
        lobby.setWinningScore(100);
        // when
        // then
        assertThrows(ResponseStatusException.class, () -> {
            teamService.getTeam(0L);
        });
    }

    @Test
    public void updateScore_success() {
        // given
        Lobby lobby = new Lobby();
        lobby.setWinningScore(100);
        Lobby createdLobby = lobbyManager.createLobby(lobby);

        Team team = createdLobby.getTeams().get(1);

        // when
        teamService.updateScore(lobby, TeamType.BLUE, 10);
        Team foundTeam = teamService.getByColorAndLobby(lobby, TeamType.BLUE);

        // then
        assertNotNull(foundTeam);
        assertEquals(team.getId(), foundTeam.getId());
        assertEquals(team.getScore() + 10, foundTeam.getScore());
    }

    @Test
    public void getByColorAndLobby_success() {
        // given
        Lobby lobby = new Lobby();
        lobby.setWinningScore(100);
        Lobby createdLobby = lobbyManager.createLobby(lobby);

        // when
        Team foundTeam = teamService.getByColorAndLobby(createdLobby, TeamType.BLUE);

        // then
        assertNotNull(foundTeam);
        assertEquals(TeamType.BLUE, foundTeam.getColor());
    }

    @Test
    public void getByColorAndLobby_fail() {
        // given
        Lobby lobby = new Lobby();
        lobby.setWinningScore(100);
        // when
        // then
        assertThrows(ResponseStatusException.class, () -> {
            teamService.getByColorAndLobby(null, TeamType.BLUE);

        });
    }

}
