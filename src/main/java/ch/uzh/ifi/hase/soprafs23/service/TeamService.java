package ch.uzh.ifi.hase.soprafs23.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs23.constant.MinigameMapper;
import ch.uzh.ifi.hase.soprafs23.constant.MinigamePlayers;
import ch.uzh.ifi.hase.soprafs23.constant.TeamType;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Team;
import ch.uzh.ifi.hase.soprafs23.repository.TeamRepository;

@Service
@Transactional
public class TeamService {
    private final Logger log = LoggerFactory.getLogger(TeamService.class);

    @Autowired
    private final TeamRepository teamRepository;

    @Autowired
    private final PlayerService playerService;

    public TeamService(@Qualifier("teamRepository") TeamRepository teamRepository, PlayerService playerService) {
        this.teamRepository = teamRepository;
        this.playerService = playerService;
    }

    public Team createTeam(Lobby lobby, String name, TeamType type) {
        Team newTeam = new Team();
        newTeam.setLobby(lobby);
        newTeam.setName(name);
        newTeam.setType(type);

        log.debug("Created Information for User: {}", newTeam);
        return newTeam;
    }

    public void addPlayer(Lobby lobby, TeamType type , Player player) {
        if (lobby == null || type == null || player == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The input was empty, please provide information!");
        }
        Team team = getByTypeAndLobby(lobby, type);
        List<Player> players = team.getPlayers();
        players.add(player);
        teamRepository.save(team);
        // teamRepository.flush();
    }
    
    public void removePlayer(Lobby lobby, TeamType type, Player player) {
        if (lobby == null || type == null || player == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The input was empty, please provide information!");
        }
        Team team = getByTypeAndLobby(lobby, type);
        List<Player> players = team.getPlayers();
        players.remove(player);
        teamRepository.save(team);
        // teamRepository.flush();
    }

    public Team getTeam(Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The team with the given Id does not exist!"));
        return team;
    }

    public void updateScore(Lobby lobby, String teamName, int score) {
        Team team = getByNameAndLobby(lobby, teamName);
        team.setScore(team.getScore() + score);
        teamRepository.save(team);
        teamRepository.flush();
    }

    public Team getByTypeAndLobby(Lobby lobby, TeamType color) {
        List<Team> teams = getTeams(lobby);
        for (Team team : teams) {
            if (team.getType().ordinal() == color.ordinal()) {
                return team;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Team with such type exists");
    }

    public Team getByNameAndLobby(Lobby lobby, String name) {
        List<Team> teams = getTeams(lobby);
        for (Team team : teams) {
            if (team.getName().equals(name)) {
                return team;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Team with such name exists");
    }

    public List<Team> getTeams(Lobby lobby) {
        List<Team> teams = teamRepository.findByLobby(lobby);
        if (teams.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No teams with such a lobby exists!");
        }
        return teams;
    }

    public List<Player> randomPlayerChoice(String teamName, Lobby lobby, MinigamePlayers amount) {
        Team team = getByNameAndLobby(lobby, teamName);
        int amountOfPlayers;

        if (amount.equals(MinigamePlayers.ALL)) {
            amountOfPlayers = lowestPlayerAmount(lobby);
        } else {
            amountOfPlayers = MinigameMapper.getMinigamePlayers().get(amount);
        }
        List<Player> players = playerService.getMinigamePlayers(team, amountOfPlayers);
        return players;
    }

    @Transactional
    public void updateNames(List<Team> teamNames) {
        if (teamNames.get(0).getName().equals(teamNames.get(1).getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The names are equal!");
        }
        for (Team update : teamNames) {
            if (update.getName().strip().equals("")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This name is invalid");
            }
            Team team = getTeam(update.getId());

            if (team.getName().equals(update.getName())) {
                continue;
            }
            team.setName(update.getName());

            teamRepository.save(team);
        }
        teamRepository.flush();
    }

    public int lowestPlayerAmount(Lobby lobby) {
        List<Team> teams = getTeams(lobby);
        int amount = -1;
        for (Team t : teams) {
            if (amount == -1) {
                amount = t.getPlayers().size();
            }
            if (t.getPlayers().size() < amount) {
                amount = t.getPlayers().size();
            }
        }
        return amount;
    }
}
