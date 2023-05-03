package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;
import ch.uzh.ifi.hase.soprafs23.constant.TeamType;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
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

import ch.uzh.ifi.hase.soprafs23.entity.Minigame;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Team;

@Service
@Transactional
public class GameService {
    
    private final Logger log = LoggerFactory.getLogger(LobbyManagement.class);

    @Autowired
    private final GameRepository gameRepository;

    @Autowired
    private final MinigameService minigameService;

    @Autowired
    private final TeamService teamService;

    @Autowired
    private final PlayerService playerService;

    @Autowired
    private final LobbyManagement lobbyManager;

    private Random randomizer = new Random();

    public GameService(@Qualifier("gameRepository") GameRepository gameRepository, MinigameService minigameService,
      TeamService teamService, PlayerService playerService, LobbyManagement lobbyManager) {
        this.gameRepository = gameRepository;
        this.minigameService = minigameService;
        this.teamService = teamService;
        this.playerService = playerService;
        this.lobbyManager = lobbyManager;
    }

    public Game getGame(Lobby lobby) {
        Game game = gameRepository.findByLobby(lobby);
        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The game with the given Lobby does not exist!");
        }
        return game;
    }

    public Game createGame(Game game){
        List<MinigameType> minigames = minigameService.chosenMinigames();
        game.setMinigamesChoice(minigames);
        game = gameRepository.save(game);
        gameRepository.flush();
        return game;
    }

    public Minigame getMinigame(Lobby lobby) {
        Game game = getGame(lobby);
        Minigame minigame = game.getUpcomingMinigame();
        if (minigame == null) {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No upcomming Minigame was found!");
        }
        return minigame;
    }

    private MinigameType getNextMinigameType(Lobby lobby) {
        Game game = getGame(lobby);
        List<MinigameType> minigamesChoice = game.getMinigamesChoice();
        List<Minigame> minigamesPlayed = game.getMinigamesPlayed();
    
        int index = randomizer.nextInt(minigamesChoice.size());
        MinigameType nextMinigameType = minigamesChoice.get(index);
        if (minigamesPlayed.size() != 0) {
          while (nextMinigameType.equals(minigamesPlayed.get(minigamesPlayed.size() - 1).getType())) {
            nextMinigameType = minigamesChoice.get(randomizer.nextInt(minigamesChoice.size()));
          }
        }
        return nextMinigameType;
    }

    public void addUpcommingMinigame(Lobby lobby) {
        MinigameType type = getNextMinigameType(lobby);
        if (type == null) {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No MinigameType has been chosen!");
        }

        // random player choice
    
        List<Team> teams = lobby.getTeams();
      
        List<Player> team1Players = new ArrayList<Player>();
        List<Player> team2Players = new ArrayList<Player>();
        Minigame nextMinigame;
        //hardcoded until better method implemented:
        if (false/*type.equals(MinigameType.HOT_POTATO)*/){
          //team1Players = playerService.getMinigamePlayers(teams.get(0), teams.get(0).getPlayers().size());
          //team2Players = playerService.getMinigamePlayers(teams.get(1), teams.get(1).getPlayers().size());
    
          // for (Player p : teams.get(0).getPlayers()){
          //   team1Players.add(p);
          // }
          // for (Player p : teams.get(1).getPlayers()){
          //   team2Players.add(p);
          // }
        }
        else{
          team1Players = playerService.getMinigamePlayers(teams.get(0),1);
          team2Players = playerService.getMinigamePlayers(teams.get(1),1);
        }
        
        nextMinigame = minigameService.createMinigame(type, team1Players, team2Players);
    
        //wip
        
        Game game = getGame(lobby);
    
        game.setUpcomingMinigame(nextMinigame);
        gameRepository.save(game);
        gameRepository.flush();        
    }

    public Team getWinner(Lobby lobby) {
        Game game = getGame(lobby);
        Team team = lobbyManager.getLeadingTeam(lobby.getId());
        if (game.getIsFinished()) {
          return team;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no winner yet!");
    }

    public void isFinished(Lobby lobby){
        Team team = lobbyManager.getLeadingTeam(lobby.getId());
        Game game = getGame(lobby);
        if (team.getScore() >= lobby.getWinningScore()){
              game.setIsFinished(true);
        }
    }

    public Minigame updateMinigame(Lobby lobby, Team winnerTeamInput){
        Game game = getGame(lobby);
        Minigame playedMinigame = game.getUpcomingMinigame();
        if (winnerTeamInput.getScore() < 0 || winnerTeamInput.getScore() > playedMinigame.getScoreToGain()){
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Scores could not be updated, because score was out of range!");
        }
        minigameService.updateMinigame(playedMinigame.getId(), winnerTeamInput.getName());
        game.addToMinigamesPlayed(playedMinigame);
        return playedMinigame;
    }







}
