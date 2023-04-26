package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;
import ch.uzh.ifi.hase.soprafs23.constant.TeamType;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import javax.persistence.Lob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


import ch.uzh.ifi.hase.soprafs23.entity.Minigame;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Team;


@Service
@Transactional
public class LobbyManagement {
    
    private final Logger log = LoggerFactory.getLogger(LobbyManagement.class);

    @Autowired
    private final LobbyRepository lobbyRepository;
    @Autowired
    private final MinigameService minigameService;
    @Autowired
    private final TeamService teamService;
    @Autowired
    private final PlayerService playerService;



    private Random randomizer = new Random();
    
  
    public LobbyManagement(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository, MinigameService minigameService, TeamService teamService, PlayerService playerService) {
        this.lobbyRepository = lobbyRepository;
        this.minigameService = minigameService;
        this.teamService = teamService;
        this.playerService = playerService;
      }   

    public Lobby createLobby(Lobby newLobby) {
        if (newLobby.getWinningScore() < 0 || newLobby.getWinningScore() > 100000){
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lobby could not be created because the winningScore was invalid!");
        }
        int inviteCode = randomizer.nextInt(900000) + 100000;
        while (lobbyRepository.findByInviteCode(inviteCode) != null){
          inviteCode = randomizer.nextInt(900000) + 100000;
        }
        newLobby.setInviteCode(inviteCode);

        List<MinigameType> minigames = minigameService.chosenMinigames();
        newLobby.setMinigamesChoice(minigames);
        
        List<Player> unassignedPlayers = new ArrayList<Player>();
        newLobby.setUnassignedPlayers(unassignedPlayers);
        

        List<Team> teams = new ArrayList<Team>();
        Team team1 = new Team();
        team1.setLobby(newLobby);
        team1.setColor(TeamType.RED);
        team1.setName("Team Red");

        Team team2 = new Team();
        team2.setLobby(newLobby);
        team2.setColor(TeamType.BLUE);
        team2.setName("Team Blue");


        teams.add(team1);
        teams.add(team2);
        newLobby.setTeams(teams);

  
        newLobby = lobbyRepository.save(newLobby);
        lobbyRepository.flush();
        return newLobby;
    
        // log.debug("Created Information for User: {}", createdLobby);

        // //ResponseStatusException missing -> TODO: add 

        // return createdLobby;
      }
    
      public Lobby getLobby(Long lobbyId) {
        Lobby lobby = lobbyRepository.findById(lobbyId).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The lobby with the given Id does not exist!"));
        return lobby;
      }

      public Lobby getLobby(int inviteCode) {
        Lobby lobby = lobbyRepository.findByInviteCode(inviteCode);
        if (lobby == null){
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The lobby with the given Invite Code does not exist!");
        }
        return lobby;
      }

      public Minigame getMinigame(Long lobbyId){
        Lobby lobby = getLobby(lobbyId);
        Minigame minigame = lobby.getUpcomingMinigame();
        if (minigame == null){
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No upcomming Minigame was found!");
        }
        return minigame;
      }

      private MinigameType getNextMinigameType(Long lobbyId){
        Lobby lobby = getLobby(lobbyId);
        List<MinigameType> minigamesChoice = lobby.getMinigamesChoice();
        List<Minigame> minigamesPlayed = lobby.getMinigamesPlayed();

        int index = randomizer.nextInt(minigamesChoice.size());
        MinigameType nextMinigameType = minigamesChoice.get(index);
        if (minigamesPlayed.size() != 0){
        while (nextMinigameType.equals(minigamesPlayed.get(minigamesPlayed.size()-1).getType())){
          nextMinigameType = minigamesChoice.get(randomizer.nextInt(minigamesChoice.size()));
        }}
        return nextMinigameType;
      }

      public void addUpcommingMinigame(Long lobbyId){
        MinigameType type = getNextMinigameType(lobbyId);
        if (type == null){
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No MinigameType has been chosen!");
        }

        // wip
        //random player choice

        Lobby lobby = getLobby(lobbyId);
        List<Team> teams = lobby.getTeams();
        Player playerTeam1 = playerService.getMinigamePlayer(teams.get(0));
        Player playerTeam2 = playerService.getMinigamePlayer(teams.get(1));



        Minigame nextMinigame = minigameService.createMinigame(type, playerTeam1, playerTeam2);
        lobby.setUpcomingMinigame(nextMinigame);
        lobbyRepository.save(lobby);
        lobbyRepository.flush();
      }

