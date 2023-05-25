package ch.uzh.ifi.hase.soprafs23.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.service.LobbyManagement;
import ch.uzh.ifi.hase.soprafs23.service.PlayerService;
import ch.uzh.ifi.hase.soprafs23.service.TeamService;
import ch.uzh.ifi.hase.soprafs23.websocket.dto.PlayerAssignTeamDTO;
import ch.uzh.ifi.hase.soprafs23.websocket.dto.PlayerDTO;
import ch.uzh.ifi.hase.soprafs23.websocket.dto.PlayerJoinDTO;
import ch.uzh.ifi.hase.soprafs23.websocket.dto.PlayerReassignTeamDTO;
import ch.uzh.ifi.hase.soprafs23.websocket.dto.PlayerRejoinDTO;
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
    private SimpMessagingTemplate messagingTemplate;


    WebsocketController(LobbyManagement lobbyManager, TeamService teamService, PlayerService playerService) {
        this.lobbyManager = lobbyManager;
        this.teamService = teamService;
        this.playerService = playerService;
    }

    @MessageMapping("/lobbies/{inviteCode}")
    @SendToUser("/queue/join")
    public PlayerDTO playerJoin(@DestinationVariable int inviteCode, PlayerJoinDTO player, SimpMessageHeaderAccessor headerAccessor) {
        Player playerToCreate = DTOMapperWebsocket.INSTANCE.convertPlayerJoinDTOtoEntity(player);
        Player createdPlayer = lobbyManager.createPlayer(inviteCode, playerToCreate);

        // lobbyManager.addToUnassignedPlayers(joinedLobby.getId(), createdPlayer);

        Lobby joinedLobby = lobbyManager.getLobby(inviteCode);

        PlayerDTO createdPlayerDTO = DTOMapperWebsocket.INSTANCE.convertEntityToPlayerDTO(createdPlayer);
        createdPlayerDTO.setAvatar(player.getAvatar());
        
        // Get the session ID of the user who sent the message
        String sessionId = headerAccessor.getSessionId();
        playerService.setCurrentSessionId(createdPlayer.getId(), sessionId);

        log.warn("Session Id: {}", sessionId);
        // // Send a message to the user's queue
        messagingTemplate.convertAndSend(String.format("/queue/lobbies/%d", joinedLobby.getId()), createdPlayerDTO);
        createdPlayerDTO.setLobbyId(joinedLobby.getId());
        return createdPlayerDTO;

    }

    @MessageMapping("/lobbies/{lobbyId}/assign")
    public void assignPlayer(@DestinationVariable long lobbyId, PlayerAssignTeamDTO assignData) {
        lobbyManager.assignPlayer(lobbyId, assignData.getPlayerId(), assignData.getTeam());
    }

    @MessageMapping("/lobbies/{lobbyId}/unassign")
    public void unassignPlayer(@DestinationVariable long lobbyId, PlayerAssignTeamDTO unassignData) {
        lobbyManager.unassignPlayer(lobbyId, unassignData.getPlayerId(), unassignData.getTeam());
    }


    @MessageMapping("/lobbies/{lobbyId}/reassign")
    public void reassignPlayer(@DestinationVariable long lobbyId, PlayerReassignTeamDTO reassignTeamDTO) {
        lobbyManager.reassignPlayer(lobbyId, reassignTeamDTO.getPlayerId(), reassignTeamDTO.getFrom(), reassignTeamDTO.getTo());
    }

    @MessageMapping("/lobbies/{lobbyId}/rejoin")
    @SendToUser("/queue/join")
    public PlayerDTO rejoinLobby(@DestinationVariable long lobbyId, PlayerRejoinDTO playerDTO, SimpMessageHeaderAccessor headerAccessor){
        Player player = lobbyManager.rejoinPlayer(playerDTO.getId(), headerAccessor.getSessionId());
        PlayerDTO playerDTOReturned = DTOMapperWebsocket.INSTANCE.convertEntityToPlayerDTO(player);
        playerDTOReturned.setAvatar(playerDTO.getAvatar());
        playerDTOReturned.setLobbyId(player.getLobby().getId());

        messagingTemplate.convertAndSend(String.format("/queue/lobbies/%d", player.getLobby().getId()), playerDTOReturned);
        return playerDTOReturned;
    }
    // @MessageMapping("/lobbies/{lobbyId}/voting")
    // @SendToUser("")
    // public void votingChoice(@DestinationVariable long lobbyId) {
        
    // }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }
}
