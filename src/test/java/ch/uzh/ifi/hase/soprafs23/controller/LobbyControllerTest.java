package ch.uzh.ifi.hase.soprafs23.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.BDDMockito.doNothing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;
import ch.uzh.ifi.hase.soprafs23.constant.TeamType;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Team;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs23.service.LobbyManagement;
import ch.uzh.ifi.hase.soprafs23.service.MinigameService;
import ch.uzh.ifi.hase.soprafs23.service.PlayerService;
import ch.uzh.ifi.hase.soprafs23.service.TeamService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;



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
    private SimpMessagingTemplate messagingTemplate;
     
    
    @Test
    public void createLobby_validInput_lobbyCreated() throws Exception {
        Lobby lobby = new Lobby();

        //given
        lobby.setId(1L);
        lobby.setWinningScore(500);
        lobby.setInviteCode(295738);
        List<MinigameType> minigames = Arrays.asList(MinigameType.values());
        lobby.setMinigamesChoice(minigames);
        List<Player> unassignedPlayers = new ArrayList<Player>();
        lobby.setUnassignedPlayers(unassignedPlayers);

        List<Team> teams = new ArrayList<Team>();
        Team team1 = new Team();
        team1.setLobby(lobby);
        team1.setColor(TeamType.RED);
        team1.setName("Team Red");

        Team team2 = new Team();
        team2.setLobby(lobby);
        team2.setColor(TeamType.BLUE);
        team2.setName("Team Blue");

        teams.add(team1);
        teams.add(team2);
        lobby.setTeams(teams);
    
        LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
        lobbyPostDTO.setWinningScore(500);
    
        given(lobbyManager.createLobby(Mockito.any())).willReturn(lobby);
    
        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(lobbyPostDTO));
    
        // then
        mockMvc.perform(postRequest)
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", is(lobby.getId().intValue())))
            .andExpect(jsonPath("$.inviteCode", is(lobby.getInviteCode())))
            .andExpect(jsonPath("$.winningScore", is(lobby.getWinningScore())))
            .andExpect(jsonPath("$.teams[*].color", contains(lobby.getTeams().get(0).getColor().toString(), lobby.getTeams().get(1).getColor().toString())))
            .andExpect(jsonPath("$.teams[*].name", contains(lobby.getTeams().get(0).getName(), lobby.getTeams().get(1).getName())))
            .andExpect(jsonPath("$.teams[*].id", contains(lobby.getTeams().get(0).getId(), lobby.getTeams().get(1).getId())))
            .andExpect(jsonPath("$.teams[*].score", contains(lobby.getTeams().get(0).getScore(), lobby.getTeams().get(1).getScore())))
            .andExpect(jsonPath("$.teams[*].players[*]", hasSize(0)))
            .andExpect(jsonPath("$.unassignedPlayers", hasSize(0)));
    }

    @Test
    public void createLobby_invalidInput_lobbyNotCreated() throws Exception {
        Lobby lobby = new Lobby();

        //given
        lobby.setId(1L);
        lobby.setInviteCode(295738);
        List<MinigameType> minigames = Arrays.asList(MinigameType.values());
        lobby.setMinigamesChoice(minigames);
        List<Player> unassignedPlayers = new ArrayList<Player>();
        lobby.setUnassignedPlayers(unassignedPlayers);

        List<Team> teams = new ArrayList<Team>();
        Team team1 = new Team();
        team1.setLobby(lobby);
        team1.setColor(TeamType.RED);
        team1.setName("Team Red");

        Team team2 = new Team();
        team2.setLobby(lobby);
        team2.setColor(TeamType.BLUE);
        team2.setName("Team Blue");

        teams.add(team1);
        teams.add(team2);
        lobby.setTeams(teams);
    
        LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
        lobbyPostDTO.setWinningScore(-500);
    
        given(lobbyManager.createLobby(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));
    
        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(lobbyPostDTO));
    
        // then
        mockMvc.perform(postRequest)
            .andExpect(status().isBadRequest());
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
