package ch.uzh.ifi.hase.soprafs23.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import ch.uzh.ifi.hase.soprafs23.constant.PlayerChoice;
import ch.uzh.ifi.hase.soprafs23.entity.Game;

@DataJpaTest
public class GameRepositoryTest {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testFindById() {
        // Create a game entity and persist it to the in-memory database
        Game game = new Game();

        entityManager.persist(game);
        entityManager.flush();

        // Call the findById method of the repository
        Game foundGame = gameRepository.findById(game.getId()).orElse(null);

        // Assert that the found game is not null and has the expected ID
        assertNotNull(foundGame);
        assertEquals(game.getId(), foundGame.getId());
    }

    // Add more test cases for other methods if needed
}