      @Transactional
      public void finishedMinigameUpdate(Long lobbyId, Team winnerTeamInput){
        Lobby lobby = getLobby(lobbyId);

        //update minigame
        Minigame playedMinigame = lobby.getUpcomingMinigame();
        minigameService.updateMinigame(playedMinigame.getId(), winnerTeamInput.getName());
        lobby.addToMinigamesPlayed(playedMinigame);

        //update roundsPlayed of players
        playerService.updatePlayer(playedMinigame.getTeam1Player().getId());
        playerService.updatePlayer(playedMinigame.getTeam2Player().getId());


        //update score of teams
        teamService.updateScore(lobby, winnerTeamInput.getColor(), winnerTeamInput.getScore());

        List<Team> teams = lobby.getTeams();
        for (Team t : teams){
          if (t.getColor().ordinal() != winnerTeamInput.getColor().ordinal()){
            int score = playedMinigame.getScoreToGain() - winnerTeamInput.getScore();
            teamService.updateScore(lobby, t.getColor(), score);
          }
        }      
      }
    
      private Team getLeadingTeam(Long lobbyId){
        Lobby lobby = getLobby(lobbyId);

        Team team = Collections.max(lobby.getTeams(), new Comparator<Team>() {
          public int compare(Team team1, Team team2) {
              return team1.getScore() - team2.getScore();
        }});
        return team;
      }

      public void isFinished(Long lobbyId){
        Team team = getLeadingTeam(lobbyId);
        Lobby lobby = getLobby(lobbyId);
        if (team.getScore() >= lobby.getWinningScore()){
              lobby.setIsFinished(true);
        }
      }

      public Team getWinner(Long lobbyId){
        Lobby lobby = getLobby(lobbyId);
        Team team = getLeadingTeam(lobbyId);
        if (lobby.getIsFinished()){return team;}
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no winner yet!");
      }

      public void addToUnassignedPlayers(Long lobbyId, Player newPlayer){
        Lobby lobby = getLobby(lobbyId);
        if (lobby == null || newPlayer == null){
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby or Player is empty!");
        }
        lobby.addToUnassignedPlayers(newPlayer);
        lobbyRepository.save(lobby);
        lobbyRepository.flush();
      }

      public void removeFromUnassignedPlayers(Long lobbyId, Player remPlayer){
        Lobby lobby = getLobby(lobbyId);
        if (lobby == null || remPlayer == null){
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby or Player is empty!");
        }
        List<Player> players = lobby.getUnassignedPlayers();
        players.remove(remPlayer);
        lobbyRepository.save(lobby);
        lobbyRepository.flush();
      }

      public boolean ableToJoin(int inviteCode, Player playerToCreate){
        Lobby lobby = getLobby(inviteCode);
        int cnt = 0;
        for (Player p : lobby.getUnassignedPlayers()){
          if (p.getNickname().equals(playerToCreate.getNickname())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Player with this Nickname already exists in this lobby!");
          }
          cnt += 1;
        }
        for (Team t : lobby.getTeams()){
          for (Player p : t.getPlayers()){
            if (p.getNickname().equals(playerToCreate.getNickname())){
              throw new ResponseStatusException(HttpStatus.CONFLICT, "Player with this Nickname already exists in this lobby!");
            }
            cnt += 1;
          }
        }
        if (cnt == 8){
          throw new ResponseStatusException(HttpStatus.LOCKED, "Player limit for lobby was reached!");
        }

        return true;
      }

      public void ableToStart(Long lobbyId){
        Lobby lobby = getLobby(lobbyId);
        List<Team> teams = lobby.getTeams();
        if (lobby.getUnassignedPlayers().size() == 0){
          if (teams.get(0).getPlayers().size() > 0 && teams.get(1).getPlayers().size() > 0){
            return;
          }
          throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "There are not enough players in the teams to start!");
        }
        throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "There players that are not assigned yet!");
      }
}
