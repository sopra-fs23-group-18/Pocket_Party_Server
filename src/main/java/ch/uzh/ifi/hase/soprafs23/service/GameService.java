package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.MinigamePlayers;
import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;
import ch.uzh.ifi.hase.soprafs23.constant.TeamType;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;

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
    
    private final Logger log = LoggerFactory.getLogger(GameService.class);

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

    public Game getGame(Long gameId) {
      Game game = gameRepository.findById(gameId).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The game with the given Id does not exist!"));
      return game;
  }

    public Minigame getMinigame(Lobby lobby) {
        Game game = getGame(lobby);
        Minigame minigame = game.getUpcomingMinigame();
        if (minigame == null) {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No upcoming Minigame was found!");
        }
        return minigame;
    }

    private MinigameType getNextMinigameType(Game game) {
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

    public Minigame addUpcomingMinigame(Game game) {
        MinigameType type = getNextMinigameType(game);
        if (type == null) {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No MinigameType has been chosen!");
        }
        Minigame nextMinigame = minigameService.createMinigame(type);
    
        game.setUpcomingMinigame(nextMinigame);
        gameRepository.save(game);
        gameRepository.flush();
        return nextMinigame;        
    }

    public Team getWinner(Long gameId) {
        Game game = getGame(gameId);
        Team team = lobbyManager.getLeadingTeam(game);
        if (game.getIsFinished()) {
          return team;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no winner yet!");
    }

    public void isFinished(Game game){
        Team team = lobbyManager.getLeadingTeam(game);
        if (team.getScore() >= game.getWinningScore()){
              game.setIsFinished(true);
        }
    }

    //new
    public Game createGame(Game newGame, Long lobbyId){
      if (newGame.getWinningScore() <= 0 || newGame.getWinningScore() > 100000){
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game could not be created because the winningScore was invalid!");
      }
      List<MinigameType> minigames;
      if (newGame.getMinigamesChoice() == null || newGame.getMinigamesChoice().isEmpty()){
        minigames = minigameService.chosenMinigames();
        newGame.setMinigamesChoice(minigames);
      }
      lobbyManager.addGame(newGame, lobbyId);
      newGame.setLobby(lobbyManager.getLobby(lobbyId));
      Game createdGame = gameRepository.save(newGame);
      gameRepository.flush();
      return createdGame;
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

    @Transactional
    public void finishedMinigameUpdate(Long gameId, Team winnerTeamInput) {
      Game game = getGame(gameId);
      Lobby lobby = lobbyManager.getLobby(game);

      //maybe remove this method
      Minigame playedMinigame = updateMinigame(lobby, winnerTeamInput);

      // update roundsPlayed of players
      playerService.updatePlayers(playedMinigame.getTeam1Players());
      playerService.updatePlayers(playedMinigame.getTeam2Players());

      // update score of teams
      teamService.updateScore(lobby, winnerTeamInput.getColor(), winnerTeamInput.getScore());

      List<Team> teams = lobby.getTeams();
      if(teams.get(0).getColor().ordinal() != winnerTeamInput.getColor().ordinal()){
        int score = playedMinigame.getScoreToGain() - winnerTeamInput.getScore();
        teamService.updateScore(lobby, teams.get(0).getColor(), score);
      }else{
        int score = playedMinigame.getScoreToGain() - winnerTeamInput.getScore();
        teamService.updateScore(lobby, teams.get(1).getColor(), score);
      }
      isFinished(game);
      gameRepository.save(game);
      gameRepository.flush();
    }

    public void updateUpcomingMinigame(Long gameId){
      Game game = getGame(gameId);
      Minigame upcomingMinigame = game.getUpcomingMinigame();
      Lobby lobby = lobbyManager.getLobby(game);
      
      MinigamePlayers amount = MinigamePlayers.ONE;
      if (upcomingMinigame.getType().equals(MinigameType.HOT_POTATO)){
        amount = MinigamePlayers.ALL;
      }
      List<Player> team1Players = teamService.randomPlayerChoice(TeamType.RED, lobby, amount);
      List<Player> team2Players = teamService.randomPlayerChoice(TeamType.BLUE, lobby, amount);

      minigameService.addPlayersToMinigame(upcomingMinigame, team1Players, team2Players);

    }




    






}
