package ch.uzh.ifi.hase.soprafs23.rest.mapper;

import ch.uzh.ifi.hase.soprafs23.entity.User;
//import ch.uzh.ifi.hase.soprafs23.entity.minigame.HotPotato;
import ch.uzh.ifi.hase.soprafs23.entity.minigame.Minigame;
import ch.uzh.ifi.hase.soprafs23.entity.minigame.Vibration;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.WinnerTeamGetDTO;
import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Team;
import ch.uzh.ifi.hase.soprafs23.constant.*;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GameOverGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GamePostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyNamesPutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.MinigameGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.ScoresGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.TeamGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.TeamNamePutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.MinigameWinnerTeamPutDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
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
  public void testConvertEntityToLobbyGetDTO() {
    // Create a Lobby entity
    Lobby lobby = new Lobby();
    lobby.setId(1L);
    lobby.setInviteCode(12345);
    // Set other properties of the Lobby entity

    // Convert Lobby entity to LobbyGetDTO
    LobbyGetDTO lobbyGetDTO = DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby);

    // Verify the mapping
    assertEquals(lobby.getId(), lobbyGetDTO.getId());
    assertEquals(lobby.getInviteCode(), lobbyGetDTO.getInviteCode());
  }

  @Test
  public void testConvertEntitiesToScoresGetDTO() {
    // Create a Lobby object and a Game object
    Lobby lobby = new Lobby();
    Game game = new Game();

    // Set the necessary properties in the Lobby and Game objects

    // Call the convertEntitiesToScoresGetDTO method
    ScoresGetDTO scoresGetDTO = DTOMapper.INSTANCE.convertEntitiesToScoresGetDTO(lobby, game);

    // Verify the mapping
    assertEquals(lobby.getTeams(), scoresGetDTO.getTeams());
    assertEquals(game.getWinningScore(), scoresGetDTO.getWinningScore());
  }

  @Test
  public void testConvertEntityToMinigameGetDTO() {
    // Create a Minigame object and set the necessary properties for testing
    Minigame minigame = new Vibration();

    // Set other necessary properties of the Minigame object

    // Call the convertEntityToMinigameGetDTO method
    MinigameGetDTO minigameGetDTO = DTOMapper.INSTANCE.convertEntityToMinigameGetDTO(minigame);

    // Assert the expected values
    assertEquals(minigame.getScoreToGain(), minigameGetDTO.getScoreToGain());
    assertEquals(minigame.getType(), minigameGetDTO.getType());
    assertEquals(minigame.getDescription(), minigameGetDTO.getDescription());
  }

  @Test
  public void testConvertMinigameWinnerTeamPutDTOtoEntity() {
    // Create a MinigameWinnerTeamPutDTO object and set the necessary properties for
    // testing
    MinigameWinnerTeamPutDTO winnerTeamPutDTO = new MinigameWinnerTeamPutDTO();
    winnerTeamPutDTO.setName("Team Name");
    winnerTeamPutDTO.setScore(100);

    // Call the convertMinigameWinnerTeamPutDTOtoEntity method
    Team team = DTOMapper.INSTANCE.convertMinigameWinnerTeamPutDTOtoEntity(winnerTeamPutDTO);

    // Assert the expected values
    assertEquals("Team Name", team.getName());
    assertEquals(100, team.getScore());
  }

  @Test
  public void testConvertEntityToTeamGetDTO() {
    // Create a Team object and set the necessary properties for testing
    Team team = new Team();
    team.setId(1L);
    team.setName("Team Name");
    team.setScore(100);

    // Call the convertEntityToTeamGetDTO method
    TeamGetDTO teamGetDTO = DTOMapper.INSTANCE.convertEntityToTeamGetDTO(team);

    // Assert the expected values
    assertEquals(1L, teamGetDTO.getId());
    assertEquals("Team Name", teamGetDTO.getName());
    assertEquals(100, teamGetDTO.getScore());
  }

  @Test
  public void testConvertEntityToWinnerTeamGetDTO() {
    // Create a Team object and set the necessary properties for testing
    Team team = new Team();
    team.setId(1L);
    team.setName("Team Name");
    team.setScore(100);
    // Set the players for the team
    List<Player> players = new ArrayList<>();
    Player player1 = new Player();
    player1.setId(1L);
    player1.setNickname("Player 1");
    players.add(player1);
    Player player2 = new Player();
    player2.setId(2L);
    player2.setNickname("Player 2");
    players.add(player2);
    team.setPlayers(players);

    // Call the convertEntityToWinnerTeamGetDTO method
    WinnerTeamGetDTO winnerTeamGetDTO = DTOMapper.INSTANCE.convertEntityToWinnerTeamGetDTO(team);

    // Assert the expected values
    assertEquals(1L, winnerTeamGetDTO.getId());
    assertEquals("Team Name", winnerTeamGetDTO.getName());
    assertEquals(100, winnerTeamGetDTO.getScore());
    // Assert the players list
    assertEquals(2, winnerTeamGetDTO.getPlayers().size());
    assertEquals(1L, winnerTeamGetDTO.getPlayers().get(0).getId());
    assertEquals("Player 1", winnerTeamGetDTO.getPlayers().get(0).getNickname());
    assertEquals(2L, winnerTeamGetDTO.getPlayers().get(1).getId());
    assertEquals("Player 2", winnerTeamGetDTO.getPlayers().get(1).getNickname());
  }

  @Test
  public void testConvertEntityToGameOverGetDTO() {
    // Create a Game object and set the necessary properties for testing
    Game game = new Game();
    game.setGameOutcome(OutcomeType.WINNER);

    // Call the convertEntityToGameOverGetDTO method
    GameOverGetDTO gameOverGetDTO = DTOMapper.INSTANCE.convertEntityToGameOverGetDTO(game);

    // Assert the expected value
    assertEquals(OutcomeType.WINNER, gameOverGetDTO.getGameOutcome());
  }

  @Test
  public void testConvertGamePostDTOtoEntity() {
    // Create a GamePostDTO object and set the necessary properties for testing
    GamePostDTO gamePostDTO = new GamePostDTO();
    List<MinigameType> minigamesChoice = Arrays.asList(MinigameType.TAPPING_GAME, MinigameType.PONG_GAME);
    gamePostDTO.setMinigamesChoice(minigamesChoice);
    gamePostDTO.setWinningScore(100);
    gamePostDTO.setPlayerChoice(PlayerChoice.RANDOM);

    // Call the convertGamePostDTOtoEntity method
    Game game = DTOMapper.INSTANCE.convertGamePostDTOtoEntity(gamePostDTO);

    // Assert the expected values
    assertEquals(minigamesChoice, game.getMinigamesChoice());
    assertEquals(100, game.getWinningScore());
    assertEquals(PlayerChoice.RANDOM, game.getPlayerChoice());
  }

  @Test
  public void testConvertEntityToGameGetDTO() {
    // Create a Game object and set the necessary properties for testing
    Game game = new Game();
    game.setId(1L);
    game.setWinningScore(100);

    // Call the convertEntityToGameGetDTO method
    GameGetDTO gameGetDTO = DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);

    // Assert the expected values
    assertEquals(1L, gameGetDTO.getId());
    assertEquals(100, gameGetDTO.getWinningScore());
  }

  @Test
  public void testConvertTeamNamePutDTOtoEntity() {
    // Create a TeamNamePutDTO object and set the necessary properties for testing
    TeamNamePutDTO teamNamePutDTO = new TeamNamePutDTO();
    teamNamePutDTO.setId(1L);
    teamNamePutDTO.setName("Team 1");

    // Call the convertTeamNamePutDTOtoEntity method
    Team team = DTOMapper.INSTANCE.convertTeamNamePutDTOtoEntity(teamNamePutDTO);

    // Assert the expected values
    assertEquals(1L, team.getId());
    assertEquals("Team 1", team.getName());
  }

  @Test
  public void testConvertLobbyNamesPutDTOtoEntity() {
    // Create a LobbyNamesPutDTO object and set the necessary properties for testing
    LobbyNamesPutDTO lobbyNamesPutDTO = new LobbyNamesPutDTO();
    // Set the required properties for conversion

    // Call the convertLobbyNamesPutDTOtoEntity method
    Lobby lobby = DTOMapper.INSTANCE.convertLobbyNamesPutDTOtoEntity(lobbyNamesPutDTO);

    // Assert the expected values
    assertNotNull(lobby);
  }

}
