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
        WebSocketSession sendTo;
        switch (signal.getType()) {
            case JOIN:
                log.debug("Got join signal: %s", signal);
                if(sessionMap.get(signal.getSenderId()) == null){
                    sessionMap.put(signal.getSenderId(), session);
                }
                
                break;
            case OFFER:
                sendTo = sessionMap.get(signal.getRecipentId());
                log.debug("Got offer signal: %s", signal);
                if (sendTo == null) {
                    return;
                }
                sendTo.sendMessage(new TextMessage(mapper.writeValueAsString(signal)));
                break;
            case ANSWER:
                sendTo = sessionMap.get(signal.getRecipentId());
                log.debug("Got answer signal: %s", signal);
                if (sendTo == null) {
                    return;
                }
                sendTo.sendMessage(new TextMessage(mapper.writeValueAsString(signal)));
                break;
            case ICE:
                sendTo = sessionMap.get(signal.getRecipentId());
                if (sendTo == null) {
                    return;
                }
                sendTo.sendMessage(new TextMessage(mapper.writeValueAsString(signal)));
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
        // remove session from sessionMap
        for (String key : sessionMap.keySet()) {
            if (sessionMap.get(key).getId() == session.getId()) {
                sessionMap.remove(key);
                break;
            }
        }
        super.afterConnectionClosed(session, status);
    }
}
