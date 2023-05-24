package ch.uzh.ifi.hase.soprafs23.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.hamcrest.Matchers.is;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Team;
import ch.uzh.ifi.hase.soprafs23.entity.minigame.Minigame;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GameOverGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GamePostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.MinigameWinnerTeamPutDTO;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.LobbyManagement;
import ch.uzh.ifi.hase.soprafs23.service.MinigameService;
import ch.uzh.ifi.hase.soprafs23.service.PlayerService;
import ch.uzh.ifi.hase.soprafs23.service.TeamService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(GameController.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LobbyManagement lobbyManager;
    @MockBean
    private MinigameService minigameService;
    @MockBean
    private TeamService teamService;
    @MockBean
    private PlayerService playerService;
    @MockBean
    private GameService gameService;
    @MockBean
    private SimpMessagingTemplate messagingTemplate;
    private Lobby lobby = new Lobby();

    @BeforeEach
    public void setup() {

        // given
        lobby.setId(1L);
        lobby.setInviteCode(295738);
        List<Player> unassignedPlayers = new ArrayList<Player>();
        lobby.setUnassignedPlayers(unassignedPlayers);

        List<Team> teams = new ArrayList<Team>();
        Team team1 = new Team();
        team1.setId(2L);
        team1.setLobby(lobby);
        team1.setName("Team Red");

        Team team2 = new Team();
        team2.setId(3L);
        team2.setLobby(lobby);
        team2.setName("Team Blue");

        teams.add(team1);
        teams.add(team2);
        lobby.setTeams(teams);
    }

    @Test
    public void testCreateGame() throws Exception {
        GamePostDTO gamePostDTO = new GamePostDTO();
        // Set the necessary properties of gamePostDTO

        Game game = new Game();
        game.setId(10L);
        // Set the necessary properties of game

        // Mock the behavior of lobbyManager and gameService
        given(lobbyManager.getLobby(1L)).willReturn(lobby);
        given(gameService.createGame(Mockito.any(Game.class), Mockito.eq(1L))).willReturn(game);

        MockHttpServletRequestBuilder postRequest = post("/lobbies/1/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(gamePostDTO));

        // Perform the POST request to create a game
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(game.getId().intValue())));
    }

    @Test
    public void testCreateGame_lobbyNotFound() throws Exception {
        GamePostDTO gamePostDTO = new GamePostDTO();
        // Set the necessary properties of gamePostDTO

        // Mock the behavior of lobbyManager
        given(gameService.createGame(Mockito.any(Game.class), Mockito.eq(1L)))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        MockHttpServletRequestBuilder postRequest = post("/lobbies/1/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(gamePostDTO));

        // Perform the POST request to create a game
        mockMvc.perform(postRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetGame() throws Exception {
        Game game = new Game();
        game.setId(10L);
        game.setLobby(lobby);

        given(gameService.getGame(10L)).willReturn(game);

        mockMvc.perform(get("/lobbies/1/games/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(game.getId().intValue())));
    }

    @Test
    public void testGetGame_notFound() throws Exception {
        given(gameService.getGame(10L)).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/lobbies/1/games/10"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetMinigame() throws Exception {
        // create a Minigame
        Minigame minigame = minigameService.createMinigame(MinigameType.PONG_GAME, 2);

        given(gameService.getMinigame(10L)).willReturn(minigame);

        mockMvc.perform(get("/lobbies/1/games/10/minigame"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetMinigame_notFound() throws Exception {
        given(gameService.getMinigame(10L)).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/lobbies/1/games/10/minigame"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddMinigame() throws Exception {
        Minigame nextMinigame = gameService.addUpcomingMinigame(10L);

        given(gameService.addUpcomingMinigame(10L)).willReturn(nextMinigame);

        mockMvc.perform(post("/lobbies/1/games/10/minigames"))
                .andExpect(status().isCreated());
    }

    @Test
    public void testAddMinigame_notFound() throws Exception {
        given(gameService.addUpcomingMinigame(10L)).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(post("/lobbies/1/games/10/minigames"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateMinigame() throws Exception {
        mockMvc.perform(put("/lobbies/1/games/10/minigames"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testUpdateScore() throws Exception {
        long gameId = 10L;

        MinigameWinnerTeamPutDTO winnerTeamPutDTO = new MinigameWinnerTeamPutDTO();
        // Set the necessary properties of winnerTeamPutDTO

        // Mock the behavior of gameService
        Mockito.doNothing().when(gameService).finishedMinigameUpdate(Mockito.eq(gameId), Mockito.any(Team.class));

        // Perform the PUT request to update the score
        mockMvc.perform(MockMvcRequestBuilders
                .put("/lobbies/1/games/{gameId}", gameId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(winnerTeamPutDTO)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    public void testGetIsFinished() throws Exception {
        long gameId = 10L;

        Game game = new Game();
        // Set the necessary properties of the game

        GameOverGetDTO expectedDTO = new GameOverGetDTO();
        // Set the necessary properties of the expectedDTO based on the game

        // Mock the behavior of gameService
        Mockito.when(gameService.getGame(Mockito.eq(gameId))).thenReturn(game);

        // Perform the GET request to check if the game is finished
        mockMvc.perform(MockMvcRequestBuilders
                .get("/lobbies/1/games/{gameId}/gameover", gameId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(asJsonString(expectedDTO)));
        // Add more assertions if needed

        // Verify that the gameService method was called with the expected argument
        Mockito.verify(gameService).getGame(gameId);
    }

    @Test
    public void testGetWinner() throws Exception {
        long gameId = 10L;

        Team winnerTeam = new Team();
        // Set the necessary properties of the winnerTeam

        // Mock the behavior of gameService
        Mockito.when(gameService.getWinner(Mockito.eq(gameId))).thenReturn(winnerTeam);

        // Perform the GET request to retrieve the winner team
        mockMvc.perform(MockMvcRequestBuilders
                .get("/lobbies/1/games/{gameId}/winner", gameId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        // Add more assertions if needed

        // Verify that the gameService method was called with the expected argument
        Mockito.verify(gameService).getWinner(gameId);
    }

    @Test
    public void testGetScores() throws Exception {
        long lobbyId = 1L;
        long gameId = 10L;

        Lobby lobby = new Lobby();
        // Set the necessary properties of the lobby

        Game game = new Game();
        // Set the necessary properties of the game

        // Mock the behavior of lobbyManager.getLobby and gameService.getGame
        Mockito.when(lobbyManager.getLobby(Mockito.eq(lobbyId))).thenReturn(lobby);
        Mockito.when(gameService.getGame(Mockito.eq(gameId))).thenReturn(game);

        // Perform the GET request to retrieve the scores
        mockMvc.perform(MockMvcRequestBuilders
                .get("/lobbies/{lobbyId}/games/{gameId}/scores", lobbyId, gameId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()); // Add more assertions if needed

        // Verify that the lobbyManager.getLobby and gameService.getGame methods were
        // called with the expected arguments
        Mockito.verify(lobbyManager).getLobby(lobbyId);
        Mockito.verify(gameService).getGame(gameId);
    }

    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e.toString()));
        }
    }

}
