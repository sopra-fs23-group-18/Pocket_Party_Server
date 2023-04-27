package ch.uzh.ifi.hase.soprafs23.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.uzh.ifi.hase.soprafs23.constant.TeamType;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Minigame;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Team;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.repository.MinigameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.PlayerRepository;
import ch.uzh.ifi.hase.soprafs23.repository.TeamRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.WinnerTeamPutDTO;

@WebAppConfiguration
@SpringBootTest
public class LobbyManagementIntegrationTest {
    
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

    @Autowired
    private PlayerService playerService;

    @Qualifier("minigameRepository")
    @Autowired
    private MinigameRepository minigameRepository;

    @Test
    public void addUpcommingMinigame_secondGame(){
        // given
        Lobby testLobby = new Lobby();
        testLobby.setWinningScore(500);

        Player player1 = new Player();
        player1.setNickname("test1");
        Player player2 = new Player();
        player2.setNickname("test2");
        Player player3 = new Player();
        player3.setNickname("test3");
        Player player4 = new Player();
        player4.setNickname("test4");

        // when
        Lobby createdLobby = lobbyManager.createLobby(testLobby);
        teamService.addPlayer(createdLobby, TeamType.BLUE, player1);
        teamService.addPlayer(createdLobby, TeamType.BLUE, player2);
        teamService.addPlayer(createdLobby, TeamType.RED, player3);
        teamService.addPlayer(createdLobby, TeamType.RED, player4);

        lobbyManager.ableToStart(createdLobby.getId());
        lobbyManager.addUpcommingMinigame(createdLobby.getId());

        Team winnerTeamInput = new Team();
        winnerTeamInput.setColor(TeamType.RED);
        winnerTeamInput.setName("Team Red");
        winnerTeamInput.setScore(300);

        lobbyManager.finishedMinigameUpdate(createdLobby.getId(), winnerTeamInput);
        Minigame game1 = lobbyManager.getMinigame(createdLobby.getId());

        lobbyManager.addUpcommingMinigame(createdLobby.getId());
        Minigame game2 = lobbyManager.getMinigame(createdLobby.getId());

        Player game1Team1Player = playerService.getPlayer(game1.getTeam1Player().getId());
        Player game1Team2Player = playerService.getPlayer(game1.getTeam2Player().getId());
        Player game2Team1Player = playerService.getPlayer(game2.getTeam1Player().getId());
        Player game2Team2Player = playerService.getPlayer(game2.getTeam2Player().getId());

        assertNotNull(lobbyManager.getMinigame(createdLobby.getId()));
        assertNotEquals(game1.getType(), game2.getType());
        assertNotEquals(game1.getTeam1Player(), game2.getTeam1Player());
        assertNotEquals(game1.getTeam2Player(), game2.getTeam2Player());
        assertNotEquals(game1.getDescription(), game2.getDescription());
        assertNotEquals(game1.getIsFinished(), game2.getIsFinished());
        assertEquals(1, game1Team1Player.getRoundsPlayed());
        assertEquals(1, game1Team2Player.getRoundsPlayed());
        assertEquals(0, game2Team1Player.getRoundsPlayed());
        assertEquals(0, game2Team2Player.getRoundsPlayed());        
    }
}
