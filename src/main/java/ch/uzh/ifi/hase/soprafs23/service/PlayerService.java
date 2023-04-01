package ch.uzh.ifi.hase.soprafs23.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.repository.PlayerRepository;


@Service
@Transactional
public class PlayerService {
    private final Logger log = LoggerFactory.getLogger(PlayerService.class);


    @Autowired
    private final PlayerRepository playerRepository;

    public PlayerService(@Qualifier("playerRepository") PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player createPLayer(Player newPlayer) {
        
        newPlayer = playerRepository.save(newPlayer);
        playerRepository.flush();

        log.debug("Created Information for User: {}", newPlayer);
        return newPlayer;
    }
    
}
