package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class LobbyRepositoryTest {

    @Autowired
    private LobbyRepository lobbyRepository;

    @Test
    public void findById_success() {
        // given
        Lobby lobby = new Lobby();
        lobby.setInviteCode(1234);
        lobby.setWinningScore(100);

        lobbyRepository.save(lobby);
        lobbyRepository.flush();

        // when
        Lobby found = lobbyRepository.findById(lobby.getId()).orElse(null);

        // then
        assertNotNull(found);
        assertEquals(lobby.getId(), found.getId());
        assertEquals(lobby.getInviteCode(), found.getInviteCode());
    }

    @Test
    public void findByInviteCode_success() {
        // given
        Lobby lobby = new Lobby();
        lobby.setInviteCode(123456);
        lobby.setWinningScore(100);

        lobbyRepository.save(lobby);
        lobbyRepository.flush();

        // when
        Lobby found = lobbyRepository.findByInviteCode(lobby.getInviteCode());

        // then
        assertNotNull(found);
        assertEquals(lobby.getId(), found.getId());
        assertEquals(lobby.getInviteCode(), found.getInviteCode());
    }

}
