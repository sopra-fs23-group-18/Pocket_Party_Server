package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.TeamType;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Minigame;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Team;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.repository.TeamRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

@WebAppConfiguration
@SpringBootTest
public class LobbyManagementTest {

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

    @Test
    public void createLobby_validWinningScore_success() {
        // given
        Lobby testLobby = new Lobby();
        testLobby.setId(1L);
        testLobby.setWinningScore(100);

        // when
        Lobby createdLobby = lobbyManager.createLobby(testLobby);

        // then
        assertEquals(testLobby.getWinningScore(), createdLobby.getWinningScore());
        assertNotNull(createdLobby.getInviteCode());
        assertEquals(false, createdLobby.getIsFinished());
        assertNotNull(createdLobby.getMinigamesChoice());
        assertEquals(true, createdLobby.getUnassignedPlayers().isEmpty());
        assertEquals(2, createdLobby.getTeams().size());

    }

    @Test
    public void createLobby_invalidWinningScore_throwsException() {
        // given
        Lobby testLobby = new Lobby();
        testLobby.setId(1L);
        testLobby.setWinningScore(-1);

        // when
        assertThrows(ResponseStatusException.class, () -> {
            lobbyManager.createLobby(testLobby);
        });
    }

    @Test
    public void createLobby_invalidWinningScore2_throwsException() {
        // given
        Lobby testLobby = new Lobby();
        testLobby.setId(1L);
        testLobby.setWinningScore(100001);

        // when
        assertThrows(ResponseStatusException.class, () -> {
            lobbyManager.createLobby(testLobby);
        });
    }

    @Test
    public void getLobby_validId_success() {
        // given
        Lobby testLobby = new Lobby();
        testLobby.setWinningScore(100);

        // when
        Lobby createdLobby = lobbyManager.createLobby(testLobby);
        Lobby foundLobby = lobbyManager.getLobby(createdLobby.getId());

        // then
        assertEquals(createdLobby.getId(), foundLobby.getId());
        assertEquals(createdLobby.getWinningScore(), foundLobby.getWinningScore());
        assertEquals(createdLobby.getInviteCode(), foundLobby.getInviteCode());
        assertEquals(createdLobby.getIsFinished(), foundLobby.getIsFinished());
        assertEquals(createdLobby.getMinigamesChoice().get(0), foundLobby.getMinigamesChoice().get(0));
        assertNotNull(foundLobby.getUnassignedPlayers());
        assertNotNull(foundLobby.getTeams());
    }

    @Test
    public void getLobby_invalidId_throwsException() {
        // when
        assertThrows(ResponseStatusException.class, () -> {
            lobbyManager.getLobby(1000L);
        });
    }

    @Test
    public void getLobby_validInviteCode_success() {
        // given
        Lobby testLobby = new Lobby();
        testLobby.setWinningScore(100);

        // when
        Lobby createdLobby = lobbyManager.createLobby(testLobby);
        Lobby foundLobby = lobbyManager.getLobby(createdLobby.getInviteCode());

        // then
        assertEquals(createdLobby.getId(), foundLobby.getId());
        assertEquals(createdLobby.getWinningScore(), foundLobby.getWinningScore());
        assertEquals(createdLobby.getInviteCode(), foundLobby.getInviteCode());
        assertEquals(createdLobby.getIsFinished(), foundLobby.getIsFinished());
        assertEquals(createdLobby.getMinigamesChoice().get(0), foundLobby.getMinigamesChoice().get(0));
        assertNotNull(foundLobby.getUnassignedPlayers());
        assertNotNull(foundLobby.getTeams());
    }

    @Test
    public void getLobby_invalidInviteCode_throwsException() {
        // when
        assertThrows(ResponseStatusException.class, () -> {
            lobbyManager.getLobby(123);
        });
    }

    @Test
    public void getMinigame_validId_success() {
        // given
        Lobby testLobby = new Lobby();
        testLobby.setWinningScore(100);

        Player player1 = new Player();
        player1.setNickname("test1");

        Player player2 = new Player();
        player2.setNickname("test2");

        // when
        Lobby createdLobby = lobbyManager.createLobby(testLobby);
        teamService.addPlayer(createdLobby, TeamType.BLUE, player1);
        teamService.addPlayer(createdLobby, TeamType.RED, player2);
        lobbyManager.addUpcommingMinigame(createdLobby.getId());
        Minigame foundMinigame = lobbyManager.getMinigame(createdLobby.getId());

        // then
        assertEquals(lobbyManager.getMinigame(createdLobby.getId()).getType(), foundMinigame.getType());
    }

