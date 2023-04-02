package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs23.entity.Minigame;


@Service
@Transactional
public class LobbyManagement {
    
    private final Logger log = LoggerFactory.getLogger(LobbyManagement.class);

    @Autowired
    private final LobbyRepository lobbyRepository;
    

    public LobbyManagement(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository) {
        this.lobbyRepository = lobbyRepository;
      }   

    public Lobby createLobby(Lobby newLobby) {
        int inviteCode = new Random().nextInt(900000) + 100000;
        while (lobbyRepository.findByInviteCode(inviteCode) != null){
          inviteCode = new Random().nextInt(900000) + 100000;
        }
        newLobby.setInviteCode(inviteCode);

        
        newLobby = lobbyRepository.save(newLobby);
        lobbyRepository.flush();
    
        log.debug("Created Information for User: {}", newLobby);
        return newLobby;
      }
    
      public Lobby getLobby(Long lobbyId) {
        Lobby lobby = lobbyRepository.findById(lobbyId).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The lobby with the given Id does not exist!"));
        return lobby;
      }

      public Minigame getMinigame(Long lobbyId){
        Lobby lobby = lobbyRepository.findById(lobbyId).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The lobby with the given Id does not exist!"));
        return lobby.getNextMinigame();
    }



}
