package ch.uzh.ifi.hase.soprafs23.websocket;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.service.LobbyManagement;
import ch.uzh.ifi.hase.soprafs23.websocket.dto.PlayerDTO;
import ch.uzh.ifi.hase.soprafs23.websocket.mapper.DTOMapperWebsocket;

public class WebSocketEventListener implements ApplicationListener<SessionDisconnectEvent> {
    private final Logger log = LoggerFactory.getLogger(WebSocketEventListener.class);
    private final LobbyManagement lobbyManagement;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public WebSocketEventListener(LobbyManagement lobbyManagement) {
        this.lobbyManagement = lobbyManagement;
    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        Player player = lobbyManagement.disconnect(sessionId);

        PlayerDTO playerDTO = DTOMapperWebsocket.INSTANCE.convertEntityToPlayerDTO(player);
        messagingTemplate.convertAndSend(String.format("/queue/lobbies/%d/leave", player.getLobby().getId()), playerDTO);

        // Perform actions upon session disconnect
        log.warn("Session disconnected: " + sessionId);
    }
}

