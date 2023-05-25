package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;
import ch.uzh.ifi.hase.soprafs23.constant.PlayerChoice;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Team;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.repository.PlayerRepository;
import ch.uzh.ifi.hase.soprafs23.repository.TeamRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@WebAppConfiguration
@SpringBootTest
public class LobbyManagementTest {

    @Autowired
    private MinigameService minigameService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private TeamService teamService;

    @Qualifier("playerRepository")
    @Autowired
    private PlayerRepository playerRepository;

    @Qualifier("lobbyRepository")
    @Autowired
    private LobbyRepository lobbyRepository;

    @Autowired
    private LobbyManagement lobbyManager;

    @Qualifier("teamRepository")
    @Autowired
    private TeamRepository teamRepository;

    @BeforeEach
    public void setup() {
        lobbyRepository.deleteAll();
        teamRepository.deleteAll();
        playerRepository.deleteAll();
    }

    @Test
    public void testCreateLobby() {
        Lobby createdLobby = lobbyManager.createLobby();

        // then

        assertNotNull(createdLobby.getInviteCode());
        assertEquals(true, createdLobby.getUnassignedPlayers().isEmpty());
        assertEquals(2, createdLobby.getTeams().size());

    }

    @Test
    public void testGetLobbyById() {
        Lobby createdLobby = lobbyManager.createLobby();
        Lobby fetchedLobby = lobbyManager.getLobby(createdLobby.getId());

        assertEquals(createdLobby.getId(), fetchedLobby.getId());
        assertEquals(createdLobby.getInviteCode(), fetchedLobby.getInviteCode());
    }

    @Test
    public void testGetLobbyByIdFail() {
        Lobby createdLobby = lobbyManager.createLobby();
        assertThrows(ResponseStatusException.class, () -> lobbyManager.getLobby(createdLobby.getId() + 1));
    }

    @Test
    public void testGetLobbyByInviteCode() {
        Lobby createdLobby = lobbyManager.createLobby();
        Lobby fetchedLobby = lobbyManager.getLobby(createdLobby.getInviteCode());

        assertEquals(createdLobby.getId(), fetchedLobby.getId());
        assertEquals(createdLobby.getInviteCode(), fetchedLobby.getInviteCode());
    }

    @Test
    public void testGetLobbyByInviteCodeFail() {
        assertThrows(ResponseStatusException.class, () -> lobbyManager.getLobby(10101));
    }

    @Test
    public void testGetLobbyByGame() {
        Lobby createdLobby = lobbyManager.createLobby();
        Lobby fetchedLobby = lobbyManager.getLobby(createdLobby.getGame());

        assertEquals(createdLobby.getId(), fetchedLobby.getId());
        assertEquals(createdLobby.getInviteCode(), fetchedLobby.getInviteCode());
    }

    @Test
    public void testGetLeadingTeam() {
        Lobby createdLobby = lobbyManager.createLobby();
        Team team = createdLobby.getTeams().get(0);
        team.setScore(10);
        teamRepository.save(team);
        Team leadingTeam = lobbyManager.getLeadingTeam(createdLobby.getGame());

        assertEquals(leadingTeam.getId(), createdLobby.getTeams().get(0).getId());
    }

    @Test
    public void testAddToUnassignedPlayers() {
        Lobby createdLobby = lobbyManager.createLobby();
        Player player = new Player();
        player.setNickname("test");
        Player newPlayer = playerService.createPlayer(player);
        lobbyManager.addToUnassignedPlayers(createdLobby.getId(), newPlayer);
        assertNotNull(createdLobby.getUnassignedPlayers());
    }

    @Test
    public void testAddToUnassignedPlayersFail() {
        Lobby createdLobby = lobbyManager.createLobby();
        assertThrows(ResponseStatusException.class,
                () -> lobbyManager.addToUnassignedPlayers(createdLobby.getId(), null));
    }

