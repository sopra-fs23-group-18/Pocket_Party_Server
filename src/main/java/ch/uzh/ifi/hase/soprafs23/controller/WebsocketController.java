package ch.uzh.ifi.hase.soprafs23.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.service.LobbyManagement;
import ch.uzh.ifi.hase.soprafs23.service.MinigameService;
import ch.uzh.ifi.hase.soprafs23.service.PlayerService;
import ch.uzh.ifi.hase.soprafs23.service.TeamService;
import ch.uzh.ifi.hase.soprafs23.websocket.dto.PlayerAssignTeamDTO;
import ch.uzh.ifi.hase.soprafs23.websocket.dto.PlayerDTO;
import ch.uzh.ifi.hase.soprafs23.websocket.dto.PlayerJoinDTO;
import ch.uzh.ifi.hase.soprafs23.websocket.dto.PlayerReassignTeamDTO;
import ch.uzh.ifi.hase.soprafs23.websocket.mapper.DTOMapperWebsocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class WebsocketController {
    
    private final Logger log = LoggerFactory.getLogger(LobbyManagement.class);

    @Autowired
    private final LobbyManagement lobbyManager;

    @Autowired
    private final TeamService teamService;

    @Autowired
    private final PlayerService playerService;

    @Autowired
    private final MinigameService minigameService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    WebsocketController(LobbyManagement lobbyManager, TeamService teamService, PlayerService playerService,
            MinigameService minigameService) {
        this.lobbyManager = lobbyManager;
        this.teamService = teamService;
        this.playerService = playerService;
        this.minigameService = minigameService;
    }

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
        createdPlayerDTO.setLobbyId(joinedLobby.getId());
        return createdPlayerDTO;

    }

    @MessageMapping("/lobbies/{lobbyId}/assign")
    public void assignPlayer(@DestinationVariable long lobbyId, PlayerAssignTeamDTO assignData) {
        Player player = playerService.getPlayer(assignData.getPlayerId());
        Lobby lobby = lobbyManager.getLobby(lobbyId);
        lobbyManager.removeFromUnassignedPlayers(lobbyId, player);
        teamService.addPlayer(lobby, assignData.getTeam(), player);
    }

    @MessageMapping("/lobbies/{lobbyId}/unassign")
    public void unassignPlayer(@DestinationVariable long lobbyId, PlayerAssignTeamDTO unassignData) {

        Player player = playerService.getPlayer(unassignData.getPlayerId());
        Lobby lobby = lobbyManager.getLobby(lobbyId);
        teamService.removePlayer(lobby, unassignData.getTeam(), player);
        lobbyManager.addToUnassignedPlayers(lobbyId, player);
    }


    @MessageMapping("/lobbies/{lobbyId}/reassign")
    public void reassignPlayer(@DestinationVariable long lobbyId, PlayerReassignTeamDTO reassignTeamDTO) {
        Player player = playerService.getPlayer(reassignTeamDTO.getPlayerId());
        Lobby lobby = lobbyManager.getLobby(lobbyId);
        teamService.removePlayer(lobby, reassignTeamDTO.getFrom(), player);
        teamService.addPlayer(lobby, reassignTeamDTO.getTo(), player);
    }
}