package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.constant.PlayerChoice;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
public class LobbyRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LobbyRepository lobbyRepository;

    @Test
    public void testFindById() {
        // Create a Lobby entity and persist it
        Lobby lobby = new Lobby();
        entityManager.persist(lobby);
        entityManager.flush();

        // Retrieve the Lobby entity by ID using the repository
        Lobby foundLobby = lobbyRepository.findById(lobby.getId()).orElse(null);

        // Assert that the retrieved Lobby is not null and has the expected ID
        assertNotNull(foundLobby);
        assertEquals(lobby.getId(), foundLobby.getId());
    }

    @Test
    public void testFindByInviteCode() {
        // Create a Lobby entity with a specific invite code and persist it
        int inviteCode = 123456;
        Lobby lobby = new Lobby();
        lobby.setInviteCode(inviteCode);
        entityManager.persist(lobby);
        entityManager.flush();

        // Retrieve the Lobby entity by invite code using the repository
        Lobby foundLobby = lobbyRepository.findByInviteCode(inviteCode);

        // Assert that the retrieved Lobby is not null and has the expected invite code
        assertNotNull(foundLobby);
        assertEquals(inviteCode, foundLobby.getInviteCode());
    }

    @Test
    public void testFindByGame() {
        // Create a Lobby entity and a corresponding Game entity, and associate them
        Lobby lobby = new Lobby();
        Game game = new Game();
        game.setPlayerChoice(PlayerChoice.VOTING);
        lobby.setGame(game);
        entityManager.persist(lobby);
        entityManager.flush();

        // Retrieve the Lobby entity by associated Game using the repository
        Lobby foundLobby = lobbyRepository.findByGame(game);

        // Assert that the retrieved Lobby is not null and has the expected associated
        // Game
        assertNotNull(foundLobby);
        assertEquals(game, foundLobby.getGame());
    }
}
