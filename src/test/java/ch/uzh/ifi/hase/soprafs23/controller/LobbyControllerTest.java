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

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import ch.uzh.ifi.hase.soprafs23.constant.TeamType;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Team;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyNamesPutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.TeamNamePutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.LobbyManagement;
import ch.uzh.ifi.hase.soprafs23.service.MinigameService;
import ch.uzh.ifi.hase.soprafs23.service.PlayerService;
import ch.uzh.ifi.hase.soprafs23.service.TeamService;
import javassist.expr.NewArray;

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
    public void testGetLobby_LobbyNotFound() throws Exception {
        long lobbyId = 10L;
        // Mock the behavior of lobbyManager.getLobby
        given(lobbyManager.getLobby(lobbyId)).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Perform the GET request to retrieve the lobby
        mockMvc.perform(MockMvcRequestBuilders
                .get("/lobbies/{lobbyId}", lobbyId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        // Verify that the lobbyManager.getLobby method was called with the expected
        // argument
        Mockito.verify(lobbyManager).getLobby(lobbyId);
    }

    @Test
    public void testStartGame() throws Exception {
        long lobbyId = 1L;
        Lobby lobby = new Lobby();
        lobby.setId(lobbyId);
        lobby.setInviteCode(555555);
        Team newTeam1 = new Team();
        newTeam1.setId(2L);
        newTeam1.setLobby(lobby);
        newTeam1.setName("test");
        newTeam1.setType(TeamType.TEAM_ONE);        
        Team newTeam2 = new Team();
        newTeam1.setId(3L);
        newTeam2.setLobby(lobby);
        newTeam2.setName("Muster");
        newTeam2.setType(TeamType.TEAM_TWO);
        List<Team> teamsSet = new ArrayList<>();
        teamsSet.add(newTeam1);
        teamsSet.add(newTeam2);
        lobby.setTeams(teamsSet);

        // Prepare the request body - LobbyNamesPutDTO
        LobbyNamesPutDTO lobbyNamesPutDTO = new LobbyNamesPutDTO();
        List<TeamNamePutDTO> teamsPut = new ArrayList<>();
        TeamNamePutDTO team1 = new TeamNamePutDTO();
        team1.setId(2L);
        team1.setName("Muster");
        teamsPut.add(team1);
        TeamNamePutDTO team2 = new TeamNamePutDTO();
        team2.setId(3L);
        team2.setName("Test");
        teamsPut.add(team2);
        lobbyNamesPutDTO.setTeams(teamsPut);

        // Mock the behavior of lobbyManager.getLobby to return a Lobby object
        List<Team> teams = DTOMapper.INSTANCE.convertLobbyNamesPutDTOtoEntity(lobbyNamesPutDTO).getTeams();

        Mockito.doNothing().when(teamService).updateNames(Mockito.eq(teams));

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

    @Test
    public void testStartGame_NamesInvalid() throws Exception {
        long lobbyId = 1L;
        Lobby lobby = new Lobby();
        lobby.setId(lobbyId);
        lobby.setInviteCode(555555);
        Team newTeam1 = new Team();
        newTeam1.setId(2L);
        newTeam1.setLobby(lobby);
        newTeam1.setName("test");
        newTeam1.setType(TeamType.TEAM_ONE);        
        Team newTeam2 = new Team();
        newTeam1.setId(3L);
        newTeam2.setLobby(lobby);
        newTeam2.setName("Muster");
        newTeam2.setType(TeamType.TEAM_TWO);
        List<Team> teamsSet = new ArrayList<>();
        teamsSet.add(newTeam1);
        teamsSet.add(newTeam2);
        lobby.setTeams(teamsSet);

        // Prepare the request body - LobbyNamesPutDTO
        LobbyNamesPutDTO lobbyNamesPutDTO = new LobbyNamesPutDTO();
        List<TeamNamePutDTO> teamsPut = new ArrayList<>();
        TeamNamePutDTO team1 = new TeamNamePutDTO();
        team1.setId(2L);
        team1.setName("Muster");
        teamsPut.add(team1);
        TeamNamePutDTO team2 = new TeamNamePutDTO();
        team2.setId(3L);
        team2.setName("Muster");
        teamsPut.add(team2);
        lobbyNamesPutDTO.setTeams(teamsPut);

        // Mock the behavior of lobbyManager.getLobby to return a Lobby object
        willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST)).given(teamService).updateNames(Mockito.any());

        // Perform the PUT request to start the game
        mockMvc.perform(MockMvcRequestBuilders
                .put("/lobbies/{lobbyId}", lobbyId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyNamesPutDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
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