    @Test
    public void getMinigame_invalidId_throwsException() {
        // when
        assertThrows(ResponseStatusException.class, () -> {
            lobbyManager.getMinigame(1000L);
        });
    }

    @Test
    public void addUpcommingMinigame_validId_success() {
        // given
        Lobby testLobby = new Lobby();
        testLobby.setWinningScore(100);

        Player player1 = new Player();
        player1.setNickname("test1");

        Player player2 = new Player();
        player2.setNickname("test2");

        // when
        Lobby createdLobby = lobbyManager.createLobby(testLobby);
        teamService.addPlayer(createdLobby, TeamType.BLUE, player1);
        teamService.addPlayer(createdLobby, TeamType.RED, player2);
        lobbyManager.addUpcommingMinigame(createdLobby.getId());

        // then
        assertNotNull(lobbyManager.getMinigame(createdLobby.getId()));
    }

    @Test
    public void addUpcommingMinigame_invalidId_throwsException() {
        // when
        assertThrows(ResponseStatusException.class, () -> {
            lobbyManager.addUpcommingMinigame(100L);
        });
    }

    @Test
    public void finishedMinigameUpdate_validId_success() {
        // given
        Lobby testLobby = new Lobby();
        testLobby.setWinningScore(100);

        Player player1 = new Player();
        player1.setNickname("test1");

        Player player2 = new Player();
        player2.setNickname("test2");

        // when
        Lobby createdLobby = lobbyManager.createLobby(testLobby);
        teamService.addPlayer(createdLobby, TeamType.BLUE, player1);
        teamService.addPlayer(createdLobby, TeamType.RED, player2);
        lobbyManager.addUpcommingMinigame(createdLobby.getId());
        lobbyManager.finishedMinigameUpdate(createdLobby.getId(), createdLobby.getTeams().get(0));
        Lobby foundLobby = lobbyManager.getLobby(createdLobby.getId());

        // then
        assertNotNull(foundLobby.getMinigamesPlayed());

    }

    @Test
    public void finishedMinigameUpdate_invalidId_throwsException() {
        // given
        Team testTeam = new Team();

        // when
        assertThrows(ResponseStatusException.class, () -> {
            lobbyManager.finishedMinigameUpdate(100L, testTeam);
        });
    }

    @Test
    public void isFinished_EnoughScore() {
        // given
        Lobby testLobby = new Lobby();
        testLobby.setWinningScore(100);

        // when
        Lobby createdLobby = lobbyManager.createLobby(testLobby);
        teamService.updateScore(createdLobby, TeamType.BLUE, 101);

        lobbyManager.isFinished(createdLobby.getId());
        Lobby foundLobby = lobbyManager.getLobby(createdLobby.getId());

        // then
        assertEquals(true, foundLobby.getIsFinished());
    }

    @Test
    public void isFinished_NotEnoughScore() {
        // given
        Lobby testLobby = new Lobby();
        testLobby.setWinningScore(100);

        // when
        Lobby createdLobby = lobbyManager.createLobby(testLobby);
        teamService.updateScore(createdLobby, TeamType.BLUE, 99);

        lobbyManager.isFinished(createdLobby.getId());
        Lobby foundLobby = lobbyManager.getLobby(createdLobby.getId());

        // then
        assertEquals(false, foundLobby.getIsFinished());
    }

    @Test
    public void getWinner_isFinished_success() {
        // given
        Lobby testLobby = new Lobby();
        testLobby.setWinningScore(100);
        testLobby.setIsFinished(true);

        // when
        Lobby createdLobby = lobbyManager.createLobby(testLobby);
        teamService.updateScore(createdLobby, TeamType.BLUE, 101);

        // then
        assertEquals(TeamType.BLUE, lobbyManager.getWinner(createdLobby.getId()).getColor());

    }

    @Test
    public void getWinner_isNotFinished_throwException() {
        // given
        Lobby testLobby = new Lobby();
        testLobby.setWinningScore(100);
        testLobby.setIsFinished(false);

        // when
        Lobby createdLobby = lobbyManager.createLobby(testLobby);

        // then
        assertThrows(ResponseStatusException.class, () -> {
            lobbyManager.getWinner(createdLobby.getId());
        });
    }

