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
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Minigame;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Team;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GameOverGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.MinigameGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.ScoresGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.TeamGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.WinnerTeamPutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.LobbyManagement;
import ch.uzh.ifi.hase.soprafs23.service.MinigameService;
import ch.uzh.ifi.hase.soprafs23.service.PlayerService;
import ch.uzh.ifi.hase.soprafs23.service.TeamService;
import ch.uzh.ifi.hase.soprafs23.websocket.dto.PlayerAssignTeamDTO;
import ch.uzh.ifi.hase.soprafs23.websocket.dto.PlayerDTO;
import ch.uzh.ifi.hase.soprafs23.websocket.dto.PlayerJoinDTO;
import ch.uzh.ifi.hase.soprafs23.websocket.dto.PlayerReassignTeamDTO;
import ch.uzh.ifi.hase.soprafs23.websocket.mapper.DTOMapperWebsocket;

@RestController
public class LobbyController {

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


    LobbyController(LobbyManagement lobbyManager, TeamService teamService, PlayerService playerService,
            MinigameService minigameService) {
        this.lobbyManager = lobbyManager;
        this.teamService = teamService;
        this.playerService = playerService;
        this.minigameService = minigameService;
    }

    /**
     * @input winningScore
    */
    @PostMapping("/lobbies")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyGetDTO createLobby(@RequestBody LobbyPostDTO lobbyPostDTO) {
        // convert API user to internal representation
        Lobby lobbyInput = DTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(lobbyPostDTO);
        
        // create user
        Lobby createdLobby = lobbyManager.createLobby(lobbyInput);

        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(createdLobby);
    }

    /**
     * @return lobby; format: id, inviteCode, winningScore, teams, unassignedPlayers
    */
    @GetMapping("/lobbies/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO getLobby(@PathVariable long lobbyId) {
        Lobby lobby = lobbyManager.getLobby(lobbyId);
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby);
    }

    /**
     * @return minigame; format: description, scoreToGain, team1Player, team2Player, type
    */
    @GetMapping("/lobbies/{lobbyId}/minigame")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public MinigameGetDTO getMinigame(@PathVariable long lobbyId) {

        Minigame nextMinigame = lobbyManager.getMinigame(lobbyId);
        return DTOMapper.INSTANCE.convertEntityToMinigameGetDTO(nextMinigame);
    }

    /**
     * @change checks if condition met & creates + adds first minigame
    */
    @PutMapping("/lobbies/{lobbyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void startGame(@PathVariable long lobbyId){
        lobbyManager.ableToStart(lobbyId);
        

        //Minigame nextMinigame = minigameService.createMinigame(type);
        lobbyManager.addUpcommingMinigame(lobbyId);
    }

    /**
     * @input winner team of minigame; format: score, color, name
     * @change updates score of teams, add winnerName to minigame, update roundsPlayed of players.
     * Creates and adds next Minigame & checks if finished.
    */
    @PutMapping("/lobbies/{lobbyId}/minigame")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateScore(@PathVariable long lobbyId, @RequestBody WinnerTeamPutDTO winnerTeamPutDTO){
        //instead of String winnerTeam put the winner TeamDTO and get score of other via total minigame score
        Team winnerTeamInput = DTOMapper.INSTANCE.convertWinnerTeamPutDTOToEntity(winnerTeamPutDTO);

        //updateScore
        lobbyManager.finishedMinigameUpdate(lobbyId, winnerTeamInput);


        //create next minigame
        lobbyManager.addUpcommingMinigame(lobbyId);

        //check if finished
        lobbyManager.isFinished(lobbyId);

    }

    /**
     * @return winning score + both teams (id, score, name, color)
    */
    @GetMapping("/lobbies/{lobbyId}/scores")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ScoresGetDTO getScores(@PathVariable long lobbyId) {
        Lobby lobby = lobbyManager.getLobby(lobbyId);
        return DTOMapper.INSTANCE.convertEntityToScoresGetDTO(lobby);
    }

    /**
     * @return boolean; true if a team has achieved the winning score.
    */
    @GetMapping("/lobbies/{lobbyId}/gameover")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameOverGetDTO getIsFinished(@PathVariable long lobbyId){
        Lobby lobby = lobbyManager.getLobby(lobbyId);
        return DTOMapper.INSTANCE.convertEntityToGameOverGetDTO(lobby);
    }

    /**
     * @return winnerTeam (id, score, name, color)
    */
    @GetMapping("/lobbies/{lobbyId}/winner")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public TeamGetDTO getWinner(@PathVariable long lobbyId) {
        Team team = lobbyManager.getWinner(lobbyId);
        return DTOMapper.INSTANCE.convertEntityToTeamGetDTO(team);
    }
}
