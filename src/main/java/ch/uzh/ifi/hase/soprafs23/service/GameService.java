package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.MinigamePlayers;
import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;
import ch.uzh.ifi.hase.soprafs23.constant.OutcomeType;
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

import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Team;
import ch.uzh.ifi.hase.soprafs23.entity.minigame.Minigame;

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

    public Game getGame(Long gameId) {
      Game game = gameRepository.findById(gameId).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The game with the given Id does not exist!"));
      return game;
    }

    public Minigame getMinigame(Long gameId) {
        Game game = getGame(gameId);
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

    public Minigame addUpcomingMinigame(Long gameId) {
        Game game = getGame(gameId);
        if (game.getUpcomingMinigame() != null){
          if (getMinigame(gameId).getMinigameOutcome() == OutcomeType.NOT_FINISHED){
            return game.getUpcomingMinigame();
          }
        }
        MinigameType type = getNextMinigameType(game);
        if (type == null) {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Something went wrong in the server, no MinigameType has been chosen!");
        }
        int lowestPlayerAmount = teamService.lowestPlayerAmount(lobbyManager.getLobby(game));
        Minigame nextMinigame = minigameService.createMinigame(type, lowestPlayerAmount);
    
        game.setUpcomingMinigame(nextMinigame);
        gameRepository.save(game);
        gameRepository.flush();
        return nextMinigame;        
    }

    public Team getWinner(Long gameId) {
        Game game = getGame(gameId);
        Team team = lobbyManager.getLeadingTeam(game);
        if (game.getGameOutcome() != OutcomeType.NOT_FINISHED) {
          return team;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no winner!");
    }

    public void isFinished(Game game){
        Lobby lobby = lobbyManager.getLobby(game);
        Team team = lobbyManager.getLeadingTeam(game);
        if (team.getScore() >= game.getWinningScore()){
          if (lobby.getTeams().size() < 2){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Something went wrong in the server, the lobby doesn't have 2 teams");
          }
          if (lobby.getTeams().get(0).getScore() == lobby.getTeams().get(1).getScore()){
            game.setGameOutcome(OutcomeType.DRAW);
          }
          else{
            game.setGameOutcome(OutcomeType.WINNER);
          }
          if (lobby.getGame() == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Something went wrong in the server, the lobby had no game set to it");
          }
          lobby.getFinishedGames().add(lobby.getGame());
        }
    }

    public Game createGame(Game newGame, Long lobbyId){
      if (newGame.getWinningScore() <= 0 || newGame.getWinningScore() > 100000){
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game could not be created because the winningScore was invalid!");
      }
      if (newGame.getPlayerChoice() == null){
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game could not be created because playerChoice was not set!");
      }
      if (newGame.getMinigamesChoice() == null || newGame.getMinigamesChoice().isEmpty()){
        List<MinigameType> minigames = minigameService.chooseAllMinigames();
        newGame.setMinigamesChoice(minigames);
      }
      lobbyManager.addGame(newGame, lobbyId);
      newGame.setLobby(lobbyManager.getLobby(lobbyId));
      Game createdGame = gameRepository.save(newGame);
      gameRepository.flush();
      return createdGame;
    }

    public Minigame updateMinigame(Game game, Team winnerTeamInput){
      Minigame playedMinigame = game.getUpcomingMinigame();
      if (playedMinigame.getMinigameOutcome() != OutcomeType.NOT_FINISHED){
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Scores can only be updated once!");
      }
      if (winnerTeamInput.getScore() < 0 || winnerTeamInput.getScore() > playedMinigame.getScoreToGain()){
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Scores could not be updated, because score was out of range!");
      }
      if (winnerTeamInput.getName() == null || winnerTeamInput.getName().isEmpty()){
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No name was provided");
      }
      String winnerTeam;
      if (winnerTeamInput.getScore() == (playedMinigame.getScoreToGain()/2)){
        winnerTeam = "";
      }
      else{
        winnerTeam = winnerTeamInput.getName();
      }
      minigameService.updateMinigame(playedMinigame.getId(), winnerTeam);
      game.addToMinigamesPlayed(playedMinigame);
      return playedMinigame;
  }

    @Transactional
    public void finishedMinigameUpdate(Long gameId, Team winnerTeamInput) {
      Game game = getGame(gameId);
      Lobby lobby = lobbyManager.getLobby(game);
      
      Minigame playedMinigame = updateMinigame(game, winnerTeamInput);

      playerService.updatePlayers(playedMinigame.getTeam1Players());
      playerService.updatePlayers(playedMinigame.getTeam2Players());

      updateScores(winnerTeamInput, lobby, playedMinigame);

      isFinished(game);
      gameRepository.save(game);
      gameRepository.flush();
    }

    private void updateScores(Team winnerTeamInput, Lobby lobby, Minigame playedMinigame) {
      teamService.updateScore(lobby, winnerTeamInput.getName(), winnerTeamInput.getScore());

      List<Team> teams = lobby.getTeams();
      if (teams.size() < 2){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Something went wrong in the server, the lobby doesn't have 2 teams");
      }

      int score = 0;
      if (winnerTeamInput.getScore() != 0){
        score = playedMinigame.getScoreToGain() - winnerTeamInput.getScore();
      }

      if(!teams.get(0).getName().equals(winnerTeamInput.getName())){
        teamService.updateScore(lobby, teams.get(0).getName(), score);
      }else{
        teamService.updateScore(lobby, teams.get(1).getName(), score);
      }
    }

    public void updateUpcomingMinigame(Long gameId){
      Game game = getGame(gameId);
      Minigame upcomingMinigame = game.getUpcomingMinigame();
      if (upcomingMinigame == null){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot update minigame, because no minigame has been found");
      }
      Lobby lobby = lobbyManager.getLobby(game);

      List<Team> teams = lobby.getTeams();
      if (teams.size() < 2){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Something went wrong in the server, the lobby doesn't have 2 teams");
      }
      
      MinigamePlayers amount = upcomingMinigame.getAmountOfPlayers();

      List<Player> team1Players = teamService.randomPlayerChoice(teams.get(0).getName(), lobby, amount);
      List<Player> team2Players = teamService.randomPlayerChoice(teams.get(1).getName(), lobby, amount);

      minigameService.addPlayersToMinigame(upcomingMinigame, team1Players, team2Players);
    }
}
