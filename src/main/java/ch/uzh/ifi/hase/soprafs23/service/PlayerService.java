package ch.uzh.ifi.hase.soprafs23.service;

import java.util.Random;
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
import ch.uzh.ifi.hase.soprafs23.repository.PlayerRepository;

@Service
@Transactional
public class PlayerService {
    private final Logger log = LoggerFactory.getLogger(PlayerService.class);
    private Random randomizer = new Random();

    @Autowired
    private final PlayerRepository playerRepository;

    public PlayerService(@Qualifier("playerRepository") PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player createPlayer(Player newPlayer) {

        newPlayer = playerRepository.save(newPlayer);
        playerRepository.flush();

        log.debug("Created Information for User: {}", newPlayer);
        return newPlayer;
    }

    public Player getPlayer(Long playerId){
        Player player = playerRepository.findById(playerId). 
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The player with the given Id does not exist!"));
        return player;
    }

    public Player getMinigamePlayer(Team team){
        int lowestAmountPlayed = -1;
        for (Player p : team.getPlayers()){
            if (lowestAmountPlayed == -1){
                lowestAmountPlayed = p.getRoundsPlayed();
            }
            if (p.getRoundsPlayed() < lowestAmountPlayed){
                lowestAmountPlayed = p.getRoundsPlayed();
            }
        }
        int optIndex;
        while (true){
            optIndex = randomizer.nextInt(team.getPlayers().size());
            Player player = team.getPlayers().get(optIndex);
            if (player.getRoundsPlayed() > lowestAmountPlayed){
                continue;
            }
            else{
                return player;
            }
        }
        
    }

    public void updatePlayer(Long playerId){
        Player player = getPlayer(playerId);
        player.setRoundsPlayed(player.getRoundsPlayed() + 1);
        playerRepository.save(player);
        playerRepository.flush();
    }
}
