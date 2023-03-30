package ch.uzh.ifi.hase.soprafs23.controller;

import java.util.ArrayList;
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
import ch.uzh.ifi.hase.soprafs23.entity.Team;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.MinigameGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.LobbyManagement;
import ch.uzh.ifi.hase.soprafs23.service.TeamService;

@RestController
public class LobbyController {

    private final LobbyManagement lobbyManager;
    private final TeamService teamService;
    
    LobbyController(LobbyManagement lobbyManager, TeamService teamService) {
        this.lobbyManager = lobbyManager;
        this.teamService = teamService;
    }

    @PostMapping("/lobbies")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyGetDTO createLobby(@RequestBody LobbyPostDTO lobbyPostDTO) {
        // convert API user to internal representation
        Lobby lobbyInput = DTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(lobbyPostDTO);

        List<Team> teams = new ArrayList<Team>();
        teams.add(teamService.createTeam(new Team()));
        teams.add(teamService.createTeam(new Team()));
        lobbyInput.setTeams(teams);
        // create user
        Lobby createdLobby = lobbyManager.createLobby(lobbyInput);
        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(createdLobby);
    }

    @GetMapping("/lobbies/{lobbyId}/minigame")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public MinigameGetDTO getMinigame(@PathVariable long lobbyId){
        
        Minigame nextMinigame = lobbyManager.getMinigame(lobbyId); 
        return DTOMapper.INSTANCE.convertEntityToMinigameGetDTO(nextMinigame);
    }


}
