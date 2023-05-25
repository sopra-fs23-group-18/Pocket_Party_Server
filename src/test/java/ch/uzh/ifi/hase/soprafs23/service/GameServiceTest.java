package ch.uzh.ifi.hase.soprafs23.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs23.constant.MinigamePlayers;
import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;
import ch.uzh.ifi.hase.soprafs23.constant.OutcomeType;
import ch.uzh.ifi.hase.soprafs23.constant.PlayerChoice;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Team;
//import ch.uzh.ifi.hase.soprafs23.entity.minigame.HotPotato;
import ch.uzh.ifi.hase.soprafs23.entity.minigame.Minigame;
import ch.uzh.ifi.hase.soprafs23.entity.minigame.RPS;
import ch.uzh.ifi.hase.soprafs23.entity.minigame.GreedyGambit;
import ch.uzh.ifi.hase.soprafs23.entity.minigame.QuickFingers;
import ch.uzh.ifi.hase.soprafs23.entity.minigame.TimingTumble;
import ch.uzh.ifi.hase.soprafs23.entity.minigame.VibrationVoyage;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.repository.MinigameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.PlayerRepository;
import ch.uzh.ifi.hase.soprafs23.repository.TeamRepository;

@WebAppConfiguration
@SpringBootTest
public class GameServiceTest {

    @Qualifier("lobbyRepository")
    @Autowired
    private LobbyRepository lobbyRepository;

    @Autowired
    private LobbyManagement lobbyManager;

    @Qualifier("teamRepository")
    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamService teamService;

    @Qualifier("playerRepository")
    @Autowired
    private PlayerRepository playerRepository;

    @Qualifier("minigameRepository")
    @Autowired
    private MinigameRepository minigameRepository;

    @Qualifier("gameRepository")
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameService gameService;

    @BeforeEach
    public void setup() {
        lobbyRepository.deleteAll();
        gameRepository.deleteAll();
        teamRepository.deleteAll();
        playerRepository.deleteAll();
        minigameRepository.deleteAll();

    }

    @Test
    public void testGetGame() {
        Game game = new Game();
        game.setPlayerChoice(PlayerChoice.RANDOM);
        gameRepository.save(game);

        Game foundGame = gameService.getGame(game.getId());

        assertEquals(game.getId(), foundGame.getId());
    }

    @Test
    public void testGetGame_invalidId() {
        assertThrows(ResponseStatusException.class, () -> gameService.getGame(1L));

    }

    @Test
    public void testGetMinigame() {
        Game game = new Game();
        game.setPlayerChoice(PlayerChoice.RANDOM);

        Minigame minigame = new TimingTumble();
        minigame.setAmountOfPlayers(MinigamePlayers.TWO);

        game.setUpcomingMinigame(minigame);
        gameRepository.save(game);
        Minigame foundMinigame = gameService.getMinigame(game.getId());

        assertEquals(minigame.getId(), foundMinigame.getId());
    }

    @Test
    public void testGetMinigameFail() {
        Game game = new Game();
        game.setPlayerChoice(PlayerChoice.RANDOM);
        gameRepository.save(game);
        assertThrows(ResponseStatusException.class, () -> gameService.getMinigame(game.getId()));

    }

    @Test
    public void testAddUpcomingMinigame() {
        Game game = new Game();
        List<MinigameType> minigameTypes = Arrays.asList(MinigameType.values());

        game.setPlayerChoice(PlayerChoice.RANDOM);
        game.setMinigamesChoice(minigameTypes);

        Lobby lobby = lobbyManager.createLobby();
        lobbyManager.addGame(game, lobby.getId());

        Minigame nextMinigame = gameService.addUpcomingMinigame(game.getId());

        assertNotNull(nextMinigame);
        assertTrue(minigameTypes.contains(nextMinigame.getType()));
    }

    @Test
    public void testGetWinner() {
        Game game = new Game();
        game.setPlayerChoice(PlayerChoice.RANDOM);
        game.setGameOutcome(OutcomeType.WINNER);

        Lobby lobby = lobbyManager.createLobby();
        lobbyManager.addGame(game, lobby.getId());

        lobby.getTeams().get(0).setScore(10);

        Team winner = gameService.getWinner(game.getId());

        assertEquals(lobby.getTeams().get(0).getId(), winner.getId());
    }

    @Test
    public void testGetWinnerFail() {
        Game game = new Game();
        game.setPlayerChoice(PlayerChoice.RANDOM);
        game.setGameOutcome(OutcomeType.NOT_FINISHED);

        Lobby lobby = lobbyManager.createLobby();
        lobbyManager.addGame(game, lobby.getId());

        assertThrows(ResponseStatusException.class, () -> gameService.getWinner(game.getId()));
    }

    @Test
    public void testIsFinished() {
        Game game = new Game();
        game.setPlayerChoice(PlayerChoice.RANDOM);
        game.setWinningScore(10);

        Lobby lobby = lobbyManager.createLobby();
        lobbyManager.addGame(game, lobby.getId());

        teamService.updateScore(lobby, lobby.getTeams().get(0).getName(), 10);

        gameService.isFinished(game);

        assertEquals(OutcomeType.WINNER, game.getGameOutcome());
    }

