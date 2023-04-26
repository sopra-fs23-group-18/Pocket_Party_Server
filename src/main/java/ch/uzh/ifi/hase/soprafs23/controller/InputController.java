package ch.uzh.ifi.hase.soprafs23.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import ch.uzh.ifi.hase.soprafs23.websocket.dto.GameStartStopDTO;
import ch.uzh.ifi.hase.soprafs23.websocket.dto.InputDTO;

@Controller
public class InputController {
    private final Logger log = LoggerFactory.getLogger(InputController.class);


    @MessageMapping("/lobbies/{lobbyId}/players/{playerId}/input")
    @SendTo("/topic/lobbies/{lobbyId}/players/{playerId}/input")
    public InputDTO input(@DestinationVariable long lobbyId, @DestinationVariable long playerId, InputDTO inputDTO) {
        return inputDTO;
    }

    @MessageMapping("/lobbies/{lobbyId}/players/{playerId}/signal")
    @SendTo("/topic/players/{playerId}/signal")
    public GameStartStopDTO startStopGame(@DestinationVariable long lobbyId, @DestinationVariable long playerId, GameStartStopDTO startStopDTO) {
        log.warn("GOT CALLED");
        return startStopDTO;
    }
}
