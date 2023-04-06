package ch.uzh.ifi.hase.soprafs23.service;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs23.constant.MinigameDescription;
import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;
import ch.uzh.ifi.hase.soprafs23.entity.Minigame;
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

    public Minigame createMinigame(MinigameType nexMinigameType){
        String description = MinigameDescription.getMinigamesDescriptions().get(nexMinigameType);
        Minigame upcommingMinigame = new Minigame();
        upcommingMinigame.setScoreToGain(500);
        upcommingMinigame.setType(nexMinigameType);
        upcommingMinigame.setDescription(description);
        upcommingMinigame = minigameRepository.save(upcommingMinigame);
        minigameRepository.flush();
        log.debug("Created Information for Minigame: {}", upcommingMinigame);
        return upcommingMinigame;
    }
        
    
}
