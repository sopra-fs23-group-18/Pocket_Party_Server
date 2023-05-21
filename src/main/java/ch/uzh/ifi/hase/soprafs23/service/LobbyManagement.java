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

        // List<Player> unassignedPlayers = new ArrayList<Player>();
        // newLobby.setUnassignedPlayers(unassignedPlayers);

        List<Team> teams = new ArrayList<Team>();
        teams.add(teamService.createTeam(newLobby, "Team Red"));
        teams.add(teamService.createTeam(newLobby, "Team Blue"));

        // Team team1 = new Team();
        // team1.setLobby(newLobby);
        // //team1.setColor(TeamType.RED);
        // team1.setName("Team Red");

        // Team team2 = new Team();
        // team2.setLobby(newLobby);
        // //team2.setColor(TeamType.BLUE);
        // team2.setName("Team Blue");

        // teams.add(team1);
        // teams.add(team2);
        newLobby.setTeams(teams);

        newLobby = lobbyRepository.save(newLobby);
        lobbyRepository.flush();
        return newLobby;

    // log.debug("Created Information for User: {}", createdLobby);
    // return createdLobby;
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

  

  // @Transactional
  // public void finishedMinigameUpdate(Long lobbyId, Team winnerTeamInput) {
  //   Lobby lobby = getLobby(lobbyId);

  //   Minigame playedMinigame = gameService.updateMinigame(lobby, winnerTeamInput);

  //   // update roundsPlayed of players
  //   playerService.updatePlayers(playedMinigame.getTeam1Players());
  //   playerService.updatePlayers(playedMinigame.getTeam2Players());

  //   // update score of teams
  //   teamService.updateScore(lobby, winnerTeamInput.getColor(), winnerTeamInput.getScore());

  //   List<Team> teams = lobby.getTeams();
  //   if(teams.get(0).getColor().ordinal() != winnerTeamInput.getColor().ordinal()){
  //     int score = playedMinigame.getScoreToGain() - winnerTeamInput.getScore();
  //     teamService.updateScore(lobby, teams.get(0).getColor(), score);
  //   }else{
  //     int score = playedMinigame.getScoreToGain() - winnerTeamInput.getScore();
  //     teamService.updateScore(lobby, teams.get(1).getColor(), score);
  //   }
  //   lobbyRepository.save(lobby);
  //   lobbyRepository.flush();   
  // }

  public Team getLeadingTeam(Game game) {
    Lobby lobby = getLobby(game);

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

  public boolean ableToJoin(int inviteCode, Player playerToCreate) {
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

    return true;
  }

  public void ableToStart(Long lobbyId) {
    Lobby lobby = getLobby(lobbyId);
    List<Team> teams = lobby.getTeams();
    if (lobby.getUnassignedPlayers().size() == 0) {
      if (teams.get(0).getPlayers().size() > 0 && teams.get(1).getPlayers().size() > 0) {
        return;
      }
      throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED,
          "There are not enough players in the teams to start!");
    }
    throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "There players that are not assigned yet!");
  }

  //new

  public void addGame(Game game, Long lobbyId){
    Lobby lobby = getLobby(lobbyId);
    lobby.setGame(game);
    if (lobby.getGame() != null){
      for (Team t : lobby.getTeams()){
        t.setScore(0);
        for (Player p : t.getPlayers()){
          p.setRoundsPlayed(0);
        }
      }
    }
    
    // lobbyRepository.save(lobby);
    // lobbyRepository.flush();
  }

  public int lowestPlayerAmount(Game game){
    Lobby lobby = getLobby(game);
    int amount = -1;
    for (Team t : lobby.getTeams()){
      if (amount == -1){
        amount = t.getPlayers().size();
      }
      if (t.getPlayers().size() < amount){
        amount = t.getPlayers().size();
      }
    }
    return amount;
  }

}
