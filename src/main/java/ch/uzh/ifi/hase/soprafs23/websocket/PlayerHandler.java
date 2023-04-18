package ch.uzh.ifi.hase.soprafs23.websocket;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.uzh.ifi.hase.soprafs23.websocket.dto.PlayerDTO;

@Component
public class PlayerHandler extends TextWebSocketHandler {
    final ObjectMapper mapper = new ObjectMapper();
    private final Logger log = LoggerFactory.getLogger(PlayerHandler.class);

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws InterruptedException, IOException {
        final String payload = message.getPayload();
        PlayerDTO player = mapper.readValue(payload, PlayerDTO.class);
        // TODO: add player to lobby
        log.debug("Got join player: %s", player);
        session.sendMessage(new TextMessage(mapper.writeValueAsString(player)));
    }

}
