package ch.uzh.ifi.hase.soprafs23.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Minigame;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Team;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.MinigameGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.TeamGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.WinnerTeamPutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.LobbyManagement;
import ch.uzh.ifi.hase.soprafs23.service.MinigameService;
import ch.uzh.ifi.hase.soprafs23.service.PlayerService;
import ch.uzh.ifi.hase.soprafs23.service.TeamService;
import ch.uzh.ifi.hase.soprafs23.websocket.dto.PlayerDTO;
import ch.uzh.ifi.hase.soprafs23.websocket.dto.PlayerJoinDTO;
import ch.uzh.ifi.hase.soprafs23.websocket.mapper.DTOMapperWebsocket;

@RestController
public class LobbyController {

    private final Logger log = LoggerFactory.getLogger(LobbyManagement.class);

    private final LobbyManagement lobbyManager;
    private final TeamService teamService;
    private final PlayerService playerService;
    private final MinigameService minigameService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    LobbyController(LobbyManagement lobbyManager, TeamService teamService, PlayerService playerService,
            MinigameService minigameService) {
        this.lobbyManager = lobbyManager;
        this.teamService = teamService;
        this.playerService = playerService;
        this.minigameService = minigameService;
    }

    @PostMapping("/lobbies")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyGetDTO createLobby(@RequestBody LobbyPostDTO lobbyPostDTO) {
        // convert API user to internal representation
        Lobby lobbyInput = DTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(lobbyPostDTO);
        
        // create user
        Lobby createdLobby = lobbyManager.createLobby(lobbyInput);

        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(createdLobby);
    }

    @GetMapping("/lobbies/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO getLobby(@PathVariable long lobbyId) {
        Lobby lobby = lobbyManager.getLobby(lobbyId);
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby);
    }

    @GetMapping("/lobbies/{lobbyId}/minigame")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public MinigameGetDTO getMinigame(@PathVariable long lobbyId) {

        Minigame nextMinigame = lobbyManager.getMinigame(lobbyId);
        return DTOMapper.INSTANCE.convertEntityToMinigameGetDTO(nextMinigame);
    }

    @PutMapping("/lobbies/{lobbyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void startGame(@PathVariable long lobbyId){
        lobbyManager.ableToStart(lobbyId);
        

        //Minigame nextMinigame = minigameService.createMinigame(type);
        lobbyManager.addUpcommingMinigame(lobbyId);
    }

    @PutMapping("/lobbies/{lobbyId}/minigame")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateScore(@PathVariable long lobbyId, WinnerTeamPutDTO winnerTeamPutDTO){
        //instead of String winnerTeam put the winner TeamDTO and get score of other via total minigame score
        Team winnerTeamInput = DTOMapper.INSTANCE.convertWinnerTeamPutDTOToEntity(winnerTeamPutDTO);


        //updateScore
        lobbyManager.finishedMinigameUpdate(lobbyId, winnerTeamInput);


        //create next minigame

        lobbyManager.addUpcommingMinigame(lobbyId);

    }

    @GetMapping("/lobbies/{lobbyId}/scores")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO getScores(@PathVariable long lobbyId) {
        Lobby lobby = lobbyManager.getLobby(lobbyId);
        return DTOMapper.INSTANCE.convertEntityToScoresGetDTO(lobby);
    }

    @GetMapping("/lobbies/{lobbyId}/winner")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public TeamGetDTO getWinner(@PathVariable long lobbyId) {
        Team team = lobbyManager.getWinner(lobbyId);
        return DTOMapper.INSTANCE.convertEntityToTeamGetDTO(team);
    }






    

    // TODO when adding settings menu: POST method to create the list of chosen
    // minigames (currently also handled in POST lobby)

    // belongs to PUT endpoint of team creation

    // Team team1 = teamService.createTeam(new Team());
    // Team team2 = teamService.createTeam(new Team());
    // team1.addPlayer(playerService.createPLayer(new Player("bob")));
    // team2.addPlayer(playerService.createPLayer(new Player("alice")));
    // List<Team> teams = Arrays.asList(team1, team2);
    // lobbyInput.setTeams(teams);

    @MessageMapping("/lobbies/{inviteCode}")
    @SendToUser("/queue/join")
    public PlayerDTO playerJoin(@DestinationVariable int inviteCode, PlayerJoinDTO player) {
        Player playerToCreate = DTOMapperWebsocket.INSTANCE.convertPlayerJoinDTOtoEntity(player);
        lobbyManager.ableToJoin(inviteCode, playerToCreate);


        Player createdPlayer = playerService.createPlayer(playerToCreate);
        Lobby joinedLobby = lobbyManager.getLobby(inviteCode);
        lobbyManager.addToUnassignedPlayers(joinedLobby.getId(), createdPlayer);
        PlayerDTO createdPlayerDTO = DTOMapperWebsocket.INSTANCE.convertEntityToPlayerDTO(createdPlayer);
        createdPlayerDTO.setAvatar(player.getAvatar());
        // Get the session ID of the user who sent the message
        // String sessionId = headerAccessor.getSessionId();
        // log.warn("Session Id: {}", sessionId);
        // // Send a message to the user's queue
        messagingTemplate.convertAndSend(String.format("/queue/lobbies/%d", joinedLobby.getId()), createdPlayerDTO);
        return createdPlayerDTO;

    }

    @MessageMapping("/lobbies/{lobbyId}/assign")
    public void assignPlayer(@DestinationVariable long lobbyId, long playerId, long teamId) {


        Player player = playerService.getPlayer(playerId);
        lobbyManager.removeFromUnassignedPlayers(lobbyId, player);
        teamService.addPlayer(teamId, player);
    }

    @MessageMapping("/lobbies/{lobbyId}/unassign")
    public void unassignPlayer(@DestinationVariable long lobbyId, long playerId, long teamId) {

        Player player = playerService.getPlayer(playerId);
        teamService.removePlayer(teamId, player);
        lobbyManager.addToUnassignedPlayers(lobbyId, player);
    }






}
