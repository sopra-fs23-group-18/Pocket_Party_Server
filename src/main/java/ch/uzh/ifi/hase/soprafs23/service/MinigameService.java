package ch.uzh.ifi.hase.soprafs23.service;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs23.constant.MinigameDescription;
import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;
import ch.uzh.ifi.hase.soprafs23.entity.Minigame;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.repository.MinigameRepository;

@Service
@Transactional
public class MinigameService {
    
    private final Logger log = LoggerFactory.getLogger(MinigameService.class);
    
    @Autowired
    private final MinigameRepository minigameRepository;
    
    public MinigameService(@Qualifier("minigameRepository") MinigameRepository minigameRepository) {
        this.minigameRepository = minigameRepository;
    }

    public List<MinigameType> chosenMinigames(){
        List<MinigameType> minigames = Arrays.asList(MinigameType.values());

        return minigames;
    }

    public Minigame createMinigame(MinigameType nexMinigameType, List<Player> team1Players, List<Player> team2Players){
        String description = MinigameDescription.getMinigamesDescriptions().get(nexMinigameType);
        Minigame upcommingMinigame = new Minigame();
        upcommingMinigame.setScoreToGain(500);
        upcommingMinigame.setType(nexMinigameType);
        upcommingMinigame.setDescription(description);

        // wip
        upcommingMinigame.setTeam1Players(team1Players);
        upcommingMinigame.setTeam2Players(team2Players);



        upcommingMinigame = minigameRepository.save(upcommingMinigame);
        minigameRepository.flush();
        log.debug("Created Information for Minigame: {}", upcommingMinigame);
        return upcommingMinigame;
    }

    public Minigame getMinigame(Long minigameId){
        Minigame minigame = minigameRepository.findById(minigameId).
            orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The Minigame with given Id does not exist!"));
        return minigame;
    }

    public void updateMinigame(Long minigameId, String winnerTeam){
        Minigame finishedMinigame = getMinigame(minigameId);

        finishedMinigame.setWinner(winnerTeam);
        
        finishedMinigame = minigameRepository.save(finishedMinigame);
        minigameRepository.flush();
    }
}
