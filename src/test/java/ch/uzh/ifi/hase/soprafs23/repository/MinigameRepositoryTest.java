package ch.uzh.ifi.hase.soprafs23.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import ch.uzh.ifi.hase.soprafs23.constant.MinigamePlayers;
import ch.uzh.ifi.hase.soprafs23.entity.minigame.HotPotato;
import ch.uzh.ifi.hase.soprafs23.entity.minigame.Minigame;

@DataJpaTest
@ActiveProfiles("test")
public class MinigameRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MinigameRepository minigameRepository;

    @Test
    public void testFindById() {
        // Create a Minigame entity and persist it
        Minigame minigame = new HotPotato();
        minigame.setAmountOfPlayers(MinigamePlayers.ALL);

        entityManager.persist(minigame);
        entityManager.flush();

        // Retrieve the Minigame entity by ID using the repository
        Minigame foundMinigame = minigameRepository.findById(minigame.getId()).orElse(null);

        // Assert that the retrieved Minigame is not null and has the expected ID
        assertThat(foundMinigame).isNotNull();
        assertThat(foundMinigame.getId()).isEqualTo(minigame.getId());
    }
}