    @Test
    public void addToUnassignedPlayers_validInput_success() {
        // given
        Lobby testLobby = new Lobby();
        testLobby.setWinningScore(100);

        Player testPlayer = new Player();
        testPlayer.setId(2L);
        testPlayer.setNickname("test");

        // when
        Lobby createdLobby = lobbyManager.createLobby(testLobby);
        lobbyManager.addToUnassignedPlayers(createdLobby.getId(), testPlayer);
        Lobby foundLobby = lobbyManager.getLobby(createdLobby.getId());

        // then
        assertNotNull(foundLobby.getUnassignedPlayers());

    }

    @Test
    public void addToUnassignedPlayers_invalidInput_throwException() {
        // given
        Player testPlayer = new Player();

        // when
        assertThrows(ResponseStatusException.class, () -> {
            lobbyManager.addToUnassignedPlayers(100L, testPlayer);
        });
    }

    @Test
    public void removeFromUnassignedPlayers_validInput_success() {
        // given
        Lobby testLobby = new Lobby();
        testLobby.setWinningScore(100);

        Player testPlayer = new Player();
        testPlayer.setNickname("test");

        // when
        Lobby createdLobby = lobbyManager.createLobby(testLobby);
        lobbyManager.addToUnassignedPlayers(createdLobby.getId(), testPlayer);
        lobbyManager.removeFromUnassignedPlayers(createdLobby.getId(), testPlayer);
    }

    @Test
    public void removeFromUnassignedPlayers_invalidInput_throwException() {
        // given
        Player testPlayer = new Player();
        testPlayer.setNickname("test");

        // when
        assertThrows(ResponseStatusException.class, () -> {
            lobbyManager.removeFromUnassignedPlayers(100L, testPlayer);
        });
    }

    @Test
    public void ableToJoin_validInput_success() {
        // given
        Lobby testLobby = new Lobby();
        testLobby.setId(1L);
        testLobby.setWinningScore(100);

        Player testPlayer = new Player();
        testPlayer.setId(2L);
        testPlayer.setNickname("test");

        // when
        Lobby createdLobby = lobbyManager.createLobby(testLobby);

        // then
        assertEquals(true, lobbyManager.ableToJoin(createdLobby.getInviteCode(), testPlayer));
    }

    @Test
    public void ableToJoin_invalidInput_throwException() {
        // given
        Lobby testLobby = new Lobby();
        testLobby.setWinningScore(100);

        Player testPlayer1 = new Player();
        testPlayer1.setNickname("test");

        Player testPlayer2 = new Player();
        testPlayer2.setNickname("test");

        // when
        Lobby createdLobby = lobbyManager.createLobby(testLobby);

        lobbyManager.addToUnassignedPlayers(createdLobby.getId(), testPlayer1);

        // then
        assertThrows(ResponseStatusException.class, () -> {
            lobbyManager.ableToJoin(createdLobby.getInviteCode(), testPlayer2);
        });

    }

    @Test
    public void ableToStart_lobbyReady_success() {
        // given
        Lobby testLobby = new Lobby();
        testLobby.setWinningScore(100);

        Player testPlayer1 = new Player();
        testPlayer1.setNickname("test");

        Player testPlayer2 = new Player();
        testPlayer2.setNickname("test");

        // when
        Lobby createdLobby = lobbyManager.createLobby(testLobby);
        teamService.addPlayer(createdLobby, TeamType.BLUE, testPlayer1);
        teamService.addPlayer(createdLobby, TeamType.RED, testPlayer2);

        lobbyManager.ableToStart(createdLobby.getId());
        lobbyManager.addUpcommingMinigame(createdLobby.getId());

        // then
        assertNotNull(lobbyManager.getMinigame(createdLobby.getId()));

    }

    @Test
    public void ableToStart_lobbyNotReady_throwException() {
        // given
        Lobby testLobby = new Lobby();
        testLobby.setWinningScore(100);

        Player testPlayer = new Player();
        testPlayer.setNickname("test");

        // when
        Lobby createdLobby = lobbyManager.createLobby(testLobby);
        lobbyManager.addToUnassignedPlayers(createdLobby.getId(), testPlayer);

        assertThrows(ResponseStatusException.class, () -> {
            lobbyManager.ableToStart(100L);
        });
    }

}
