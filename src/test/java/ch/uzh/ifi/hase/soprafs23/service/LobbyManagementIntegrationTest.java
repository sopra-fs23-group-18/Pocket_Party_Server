package ch.uzh.ifi.hase.soprafs23.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;

import javax.management.MBeanInfo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.uzh.ifi.hase.soprafs23.constant.MinigamePlayers;
import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;
import ch.uzh.ifi.hase.soprafs23.constant.TeamType;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Team;
import ch.uzh.ifi.hase.soprafs23.entity.minigame.Minigame;
import ch.uzh.ifi.hase.soprafs23.entity.minigame.QuickFingers;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.repository.MinigameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.PlayerRepository;
import ch.uzh.ifi.hase.soprafs23.repository.TeamRepository;

@WebAppConfiguration
@SpringBootTest
public class LobbyManagementIntegrationTest {

    @Qualifier("lobbyRepository")
    @Autowired
    private LobbyRepository lobbyRepository;

    @Autowired
    private LobbyManagement lobbyManager;

    @Autowired
    private GameService gameService;

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

    @Test
    public void addUpcommingMinigame_secondGame() {
        // given
        Player player1 = new Player();
        player1.setNickname("test1");
        Player player2 = new Player();
        player2.setNickname("test2");
        Player player3 = new Player();
        player3.setNickname("test3");
        Player player4 = new Player();
        player4.setNickname("test4");

        Game game = new Game();
        List<MinigameType> minigameTypes = Arrays.asList(MinigameType.values());
        game.setMinigamesChoice(minigameTypes);

        // when
        Lobby createdLobby = lobbyManager.createLobby();
        teamService.addPlayer(createdLobby, createdLobby.getTeams().get(1).getType(), player1);
        teamService.addPlayer(createdLobby, createdLobby.getTeams().get(1).getType(), player2);
        teamService.addPlayer(createdLobby, createdLobby.getTeams().get(0).getType(), player3);
        teamService.addPlayer(createdLobby, createdLobby.getTeams().get(0).getType(), player4);

        lobbyManager.ableToStart(createdLobby.getId());
        lobbyManager.addGame(game, createdLobby.getId());
        gameService.addUpcomingMinigame(game.getId());

        Team winnerTeamInput = new Team();
        winnerTeamInput.setName(createdLobby.getTeams().get(1).getName());
        winnerTeamInput.setScore(200);

        gameService.finishedMinigameUpdate(game.getId(), winnerTeamInput);
        Minigame game1 = gameService.getMinigame(game.getId());

        gameService.addUpcomingMinigame(game.getId());
        Minigame game2 = gameService.getMinigame(game.getId());

        // Player game1Team1Player =
        // playerService.getPlayer(game1.getTeam1Players().get(0).getId());
        // Player game1Team2Player =
        // playerService.getPlayer(game1.getTeam2Players().get(0).getId());
        // Player game2Team1Player =
        // playerService.getPlayer(game2.getTeam1Players().get(0).getId());
        // Player game2Team2Player =
        // playerService.getPlayer(game2.getTeam2Players().get(0).getId());

        assertNotNull(gameService.getMinigame(game.getId()));
        assertNotEquals(game1.getType(), game2.getType());
        assertNotEquals(game1.getTeam1Players(), game2.getTeam1Players());
        assertNotEquals(game1.getTeam2Players(), game2.getTeam2Players());
        assertNotEquals(game1.getDescription(), game2.getDescription());
        assertNotEquals(game1.getMinigameOutcome(), game2.getMinigameOutcome());
        // assertEquals(1, game1Team1Player.getRoundsPlayed());
        // assertEquals(1, game1Team2Player.getRoundsPlayed());
        // assertEquals(0, game2Team1Player.getRoundsPlayed());
        // assertEquals(0, game2Team2Player.getRoundsPlayed());
    }

    @Test
    public void testDragAndDropPlayer(){
        Lobby lobby = lobbyManager.createLobby();
        // given
        Player player1 = new Player();
        player1.setNickname("test1");
        Player player2 = new Player();
        player2.setNickname("test2");
        Player player3 = new Player();
        player3.setNickname("test3");
        Player player4 = new Player();
        player4.setNickname("test4");

        lobbyManager.createPlayer(lobby.getInviteCode(), player1);
        lobbyManager.createPlayer(lobby.getInviteCode(), player2);
        lobbyManager.createPlayer(lobby.getInviteCode(), player3);
        lobbyManager.createPlayer(lobby.getInviteCode(), player4);

        lobbyManager.assignPlayer(lobby.getId(), player1.getId(), TeamType.TEAM_ONE);
        lobbyManager.assignPlayer(lobby.getId(), player2.getId(), TeamType.TEAM_ONE);
        lobbyManager.assignPlayer(lobby.getId(), player3.getId(), TeamType.TEAM_TWO);
        lobbyManager.assignPlayer(lobby.getId(), player4.getId(), TeamType.TEAM_TWO);

        Lobby foundLobby = lobbyManager.getLobby(lobby.getId());
        List<Team> teams = teamService.getTeams(foundLobby);
        
        lobbyManager.ableToStart(lobby.getId());

        lobbyManager.reassignPlayer(lobby.getId(), player1.getId(), TeamType.TEAM_ONE, TeamType.TEAM_TWO);
        lobbyManager.unassignPlayer(lobby.getId(), player4.getId(), TeamType.TEAM_TWO );
        lobbyManager.assignPlayer(lobby.getId(), player4.getId(), TeamType.TEAM_ONE);

        lobbyManager.ableToStart(lobby.getId());
    }
}
