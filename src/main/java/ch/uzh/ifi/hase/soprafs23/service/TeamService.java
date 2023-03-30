package ch.uzh.ifi.hase.soprafs23.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    
}