    @Test
    public void testRemoveFromUnassignedPlayers() {
        Lobby createdLobby = lobbyManager.createLobby();
        Player player = new Player();
        player.setNickname("test");
        Player newPlayer = playerService.createPlayer(player);
        lobbyManager.addToUnassignedPlayers(createdLobby.getId(), newPlayer);
        lobbyManager.removeFromUnassignedPlayers(createdLobby.getId(), newPlayer);
        assertEquals(0, createdLobby.getUnassignedPlayers().size());
    }

    @Test
    public void testRemoveFromUnassignedPlayersFail() {
        Lobby createdLobby = lobbyManager.createLobby();
        assertThrows(ResponseStatusException.class,
                () -> lobbyManager.removeFromUnassignedPlayers(createdLobby.getId(), null));
    }

    @Test
    public void testAbleToJoin() {
        Lobby createdLobby = lobbyManager.createLobby();
        Player player = new Player();
        player.setNickname("test");
        Player newPlayer = playerService.createPlayer(player);
        Player player2 = new Player();
        player2.setNickname("test2");
        lobbyManager.addToUnassignedPlayers(createdLobby.getId(), newPlayer);
        lobbyManager.ableToJoin(createdLobby.getInviteCode(), player2);
    }

    @Test
    public void testAbleToJoinFail() {
        Lobby createdLobby = lobbyManager.createLobby();
        Player player = new Player();
        player.setNickname("test");
        Player newPlayer = playerService.createPlayer(player);
        lobbyManager.addToUnassignedPlayers(createdLobby.getId(), newPlayer);
        assertThrows(ResponseStatusException.class,
                () -> lobbyManager.ableToJoin(createdLobby.getInviteCode(), newPlayer));
    }

    @Test
    public void testAbleToStart() {
        Lobby createdLobby = lobbyManager.createLobby();
        Player player1 = new Player();
        player1.setNickname("test");
        Player player2 = new Player();
        player2.setNickname("test2");
        createdLobby.getTeams().get(0).setPlayers(List.of(player1));
        createdLobby.getTeams().get(1).setPlayers(List.of(player2));
        lobbyRepository.save(createdLobby);
        lobbyManager.ableToStart(createdLobby.getId());
    }

    @Test
    public void testAbleToStartFail1() {
        Lobby createdLobby = lobbyManager.createLobby();
        assertThrows(ResponseStatusException.class, () -> lobbyManager.ableToStart(createdLobby.getId()));
    }

    @Test
    public void testAbleToStartFail2() {
        Lobby createdLobby = lobbyManager.createLobby();
        Player player1 = new Player();
        player1.setNickname("test");
        Player player2 = new Player();
        player2.setNickname("test2");
        createdLobby.getTeams().get(0).setPlayers(List.of(player1));
        lobbyRepository.save(createdLobby);
        assertThrows(ResponseStatusException.class, () -> lobbyManager.ableToStart(createdLobby.getId()));
    }

    @Test
    public void testAbleToStartFail3() {
        Lobby createdLobby = lobbyManager.createLobby();
        Player player1 = new Player();
        player1.setNickname("test");
        Player player2 = new Player();
        player2.setNickname("test2");
        lobbyManager.addToUnassignedPlayers(createdLobby.getId(), player2);
        lobbyRepository.save(createdLobby);
        assertThrows(ResponseStatusException.class, () -> lobbyManager.ableToStart(createdLobby.getId()));
    }

    @Test
    public void testAddGame() {
        Lobby createdLobby = lobbyManager.createLobby();
        Game game = new Game();
        List<MinigameType> minigames = minigameService.chooseAllMinigames();
        game.setMinigamesChoice(minigames);

        lobbyManager.addGame(game, createdLobby.getId());

        Lobby lobby = lobbyManager.getLobby(createdLobby.getId());

        assertEquals(game.getId(), lobby.getGame().getId());
    }

    @Test
    public void testLowestPlayerAmount() {
        Lobby createdLobby = lobbyManager.createLobby();
        Game game = new Game();
        lobbyManager.addGame(game, createdLobby.getId());
        assertEquals(0, teamService.lowestPlayerAmount(createdLobby));
    }

}
