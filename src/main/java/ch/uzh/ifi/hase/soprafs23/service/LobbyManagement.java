package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.TeamType;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Team;

@Service
@Transactional
public class LobbyManagement {

  private final Logger log = LoggerFactory.getLogger(LobbyManagement.class);

  @Autowired
  private final LobbyRepository lobbyRepository;

  @PersistenceContext
    private EntityManager entityManager;
  @Autowired
  private final TeamService teamService;
  
  private Random randomizer = new Random();

  public LobbyManagement(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository, TeamService teamService) {
    this.lobbyRepository = lobbyRepository;
    this.teamService = teamService;
  }

  public Lobby createLobby() {
      Lobby newLobby = new Lobby();
      int inviteCode = randomizer.nextInt(900000) + 100000;
      while (lobbyRepository.findByInviteCode(inviteCode) != null){
        inviteCode = randomizer.nextInt(900000) + 100000;
      }
      newLobby.setInviteCode(inviteCode);

      List<Team> teams = new ArrayList<Team>();
      teams.add(teamService.createTeam(newLobby, "Team 1", TeamType.TEAM_ONE));
      teams.add(teamService.createTeam(newLobby, "Team 2", TeamType.TEAM_TWO));
      newLobby.setTeams(teams);

      newLobby = lobbyRepository.save(newLobby);
      lobbyRepository.flush();
      return newLobby;
  }

  public Lobby getLobby(Long lobbyId) {
    Lobby lobby = lobbyRepository.findById(lobbyId).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The lobby with the given Id does not exist!"));
    return lobby;
  }

  public Lobby getLobby(int inviteCode) {
    Lobby lobby = lobbyRepository.findByInviteCode(inviteCode);
    if (lobby == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The lobby with the given Invite Code does not exist!");
    }
    return lobby;
  }

  public Lobby getLobby(Game game) {
    Lobby lobby = lobbyRepository.findByGame(game);
    if (lobby == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The lobby with the given game does not exist!");
    }
    return lobby;
  }

  public Team getLeadingTeam(Game game) {
    Lobby lobby = getLobby(game);

    if (lobby.getTeams().isEmpty()){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "List of teams was empty");
    }

    Team team = Collections.max(lobby.getTeams(), new Comparator<Team>() {
      public int compare(Team team1, Team team2) {
        return team1.getScore() - team2.getScore();
      }
    });
    return team;
  }

  public void addToUnassignedPlayers(Long lobbyId, Player newPlayer) {
    Lobby lobby = getLobby(lobbyId);
    if (lobby == null || newPlayer == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby or Player is empty!");
    }

    lobby.getUnassignedPlayers().size();
    lobby.addToUnassignedPlayers(newPlayer);
    lobbyRepository.save(lobby);
    lobbyRepository.flush();
  }

  public void removeFromUnassignedPlayers(Long lobbyId, Player remPlayer) {
    Lobby lobby = getLobby(lobbyId);
    if (lobby == null || remPlayer == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby or Player is empty!");
    }
    List<Player> players = lobby.getUnassignedPlayers();
    players.remove(remPlayer);
    lobbyRepository.save(lobby);
    lobbyRepository.flush();
  }

  public void ableToJoin(int inviteCode, Player playerToCreate) {
    Lobby lobby = getLobby(inviteCode);
    int cnt = 0;
    for (Player p : lobby.getUnassignedPlayers()) {
      if (p.getNickname().toUpperCase().equals(playerToCreate.getNickname().toUpperCase())) {
        throw new ResponseStatusException(HttpStatus.CONFLICT,
            "Player with this Nickname already exists in this lobby!");
      }
      cnt += 1;
    }
    for (Team t : lobby.getTeams()) {
      for (Player p : t.getPlayers()) {
        if (p.getNickname().toUpperCase().equals(playerToCreate.getNickname().toUpperCase())) {
          throw new ResponseStatusException(HttpStatus.CONFLICT,
              "Player with this Nickname already exists in this lobby!");
        }
        cnt += 1;
      }
    }
    if (cnt == 8) {
      throw new ResponseStatusException(HttpStatus.LOCKED, "Player limit for lobby was reached!");
    }
  }

  public void ableToStart(Long lobbyId) {
    Lobby lobby = getLobby(lobbyId);
    List<Team> teams = lobby.getTeams();
    if (teams.size() < 2){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The lobby doesn't have 2 teams");
    }
    int team1Size = teams.get(0).getPlayers().size();
    int team2Size = teams.get(1).getPlayers().size();
    if (lobby.getUnassignedPlayers().size() == 0) {
      if (team1Size > 0 && team2Size > 0) {
        if (Math.abs(team1Size - team2Size) < 2){
          return;
        }
        throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Please split the players more evenly!");
      }
      throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED,
          "There are not enough players in the teams to start!");
    }
    throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "There players that are not assigned yet!");
  }

  public void addGame(Game game, Long lobbyId){
    Lobby lobby = getLobby(lobbyId);
    if (game == null){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game was not created successfully");
    }
    lobby.setGame(game);
    if (lobby.getGame() != null){
      for (Team t : lobby.getTeams()){
        t.setScore(0);
        for (Player p : t.getPlayers()){
          p.setRoundsPlayed(0);
        }
      }
    }
  }

  public boolean isGameSet(long lobbyId){
    Lobby lobby = getLobby(lobbyId);
    return lobby.getGame() == null ? false : true;
  }

  public void removePlayer(Player player, long lobbyId){
    Lobby lobby = getLobby(lobbyId);

    List<Player> unassigned = lobby.getUnassignedPlayers();
    List<Team> teams = lobby.getTeams();

    for(Team team : teams){
      team.getPlayers().remove(player);
    }
    unassigned.remove(player);

    lobbyRepository.saveAndFlush(lobby);
  }
}
