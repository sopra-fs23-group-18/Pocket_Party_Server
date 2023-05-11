package ch.uzh.ifi.hase.soprafs23.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.when;
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

import ch.uzh.ifi.hase.soprafs23.constant.MinigameDescription;
import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;
import ch.uzh.ifi.hase.soprafs23.constant.TeamType;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Minigame;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Team;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.WinnerTeamPutDTO;
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
    private Lobby lobby = new Lobby();
     
    @BeforeEach
    public void setup() {
        //given
        lobby.setId(1L);
        lobby.setInviteCode(295738);
        List<Player> unassignedPlayers = new ArrayList<Player>();
        lobby.setUnassignedPlayers(unassignedPlayers);

        List<Team> teams = new ArrayList<Team>();
        Team team1 = new Team();
        team1.setId(2L);
        team1.setLobby(lobby);
        team1.setColor(TeamType.RED);
        team1.setName("Team Red");

        Team team2 = new Team();
        team2.setId(3L);
        team2.setLobby(lobby);
        team2.setColor(TeamType.BLUE);
        team2.setName("Team Blue");

        teams.add(team1);
        teams.add(team2);
        lobby.setTeams(teams);
  }
    
    @Test
    public void createLobby_validInput_lobbyCreated() throws Exception {
            
        LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
    
        given(lobbyManager.createLobby()).willReturn(lobby);
    
        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(lobbyPostDTO));
    
        // then
        mockMvc.perform(postRequest)
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", is(lobby.getId().intValue())))
            .andExpect(jsonPath("$.inviteCode", is(lobby.getInviteCode())))
            .andExpect(jsonPath("$.teams[*].color", contains(lobby.getTeams().get(0).getColor().toString(), lobby.getTeams().get(1).getColor().toString())))
            .andExpect(jsonPath("$.teams[*].name", contains(lobby.getTeams().get(0).getName(), lobby.getTeams().get(1).getName())))
            .andExpect(jsonPath("$.teams[*].id", contains(lobby.getTeams().get(0).getId().intValue(), lobby.getTeams().get(1).getId().intValue())))
            .andExpect(jsonPath("$.teams[*].score", contains(lobby.getTeams().get(0).getScore(), lobby.getTeams().get(1).getScore())))
            .andExpect(jsonPath("$.teams[*].players[*]", hasSize(0)))
            .andExpect(jsonPath("$.unassignedPlayers", hasSize(0)));
    }

    @Test
    public void createLobby_invalidInput_lobbyNotCreated() throws Exception {
           
        LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
    
        given(lobbyManager.createLobby()).willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));
    
        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(lobbyPostDTO));
    
        // then
        mockMvc.perform(postRequest)
            .andExpect(status().isBadRequest());
    }

    @Test
    public void givenLobby_whenGetLobby_thenReturnLobby() throws Exception {
        
        given(lobbyManager.getLobby(Mockito.anyLong())).willReturn(lobby);

        // when
        MockHttpServletRequestBuilder getRequest = get("/lobbies/1").contentType(MediaType.APPLICATION_JSON);


        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(lobby.getId().intValue())))
            .andExpect(jsonPath("$.inviteCode", is(lobby.getInviteCode())))
            .andExpect(jsonPath("$.teams[*].color", contains(lobby.getTeams().get(0).getColor().toString(), lobby.getTeams().get(1).getColor().toString())))
            .andExpect(jsonPath("$.teams[*].name", contains(lobby.getTeams().get(0).getName(), lobby.getTeams().get(1).getName())))
            .andExpect(jsonPath("$.teams[*].id", contains(lobby.getTeams().get(0).getId().intValue(), lobby.getTeams().get(1).getId().intValue())))
            .andExpect(jsonPath("$.teams[*].score", contains(lobby.getTeams().get(0).getScore(), lobby.getTeams().get(1).getScore())))
            .andExpect(jsonPath("$.teams[*].players[*]", hasSize(0)))
            .andExpect(jsonPath("$.unassignedPlayers", hasSize(0)));
    }

    @Test
    public void givenLobby_whenGetLobby_thenThrowException() throws Exception {
      
        given(lobbyManager.getLobby(Mockito.anyLong())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        // when
        MockHttpServletRequestBuilder getRequest = get("/lobbies/100").contentType(MediaType.APPLICATION_JSON);


        // then
        mockMvc.perform(getRequest).andExpect(status().isNotFound());
    }

    @Test
    public void givenMinigame_whenGetMinigame_thenReturnMinigame() throws Exception {
        Minigame minigame = new Minigame();

        //given
        minigame.setId(8L);
        minigame.setType(MinigameType.TAPPING_GAME);
        minigame.setDescription("Tap the screen as fast as you can!");
        minigame.setScoreToGain(500);

        //get's randomly selected
        Player player1 = new Player();
        player1.setId(4L);
        player1.setNickname("Test Nickname");
        List<Player> team1Players = new ArrayList<Player>();
        team1Players.add(player1);

        Player player2 = new Player();
        player2.setId(5L);
        player2.setNickname("Test2 Nickname");
        List<Player> team2Players = new ArrayList<Player>();
        team2Players.add(player2);

        lobby.getTeams().get(0).getPlayers().add(player1);
        lobby.getTeams().get(1).getPlayers().add(player2);

        minigame.setTeam1Players(team1Players);
        minigame.setTeam2Players(team2Players);

        lobby.setUpcomingMinigame(minigame);

        given(lobbyManager.getMinigame(Mockito.anyLong())).willReturn(minigame);

        // when
        MockHttpServletRequestBuilder getRequest = get("/lobbies/1/minigame").contentType(MediaType.APPLICATION_JSON);


        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
            .andExpect(jsonPath("$.description", is(minigame.getDescription())))
            .andExpect(jsonPath("$.scoreToGain", is(minigame.getScoreToGain())))
            .andExpect(jsonPath("$.type", is(minigame.getType().toString())))
            .andExpect(jsonPath("$.team1Players[*].id", contains(minigame.getTeam1Players().get(0).getId().intValue())))
            .andExpect(jsonPath("$.team1Players[*].nickname", contains(minigame.getTeam1Players().get(0).getNickname())))
            .andExpect(jsonPath("$.team1Players[*].roundsPlayed", contains(minigame.getTeam1Players().get(0).getRoundsPlayed())))
            .andExpect(jsonPath("$.team2Players[*].id", contains(minigame.getTeam2Players().get(0).getId().intValue())))
            .andExpect(jsonPath("$.team2Players[*].nickname", contains(minigame.getTeam2Players().get(0).getNickname())))
            .andExpect(jsonPath("$.team2Players[*].roundsPlayed", contains(minigame.getTeam2Players().get(0).getRoundsPlayed())));
    }

    //add test for more than 1 player minigames

    @Test
    public void givenMinigame_whenGetMinigame_thenThrowException() throws Exception {
        Minigame minigame = new Minigame();

        //given
        minigame.setId(8L);
        minigame.setType(MinigameType.TAPPING_GAME);
        minigame.setDescription("Tap the screen as fast as you can!");
        minigame.setScoreToGain(500);

        //get's randomly selected
        Player player1 = new Player();
        player1.setId(4L);
        player1.setNickname("Test Nickname");
        List<Player> team1Players = new ArrayList<Player>();
        team1Players.add(player1);

        Player player2 = new Player();
        player2.setId(5L);
        player2.setNickname("Test2 Nickname");
        List<Player> team2Players = new ArrayList<Player>();
        team2Players.add(player2);

        lobby.getTeams().get(0).getPlayers().add(player1);
        lobby.getTeams().get(1).getPlayers().add(player2);

        minigame.setTeam1Players(team1Players);
        minigame.setTeam2Players(team2Players);

        given(lobbyManager.getMinigame(Mockito.anyLong())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        // when
        MockHttpServletRequestBuilder getRequest = get("/lobbies/1/minigame").contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isNotFound());
            
    }

    @Test
    public void givenMinigame_whenGetMinigame_invalidLobby_thenThrowException() throws Exception {
        Minigame minigame = new Minigame();

        //given
        minigame.setId(8L);
        minigame.setType(MinigameType.TAPPING_GAME);
        minigame.setDescription("Tap the screen as fast as you can!");
        minigame.setScoreToGain(500);

        //get's randomly selected
        Player player1 = new Player();
        player1.setId(4L);
        player1.setNickname("Test Nickname");
        List<Player> team1Players = new ArrayList<Player>();
        team1Players.add(player1);

        Player player2 = new Player();
        player2.setId(5L);
        player2.setNickname("Test2 Nickname");
        List<Player> team2Players = new ArrayList<Player>();
        team2Players.add(player2);

        lobby.getTeams().get(0).getPlayers().add(player1);
        lobby.getTeams().get(1).getPlayers().add(player2);

        minigame.setTeam1Players(team1Players);
        minigame.setTeam2Players(team2Players);

        given(lobbyManager.getMinigame(Mockito.anyLong())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        // when
        MockHttpServletRequestBuilder getRequest = get("/lobbies/100/minigame").contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isNotFound());
            
    }

    
    @Test
    public void startGame_lessThan2Players_lobbyNotUpdated() throws Exception {
        
        willThrow(new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED)).given(lobbyManager).ableToStart(Mockito.anyLong());

        // when
        MockHttpServletRequestBuilder putRequest = put("/lobbies/1")
            .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(putRequest)
            .andExpect(status().isMethodNotAllowed());
            
    }

    @Test
    public void startGame_playersLeftInUnassignedPlayers_lobbyNotUpdated() throws Exception {
        
        Player player1 = new Player();
        player1.setId(4L);
        player1.setNickname("Test Nickname");

        Player player2 = new Player();
        player2.setId(5L);
        player2.setNickname("Test2 Nickname");

        lobby.getUnassignedPlayers().add(player1);
        lobby.getUnassignedPlayers().add(player2);

        willThrow(new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED)).given(lobbyManager).ableToStart(Mockito.anyLong());

        // when
        MockHttpServletRequestBuilder putRequest = put("/lobbies/1")
            .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(putRequest)
            .andExpect(status().isMethodNotAllowed());
            
    }

    @Test
    public void startGame_lobbyNotFound_lobbyNotUpdated() throws Exception {
        
        willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).given(lobbyManager).ableToStart(Mockito.anyLong());

        // when
        MockHttpServletRequestBuilder putRequest = put("/lobbies/100")
            .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(putRequest)
            .andExpect(status().isNotFound());
            
    }

    @Test
    public void startGame_valid_lobbyUpdated() throws Exception {
        Minigame minigame = new Minigame();

        //given
        minigame.setId(8L);
        minigame.setType(MinigameType.TAPPING_GAME);
        minigame.setDescription("Tap the screen as fast as you can!");
        minigame.setScoreToGain(500);

        //get's randomly selected
        Player player1 = new Player();
        player1.setId(4L);
        player1.setNickname("Test Nickname");

        Player player2 = new Player();
        player2.setId(5L);
        player2.setNickname("Test2 Nickname");

        List<Player> team1Players = new ArrayList<Player>();
        team1Players.add(player1);
        List<Player> team2Players = new ArrayList<Player>();
        team2Players.add(player2);

        lobby.getTeams().get(0).getPlayers().add(player1);
        lobby.getTeams().get(1).getPlayers().add(player2);

        minigame.setTeam1Players(team1Players);
        minigame.setTeam2Players(team2Players);

        lobby.setUpcomingMinigame(minigame);


        doNothing().when(lobbyManager).ableToStart(Mockito.anyLong());
        doNothing().when(lobbyManager).addUpcommingMinigame(Mockito.anyLong());

        // when
        MockHttpServletRequestBuilder putRequest = put("/lobbies/1")
            .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(putRequest)
            .andExpect(status().isNoContent());
            
    }

    @Test
    public void updateScores_lobbyNotFound_lobbyNotUpdated() throws Exception {
        
        WinnerTeamPutDTO winnerTeamPutDTO = new WinnerTeamPutDTO();
        winnerTeamPutDTO.setColor(TeamType.RED);
        winnerTeamPutDTO.setName("Team Red");
        winnerTeamPutDTO.setScore(300);

        willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).given(lobbyManager).finishedMinigameUpdate((Mockito.anyLong()), Mockito.any());

        // when
        MockHttpServletRequestBuilder putRequest = put("/lobbies/100/minigame")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(winnerTeamPutDTO));

        // then
        mockMvc.perform(putRequest)
            .andExpect(status().isNotFound());
            
            
    }

    @Test
    public void updateScores_MinigameNotFound_lobbyNotUpdated() throws Exception {
        
        WinnerTeamPutDTO winnerTeamPutDTO = new WinnerTeamPutDTO();
        winnerTeamPutDTO.setColor(TeamType.RED);
        winnerTeamPutDTO.setName("Team Red");
        winnerTeamPutDTO.setScore(300);
        
        willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).given(lobbyManager).finishedMinigameUpdate((Mockito.anyLong()), Mockito.any());

        // when
        MockHttpServletRequestBuilder putRequest = put("/lobbies/1/minigame")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(winnerTeamPutDTO));

        // then
        mockMvc.perform(putRequest)
            .andExpect(status().isNotFound());    
    }

    @Test
    public void updateScores_invalidScore_lobbyNotUpdated() throws Exception {
        
        WinnerTeamPutDTO winnerTeamPutDTO = new WinnerTeamPutDTO();
        winnerTeamPutDTO.setColor(TeamType.RED);
        winnerTeamPutDTO.setName("Team Red");
        winnerTeamPutDTO.setScore(-200);
        
        willThrow(new ResponseStatusException(HttpStatus.FORBIDDEN)).given(lobbyManager).finishedMinigameUpdate((Mockito.anyLong()), Mockito.any());

        // when
        MockHttpServletRequestBuilder putRequest = put("/lobbies/1/minigame")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(winnerTeamPutDTO));

        // then
        mockMvc.perform(putRequest)
            .andExpect(status().isForbidden());    
    }

    @Test
    public void updateScores_PlayerNotFound_lobbyNotUpdated() throws Exception {
        
        Minigame minigame = new Minigame();

        //given
        minigame.setId(8L);
        minigame.setType(MinigameType.TAPPING_GAME);
        minigame.setDescription("Tap the screen as fast as you can!");
        minigame.setScoreToGain(500);

        Player player1 = new Player();
        player1.setId(4L);
        player1.setNickname("Test Nickname");

        Player player2 = new Player();
        player2.setId(5L);
        player2.setNickname("Test2 Nickname");

        lobby.getTeams().get(0).getPlayers().add(player1);
        lobby.getTeams().get(1).getPlayers().add(player2);

        lobby.setUpcomingMinigame(minigame);


        WinnerTeamPutDTO winnerTeamPutDTO = new WinnerTeamPutDTO();
        winnerTeamPutDTO.setColor(TeamType.RED);
        winnerTeamPutDTO.setName("Team Red");
        winnerTeamPutDTO.setScore(300);
        
        willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).given(lobbyManager).finishedMinigameUpdate((Mockito.anyLong()), Mockito.any());

        // when
        MockHttpServletRequestBuilder putRequest = put("/lobbies/1/minigame")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(winnerTeamPutDTO));

        // then
        mockMvc.perform(putRequest)
            .andExpect(status().isNotFound());   
             
    }

    @Test
    public void updateScores_TeamNotFound_lobbyNotUpdated() throws Exception {
        
        Minigame minigame = new Minigame();

        //given
        minigame.setId(8L);
        minigame.setType(MinigameType.TAPPING_GAME);
        minigame.setDescription("Tap the screen as fast as you can!");
        minigame.setScoreToGain(500);

        Player player1 = new Player();
        player1.setId(4L);
        player1.setNickname("Test Nickname");

        Player player2 = new Player();
        player2.setId(5L);
        player2.setNickname("Test2 Nickname");

        List<Player> team1Players = new ArrayList<Player>();
        team1Players.add(player1);
        List<Player> team2Players = new ArrayList<Player>();
        team2Players.add(player2);

        minigame.setTeam1Players(team1Players);
        minigame.setTeam2Players(team2Players);

        lobby.setUpcomingMinigame(minigame);


        WinnerTeamPutDTO winnerTeamPutDTO = new WinnerTeamPutDTO();
        winnerTeamPutDTO.setColor(TeamType.RED);
        winnerTeamPutDTO.setName("Team Red");
        winnerTeamPutDTO.setScore(300);
        
        willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).given(lobbyManager).finishedMinigameUpdate((Mockito.anyLong()), Mockito.any());

        // when
        MockHttpServletRequestBuilder putRequest = put("/lobbies/1/minigame")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(winnerTeamPutDTO));

        // then
        mockMvc.perform(putRequest)
            .andExpect(status().isNotFound());   
             
    }

    @Test
    public void updateScores_valid_lobbyUpdated() throws Exception {
        
        Minigame minigame = new Minigame();

        //given
        minigame.setId(8L);
        minigame.setType(MinigameType.TAPPING_GAME);
        minigame.setDescription("Tap the screen as fast as you can!");
        minigame.setScoreToGain(500);

        Player player1 = new Player();
        player1.setId(4L);
        player1.setNickname("Test Nickname");

        Player player2 = new Player();
        player2.setId(5L);
        player2.setNickname("Test2 Nickname");

        lobby.getTeams().get(0).getPlayers().add(player1);
        lobby.getTeams().get(1).getPlayers().add(player2);

        List<Player> team1Players = new ArrayList<Player>();
        team1Players.add(player1);
        List<Player> team2Players = new ArrayList<Player>();
        team2Players.add(player2);

        minigame.setTeam1Players(team1Players);
        minigame.setTeam2Players(team2Players);

        lobby.setUpcomingMinigame(minigame);

        WinnerTeamPutDTO winnerTeamPutDTO = new WinnerTeamPutDTO();
        winnerTeamPutDTO.setColor(TeamType.RED);
        winnerTeamPutDTO.setName("Team Red");
        winnerTeamPutDTO.setScore(300);
        
        doNothing().when(lobbyManager).finishedMinigameUpdate(Mockito.anyLong(), Mockito.any());
        doNothing().when(lobbyManager).addUpcommingMinigame(Mockito.anyLong());
        doNothing().when(lobbyManager).isFinished(Mockito.anyLong());


        // when
        MockHttpServletRequestBuilder putRequest = put("/lobbies/1/minigame")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(winnerTeamPutDTO));

        // then
        mockMvc.perform(putRequest)
            .andExpect(status().isNoContent());   
             
    }

    @Test
    public void givenScores_whenGetScores_validLobby_thenReturnScores_() throws Exception {
        
        lobby.getTeams().get(0).setScore(300);
        lobby.getTeams().get(0).setScore(200);

        given(lobbyManager.getLobby(Mockito.anyLong())).willReturn(lobby);

        // when
        MockHttpServletRequestBuilder getRequest = get("/lobbies/1/scores")
            .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.winningScore", is(lobby.getWinningScore())))
            .andExpect(jsonPath("$.teams[*].color", contains(lobby.getTeams().get(0).getColor().toString(), lobby.getTeams().get(1).getColor().toString())))
            .andExpect(jsonPath("$.teams[*].name", contains(lobby.getTeams().get(0).getName(), lobby.getTeams().get(1).getName())))
            .andExpect(jsonPath("$.teams[*].id", contains(lobby.getTeams().get(0).getId().intValue(), lobby.getTeams().get(1).getId().intValue())))
            .andExpect(jsonPath("$.teams[*].score", contains(lobby.getTeams().get(0).getScore(), lobby.getTeams().get(1).getScore())));     
    }

    @Test
    public void givenScores_whenGetScores_invalidLobby_thenThrowException_() throws Exception {
        
        lobby.getTeams().get(0).setScore(300);
        lobby.getTeams().get(1).setScore(200);

        given(lobbyManager.getLobby(Mockito.anyLong())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));


        // when
        MockHttpServletRequestBuilder getRequest = get("/lobbies/100/scores")
            .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
            .andExpect(status().isNotFound());
    }
    
    @Test
    public void givenLobby_whenGetIsFinished_validLobby_thenReturnIsFinished_() throws Exception {

        given(lobbyManager.getLobby(Mockito.anyLong())).willReturn(lobby);

        // when
        MockHttpServletRequestBuilder getRequest = get("/lobbies/1/gameover")
            .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.isFinished", is(lobby.getIsFinished())));
    }

    @Test
    public void givenLobby_whenGetIsFinished_invalidLobby_thenThrowException_() throws Exception {
        
        given(lobbyManager.getLobby(Mockito.anyLong())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));


        // when
        MockHttpServletRequestBuilder getRequest = get("/lobbies/100/gameover")
            .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
            .andExpect(status().isNotFound());
    }

    @Test
    public void givenLobby_whenGetWinner_validLobby_thenReturnWinner_() throws Exception {

        Player player1 = new Player();
        player1.setId(4L);
        player1.setNickname("Test Nickname");

        Player player2 = new Player();
        player2.setId(5L);
        player2.setNickname("Test2 Nickname");

        lobby.getTeams().get(0).getPlayers().add(player1);
        lobby.getTeams().get(1).getPlayers().add(player2);

        Team teamRed = lobby.getTeams().get(0);
        teamRed.setScore(400);
        Team teamBlue = lobby.getTeams().get(1);
        teamBlue.setScore(600);


        given(lobbyManager.getWinner(Mockito.anyLong())).willReturn(teamBlue);

        // when
        MockHttpServletRequestBuilder getRequest = get("/lobbies/1/winner")
            .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(teamBlue.getId().intValue())))
            .andExpect(jsonPath("$.score", is(teamBlue.getScore())))
            .andExpect(jsonPath("$.name", is(teamBlue.getName())))
            .andExpect(jsonPath("$.color", is(teamBlue.getColor().toString())));

    }

    @Test
    public void givenLobby_whenGetWinner_invalidLobby_thenThrowException_() throws Exception {
        
        given(lobbyManager.getWinner(Mockito.anyLong())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));


        // when
        MockHttpServletRequestBuilder getRequest = get("/lobbies/100/winner")
            .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
            .andExpect(status().isNotFound());
    }

    @Test
    public void givenLobby_whenGetWinner_noWinner_thenThrowException_() throws Exception {
        
        Player player1 = new Player();
        player1.setId(4L);
        player1.setNickname("Test Nickname");

        Player player2 = new Player();
        player2.setId(5L);
        player2.setNickname("Test2 Nickname");

        lobby.getTeams().get(0).getPlayers().add(player1);
        lobby.getTeams().get(1).getPlayers().add(player2);

        Team teamRed = lobby.getTeams().get(0);
        teamRed.setScore(400);
        Team teamBlue = lobby.getTeams().get(1);
        teamBlue.setScore(400);

        given(lobbyManager.getWinner(Mockito.anyLong())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));


        // when
        MockHttpServletRequestBuilder getRequest = get("/lobbies/1/winner")
            .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
            .andExpect(status().isNotFound());
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
