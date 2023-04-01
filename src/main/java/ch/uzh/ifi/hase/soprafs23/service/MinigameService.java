package ch.uzh.ifi.hase.soprafs23.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    public Minigame createMinigame(Minigame minigame) {
        minigame = minigameRepository.save(minigame);
        minigameRepository.flush();
        log.debug("Created Information for Minigame: {}", minigame);
        return minigame;
    }
        
    
}
