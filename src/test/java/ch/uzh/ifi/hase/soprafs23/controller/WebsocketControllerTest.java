package ch.uzh.ifi.hase.soprafs23.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.when;
import static org.mockito.BDDMockito.doNothing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;
import ch.uzh.ifi.hase.soprafs23.constant.TeamType;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Team;
import ch.uzh.ifi.hase.soprafs23.entity.minigame.Minigame;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.MinigameWinnerTeamPutDTO;
import ch.uzh.ifi.hase.soprafs23.service.LobbyManagement;
import ch.uzh.ifi.hase.soprafs23.service.MinigameService;
import ch.uzh.ifi.hase.soprafs23.service.PlayerService;
import ch.uzh.ifi.hase.soprafs23.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import static java.util.concurrent.TimeUnit.SECONDS;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebsocketControllerTest {
    
    @MockBean
    private LobbyManagement lobbyManager;

    @MockBean
    private TeamService teamService;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private SimpMessagingTemplate messagingTemplate;

    @LocalServerPort
    private Integer port;

    private WebSocketStompClient webSocketStompClient;

    private StompSession stompSession;


    @BeforeEach
    void setup() {
        this.webSocketStompClient = new WebSocketStompClient (new SockJsClient(
            List.of(new WebSocketTransport(new StandardWebSocketClient()))));
        this.webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());
        
    }

    // @Test
    // public void connectToSocket_Test() throws Exception {
    //     stompSession = webSocketStompClient
    //         .connect(getWsPath(), new StompSessionHandlerAdapter() {})
    //         .get(1, SECONDS);
    //     assertEquals(true,stompSession.isConnected());
    // }

    // @Test
    // public void first(){
        
    // }

    // private String getWsPath() {
    //     return "ws://localhost:" + port + "/game";
    // }






}
