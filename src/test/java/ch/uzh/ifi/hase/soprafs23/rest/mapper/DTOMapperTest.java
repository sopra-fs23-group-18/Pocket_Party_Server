package ch.uzh.ifi.hase.soprafs23.rest.mapper;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.entity.minigame.Minigame;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;

import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Team;
import ch.uzh.ifi.hase.soprafs23.constant.*;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GameOverGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.MinigameGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.ScoresGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.TeamGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.MinigameWinnerTeamPutDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation
 * works.
 */
public class DTOMapperTest {
  @Test
  public void testCreateUser_fromUserPostDTO_toUser_success() {
    // create UserPostDTO
    UserPostDTO userPostDTO = new UserPostDTO();
    userPostDTO.setName("name");
    userPostDTO.setUsername("username");

    // MAP -> Create user
    User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

    // check content
    assertEquals(userPostDTO.getName(), user.getName());
    assertEquals(userPostDTO.getUsername(), user.getUsername());
  }

  @Test
  public void testGetUser_fromUser_toUserGetDTO_success() {
    // create User
    User user = new User();
    user.setName("Firstname Lastname");
    user.setUsername("firstname@lastname");
    user.setStatus(UserStatus.OFFLINE);
    user.setToken("1");

    // MAP -> Create UserGetDTO
    UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

    // check content
    assertEquals(user.getId(), userGetDTO.getId());
    assertEquals(user.getName(), userGetDTO.getName());
    assertEquals(user.getUsername(), userGetDTO.getUsername());
    assertEquals(user.getStatus(), userGetDTO.getStatus());
  }

  @Test
  public void testCreateLobby_fromLobbyPostDTO_toLobby_success() {
    // create LobbyPostDTO
    LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
    lobbyPostDTO.setWinningScore(100);

    // MAP -> Create Lobby
    Lobby lobby = DTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(lobbyPostDTO);

    // check content
    assertEquals(lobbyPostDTO.getWinningScore(), lobby.getWinningScore());
  }

  @Test
  public void testGetLobby_fromLobby_toLobbyGetDTO_success() {
    // create Lobby
    Lobby lobby = new Lobby();
    lobby.setId(1L);
    lobby.setWinningScore(100);
    lobby.setInviteCode(000000);
    lobby.setTeams(List.of(new Team(), new Team()));
    lobby.setUnassignedPlayers(List.of(new Player(), new Player()));

    // MAP -> Create LobbyGetDTO
    LobbyGetDTO lobbyGetDTO = DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby);

