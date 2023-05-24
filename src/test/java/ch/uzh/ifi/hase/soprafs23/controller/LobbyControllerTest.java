package ch.uzh.ifi.hase.soprafs23.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyNamesPutDTO;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.LobbyManagement;
import ch.uzh.ifi.hase.soprafs23.service.MinigameService;
import ch.uzh.ifi.hase.soprafs23.service.PlayerService;
import ch.uzh.ifi.hase.soprafs23.service.TeamService;

@WebMvcTest(LobbyController.class)
public class LobbyControllerTest {

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

    @Test
    public void testCreateLobby() throws Exception {
        // Perform the POST request to create a lobby
        mockMvc.perform(MockMvcRequestBuilders
                .post("/lobbies")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        // Add more assertions if needed

        // Verify that the lobbyManager.createLobby method was called
        Mockito.verify(lobbyManager).createLobby();
    }

    @Test
    public void testGetLobby() throws Exception {
        long lobbyId = 1L;

        Lobby lobby = new Lobby();
        // Set the necessary properties of the lobby

        // Mock the behavior of lobbyManager.getLobby
        Mockito.when(lobbyManager.getLobby(Mockito.eq(lobbyId))).thenReturn(lobby);

        // Perform the GET request to retrieve the lobby
        mockMvc.perform(MockMvcRequestBuilders
                .get("/lobbies/{lobbyId}", lobbyId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        // Add more assertions if needed

        // Verify that the lobbyManager.getLobby method was called with the expected
        // argument
        Mockito.verify(lobbyManager).getLobby(lobbyId);
    }

    @Test
    public void testStartGame() throws Exception {
        long lobbyId = 1L;

        // Prepare the request body - LobbyNamesPutDTO
        LobbyNamesPutDTO lobbyNamesPutDTO = new LobbyNamesPutDTO();
        // Set the necessary properties of the lobbyNamesPutDTO

        // Mock the behavior of lobbyManager.getLobby to return a Lobby object
        Lobby lobby = new Lobby();
        Mockito.when(lobbyManager.getLobby(Mockito.eq(lobbyId))).thenReturn(lobby);

        // Perform the PUT request to start the game
        mockMvc.perform(MockMvcRequestBuilders
                .put("/lobbies/{lobbyId}", lobbyId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyNamesPutDTO)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        // Add more assertions if needed

        // Verify that the lobbyManager.ableToStart method was called with the expected
        // lobbyId
        Mockito.verify(lobbyManager).ableToStart(lobbyId);

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
