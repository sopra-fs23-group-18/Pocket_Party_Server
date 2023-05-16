package ch.uzh.ifi.hase.soprafs23.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Team;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyNamesPutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.ScoresGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.TeamNamePutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.LobbyManagement;
import ch.uzh.ifi.hase.soprafs23.service.PlayerService;
import ch.uzh.ifi.hase.soprafs23.service.TeamService;


@RestController
public class LobbyController {

    private final Logger log = LoggerFactory.getLogger(LobbyManagement.class);

    @Autowired
    private final LobbyManagement lobbyManager;

    @Autowired
    private final PlayerService playerService;

    @Autowired
    private final TeamService teamService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    LobbyController(LobbyManagement lobbyManager, PlayerService playerService, TeamService teamService) {
        this.lobbyManager = lobbyManager;
        this.playerService = playerService;
        this.teamService = teamService;
    }

    /**
     * @input none
    */
    @PostMapping("/lobbies")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyGetDTO createLobby() {
                
        // create lobby
        Lobby createdLobby = lobbyManager.createLobby();

        // convert internal representation of lobby back to API
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(createdLobby);
    }

    //NOTE: create lobbyy and return it (in here a game instance also gets created that has empty values at first)
    //this empty game is then updated when the settings are set via a put method for the game

    

    /**
     * @return lobby; format: id, inviteCode, teams, unassignedPlayers
    */
    @GetMapping("/lobbies/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO getLobby(@PathVariable long lobbyId) {
        Lobby lobby = lobbyManager.getLobby(lobbyId);
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby);
    }

    
    /**
     * @change checks if condition met & creates + adds first minigame
    */
    @PutMapping("/lobbies/{lobbyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void startGame(@PathVariable long lobbyId, @RequestBody LobbyNamesPutDTO lobbyNamesPutDTO){
        lobbyManager.ableToStart(lobbyId);
        //teamname update
        Lobby lobby = lobbyManager.getLobby(lobbyId);
        // List<Team> teams = new ArrayList<Team>();
        // for (TeamNamePutDTO teamPutDTO : teamNamesPutDTO){
        //     Team team = DTOMapper.INSTANCE.convertTeamNamePutDTOToEntity(teamPutDTO);
        //     teams.add(team);
        // }

        List<Team> teams = DTOMapper.INSTANCE.convertLobbyNamesPutDTOtoEntity(lobbyNamesPutDTO).getTeams();
        teamService.updateNames(lobby, teams);
        

        //TODO:
        //maybe do something in lobby to set it to be fixed or similar
    }

    // /**
    //  * @input winner team of minigame; format: score, color, name
    //  * @change updates score of teams, add winnerName to minigame, update roundsPlayed of players.
    //  * Creates and adds next Minigame & checks if finished.
    // */
    // @PutMapping("/lobbies/{lobbyId}/minigame")
    // @ResponseStatus(HttpStatus.NO_CONTENT)
    // public void updateScore(@PathVariable long lobbyId, @RequestBody WinnerTeamPutDTO winnerTeamPutDTO){
    //     //instead of String winnerTeam put the winner TeamDTO and get score of other via total minigame score
    //     Team winnerTeamInput = DTOMapper.INSTANCE.convertWinnerTeamPutDTOToEntity(winnerTeamPutDTO);

    //     //updateScore
    //     lobbyManager.finishedMinigameUpdate(lobbyId, winnerTeamInput);

    //     Lobby lobby = lobbyManager.getLobby(lobbyId);
    // }


    // /**
    //  * @return winnerTeam (id, score, name, color)
    // */
    // @GetMapping("/lobbies/{lobbyId}/winner")
    // @ResponseStatus(HttpStatus.OK)
    // @ResponseBody
    // public TeamGetDTO getWinner(@PathVariable long lobbyId) {
    //     Lobby lobby = lobbyManager.getLobby(lobbyId);
    //     Team team = gameService.getWinner(lobby);
    //     return DTOMapper.INSTANCE.convertEntityToTeamGetDTO(team);
    // }

}
