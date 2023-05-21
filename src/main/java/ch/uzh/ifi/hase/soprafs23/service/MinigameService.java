package ch.uzh.ifi.hase.soprafs23.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
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

import ch.uzh.ifi.hase.soprafs23.constant.MinigameMapper;
import ch.uzh.ifi.hase.soprafs23.constant.MinigamePlayers;
import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.minigame.Minigame;
import ch.uzh.ifi.hase.soprafs23.repository.MinigameRepository;

@Service
@Transactional
public class MinigameService {
    
    private final Logger log = LoggerFactory.getLogger(MinigameService.class);

    private Random randomizer = new Random();
    
    @Autowired
    private final MinigameRepository minigameRepository;
    
    public MinigameService(@Qualifier("minigameRepository") MinigameRepository minigameRepository) {
        this.minigameRepository = minigameRepository;
    }

    public List<MinigameType> chooseAllMinigames(){
        List<MinigameType> minigames = Arrays.asList(MinigameType.values());
        return minigames;
    }

    public Minigame createMinigame(MinigameType nexMinigameType, int lowestPlayerAmount){
        Minigame upcomingMinigame = getMinigameInstance(nexMinigameType);
        MinigamePlayers[] options = upcomingMinigame.getAmntPlayersOptions();

        int index = randomizer.nextInt(options.length);
        MinigamePlayers amount = options[index];
        if (amount == MinigamePlayers.TWO && lowestPlayerAmount < 2) {
            amount = MinigamePlayers.ONE;
        }       
        upcomingMinigame.setAmountOfPlayers(amount);

        upcomingMinigame = minigameRepository.save(upcomingMinigame);
        minigameRepository.flush();
        log.debug("Created Information for Minigame: {}", upcomingMinigame);
        return upcomingMinigame;
    }

    private Minigame getMinigameInstance(MinigameType nexMinigameType) {
        Class<? extends Minigame> minigameClass = MinigameMapper.getMinigameClasses().get(nexMinigameType);
        try {
            return minigameClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("Unable to create minigame instance", e);
        }
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

    public void addPlayersToMinigame(Minigame minigame, List<Player> team1Players, List<Player> team2Players){
        Minigame upcomingMinigame = getMinigame(minigame.getId());
        upcomingMinigame.setTeam1Players(team1Players);
        upcomingMinigame.setTeam2Players(team2Players);
        minigameRepository.save(upcomingMinigame);
        minigameRepository.flush();
    }
}
