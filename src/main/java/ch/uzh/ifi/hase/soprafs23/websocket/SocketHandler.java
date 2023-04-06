package ch.uzh.ifi.hase.soprafs23.websocket;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.uzh.ifi.hase.soprafs23.websocket.dto.SignalDTO;

@Component
public class SocketHandler extends TextWebSocketHandler {
    List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    ConcurrentHashMap<String, WebSocketSession> sessionMap = new ConcurrentHashMap<String, WebSocketSession>();
    final ObjectMapper mapper = new ObjectMapper();
    private final Logger log = LoggerFactory.getLogger(SocketHandler.class);

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws InterruptedException, IOException {

        final String payload = message.getPayload();
        SignalDTO signal = mapper.readValue(payload, SignalDTO.class);

        switch (signal.getType()) {
            case JOIN:
                log.debug("Got join signal: %s", signal);
                sessionMap.put(signal.getSenderId(), session);
                break;
            case OFFER:
                log.debug("Got offer signal: %s", signal);
                sessionMap.get(signal.getRecipentId()).sendMessage(new TextMessage(mapper.writeValueAsString(signal)));
                break;
            case ANSWER:
                log.debug("Got answer signal: %s", signal);
                sessionMap.get(signal.getRecipentId()).sendMessage(new TextMessage(mapper.writeValueAsString(signal)));
                break;
            case ICE:
                sessionMap.get(signal.getRecipentId()).sendMessage(new TextMessage(mapper.writeValueAsString(signal)));
                break;
            default:
                break;
        }

        for (WebSocketSession webSocketSession : sessions) {
            if (webSocketSession.isOpen() && !session.getId().equals(webSocketSession.getId())) {
                webSocketSession.sendMessage(message);
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.debug("New websocket session established: %s", session);

        sessions.add(session);
        super.afterConnectionEstablished(session);
    }

    @Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		sessions.remove(session);
		super.afterConnectionClosed(session, status);	}
}
