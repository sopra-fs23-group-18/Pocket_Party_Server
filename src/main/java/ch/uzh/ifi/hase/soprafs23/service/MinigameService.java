package ch.uzh.ifi.hase.soprafs23.service;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;
import ch.uzh.ifi.hase.soprafs23.entity.Minigame;
import ch.uzh.ifi.hase.soprafs23.entity.TappingGame;
import ch.uzh.ifi.hase.soprafs23.entity.TimingGame;
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

    private Minigame createMinigame(Minigame minigame) {
        minigame = minigameRepository.save(minigame);
        minigameRepository.flush();
        log.debug("Created Information for Minigame: {}", minigame);
        return minigame;
    }

    public List<Minigame> chosenMinigames(){
        //List<MinigameType> minigames = Arrays.asList(MinigameType.values());

        Minigame tappingGame = createMinigame(new TappingGame(100));
        Minigame timingGame = createMinigame(new TimingGame(200));
        List<Minigame> minigames = Arrays.asList(tappingGame, timingGame);
        
        return minigames;
    }
        
    
}
