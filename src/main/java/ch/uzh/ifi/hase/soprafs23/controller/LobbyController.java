package ch.uzh.ifi.hase.soprafs23.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Team;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyNamesPutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
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
                Lobby createdLobby = lobbyManager.createLobby();
                return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(createdLobby);
        }

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
         * @change checks if condition to start met & updates team names
         */
        @PutMapping("/lobbies/{lobbyId}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void startGame(@PathVariable long lobbyId, @RequestBody LobbyNamesPutDTO lobbyNamesPutDTO) {
                lobbyManager.ableToStart(lobbyId);
                List<Team> teams = DTOMapper.INSTANCE.convertLobbyNamesPutDTOtoEntity(lobbyNamesPutDTO).getTeams();
                teamService.updateNames(teams);
        }
    }