    @Test
    public void testNotFinished() {
        Game game = new Game();
        game.setPlayerChoice(PlayerChoice.RANDOM);
        game.setWinningScore(10);

        Lobby lobby = lobbyManager.createLobby();
        lobbyManager.addGame(game, lobby.getId());

        teamService.updateScore(lobby, lobby.getTeams().get(0).getName(), 9);

        gameService.isFinished(game);

        assertEquals(OutcomeType.NOT_FINISHED, game.getGameOutcome());
    }

    @Test
    public void testCreateGame() {
        Game game = new Game();
        game.setPlayerChoice(PlayerChoice.RANDOM);
        game.setWinningScore(10);

        Lobby lobby = lobbyManager.createLobby();

        Game createdGame = gameService.createGame(game, lobby.getId());

        assertNotNull(createdGame);
        assertEquals(game.getId(), createdGame.getId());
    }

    @Test
    public void testCreateGameFail1() {
        Game game = new Game();
        game.setWinningScore(10);

        Lobby lobby = lobbyManager.createLobby();

        assertThrows(ResponseStatusException.class, () -> gameService.createGame(game, lobby.getId()));
    }

    @Test
    public void testCreateGameFail2() {
        Game game = new Game();
        game.setPlayerChoice(PlayerChoice.RANDOM);
        game.setWinningScore(-1);

        Lobby lobby = lobbyManager.createLobby();

        assertThrows(ResponseStatusException.class, () -> gameService.createGame(game, lobby.getId()));
    }

    @Test
    public void testUpdateMinigame() {
        Game game = new Game();
        game.setPlayerChoice(PlayerChoice.RANDOM);
        game.setWinningScore(10);

        Minigame minigame = new QuickFingers();
        minigame.setAmountOfPlayers(MinigamePlayers.ONE);

        game.setUpcomingMinigame(minigame);

        Lobby lobby = lobbyManager.createLobby();
        lobbyManager.addGame(game, lobby.getId());

        Team team = lobby.getTeams().get(0);

        gameService.updateMinigame(game, team);

        assertEquals(minigame.getAmountOfPlayers(), game.getMinigamesPlayed().get(0).getAmountOfPlayers());
        assertEquals(minigame.getType(), game.getMinigamesPlayed().get(0).getType());
    }

    @Test
    public void testUpdateMinigameFail1() {
        Game game = new Game();
        game.setPlayerChoice(PlayerChoice.RANDOM);
        game.setWinningScore(10);
        Minigame minigame = new VibrationVoyage();
        minigame.setAmountOfPlayers(MinigamePlayers.ONE);
        minigame.setMinigameOutcome(OutcomeType.WINNER);

        game.setUpcomingMinigame(minigame);

        Lobby lobby = lobbyManager.createLobby();
        lobbyManager.addGame(game, lobby.getId());

        Team team = lobby.getTeams().get(0);

        assertThrows(ResponseStatusException.class, () -> gameService.updateMinigame(game, team));
    }

    @Test
    public void testUpdateMinigameFail2() {
        Game game = new Game();
        game.setPlayerChoice(PlayerChoice.RANDOM);
        game.setWinningScore(10);
        Minigame minigame = new RPS();
        minigame.setAmountOfPlayers(MinigamePlayers.ONE);
        minigame.setMinigameOutcome(OutcomeType.NOT_FINISHED);

        game.setUpcomingMinigame(minigame);

        Lobby lobby = lobbyManager.createLobby();
        lobbyManager.addGame(game, lobby.getId());

        Team team = lobby.getTeams().get(0);
        team.setScore(-1);

        assertThrows(ResponseStatusException.class, () -> gameService.updateMinigame(game, team));
    }

    @Test
    public void testFinishedMinigameUpdate() {
        Game game = new Game();
        game.setPlayerChoice(PlayerChoice.RANDOM);
        game.setWinningScore(10);
        Minigame minigame = new GreedyGambit();
        minigame.setAmountOfPlayers(MinigamePlayers.TWO);

        game.setUpcomingMinigame(minigame);

        Lobby lobby = lobbyManager.createLobby();
        lobbyManager.addGame(game, lobby.getId());

        Team team = lobby.getTeams().get(0);
        team.setScore(1);

        gameService.finishedMinigameUpdate(game.getId(), team);

        assertEquals(1, team.getScore());
    }

    @Test
    public void testUpdateUpcomingMinigame() {
        Game game = new Game();
        List<MinigameType> minigameTypes = Arrays.asList(MinigameType.values());
        game.setPlayerChoice(PlayerChoice.RANDOM);
        game.setWinningScore(10);
        game.setMinigamesChoice(minigameTypes);
        Minigame minigame = new TimingTumble();
        minigame.setAmountOfPlayers(MinigamePlayers.ONE);

        game.setUpcomingMinigame(minigame);

        Lobby lobby = lobbyManager.createLobby();
        Player player1 = new Player();
        player1.setNickname("test1");
        Player player2 = new Player();
        player2.setNickname("test2");
        teamService.addPlayer(lobby, lobby.getTeams().get(0).getType(), player1);
        teamService.addPlayer(lobby, lobby.getTeams().get(1).getType(), player2);
        lobbyManager.addGame(game, lobby.getId());

        gameService.updateUpcomingMinigame(game.getId());

        assertNotNull(minigame.getTeam1Players());
        assertNotNull(minigame.getTeam2Players());
    }

}
