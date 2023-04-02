package ch.uzh.ifi.hase.soprafs23.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Minigame;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.TappingGame;
import ch.uzh.ifi.hase.soprafs23.entity.Team;
import ch.uzh.ifi.hase.soprafs23.entity.TimingGame;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.MinigameGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.LobbyManagement;
import ch.uzh.ifi.hase.soprafs23.service.MinigameService;
import ch.uzh.ifi.hase.soprafs23.service.PlayerService;
import ch.uzh.ifi.hase.soprafs23.service.TeamService;

@RestController
public class LobbyController {

    private final LobbyManagement lobbyManager;
    private final TeamService teamService;
    private final PlayerService playerService;
    private final MinigameService minigameService;
    
    LobbyController(LobbyManagement lobbyManager, TeamService teamService, PlayerService playerService, MinigameService minigameService) {
        this.lobbyManager = lobbyManager;
        this.teamService = teamService;
        this.playerService = playerService;
        this.minigameService = minigameService;  
    }
   

    @PostMapping("/lobbies")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyGetDTO createLobby(@RequestBody LobbyPostDTO lobbyPostDTO) {
        // convert API user to internal representation
        Lobby lobbyInput = DTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(lobbyPostDTO);

        List<Minigame> minigames = minigameService.chosenMinigames();
        lobbyInput.setMinigames(minigames);
        
        // create user
        Lobby createdLobby = lobbyManager.createLobby(lobbyInput);
        
        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(createdLobby);
    }

    @GetMapping("/lobbies/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO getLobby(@PathVariable long lobbyId) {
        Lobby lobby = lobbyManager.getLobby(lobbyId);
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby);
    }

    @GetMapping("/lobbies/{lobbyId}/minigame")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public MinigameGetDTO getMinigame(@PathVariable long lobbyId){
        
        Minigame nextMinigame = lobbyManager.getMinigame(lobbyId); 
        return DTOMapper.INSTANCE.convertEntityToMinigameGetDTO(nextMinigame);
    }


    //TODO when adding settings menu: POST method to create the list of chosen minigames (currently also handled in POST lobby)


    // belongs to PUT endpoint of team creation

        // Team team1 = teamService.createTeam(new Team());
        // Team team2 = teamService.createTeam(new Team());
        // team1.addPlayer(playerService.createPLayer(new Player("bob")));
        // team2.addPlayer(playerService.createPLayer(new Player("alice")));
        // List<Team> teams = Arrays.asList(team1, team2);
        // lobbyInput.setTeams(teams);
}
