package ch.uzh.ifi.hase.soprafs23.controller;

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

import ch.uzh.ifi.hase.soprafs23.constant.TeamType;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Minigame;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Team;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GameOverGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.MinigameGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.ScoresGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.TeamGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.WinnerTeamPutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.LobbyManagement;
import ch.uzh.ifi.hase.soprafs23.service.MinigameService;
import ch.uzh.ifi.hase.soprafs23.service.PlayerService;
import ch.uzh.ifi.hase.soprafs23.service.TeamService;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GamePostDTO;

@RestController
public class GameController {

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
    private final GameService gameService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    GameController(LobbyManagement lobbyManager, TeamService teamService, PlayerService playerService,
            MinigameService minigameService, GameService gameService) {
        this.lobbyManager = lobbyManager;
        this.teamService = teamService;
        this.playerService = playerService;
        this.minigameService = minigameService;
        this.gameService = gameService;
    }

    // /**
    //  * @input chosenMinigames
    // */
    // @PostMapping("/games")
    // @ResponseStatus(HttpStatus.CREATED)
    // @ResponseBody
    // public GameGetDTO createGame(@RequestBody GamePostDTO gamePostDTO) {
    //     // convert API user to internal representation
    //     Game game = DTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(gamePostDTO);
        
    //     // create user
    //     Lobby createdLobby = lobbyManager.createLobby(lobbyInput);

    //     // convert internal representation of user back to API
    //     return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(createdLobby);
    // }
}
