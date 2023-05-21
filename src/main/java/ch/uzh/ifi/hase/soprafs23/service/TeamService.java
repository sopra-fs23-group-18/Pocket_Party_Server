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

    public Team createTeam(Lobby lobby, String name) {
        Team newTeam = new Team();
        newTeam.setLobby(lobby);
        newTeam.setName(name);

        log.debug("Created Information for User: {}", newTeam);
        return newTeam;
    }

    public void addPlayer(Lobby lobby, String teamName, Player player) {
        if (lobby == null || teamName == null || player == null) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT,
                    "The input was empty, please provide information!");
        }
        Team team = getByNameAndLobby(lobby, teamName);
        List<Player> players = team.getPlayers();
        players.add(player);
        teamRepository.save(team);
        teamRepository.flush();
    }

    public void removePlayer(Lobby lobby, String teamName, Player player) {
        if (player == null) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT,
                    "The input was empty, please provide information!");
        }
        Team team = getByNameAndLobby(lobby, teamName);
        List<Player> players = team.getPlayers();
        players.remove(player);
        teamRepository.save(team);
        teamRepository.flush();
        // does this work? or do we need to set id again
    }

    public Team getTeam(Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The team with the given Id does not exist!"));
        return team;
    }

    public void updateScore(Lobby lobby, String teamName, int score) {
        Team team = getByNameAndLobby(lobby, teamName);
        if (team == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Team with such name exists");
        }
        team.setScore(team.getScore() + score);
        teamRepository.save(team);
        teamRepository.flush();
    }

    // public Team getByColorAndLobby(Lobby lobby, TeamType color) {
    //     List<Team> teams = getTeams(lobby);
    //     for (Team team : teams) {
    //         if (team.getColor().ordinal() == color.ordinal()) {
    //             return team;
    //         }
    //     }
    //     return null;
    // }

    public Team getByNameAndLobby(Lobby lobby, String name) {
        List<Team> teams = getTeams(lobby);
        for (Team team : teams) {
            if (team.getName().equals(name)) {
                return team;
            }
        }
        return null;
    }

    public List<Team> getTeams(Lobby lobby){
        List<Team> teams = teamRepository.findByLobby(lobby);
        if (teams.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No team with such a lobby exists!");
        }
        return teams;
    }

    public List<Player> randomPlayerChoice(String teamName, Lobby lobby, MinigamePlayers amount){

        List<Player> players = new ArrayList<Player>();
        Team team = getByNameAndLobby(lobby, teamName);

        //mapper that maps ONE, TWO to 1,2

        if (amount.equals(MinigamePlayers.ALL)){
            for (Player p :team.getPlayers()){
            players.add(p);
            }
        }
        else{
            int amountOfPlayers = MinigameMapper.getMinigamePlayers().get(amount);
            players = playerService.getMinigamePlayers(team, amountOfPlayers);
        }
        return players;
    }

    @Transactional
    public void updateNames(Lobby lobby, List<Team> teamNames){
        if (teamNames.get(0).getName().equals(teamNames.get(1).getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The names are equal!");
        }
        //check if both names unique, maybe via getByNameAndLobby and see if already in there (if null then doesn't exist)
        for (Team update : teamNames){
            if (update.getName().strip().equals("")){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This name is invalid");
            }
            Team team = getTeam(update.getId());

            if (team.getName().equals(update.getName())){continue;}
            // if (getByNameAndLobby(lobby, update.getName()) == null){
            //     throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "null")
            // }
            team.setName(update.getName());

            teamRepository.save(team);
            //if something doesnt work, throw error
        }
        teamRepository.flush();
    }


    


}
