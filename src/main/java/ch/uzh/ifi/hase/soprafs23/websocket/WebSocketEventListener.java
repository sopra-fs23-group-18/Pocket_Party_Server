package ch.uzh.ifi.hase.soprafs23.websocket;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import ch.uzh.ifi.hase.soprafs23.service.PlayerService;

public class WebSocketEventListener implements ApplicationListener<SessionDisconnectEvent> {
    private final Logger log = LoggerFactory.getLogger(WebSocketEventListener.class);
    private final PlayerService playerService;

    public WebSocketEventListener(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        
        playerService.disconnect(playerService.getPlayerBySession(sessionId).getId());
        // Perform actions upon session disconnect
        log.warn("Session disconnected: " + sessionId);
    }
}

