package ch.uzh.ifi.hase.soprafs23.service;

import java.util.List;

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
import ch.uzh.ifi.hase.soprafs23.repository.TeamRepository;

@Service
@Transactional
public class TeamService {
    private final Logger log = LoggerFactory.getLogger(TeamService.class);


    @Autowired
    private final TeamRepository teamRepository;

    public TeamService(@Qualifier("teamRepository") TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public Team createTeam(Team newTeam) {
        
        newTeam = teamRepository.save(newTeam);
        teamRepository.flush();

        log.debug("Created Information for User: {}", newTeam);
        return newTeam;
    }

    public void addPlayer(Long teamId, Player player){
        Team team = getTeam(teamId);
        team.addPlayer(player);
        teamRepository.save(team);
        teamRepository.flush();
    }

    public void removePlayer(Long teamId, Player player){
        Team team = getTeam(teamId);
        List<Player> players = team.getPlayers();
        players.remove(player);
        teamRepository.save(team);
        teamRepository.flush();
        //does this work? or do we need to set id again
    }
    
    public Team getTeam(Long teamId) {
        Team team = teamRepository.findById(teamId).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The team with the given Id does not exist!"));
        return team;
    }
}
