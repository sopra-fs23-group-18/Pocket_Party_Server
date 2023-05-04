package ch.uzh.ifi.hase.soprafs23.service;

import java.util.ArrayList;
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

    public Minigame createMinigame(MinigameType nexMinigameType, List<Long> team1Players, List<Long> team2Players){
        String description = MinigameDescription.getMinigamesDescriptions().get(nexMinigameType);
        Minigame upcommingMinigame = new Minigame();
        upcommingMinigame.setScoreToGain(500);
        upcommingMinigame.setType(nexMinigameType);
        upcommingMinigame.setDescription(description);

        List<Player> test1 = new ArrayList<Player>();
        List<Player> test2 = new ArrayList<Player>();

        

        // wip
        upcommingMinigame.setTeam1Players(test1);
        upcommingMinigame.setTeam2Players(test2);



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



    public List<Player> getTeam1Players(Long minigameId){
        Minigame minigame = getMinigame(minigameId);
        return minigame.getTeam1Players();
    }

    public List<Player> getTeam2Players(Long minigameId){
        Minigame minigame = getMinigame(minigameId);
        return minigame.getTeam2Players();
    }

    public void addPlayerToTeam1(Player player, Long minigameId){
        Minigame minigame = getMinigame(minigameId);
        minigame.getTeam1Players().add(player);
        minigameRepository.save(minigame);
        minigameRepository.flush();
    }

    public void addPlayerToTeam2(Player player, Long minigameId){
        Minigame minigame = getMinigame(minigameId);
        minigame.getTeam2Players().add(player);
        minigameRepository.save(minigame);
        minigameRepository.flush();
    }
}