    // check content
    assertEquals(lobby.getId(), lobbyGetDTO.getId());
    assertEquals(lobby.getWinningScore(), lobbyGetDTO.getWinningScore());
    assertEquals(lobby.getInviteCode(), lobbyGetDTO.getInviteCode());
    assertEquals(lobby.getTeams(), lobbyGetDTO.getTeams());
    assertEquals(lobby.getUnassignedPlayers(), lobbyGetDTO.getUnassignedPlayers());
  }

  @Test
  public void testGetScore_fromLobby_toScoresGetDTO_success() {
    // create Lobby
    Lobby lobby = new Lobby();
    lobby.setWinningScore(100);

    Team team1 = new Team();
    team1.setScore(50);
    team1.setColor(TeamType.RED);
    team1.setId(3L);

    team1.setName("test");

    Team team2 = new Team();
    team2.setId(4L);
    team2.setColor(TeamType.BLUE);
    team2.setName("test2");
    team2.setScore(60);

    lobby.setTeams(List.of(team1, team2));

    // MAP -> Create ScoresGetDTO
    ScoresGetDTO scoresGetDTO = DTOMapper.INSTANCE.convertEntityToScoresGetDTO(lobby);

    // check content
    assertEquals(lobby.getWinningScore(), scoresGetDTO.getWinningScore());
    assertEquals(lobby.getTeams().get(0).getId(), scoresGetDTO.getTeams().get(0).getId());
    assertEquals(lobby.getTeams().get(0).getColor(), scoresGetDTO.getTeams().get(0).getColor());
    assertEquals(lobby.getTeams().get(0).getName(), scoresGetDTO.getTeams().get(0).getName());
    assertEquals(lobby.getTeams().get(0).getScore(), scoresGetDTO.getTeams().get(0).getScore());
    assertEquals(lobby.getTeams().get(1).getId(), scoresGetDTO.getTeams().get(1).getId());
    assertEquals(lobby.getTeams().get(1).getColor(), scoresGetDTO.getTeams().get(1).getColor());
    assertEquals(lobby.getTeams().get(1).getName(), scoresGetDTO.getTeams().get(1).getName());
    assertEquals(lobby.getTeams().get(1).getScore(), scoresGetDTO.getTeams().get(1).getScore());
  }

  @Test
  public void testGetMinigame_fromMinigame_toMinigameGetDTO_success() {
    // create Minigame
    Minigame minigame = new Minigame();
    minigame.setScoreToGain(100);
    minigame.setType(MinigameType.TIMING_GAME);
    minigame.setDescription("test");
    Player player1 = new Player();
    Player player2 = new Player();

    List<Player> team1Players = new ArrayList<Player>();
    team1Players.add(player1);
    List<Player> team2Players = new ArrayList<Player>();
    team2Players.add(player2);
    minigame.setTeam1Players(team1Players);
    minigame.setTeam2Players(team2Players);

    // MAP -> Create MinigameGetDTO
    MinigameGetDTO minigameGetDTO = DTOMapper.INSTANCE.convertEntityToMinigameGetDTO(minigame);

    // check content
    assertEquals(minigame.getScoreToGain(), minigameGetDTO.getScoreToGain());
    assertEquals(minigame.getType(), minigameGetDTO.getType());
    assertEquals(minigame.getDescription(), minigameGetDTO.getDescription());
    assertEquals(minigame.getTeam1Players(), minigameGetDTO.getTeam1Players());
    assertEquals(minigame.getTeam2Players(), minigameGetDTO.getTeam2Players());
  }

  @Test
  public void testGetWinnerTeam_fromWinnerTeamPutDTO_toTeam_success() {
    // create WinnerTeamPutDTO
    MinigameWinnerTeamPutDTO winnerTeamPutDTO = new MinigameWinnerTeamPutDTO();
    winnerTeamPutDTO.setColor(TeamType.RED);
    winnerTeamPutDTO.setName("test");
    winnerTeamPutDTO.setScore(100);

    // MAP -> Create Team
    Team team = DTOMapper.INSTANCE.convertMinigameWinnerTeamPutDTOToEntity(winnerTeamPutDTO);

    // check content
    assertEquals(winnerTeamPutDTO.getColor(), team.getColor());
    assertEquals(winnerTeamPutDTO.getName(), team.getName());
    assertEquals(winnerTeamPutDTO.getScore(), team.getScore());
  }

  @Test
  public void testGetTeam_fromTeam_toTeamGetDTO_success() {
    // create Team
    Team team = new Team();
    team.setId(1L);
    team.setColor(TeamType.RED);
    team.setName("test");
    team.setScore(100);

    // MAP -> Create TeamGetDTO
    TeamGetDTO teamGetDTO = DTOMapper.INSTANCE.convertEntityToTeamGetDTO(team);

    // check content
    assertEquals(team.getId(), teamGetDTO.getId());
    assertEquals(team.getColor(), teamGetDTO.getColor());
    assertEquals(team.getName(), teamGetDTO.getName());
    assertEquals(team.getScore(), teamGetDTO.getScore());
  }

  @Test
  public void testGetGameOver_fromLobby_toGameOverGetDTO_success() {
    // create Lobby
    Lobby lobby = new Lobby();
    lobby.setIsFinished(false);

    // MAP -> Create GameOverGetDTO
    GameOverGetDTO gameOverGetDTO = DTOMapper.INSTANCE.convertEntityToGameOverGetDTO(lobby);

    // check content
    assertEquals(lobby.getIsFinished(), gameOverGetDTO.getIsFinished());
  }

}
